var dbDest = dbDest || connect("localhost:27017/Kuorum");

dbDest.kuorumUserReputationVote.find({averageEvaluation:{$exists:0}}).forEach(function(vote){

    var instantAverageAggregation = dbDest.kuorumUserReputationVote.aggregate([
        {$match: {
            "votedUser":vote.votedUser,
            "voteDate":{$lte:vote.voteDate}
        }
        },
        {$sort:{
            "_id":1
        }
        },
        {$project: {
            eval: "$evaluation",
            voteDate: "$voteDate",
            evaluatorId:"$evaluatorId"
        }
        },
        {$group:{
            _id:"$evaluatorId",
            voteDate : { $last : '$voteDate' },
            eval: {$last:"$eval"}
        }
        },
        {
            $group: {
                _id: null,
                evaluation: {$avg: "$eval"}
            }
        }
    ])

    var instantAverage = instantAverageAggregation.next().evaluation;
    dbDest.kuorumUserReputationVote.update({_id:vote._id},{$set:{averageEvaluation:instantAverage}})
})


//db.kuorumUserReputationVote.aggregate([
//    {$match: {
//        "votedUser":{"$ref":"kuorumUser", $id:ObjectId("562897aae4b04fc5e3d80a37")},
//        "voteDate":{$lte:ISODate("2016-06-11T08:18:03.882Z")}
//    }
//    },
//    {$sort:{
//        "_id":1
//    }
//    },
//    {$project: {
//        eval: "$evaluation",
//        voteDate: "$voteDate",
//        evaluatorId:"$evaluatorId"
//    }
//    },
//    {$group:{
//        _id:"$evaluatorId",
//        voteDate : { $last : '$voteDate' },
//        eval: {$last:"$eval"}
//    }
//    },
//    {
//        $group: {
//            _id: null,
//            evaluation: {$avg: "$eval"}
//        }
//    }
//])