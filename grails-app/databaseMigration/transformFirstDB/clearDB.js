
db = connect("localhost:27017/KuorumDev");

db.kuorumUser.remove({email:/^(?!.*@example.com$)/i});
db.facebookUser.remove({});
db.law.remove({});
db.post.remove({})
db.cluck.remove({})
db.postVote.remove({})
