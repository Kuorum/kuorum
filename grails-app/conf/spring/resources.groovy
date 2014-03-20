import grails.spring.BeanBuilder
import kuorum.solr.IndexSolrService
import kuorum.solr.SearchSolrService
import kuorum.springSecurity.MongoUserDetailsService
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer
import org.apache.solr.client.solrj.impl.HttpSolrServer
import org.springframework.beans.factory.annotation.Value
import org.apache.solr.core.CoreContainer
import grails.util.Environment

// Place your Spring DSL code here
beans = {
   userDetailsService(MongoUserDetailsService)



    def bb = new BeanBuilder()
    bb.beans{
        if (Boolean.parseBoolean(application.config.solr.embedded)){
//            System.setProperty("solr.solr.home", application.config.solr.solrHome);
            String solrDir = application.config.solr.solrHome;
            File solrConfig = new File("$solrDir/solr.xml")
            CoreContainer container = CoreContainer.createAndLoad(solrDir,solrConfig);

//            container.load();
//            EmbeddedSolrServer server = new EmbeddedSolrServer( container, "core name as defined in solr.xml" );
            solrServer(EmbeddedSolrServer,container,container.allCoreNames[0])
        }else{
            String solrUrl =application.config?.solr.solrUrl
            solrServer(HttpSolrServer,solrUrl)
        }
    }
//   def solrUrl
   //def solrServer = new HttpSolrServer("http://localhost:9080/solr/kuorumUsers")

    indexSolrService(IndexSolrService){
        server = solrServer
        grailsApplication = ref('grailsApplication')
    }
   searchSolrService(SearchSolrService){
       server = solrServer
       indexSolrService = indexSolrService
   }


    xmlns task:"http://www.springframework.org/schema/task"

    task.'annotation-driven'('proxy-target-class':true, 'mode':'proxy')


}
