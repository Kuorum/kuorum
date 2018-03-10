package kuorum.solr

import grails.transaction.Transactional
import groovy.json.JsonSlurper
import groovy.time.TimeCategory
import groovy.time.TimeDuration
import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.RESTClient
import kuorum.post.Post
import kuorum.project.Project
import kuorum.users.KuorumUser
import org.apache.solr.client.solrj.SolrServer
import org.apache.solr.client.solrj.impl.HttpSolrServer
import org.springframework.beans.factory.annotation.Value

@Transactional
class IndexSolrService {

    def grailsApplication

    private final def CLASSNAMES_TO_INDEX = [KuorumUser.name, Project.name, Post.name]

    SolrServer server

    @Value('${solr.bulkUpdateQuentity}')
    Integer solrBulkUpdateQuantity = 1000

    def clearIndex(){
        log.warn("Clearing solr index")
        server.deleteByQuery("*:*")
        server.commit()
    }

    def fullIndex() {

        Date start = new Date()
        def res = [:]
        log.warn("Reindexing all mongo")
        Integer numIndexed = solrFullIndex()
        TimeDuration td = TimeCategory.minus( new Date(), start )

        log.info("Indexed $numIndexed docs. Time indexing: ${td}" )
        res.put("total", numIndexed)
        res.put("time", td)
        res
    }

    private Integer solrFullIndex(){
        solrIndex([command:'full-import', clean:'true', commit:'true', optimize:'true', wt:'json'], true)
    }

    private Integer solrDeltaIndex(){
        solrIndex([command: 'delta-import', commit: 'true', wt: 'json'])
    }
    private Integer solrIndex(Map options, Boolean waitEnd = false){
        if (server instanceof HttpSolrServer){
            Integer numIndexed = 0;
            HttpSolrServer httpSolrServer = (HttpSolrServer) server
            String baseUrl = httpSolrServer.getBaseURL()
            String path = "/dataimport"
            RESTClient mailKuorumServices = new RESTClient( baseUrl +path)
            def response = mailKuorumServices.get(
                    headers: ["User-Agent": "Kuorum Web"],
                    query:options,
                    requestContentType : groovyx.net.http.ContentType.JSON
            )
            if (waitEnd){
                // ESPERAR A QUE TERMINE LA IMPORTACION - NO LO QUEREMOS
                boolean importing = true;
                def jsonSlurper = new JsonSlurper()
                while (importing){
                    HttpResponseDecorator responseStatus = mailKuorumServices.get(
                            headers: ["User-Agent": "Kuorum Web"],
                            query:[command:'status', wt:'json'],
                            requestContentType : groovyx.net.http.ContentType.JSON
                    )

                    def responseStatusData = jsonSlurper.parse(responseStatus.data)
                    Thread.sleep(1000)
                    if (responseStatusData."status" != "busy"){
                        importing = false;
                        numIndexed = Integer.parseInt(responseStatusData."statusMessages"."Total Rows Fetched")
                    }
                }
                return numIndexed
            }else{
                return -1;
            }
        }else{
            log.error("Trying to index via solr but the server is not http it is ${server.class.name}")
        }
    }

    void index(KuorumUser user){
//        Delta index is going to be via quartz
//        log.info("Indexing user:${user}")
        solrDeltaIndex()
    }

    public void deltaIndex(){
        this.solrDeltaIndex()
    }

    private void deleteDocument(String id){
//        server.deleteByQuery("id:${id}")
        server.deleteById(id)
        server.commit()
    }

    void delete(KuorumUser user){
        deleteDocument(user.id.toString())
    }
}
