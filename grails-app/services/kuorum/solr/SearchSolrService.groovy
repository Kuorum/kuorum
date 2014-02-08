package kuorum.solr

import grails.transaction.Transactional
import org.apache.solr.client.solrj.SolrQuery
import org.apache.solr.client.solrj.SolrServer
import org.apache.solr.client.solrj.impl.HttpSolrServer
import org.apache.solr.client.solrj.response.QueryResponse
import org.apache.solr.common.SolrDocumentList
import org.apache.solr.common.params.CommonParams
import org.apache.solr.common.params.TermsParams
import org.springframework.beans.factory.annotation.Value

@Transactional(readOnly = true)
class SearchSolrService {

    SolrServer server


    def search(String word) {

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
