package kuorum.solr

import grails.transaction.Transactional
import kuorum.core.exception.KuorumExceptionUtil
import kuorum.core.model.CommissionType
import kuorum.core.model.search.SearchParams
import kuorum.core.model.search.SearchPolitician
import kuorum.core.model.search.SearchProjects
import kuorum.core.model.search.SearchType
import kuorum.core.model.solr.*
import org.apache.solr.client.solrj.SolrQuery
import org.apache.solr.client.solrj.SolrRequest
import org.apache.solr.client.solrj.SolrServer
import org.apache.solr.client.solrj.response.Group
import org.apache.solr.client.solrj.response.QueryResponse
import org.apache.solr.client.solrj.util.ClientUtils
import org.apache.solr.common.SolrDocument
import org.apache.solr.common.SolrDocumentList
import org.apache.solr.common.params.CommonParams

@Transactional(readOnly = true)
class SearchSolrService {

    SolrServer server
    IndexSolrService indexSolrService

    SolrResults search(SearchParams params) {

        SolrQuery query = new SolrQuery();
        query.setRequestHandler("/query")
//        query.setParam(CommonParams.QT, "query");
        query.setParam(CommonParams.START, "${params.offset}");
        query.setParam(CommonParams.ROWS, "${params.max}");
        query.setParam("tie", "0");
        prepareWord(params, query)
        prepareFilter(params, query)
        prepareBoosted(params, query)
        prepareScore(params, query)

        query.setSort("score", SolrQuery.ORDER.desc)
        query.addSort("relevance", SolrQuery.ORDER.desc)
        query.addSort("kuorumRelevance", SolrQuery.ORDER.desc)
        query.addSort("constituencyIso3166_2Length", SolrQuery.ORDER.asc)
        query.addSort("regionIso3166_2Length", SolrQuery.ORDER.asc)
        query.addSort("dateCreated", SolrQuery.ORDER.desc)

        QueryResponse rsp = server.query( query, SolrRequest.METHOD.POST );
        SolrDocumentList docs = rsp.getResults();

        SolrResults solrResults = new SolrResults()
        solrResults.elements = docs.collect{indexSolrService.recoverSolrElementFromSolr(it)}
        solrResults.numResults = docs.numFound
        solrResults.suggest = prepareSuggestions(rsp)
        solrResults.facets = prepareFacets(rsp)
        prepareHighlighting(solrResults,rsp)
        solrResults
    }

    private SolrSuggest prepareSuggestions(QueryResponse rsp){
        SolrSuggest solrSuggest = null
        if (rsp?._spellInfo?.suggestions?.get("collation")){
            solrSuggest = new SolrSuggest()
            solrSuggest.suggestedQuery = rsp._spellInfo.suggestions.collation.collationQuery
            solrSuggest.hits = rsp._spellInfo.suggestions.collation.hits
        }
        solrSuggest
    }

    private Map<String, List<SolrFacets>> prepareFacets(QueryResponse rsp){
        def res = [:]
        rsp.facetFields.each{
            res.put(it.name, it.values.collect{new SolrFacets(facetName: it.name, hits: it.count)})
        }
        res
//        rsp.facetFields.collect{it._values}.flatten().collect{
//            new SolrFacets(facetName: it.name, hits: it.count)
//        }
    }

    private prepareHighlighting(SolrResults solrResults, QueryResponse rsp){
        rsp.highlighting.each{id,changes->
            SolrElement solrElement = solrResults.elements.find{it.id == id}
            changes.each{field, val ->
                if (solrElement.hasProperty(field))
                    solrElement.highlighting.storage.put(field,val[0])
            }
        }

    }

