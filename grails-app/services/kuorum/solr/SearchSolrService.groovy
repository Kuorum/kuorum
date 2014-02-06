package kuorum.solr

import grails.transaction.Transactional
import org.apache.solr.client.solrj.SolrQuery
import org.apache.solr.client.solrj.SolrServer
import org.apache.solr.client.solrj.impl.HttpSolrServer
import org.apache.solr.client.solrj.response.QueryResponse
import org.apache.solr.common.SolrDocumentList
import org.apache.solr.common.params.CommonParams
import org.apache.solr.common.params.TermsParams

@Transactional(readOnly = true)
class SearchSolrService {

    //CUando esto crezca pasarlo a CLOUD
    String solrUrl  = "http://localhost:9080/solr/kuorumUsers"


    def search(String word) {

        SolrServer server = new HttpSolrServer(solrUrl);

        SolrQuery query = new SolrQuery();
        query.setQuery( "username:$word" );

        QueryResponse rsp = server.query( query );
        SolrDocumentList docs = rsp.getResults();

        docs
    }

    def suggest(String word){

        SolrServer server = new HttpSolrServer(solrUrl);

        SolrQuery query = new SolrQuery();
        query.setParam(CommonParams.QT, "/suggest");
        query.setParam(TermsParams.TERMS_FIELD, "name", "username");
        query.setParam("q", word);

        QueryResponse rsp = server.query( query );
        SolrDocumentList docs = rsp.getResults();

        rsp

    }
}
