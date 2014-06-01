
load("htmlDecoder.js")
load("imageHelper.js")
dbOrigin = connect("localhost:27017/KuorumWeb");
dbDest = connect("localhost:27017/KuorumDev");

//db.notification.update({},{$set:{dismiss:false}},{multi:true})
//db.message.update({_class: "NormalDebate"},{$set:{convertAsLeader:false}},{multi:true})
//db.secUser.remove({"_id": ObjectId("52927047e4b02ef3bc0ed83f")})

dbOrigin.generalLaw.find({_class:"Law"}).forEach(function(law){
    var destLaw = createLawFromOldLaw(law)
    dbDest.law.insert(destLaw)
    print("ley creada:"+destLaw.hashtag +(destLaw._id))
    dbOrigin.message.find({law:law._id}).forEach(function(message){
        var destPost = createPostFromOldPost(destLaw,message)
        var existsOwner = dbDest.kuorumUser.count({_id:destPost.owner})
        if (existsOwner == 1){

            dbDest.post.insert(destPost)
            var cluck = createFirstCluck(destPost)
            dbDest.cluck.insert(cluck)
            print("ley ("+destLaw.hashtag +") => Post:"+destPost.title)
            createPostVotesFromLikes(destPost, message)
            createCommentsFromSubMessages(destPost, message)
        }
    });
});

function updateUserActivity(post){
    var owner = dbDest.kuorumUser.find({_id:post.owner}).next()
    if ("PURPOSE" == post.postType){
        owner.activity.numPurposes ++
        owner.activity.purposes.push(post.id)
    }else if("QUESTION" == post.postType){
        owner.activity.numQuestions ++
        owner.activity.questions.push(post.id)
    }else if("HISTORY" == post.postType){
        owner.activity.numHistories ++
        owner.activity.histories.push(post.id)
    }
    dbDest.kuorumUser.insert(owner)
}

function createLawFromOldLaw(law){
    var id = new ObjectId();
    var institution = dbDest.institution.find({name:/Parlamento Espa.*/})[0]
    var spain = dbDest.region.find({"iso3166_2" : "EU-ES"})[0]
    if (law.purposedBy !=undefined)
        var parliamentaryGroup = dbDest.parliamentaryGroup.find({name:law.purposedBy.name, 'region._id':spain._id})[0]
    var status = law.lawStatus=="OPEN"?"OPEN":law.approvedInCongress?"APPROVED":"REJECTED"
    var peopleVotes = createPeopleVotes(law, id)
    var destLaw = {
//        "_id" :law._id,
        "_id":id,
        "commissions" : translateCommissions(law.commissions),
        "urlPdf":law.urlPdf==undefined?'':law.urlPdf.split("#")[0], //removes data after #
        "dateCreated" : law.dateCreated,
        "description" : law.longDescription,
        "hashtag" : "#"+removeDiacritics(law.shortTitle.replace(/ /g, '').toLowerCase()),
        "institution" : institution._id,
        "introduction" : law.brief,
        "open" : law.lawStatus=="OPEN",
        "published" : law.publishDate!=null,
        "publishDate" : law.publishDate,
        "realName" : law.officialTitle,
        "region" : spain,
        "shortName" : law.shortTitle,
        "image":createAvatar(id,"LAW_IMAGE", law.image),
        "status":status,
        peopleVotes:peopleVotes,
        "version" : 0,
        parliamentaryGroup:parliamentaryGroup
    }
    return destLaw
}

function createPeopleVotes(oldLaw, idNewLaw){

    dbOrigin.personVote.find({law:oldLaw._id}).forEach(function(vote){
        var kuorumUser = dbDest.kuorumUser.find({_id:vote.user})[0]
        if (kuorumUser!= undefined ){
            var lawVote = {
                kuorumUser:vote.user,
                personalData:kuorumUser.personalData,
                law:idNewLaw,
                voteType:vote.voteType,
                dateCreated:vote.dateCreated
            }
            dbDest.lawVote.insert(lawVote)
        }
    });

    var peopleVotes = {
        yes:0,
        no:0,
        abs:0
    }
    dbDest.lawVote.aggregate([{$match:{law:idNewLaw}},{$group:{_id:"$voteType",total:{$sum:1}}}]).forEach(function(vote){
        if (vote._id=="POSITIVE"){
            peopleVotes.yes = vote.total;
        }else if (vote._id=="NEGATIVE"){
            peopleVotes.no = vote.total;
        }else{
            peopleVotes.abs = vote.total;
        };
    })
    return peopleVotes;
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
        defendedBy:null,
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
//        print("post sin mensajes:")
    }
}