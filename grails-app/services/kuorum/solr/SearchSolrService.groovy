package kuorum.solr

import grails.transaction.Transactional
import kuorum.core.model.solr.SolrAutocomplete
import kuorum.core.model.solr.SolrKuorumUser
import kuorum.core.model.solr.SolrLaw

import kuorum.core.model.solr.SolrType
import org.apache.solr.client.solrj.SolrQuery
import org.apache.solr.client.solrj.SolrServer
import org.apache.solr.client.solrj.response.QueryResponse
import org.apache.solr.common.SolrDocument
import org.apache.solr.common.SolrDocumentList
import org.apache.solr.common.params.CommonParams

@Transactional(readOnly = true)
class SearchSolrService {

    SolrServer server
    IndexSolrService indexSolrService


    def search(String word) {

        SolrQuery query = new SolrQuery();
        query.setQuery( "name:$word" );

        QueryResponse rsp = server.query( query );
        SolrDocumentList docs = rsp.getResults();

        docs
    }

    SolrAutocomplete suggest(String word){

        SolrQuery query = new SolrQuery();
        query.setParam(CommonParams.QT, "/suggest");
        //query.setParam(TermsParams.TERMS_FIELD, "name", "username");
        query.setParam(CommonParams.Q, word);
        query.setParam("facet.prefix",word)

        QueryResponse rsp = server.query( query );

        SolrAutocomplete solrAutocomplete = new SolrAutocomplete()
        solrAutocomplete.suggests = prepareSuggestions(rsp)
        def elements = prepareSolrElements(rsp)
        solrAutocomplete.kuorumUsers = elements.kuorumUsers
        solrAutocomplete.laws = elements.laws
        solrAutocomplete.numResults =rsp.results.numFound

        solrAutocomplete
    }

    private ArrayList<String> prepareSuggestions(QueryResponse rsp){
        ArrayList<String> suggests = new ArrayList<String>(rsp.facetFields.size())
        rsp.facetFields[0].values.each{suggest ->
            suggests.add( suggest.name)
        }
        suggests
    }

    private def prepareSolrElements(QueryResponse rsp){
        ArrayList<SolrKuorumUser> kuorumUsers = []
        ArrayList<SolrLaw> laws = []
        rsp.results.each{ SolrDocument solrDocument ->
            switch (SolrType.valueOf(solrDocument.type)){
                case SolrType.KUORUM_USER:
                    kuorumUsers.add(indexSolrService.recoverKuorumUserFromSolr(solrDocument))
                    break
                case SolrType.LAW:
                    laws.add(indexSolrService.recoverLawFromSolr(solrDocument))
                    break
                case SolrType.POST:
                    break
                default:
                    log.warn("No se ha podido recuperar el elemento de sorl ${solrElement.id}")
            }
        }
        [laws:laws, kuorumUsers:kuorumUsers]
    }
}
