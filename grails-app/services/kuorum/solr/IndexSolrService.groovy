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

    def solrIndex(){

    }

    def fullIndex() {

        Date start = new Date()
//        createUserScore(start)
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
        if (server instanceof HttpSolrServer){
            Integer numIndexed = 0;
            HttpSolrServer httpSolrServer = (HttpSolrServer) server
            String baseUrl = httpSolrServer.getBaseURL()
            String path = "/dataimport"
            RESTClient mailKuorumServices = new RESTClient( baseUrl +path)
            def response = mailKuorumServices.get(
                    headers: ["User-Agent": "Kuorum Web"],
                    query:[command:'full-import', clean:'true', commit:'true', optimize:'true', wt:'json'],
                    requestContentType : groovyx.net.http.ContentType.JSON
            )
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

    SolrPost index(Post post){
        log.info("Indexing post: ${post}")
        indexDomainObject(post)
    }
    SolrKuorumUser index(KuorumUser user){
        log.info("Indexing user:${user}")
        indexDomainObject(user)
    }

    SolrProject index(Project project){
        log.info("Indexing project: ${project}")
        indexDomainObject(project)
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
        DBCollection bestUsersCollection = createUserScore(new Date());
        Map<String, Object> bestUserData = [:]
        DBObject limitFields = new BasicDBObject();
        limitFields.put("score",1)
        limitFields.put("numFollowers",1)
        DBObject mongoData = bestUsersCollection.findOne(new BasicDBObject("_id", kuorumUser.id), limitFields)
        if (mongoData){
            bestUserData = mongoData.toMap()
        }else{
            bestUserData = ["score":0, "numFollowers":0]
        }

        SolrType type = kuorumUser.userType==UserType.POLITICIAN?SolrType.POLITICIAN:SolrType.KUORUM_USER
        new SolrKuorumUser(
                id:kuorumUser.id.toString(),
                name: kuorumUser.toString(),
                type:type,
                subType:type.generateSubtype(kuorumUser),
                dateCreated:kuorumUser.dateCreated,
                commissions: kuorumUser.relevantCommissions,
                regionName: regionName,
                tags:kuorumUser.tags,
                regionIso3166_2: regionIso,
                urlImage: kuorumUser.avatar?.url,
                role:kuorumUser.gamification.activeRole,
                gender:kuorumUser.personalData.gender?:Gender.FEMALE,
                text:kuorumUser.bio,
                kuorumRelevance: bestUserData.score,
                numberPeopleInterestedFor: bestUserData.numFollowers,
                regionIso3166_2Length: regionIso.length()
        )
    }

    SolrKuorumUser recoverKuorumUserFromSolr(SolrDocument solrDocument){
        new SolrKuorumUser(
                id:new ObjectId(solrDocument.id),
                name:solrDocument.name,
                type:SolrType.valueOf(solrDocument.type),
                subType:SolrSubType.valueOf(solrDocument.subType),
                dateCreated:solrDocument.dateCreated,
                commissions: solrDocument.commissions.collect{CommissionType.valueOf(it)},
                urlImage: solrDocument.urlImage,
                tags: solrDocument.tags,
                role:GamificationAward.valueOf(solrDocument.role),
                gender: Gender.valueOf(solrDocument.gender),
                regionName: solrDocument.regionName,
                regionIso3166_2: solrDocument.regionIso3166_2,
                text:solrDocument.text,
                institutionName:solrDocument.institutionName,
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
                subType:SolrType.PROJECT.generateSubtype(project.status),
                text:project.description,
                dateCreated:project.dateCreated,
                tags:[project.hashtag],
                hashtag:project.hashtag,
                commissions:project.commissions,
//                institutionName:project.institution.name,
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
            case SolrType.POLITICIAN:   return recoverKuorumUserFromSolr(solrDocument); break;
            case SolrType.PROJECT:      return recoverProjectFromSolr(solrDocument); break;
            case SolrType.POST:         return recoverPostFromSolr(solrDocument); break;
            default: throw new KuorumException("No se ha reconocido el tipo ${solrDocument.type}")
        }
    }

    private Date chapuSyncReloadScore = new Date() -1

    private static  final Integer SCORE_POST_CREATED = 1;
    private static  final Integer SCORE_POST_DEBATE = 2;
    private static  final Integer SCORE_POST_DEFEND = 3;
    private static  final Integer SCORE_PROJECT_OPEN = 15;
    private static  final Integer SCORE_PROJECT_CLOSE = 2;
    public DBCollection createUserScore(Date startDate){

        def tempCollectionName = "bestPoliticians"
        DBCollection userScoredCollection = Post.collection.getDB().getCollection(tempCollectionName);
        //TODO: HACER ESTO MEJOR QUE CON ESTE SYNC CHAAAPUUU
        synchronized (this){
            Boolean reloadScore = chapuSyncReloadScore < new Date() -1
            if (!reloadScore && userScoredCollection.count()>0){
                return userScoredCollection
            }
            userScoredCollection.drop();
            chapuSyncReloadScore = chapuSyncReloadScore.clearTime()+1

            log.warn("Calculando SCORE. Operacion lenta. Hay que cachearla o hacerla por la noche")

            //TODO: CACHE THIS QUERY
            //TODO: Use startDate. Now is getting best politicians ever
            String mapPost = """
                function() {
                    if (this.defender != undefined){
                        emit(this.defender, ${SCORE_POST_DEFEND})
                    }
                    if (this.debates != undefined) {
                        this.debates.forEach( function(debate) {
                            emit(debate.kuorumUserId, ${SCORE_POST_DEBATE})
                        });
                    }
                    emit(this.owner, ${SCORE_POST_CREATED})
                }
            """

            String reducePost = """
                function(key, values) {
                    var acc = 0;
                    values.forEach( function(value) {
                        acc +=value;
                    });
                    return acc;
                }
            """

            DBObject queryPost = new BasicDBObject();
            DBObject existsDefender = new BasicDBObject(); existsDefender.put('$exists','1');
            queryPost.put("defender",existsDefender);
            DBObject sortResult = new BasicDBObject();
            sortResult.put('value',-1)

            DBCollection postCollection = Post.collection

            MapReduceCommand bestPoliticians = new MapReduceCommand(
                    postCollection,
                    mapPost,
                    reducePost ,
                    tempCollectionName,
                    MapReduceCommand.OutputType.MERGE,null);
            //        bestPoliticians.sort = sortByValue
            //        bestPoliticians.limit = pagination.max

            MapReduceOutput result = Post.collection.mapReduce(bestPoliticians)


            def addProjectScore = """
            function (){
                db.project.find().forEach(function(project){
                    var ownerScore = db.${tempCollectionName}.find({_id:project.owner})[0];
                    var projectScore = ${SCORE_PROJECT_CLOSE};
                    if (project.status=="${ProjectStatusType.OPEN}"){
                        projectScore = ${SCORE_PROJECT_OPEN};
                    }
                    if (ownerScore == undefined || ownerScore == null){
                        ownerScore = {
                            _id: project.owner,
                            value : 0
                        };
                    }
                    ownerScore.value = ownerScore.value + projectScore;
                    db.${tempCollectionName}.save(ownerScore);
                });
            }

            """

            userScoredCollection.getDB().eval(addProjectScore)

            def cpUserData = """
            function (){
                db.${tempCollectionName}.find().forEach(function(score){
                    var kuorumUser = db.kuorumUser.find({_id:score._id})[0];
                    var numFollowers = kuorumUser.followers.length;
                    var iso3166Length = 0;
                    if (kuorumUser.politicianOnRegion != undefined){
                        iso3166Length = kuorumUser.politicianOnRegion.iso3166_2.length
                    }
                    db.${tempCollectionName}.update(
                        {_id:score._id},
                        {\$set:{
                            numFollowers:numFollowers,
                            iso3166Length : iso3166Length,
                            user:kuorumUser,
                            score:score.value
                            }
                        }
                    );
                });
            }
            """
            userScoredCollection.getDB().eval(cpUserData)

            def cpPoliticiansWithOutScore = """
            function (){
                db.kuorumUser.find({userType:'${UserType.POLITICIAN}'}).forEach(function(politician){
                    var score = db.${tempCollectionName}.find({_id:politician._id})[0];
                    var iso3166Length = 0
                    if (politician.politicianOnRegion != undefined){
                        iso3166Length = politician.politicianOnRegion.iso3166_2.length
                    }
                    if (score == undefined){
                        var numFollowers = politician.followers.length
                        var newScore = {
                            _id: politician._id,
                            score : 0,
                            numFollowers:numFollowers,
                            iso3166Length : iso3166Length,
                            user:politician
                        };
                        db.${tempCollectionName}.save(newScore);
                    }
                });
            }
            """
            userScoredCollection.getDB().eval(cpPoliticiansWithOutScore)
            DBObject index = new BasicDBObject("score", -1)
            index.append("iso3166Length", -1)
            userScoredCollection.createIndex(index)

            DBObject removeFakePolitician = new BasicDBObject()
            removeFakePolitician.append("_id", new ObjectId("54ce5269e4b0d335a40f5ee3"));//Usuario congreso
            userScoredCollection.remove(removeFakePolitician);
            return userScoredCollection
        }
    }
}