    SolrAutocomplete suggest(SearchParams params){

        if (!params.validate()){
            throw KuorumExceptionUtil.createExceptionFromValidatable(params,"Parametros de búsqueda erroneos")
        }
        SolrQuery query = new SolrQuery();
//        query.setParam(CommonParams.QT, "/suggest");
        query.setRequestHandler("/suggest");
//        query.setParam("spellcheck.q", params.word.split(" ").last());
        query.setParam("spellcheck.q", params.word);
        //query.setParam(TermsParams.TERMS_FIELD, "name", "username");
        prepareFilter(params, query)
        query.setParam("facet.prefix",params.word)

        QueryResponse rsp = server.query( query, SolrRequest.METHOD.POST );

        SolrAutocomplete solrAutocomplete = new SolrAutocomplete()
        solrAutocomplete.suggests = prepareAutocompleteSuggestions(rsp)
//        if (params.word.split(" ").size()>1){
//            String prevWords = params.word.split(" ")[0..params.word.split(" ").size()-2].join(" ")
//            solrAutocomplete.suggests = solrAutocomplete.suggests.collect{prevWords +" "+it}
//        }
        def elements = prepareSolrElements(rsp)
        solrAutocomplete.kuorumUsers = elements.kuorumUsers
        solrAutocomplete.numResults =rsp.results.numFound

        solrAutocomplete
    }


    private void prepareScore(SearchParams params, SolrQuery query){
        switch (params.searchType){
            case SearchType.REGION:
                query.setParam("qf","regionIso3166_2^10.0 constituencyIso3166_2^5.0")
                break;
            case SearchType.CAUSE:
                query.setParam("qf","tags^10.0")
                break;
            case SearchType.ALL:
                break;
            default:
            //DEFAULT
                break;
        }
    }
    private void prepareBoosted(SearchParams params, SolrQuery query){
        if (params.boostedRegions){
//            String boost = params.boostedRegions.collect{"regionIso3166_2:${it.replace('-', '')}^${Math.pow(it.length(),2)}"}.join(" ")
//            boost = "${boost} name^1000 hashtag^800.0"
//            query.setParam("bq", boost)
            query.setParam("bf","ord(kuorumRelevance)^1 ord(relevance)^50 ord(regionIso3166_2Length)^100")
        }else{
            query.setParam("bf","ord(kuorumRelevance)^1 ord(relevance)^50")
        }
    }

    private void prepareWord(SearchParams params, SolrQuery query){
        String word = params.word
        if (params.boostedRegions){
            word = "${word} regionIso3166_2:${params.boostedRegions.collect{it.replace('-', '')}.join(" regionIso3166_2:")}"
        }
        if (!word){
            word = "*"
        }
        if (params.searchType == SearchType.CAUSE){
            word = "\"${word}\""
        }
        query.setParam(CommonParams.Q, word);
    }

    private void prepareFilter(SearchParams params, SolrQuery query){
        StringBuffer filterQuery = new StringBuffer("");
        String separator = ""
        if (params.type){
            filterQuery.append(separator)
            filterQuery.append("type:(${params.type})")
            separator = " AND "
        }

        if (params.regionIsoCodes){
            filterQuery.append(separator)
            String subQuery = convertToOrList(params.regionIsoCodes)
            filterQuery.append("regionIso3166_2:(${subQuery})")
            separator = " AND "
        }

        if (params.filteredUserIds){
            filterQuery.append(separator)
            filterQuery.append("-id:(${params.filteredUserIds.join(" ")})")
            separator = " AND "
        }

        if (filterQuery.length()){
            query.setParam(CommonParams.FQ, filterQuery.toString())
        }
    }

    private String convertToOrList(List data){
        def subTypeQuery = ""
        def OR = ""
        data.each {
            subTypeQuery += " ${OR} $it "
            OR = "OR"
        }
        return subTypeQuery
    }

    private ArrayList<String> prepareAutocompleteSuggestions(QueryResponse rsp){
//        def collations = rsp._spellInfo.suggestions.getAll("collation")
//        ArrayList<String> suggests = new ArrayList<String>(collations.size())
//        if (collations.size()>0){
//            rsp._spellInfo.suggestions.getAll("collation").each{
//                if (it.hits >  0){
//                    suggests.add(it.misspellingsAndCorrections.first().value)
//                }
//            }
//        }
//        suggests
        if (rsp?._spellInfo?.suggestions && rsp._spellInfo.suggestions.size()>0){
            rsp._spellInfo.suggestions.first().value.suggestion
        }else{
            []
        }

    }

