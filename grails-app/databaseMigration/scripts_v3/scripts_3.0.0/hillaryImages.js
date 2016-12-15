
var dbDest = dbDest || connect("localhost:27017/Kuorum");

var hillaryAvatarUrl = "https://s3-eu-west-1.amazonaws.com/kuorumorg/static/ipdb/hillary_pic.png";
var hillaryCoverUrl = "https://s3-eu-west-1.amazonaws.com/kuorumorg/static/ipdb/hillary_cover.png";
var hillaryId = ObjectId("562897aae4b04fc5e3d80a37");

dbDest.kuorumUser.update(
    {_id:hillaryId},
    {$set:{
        'imageProfile.url':hillaryCoverUrl,
        'imageProfile.urlThumb':hillaryCoverUrl,
        'avatar.url':hillaryAvatarUrl,
        'avatar.urlThumb':hillaryAvatarUrl
    }}
)