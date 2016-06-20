
var dbDest = dbDest || connect("localhost:27017/Kuorum");

var iduetxeUserKey = {
    "_class" : "org.kuorum.provider.mongo.model.restUser.RestUser",
    "username" : "iduetxe",
    "token" : "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJpZHVldHhlIn0.2HHu9anjqYmrd7EPIXrtjNql-o2kwNhenvEA55dGGCs92tY4USFXvuUepTL-_3FJwp8DRMl069n41pgJp5bb1w",
    "kuorumUser" : ObjectId("52220293e4b047381ebea0cf"),
    "authorities": ['ROLE_CAUSE',
        'ROLE_GEOLOCATION',
        'ROLE_NOTIFICATIONS',
        'ROLE_INDEX',
        'ROLE_SEARCH',
        'ROLE_USER_STATS',
        'ROLE_USER_NEWS',
        'ROLE_USER_CAUSE'
    ]
};

dbDest.restUser.save(iduetxeUserKey)