    private def prepareSolrElements(QueryResponse rsp){
        ArrayList<SolrKuorumUser> kuorumUsers = []
        rsp.results.each{ SolrDocument solrDocument ->
            switch (SolrType.valueOf(solrDocument.type)){
                case SolrType.KUORUM_USER:
                    kuorumUsers.add(indexSolrService.recoverKuorumUserFromSolr(solrDocument))
                    break
                default:
                    log.warn("No se ha podido recuperar el elemento de sorl ${solrDocument}")
            }
        }
        [kuorumUsers:kuorumUsers]
    }

    SolrResults searchProjects(SearchProjects params){
        if (!params.validate()){
            KuorumExceptionUtil.createExceptionFromValidatable(params, "Se necesita una region para buscar por ella")
        }
        SolrQuery query = new SolrQuery();
        query.setParam(CommonParams.QT, "/select");
        query.setParam(CommonParams.START, "${params.offset}");
        query.setParam(CommonParams.SORT, "relevance desc, deadLine desc");
        query.setFacet(true)
        StringBuffer filterQuery = new StringBuffer("type:${SolrType.PROJECT}")
        if (params.commissionType){
            filterQuery.append(" AND ")
            filterQuery.append("commissions:${params.commissionType}")
        }

        if (params.regionName){
            filterQuery.append(" AND ")
            filterQuery.append("regionName:${ClientUtils.escapeQueryChars(params.regionName)}")
        }
        query.setParam(CommonParams.Q, filterQuery.toString())

        query.setFacet(true)
        query.addFacetField("commissions")
        query.addFacetField("regionName")
        query.addFacetField("subType")
        query.setFacetMinCount(1)
        query.setStart(params.offset.toInteger())
        query.setRows(params.max.toInteger())
        QueryResponse rsp = server.query( query, SolrRequest.METHOD.POST );
        SolrDocumentList docs = rsp.getResults();

        SolrResults solrResults = new SolrResults()
        solrResults.elements = docs.collect{indexSolrService.recoverSolrElementFromSolr(it)}
        solrResults.numResults = docs.numFound
        solrResults.facets = prepareFacets(rsp)
//        solrResults.elements
//        docs.collect{Project.get(it.id)}
        solrResults
    }


    public List<String> suggestTags(SearchParams params){
        if (!params.validate()){
            throw KuorumExceptionUtil.createExceptionFromValidatable(params,"Parametros de búsqueda erroneos")
        }
        SolrQuery query = new SolrQuery();
        query.setParam(CommonParams.QT, "/suggestTags");
        query.setParam("spellcheck.q", params.word);
        //query.setParam(TermsParams.TERMS_FIELD, "name", "username");
        prepareWord(params,query)
        prepareFilter(params, query)
        query.setParam("facet.prefix",params.word)
        QueryResponse rsp = server.query( query, SolrRequest.METHOD.POST );
        List<String> suggestions = prepareAutocompleteSuggestions(rsp)
        suggestions
    }

    /**
     * Returns a map of alias and names
     * @param search
     * @param boostedAlias
     * @return
     */
    public def suggestAlias(String search, List<String> boostedAlias, List<String> friendsAlias){
        SolrQuery query = new SolrQuery();
        query.setParam(CommonParams.QT, "/query");
        String searchEscapedSpaces = search.replace(" ", "\\ ")
        query.setParam(CommonParams.Q, "suggestAlias:${searchEscapedSpaces}* suggestName:${searchEscapedSpaces}*");
        query.setParam("qf", "suggestAlias^5.0 suggestName^1");
        query.setParam("bf", "ord(relevance)^0.5");
        query.setParam(CommonParams.ROWS, "5");
        query.setParam(CommonParams.FL, "alias,name,urlImage");
        query.setParam(CommonParams.FQ, "type:"+SolrType.KUORUM_USER);
        query.setParam("defType", "edismax");
        String boost = "";
        if (boostedAlias){
            boost= "suggestAlias:(${boostedAlias.join(' ')})^5";
        }
        if (friendsAlias){
            boost= boost + " suggestAlias:(${friendsAlias.join(' ')})^1"
        }
        if (boost){
            query.setParam("bq", boost);
        }

        query.setParam("facet", "false");
        query.setParam("spellcheck", "false");
        query.setParam("hl", "false");
        QueryResponse rsp = server.query( query, SolrRequest.METHOD.POST );
        def suggestions = rsp.results.collect{[alias:it.alias, name:it.name, avatar:it.urlImage]}
        suggestions
    }
}
