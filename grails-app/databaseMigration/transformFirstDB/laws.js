
load("htmlDecoder.js")
dbOrigin = connect("localhost:27017/KuorumWeb");
dbDest = connect("localhost:27017/KuorumDev");

//db.notification.update({},{$set:{dismiss:false}},{multi:true})
//db.message.update({_class: "NormalDebate"},{$set:{convertAsLeader:false}},{multi:true})
//db.secUser.remove({"_id": ObjectId("52927047e4b02ef3bc0ed83f")})

dbOrigin.generalLaw.find({_class:"Law"}).forEach(function(law){
    var destLaw = createLawFromOldLaw(law)
    dbDest.law.insert(destLaw)
    print("ley creada:"+destLaw.hashtag +(destLaw._id))
    dbOrigin.message.find().forEach(function(message){
        var destPost = createPostFromOldPost(destLaw,message)
        dbDest.post.insert(destPost)
        var cluck = createFirstCluck(destPost)
        dbDest.cluck.insert(cluck)
        print("ley ("+destLaw.hashtag +") => Post:"+destPost._id)
        createPostVotesFromLikes(destPost, message)
        createCommentsFromSubMessages(destPost, message)
    });
});

function createLawFromOldLaw(law){
    var id = new ObjectId();
//    var regionn = dbDest.
    var destLaw = {
//        "_id" :law._id,
        "_id":id,
        "commissions" : translateCommissions(law.commissions),
        "dateCreated" : law.dateCreated,
        "description" : law.longDescription,
        "hashtag" : "#"+law.shortTitle.replace(/ /g, '').toLowerCase(),
        "institution" : ObjectId("532828c644aeeccebb29a606"),
        "introduction" : law.brief,
        "open" : law.lawStatus=="OPEN",
        "published" : law.published!=null,
        "realName" : law.officialTitle,
        "region" : {
            "_id" : ObjectId("532828c644aeeccebb29a605"),
            "iso3166_2" : "EU-ES",
            "name" : "España",
            "superRegion" : ObjectId("532828c644aeeccebb29a604"),
            "version" : NumberLong(0)
        },
        "shortName" : law.shortTitle,
        "version" : 0
    }
    return destLaw
}

function translateCommissions(commissions){
    var translate = {}
    translate["DEVELOPING"]="RESEARCH_DEVELOP"

    Object.keys(translate).forEach(function(key){
        var index = commissions.indexOf(key);

        if (index !== -1) {
            commissions[index] = translate[key];
        }
    })
    return commissions
}

function createPostFromOldPost(destLaw,message){
    var destPost = {
        _id:message._id,
        law:destLaw._id,
        numClucks:1,
        numVotes:message.userIdLikes.length,
        owner:message.user,
        dateCreated:message.dateCreated,
        postType:"PURPOSE",
        title:HtmlDecode(message.title),
        text:HtmlDecode(message.message),
        published:true,
        victory:false,
        comments:[],
        debates:[]
    }
    return destPost
}

function createFirstCluck(post){
    var cluck ={
        owner:post.owner,
        postOwner:post.owner,
        supportedBy:null,
        sponsors:[],
        isFirstCluck:true,
        law:post.law,
        post:post._id,
        dateCreated:post.dateCreated,
        lastUpdated:post.dateCreated
    }
    return cluck
}

function createPostVotesFromLikes(destPost,message){
    if (message.userIdLikes.length>0){
        message.userIdLikes.forEach(function(idUser){
            var kuorumUser = dbDest.kuorumUser.find({_id:idUser})[0]
            if (kuorumUser != undefined){
//                    print("Creando voto:(["+idUser+"])")
                var postVote = {
                    post:destPost._id,
                    user:kuorumUser._id,
                    personalData:kuorumUser.personalData,
                    dateCreated: new Date()
                }
                dbDest.postVote.insert(postVote)
            }
        })
    }else{
//        print("post sin likes")
    }
    destPost.numVotes = dbDest.postVote.find({post:destPost._id}).count()//Likes from non existing users are removed
    dbDest.post.save(destPost)
}

function createCommentsFromSubMessages(destPost, message){
    if (message.messages!= undefined && message.messages.length>0){
        var comments = []
        message.messages.forEach(function(subComment){
            var comment = {
                kuorumUserId:subComment.userId,
                text:HtmlDecode(subComment.message),
                dateCreated:new Date(),
                moderated:false,
                deleted:false
            }
            comments.push(comment)
        })
        destPost.comments = comments
        dbDest.post.save(destPost)
    }else{
        print("post sin mensajes:")
    }
}