package kuorum.solr

import grails.transaction.Transactional
import kuorum.core.exception.KuorumExceptionUtil
import kuorum.core.model.CommissionType
import kuorum.core.model.search.SearchProjects
import kuorum.core.model.search.SearchParams
import kuorum.core.model.search.SearchPolitician
import kuorum.core.model.solr.*
import kuorum.project.Project
import org.apache.solr.client.solrj.SolrQuery
import org.apache.solr.client.solrj.SolrServer
import org.apache.solr.client.solrj.response.Group
import org.apache.solr.client.solrj.response.QueryResponse
import org.apache.solr.common.SolrDocument
import org.apache.solr.common.SolrDocumentList
import org.apache.solr.common.params.CommonParams

@Transactional(readOnly = true)
class SearchSolrService {

    SolrServer server
    IndexSolrService indexSolrService


    SolrResults search(SearchParams params) {

        SolrQuery query = new SolrQuery();
        query.setParam(CommonParams.QT, "/query");
        query.setParam(CommonParams.START, "${params.offset}");
        prepareFilter(params, query)

        QueryResponse rsp = server.query( query );
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
        if (rsp._spellInfo.suggestions.get("collation")){
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
        query.setParam(CommonParams.QT, "/suggest");
        query.setParam("spellcheck.q", params.word);
        //query.setParam(TermsParams.TERMS_FIELD, "name", "username");
        prepareFilter(params, query)
        query.setParam("facet.prefix",params.word)

        QueryResponse rsp = server.query( query );

        SolrAutocomplete solrAutocomplete = new SolrAutocomplete()
        solrAutocomplete.suggests = prepareAutocompleteSuggestions(rsp)
        def elements = prepareSolrElements(rsp)
        solrAutocomplete.kuorumUsers = elements.kuorumUsers
        solrAutocomplete.projects = elements.projects
        solrAutocomplete.numResults =rsp.results.numFound

        solrAutocomplete
    }

    private void prepareFilter(SearchParams params, SolrQuery query){
        query.setParam(CommonParams.Q, params.word);
//        if (params.type) query.setParam(CommonParams.FQ, "type:${params.type}")
        if (params.type){
            def subTypesQuery = ""
            def OR = ""
            params.type.each {
                subTypesQuery += " ${OR} $it "
                OR = "OR"
            }
            query.setParam(CommonParams.FQ, "type:(${subTypesQuery})")
        }
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
        if (rsp._spellInfo.suggestions.size()>0){
            rsp._spellInfo.suggestions.first().value.suggestion
        }else{
            []
        }

    }

    private def prepareSolrElements(QueryResponse rsp){
        ArrayList<SolrKuorumUser> kuorumUsers = []
        ArrayList<SolrProject> projects = []
        rsp.results.each{ SolrDocument solrDocument ->
            switch (SolrType.valueOf(solrDocument.type)){
                case SolrType.KUORUM_USER:
                    kuorumUsers.add(indexSolrService.recoverKuorumUserFromSolr(solrDocument))
                    break
                case SolrType.PROJECT:
                    projects.add(indexSolrService.recoverProjectFromSolr(solrDocument))
                    break
                case SolrType.POST:
                    break
                default:
                    log.warn("No se ha podido recuperar el elemento de sorl ${solrDocument}")
            }
        }
        [projects:projects, kuorumUsers:kuorumUsers]
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
        if (params.projectStatusType){
            filterQuery.append(" AND ")
            filterQuery.append("subType:${SolrType.PROJECT.generateSubtype(params.projectStatusType)}")
        }
        if (params.regionName){
            filterQuery.append(" AND ")
            filterQuery.append("regionName:${params.regionName}")
        }
        query.setParam(CommonParams.Q, filterQuery.toString())

        query.setFacet(true)
        query.addFacetField("commissions")
        query.addFacetField("regionName")
        query.addFacetField("subType")
        query.setFacetMinCount(1)
        query.setStart(params.offset.toInteger())
        query.setRows(params.max.toInteger())
        QueryResponse rsp = server.query( query );
        SolrDocumentList docs = rsp.getResults();

        SolrResults solrResults = new SolrResults()
        solrResults.elements = docs.collect{indexSolrService.recoverSolrElementFromSolr(it)}
        solrResults.numResults = docs.numFound
        solrResults.facets = prepareFacets(rsp)
//        solrResults.elements
//        docs.collect{Project.get(it.id)}
        solrResults
    }

    List<SolrProjectsGrouped> listProjects(SearchProjects params){
        if (!params.validate()){
            KuorumExceptionUtil.createExceptionFromValidatable(params, "Se necesita una region para buscar por ella")
        }
        SolrQuery query = new SolrQuery();
        query.setParam(CommonParams.QT, "/select");
        query.setParam(CommonParams.ROWS, "${params.max}");
        query.setParam(CommonParams.Q, "regionName:\"${params.regionName}\"")
        query.setParam("q.op", "AND")
        def fq = ["type:${SolrType.PROJECT}"]

        if (params.projectStatusType){
            fq << "subType:${SolrSubType.fromOriginalType(params.projectStatusType)}"
        }
        if (params.commissionType){
            fq << "commissions:${params.commissionType}"
        }
        query.setParam(CommonParams.FQ, "(${fq.sum{ it +" " }})")

        query.setParam("group", true)
        query.setParam("group.field", "commission_group")
        query.setParam("group.limit","${params.max}")
        query.setParam("group.offset","${params.offset}")
        query.setParam("group.sort","name desc")
//        query.setParam(CommonParams.SORT, "name desc") // Default is score

        QueryResponse rsp = server.query( query );
        SolrDocumentList docs = rsp.getResults();
        List<SolrProjectsGrouped> groupedElements= []

        rsp.groupResponse.values[0].values.each{Group group ->

            SolrProjectsGrouped element = new SolrProjectsGrouped()
            element.commission = CommissionType.valueOf(group.groupValue)
            element.elements = group.result.collect{doc ->indexSolrService.recoverProjectFromSolr(doc)}
            groupedElements.add(element)
        }

        groupedElements
    }

    List<SolrPoliticiansGrouped> listPoliticians(SearchPolitician params){
        if (!params.validate()){
            KuorumExceptionUtil.createExceptionFromValidatable(params, "Se necesita una institución para buscar")
        }
        SolrQuery query = new SolrQuery();
        query.setParam(CommonParams.QT, "/select");
        query.setParam(CommonParams.ROWS, "${params.max}");
        query.setParam("q.op", "AND")

        def q = "institutionName:\"${params.institutionName}\""
        def fq = ["type:${SolrType.KUORUM_USER}","subType:${SolrSubType.POLITICIAN}"]
        if (params.regionIso3166_2){
            fq << "regionIso3166_2:${params.regionIso3166_2}"
        }
        query.setParam(CommonParams.Q, q)
        query.setParam(CommonParams.FQ, "(${fq.sum{ it +" " }})")


        query.setParam("group", true)
        query.setParam("group.field", "regionIso3166_2")
        query.setParam("group.limit","${params.max}")
        query.setParam("group.offset","${params.offset}")
        query.setParam("group.sort","name desc")
//        query.setParam(CommonParams.SORT, "name desc") // Default is score

        QueryResponse rsp = server.query( query );
        SolrDocumentList docs = rsp.getResults();
        List<SolrPoliticiansGrouped> groupedElements= []

        rsp.groupResponse.values[0].values.each{Group group ->

            SolrPoliticiansGrouped element = new SolrPoliticiansGrouped()
            element.politicians = group.result.collect{doc ->indexSolrService.recoverSolrElementFromSolr(doc)}
            element.regionName = element.politicians[0].regionName
            element.iso3166_2 = element.politicians[0].regionIso3166_2
            groupedElements.add(element)
        }

        groupedElements
    }
}
