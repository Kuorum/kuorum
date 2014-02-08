import grails.spring.BeanBuilder
import kuorum.solr.IndexSolrService
import kuorum.solr.SearchSolrService
import kuorum.springSecurity.MongoUserDetailsService
import org.apache.solr.client.solrj.impl.HttpSolrServer
import org.springframework.beans.factory.annotation.Value

// Place your Spring DSL code here
beans = {
   userDetailsService(MongoUserDetailsService)

    String solrUrl =application.config?.solr.solrUrl

    def bb = new BeanBuilder()
    bb.beans{
        solrServer(HttpSolrServer,solrUrl)

    }
//   def solrUrl
   //def solrServer = new HttpSolrServer("http://localhost:9080/solr/kuorumUsers")

   searchSolrService(SearchSolrService){
       server = solrServer
   }

    indexSolrService(IndexSolrService){
        server = solrServer
    }

}
