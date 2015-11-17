
var dbDest = dbDest || connect("localhost:27017/Kuorum");

var sedaviRegion = dbDest.region.findOne({iso3166_2:"EU-ES-CV-SE"})
var spain = dbDest.region.findOne({iso3166_2:"EU-ES"})
var userId = ObjectId("564982d0e4b0ed5e68e19d31")
dbDest.kuorumUser.update(
    {_id:userId},
    {$set:{
        'alias':'jbaixauli',
        'professionalDetails.region':sedaviRegion,
        'professionalDetails.constituency':sedaviRegion,
        'verified' : true,
        'professionalDetails.politicalParty':'Compromis',
        'personalData.province':sedaviRegion._id,
        'personalData.country':spain._id,
        'personalData.provinceCode':sedaviRegion.iso3166_2,
        'personalData.postalCode':'46910',
        'userType' : 'POLITICIAN',
        'socialLinks.twitter':'@jbaixauli',
        'politicianActivity' : {
            'numDebates' : 0,
            'numDefends' : 0,
            'numVictories' : 0
        },
        'authorities' : [
            {
                '_id' : ObjectId('538f3468e4b0f5aaca4edcfa'),
                'authority' : 'ROLE_USER',
                'version' : NumberLong(0)
            },
            {
                '_id' : ObjectId('538f3468e4b0f5aaca4edd07'),
                'authority' : 'ROLE_POLITICIAN',
                'version' : 0
            }
        ],
        'politicianOnRegion' : sedaviRegion
    }
    }
)