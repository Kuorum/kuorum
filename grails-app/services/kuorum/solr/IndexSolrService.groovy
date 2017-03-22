package kuorum.solr

import com.mongodb.*
import grails.transaction.Transactional
import groovy.json.JsonSlurper
import groovy.time.TimeCategory
import groovy.time.TimeDuration
import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.RESTClient
import kuorum.Region
import kuorum.core.exception.KuorumException
import kuorum.core.model.CommissionType
import kuorum.core.model.Gender
import kuorum.core.model.ProjectStatusType
import kuorum.core.model.UserType
import kuorum.core.model.gamification.GamificationAward
import kuorum.core.model.solr.*
import kuorum.post.Post
import kuorum.project.Project
import kuorum.users.KuorumUser
import org.apache.solr.client.solrj.SolrServer
import org.apache.solr.client.solrj.impl.HttpSolrServer
import org.apache.solr.common.SolrDocument
import org.apache.solr.common.SolrInputDocument
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Value

import javax.annotation.PreDestroy
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

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
            log.warn("Trying to index via solr but the server is not http it is ${server.class.name}")
            return programaticFullIndex()
        }
    }

    private Integer programaticFullIndex(){
        clearIndex()
        Integer numIndexed = 0;
        log.info("BulkUpdates: $solrBulkUpdateQuantity")

        CLASSNAMES_TO_INDEX.each{className ->
            Integer numPartialIndexed = indexByClassName(className)
            res.put(className, numPartialIndexed)
            numIndexed += numPartialIndexed
            System.gc()
        }
        return numIndexed;
    }

    void index(Post post){
//        Delta index is going to be via quartz
//        log.info("Indexing post: ${post}")
//        solrDeltaIndex()
    }
    void index(KuorumUser user){
//        Delta index is going to be via quartz
//        log.info("Indexing user:${user}")
//        solrDeltaIndex()
    }

    void index(Project project){
//        Delta index is going to be via quartz
//        log.info("Indexing project: ${project}")
//        solrDeltaIndex()
    }

    public void deltaIndex(){
        this.solrDeltaIndex()
    }

    private void deleteDocument(String id){
        server.deleteByQuery("id:${id}")
        server.commit()
    }
    void delete(Project project){
        deleteDocument(project.id.toString())
    }
    void delete(Post post){
        deleteDocument(post.id.toString())
    }
    void delete(KuorumUser user){
        deleteDocument(user.id.toString())
    }

    @Deprecated
    private SolrElement indexDomainObject(element){
        SolrElement solrElement = createSolrElement(element)
        if (solrElement){//Si es null es que no se indexa por alguna razon
            SolrInputDocument solrInputDocument = createSolrInputDocument(solrElement)
            server.add(solrInputDocument)
            server.commit()
        }
        solrElement
    }

    private Integer indexByClassName(String className){
        Integer numIndexed = 0
        log.info("Indexing $className")
        Date start = new Date()

        java.util.ArrayList<SolrInputDocument> solrDocuments = new ArrayList<SolrInputDocument>(solrBulkUpdateQuantity)
        DBCollection collectionToIndex = grailsApplication.getClassForName(className).collection
        DBCursor cursor = collectionToIndex.find()
        cursor.batchSize(solrBulkUpdateQuantity)
        while (cursor.hasNext()){
            DBObject id = cursor.next()
            def mongoData = grailsApplication.getClassForName(className).get(id.get("_id"));
            SolrElement solrElement = createSolrElement(mongoData )
            if(solrElement){
                SolrInputDocument solrInputDocument = createSolrInputDocument(solrElement)
                solrDocuments.add(solrInputDocument)
                if (solrDocuments.size() == solrBulkUpdateQuantity){
                    server.add(solrDocuments)
                    server.commit()
                    log.info("Commited ${solrBulkUpdateQuantity} documents")
                    numIndexed += solrDocuments.size()
                    solrDocuments.clear()
                    System.gc()
                }
            }
        }
        if (solrDocuments){
            server.add(solrDocuments)
            server.commit()
            numIndexed += solrDocuments.size()
        }
        server.optimize()
        TimeDuration td = TimeCategory.minus( new Date(), start )
        log.info("Indexed $numIndexed '${className}'. Time indexing: ${td}" )
        numIndexed
    }

    private SolrInputDocument createSolrInputDocument(SolrElement solrElement){
        SolrInputDocument solrInputDocument = new SolrInputDocument()
        solrElement.properties.each{k,v->
            if (k!="class" && k!="highlighting")
                solrInputDocument.addField(k,v)
        }
        solrInputDocument
    }

    SolrPost createSolrElement(Post post){
        if (!post.published){
            log.info("No se indexa el post ${post.id} porque no está publicado")
            return null // Skipping because is not published

        }

        new SolrPost(
            id:post.id.toString(),
            name:post.title,
            type:SolrType.POST,
            text:post.text,
            dateCreated:post.dateCreated,
            tags:[post.project.hashtag],
            hashtagProject:post.project.hashtag,
            owner:"${post.owner.name}",
            ownerId: "${post.owner.id.toString()}",
            victory: post.victory,
            commissions: post.project.commissions,
            regionName: post.project.region.name,
//            institutionName:post.project.institution.name,
            regionIso3166_2: post.project.region.iso3166_2,
            urlImage: post.multimedia?.url,
            kuorumRelevance: 0,
            numberPeopleInterestedFor: post.numVotes,
            regionIso3166_2Length: post.project.region.iso3166_2.length()
        )
    }

    SolrPost recoverPostFromSolr(SolrDocument solrDocument){
        new SolrPost(
                id:new ObjectId(solrDocument.id),
                name:solrDocument.name,
                type:SolrType.valueOf(solrDocument.type),
                text:solrDocument.text,
                dateCreated:solrDocument.dateCreated,
                hashtagProject:solrDocument.hashtagProject,
                owner:solrDocument.owner,
                ownerId:solrDocument.ownerId,
                victory:solrDocument.victory,
                commissions: solrDocument.commissions.collect{CommissionType.valueOf(it)},
                regionName:solrDocument.regionName,
                institutionName:solrDocument.institutionName,
                regionIso3166_2: solrDocument.regionIso3166_2,
                urlImage: solrDocument.urlImage,
                kuorumRelevance: solrDocument.kuorumRelevance,
                numberPeopleInterestedFor: solrDocument.numberPeopleInterestedFor,
                regionIso3166_2Length: solrDocument.regionIso3166_2Length
        )
    }


    SolrKuorumUser createSolrElement(KuorumUser kuorumUser){
        //new SolrKuorumUser(kuorumUser.properties.findAll { k, v -> k in SolrKuorumUser.metaClass.properties*.name} )
        String regionName = ""
        String regionIso = ""
        String postalCode = null
        if (UserType.ORGANIZATION.equals(kuorumUser.userType)){
            //TODO: PENSAR QUE HUEVOS ES PARA LAS EMPRESAS
            Region spain = Region.findByIso3166_2("EU-ES")
            regionName = spain.name
            regionIso = spain.iso3166_2
        }else{
            //User or Politicians
//            if (!kuorumUser.personalData.postalCode || !kuorumUser.personalData.gender){
            if (kuorumUser.authorities.find{it.authority=="ROLE_INCOMPLETE_USER"}){
                log.info("No se indexa al usuario ${kuorumUser.email} porque no tenemos sus datos básicos")
                return null // Skipping user because we don't have basic data
            }

            if (kuorumUser?.professionalDetails?.region){
                regionName = kuorumUser.professionalDetails.region.name
                regionIso = kuorumUser.professionalDetails.region.iso3166_2
                postalCode = kuorumUser?.personalData?.postalCode?:"00000"
            }else if (!kuorumUser.personalData.postalCode){
                //Usuarios de antigua web que se indexen
                regionName = "España"
                regionIso = "EU-ES"
                postalCode = "00000"
            }else{
                regionName = kuorumUser.personalData.province.name
                regionIso = kuorumUser.personalData.province.iso3166_2
                postalCode = kuorumUser.personalData.postalCode
            }
        }

        new SolrKuorumUser(
                id:kuorumUser.id.toString(),
                name: kuorumUser.toString(),
                type:SolrType.KUORUM_USER,
                dateCreated:kuorumUser.dateCreated,
                regionName: regionName,
//                tags:kuorumUser.tags,
                regionIso3166_2: regionIso,
                urlImage: kuorumUser.avatar?.url,
                gender:kuorumUser.personalData.gender?:Gender.FEMALE,
                text:kuorumUser.bio,
                kuorumRelevance: 0,
                numberPeopleInterestedFor: 0,
                regionIso3166_2Length: regionIso.length()
        )
    }

    SolrKuorumUser recoverKuorumUserFromSolr(SolrDocument solrDocument){
        new SolrKuorumUser(
                id:new ObjectId(solrDocument.id),
                name:solrDocument.name,
                type:SolrType.valueOf(solrDocument.type),
                dateCreated:solrDocument.dateCreated,
                urlImage: solrDocument.urlImage,
                tags: solrDocument.tags,
                gender: Gender.valueOf(solrDocument.gender),
                regionName: solrDocument.regionName,
                regionIso3166_2: solrDocument.regionIso3166_2,
                text:solrDocument.text,
                kuorumRelevance: solrDocument.kuorumRelevance,
                numberPeopleInterestedFor: solrDocument.numberPeopleInterestedFor,
                regionIso3166_2Length: solrDocument.regionIso3166_2Length
        )
    }

    SolrProject createSolrElement(Project project){
        if (!project.published){
            log.info("No se indexa el post ${project.hashtag} porque no está publicada")
            return null // Skipping because is not published
        }
        SolrProject solrProject = new SolrProject(
                id:project.id.toString(),
                name:project.shortName,
                type:SolrType.PROJECT,
                text:project.description,
                dateCreated:project.dateCreated,
                tags:[project.hashtag],
                hashtag:project.hashtag,
                regionName: project.region.name,
                regionIso3166_2: project.region.iso3166_2,
                urlImage: project.image?.url,
                owner:project.owner.name,
                ownerId:project.owner.id,
                relevance: project.relevance,
                deadLine:project.deadline,
                kuorumRelevance: 0,
                numberPeopleInterestedFor: project.peopleVotes.total,
                regionIso3166_2Length:project.region.iso3166_2.length()
        )
        //Ñapilla para poder agrupar por comision que solr no puede agrupar con listados
        solrProject.metaClass.commission_group = project.commissions[0]
        solrProject
    }

    SolrProject recoverProjectFromSolr(SolrDocument solrDocument){
        SolrSubType solrSubType = null;
        try{
            solrSubType = SolrSubType.valueOf(solrDocument.subType)
        }catch (Exception e){
            //TODO: FIX Rápido para que a ABBY le funcione y no de nullPointer
            log.error("No se ha reconocido el tipo de projecto: ${solrDocument.subType}. Hay algo mal indexado. Se supone que el proyecto está cerrado")
            solrSubType = SolrSubType.REJECTED;

        }
        new SolrProject(
                id:new ObjectId(solrDocument.id),
                name:solrDocument.name,
                type:SolrType.valueOf(solrDocument.type),
                subType:solrSubType,
                text:solrDocument.text,
                dateCreated:solrDocument.dateCreated,
                hashtag:solrDocument.hashtag,
                commissions: solrDocument.commissions.collect{CommissionType.valueOf(it)},
                institutionName:solrDocument.institutionName,
                regionName:solrDocument.regionName,
                regionIso3166_2: solrDocument.regionIso3166_2,
                urlImage: solrDocument.urlImage,
                owner:solrDocument.owner,
                ownerId: solrDocument.ownerId,
                relevance: solrDocument.relevance,
                deadLine:solrDocument.deadLine,
                kuorumRelevance: solrDocument.kuorumRelevance,
                numberPeopleInterestedFor: solrDocument.numberPeopleInterestedFor,
                regionIso3166_2Length: solrDocument.regionIso3166_2Length
        )
    }

    SolrElement recoverSolrElementFromSolr(SolrDocument solrDocument){
        switch (SolrType.valueOf(solrDocument.type)){
            case SolrType.KUORUM_USER:
            case SolrType.CANDIDATE:
            case SolrType.ORGANIZATION:
            case SolrType.POLITICIAN:   return recoverKuorumUserFromSolr(solrDocument); break;
            case SolrType.PROJECT:      return recoverProjectFromSolr(solrDocument); break;
            case SolrType.POST:         return recoverPostFromSolr(solrDocument); break;
            default: throw new KuorumException("No se ha reconocido el tipo ${solrDocument.type}")
        }
    }
}
