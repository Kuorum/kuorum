var dbDest = dbDest || connect("localhost:27017/Kuorum");

//SUPER REGION
var parentRegionCode ="LA";

dbDest.region.save({
    "iso3166_2" : parentRegionCode,
    "name" : 'America Latina',
    "version" : 0,
    "postalCodes" : [],
    "regionType": 'SUPRANATIONAL'
});


//COUNTRY
var countryPostFixCode = "CO";
var regionName="Colombia";
var institutionCountryName="Congreso de colombia";
var countryType="NATION"

var countryCode=parentRegionCode+"-"+countryPostFixCode;
var parentRegion = dbDest.region.find({iso3166_2:parentRegionCode}).next()


dbDest.region.save({
    "iso3166_2" : countryCode,
    "name" : regionName,
    "superRegion" : parentRegion._id,
    "version" : 0,
    "postalCodes" : [],
    "prefixPhone" : "+57",
    "regionType": countryType
});

var country = dbDest.region.find({iso3166_2:countryCode}).next()
dbDest.institution.save({
    "name" : institutionCountryName,
    "region" : country,
    "version" : 0
});

//PROVINCES

var provinces = [
    {regionCode: 'BO', name:'Bogotá',	                  postalCodePrefix: ['11']},
    {regionCode: 'AM', name:'Amazonas',	              postalCodePrefix: ['91']},
    {regionCode: 'AN', name:'Antioquia',	              postalCodePrefix: ['05']},
    {regionCode: 'AR', name:'Arauca',	                  postalCodePrefix: ['81']},
    {regionCode: 'AT', name:'Atlántico',	              postalCodePrefix: ['08']},
    {regionCode: 'BO', name:'Bolívar',	              postalCodePrefix: ['13']},
    {regionCode: 'BY', name:'Boyacá',	                  postalCodePrefix: ['15']},
    {regionCode: 'CL', name:'Caldas',	                  postalCodePrefix: ['17']},
    {regionCode: 'CA', name:'Caquetá',	              postalCodePrefix: ['18']},
    {regionCode: 'CS', name:'Casanare',	              postalCodePrefix: ['85']},
    {regionCode: 'CC', name:'Cauca',	                  postalCodePrefix: ['19']},
    {regionCode: 'CE', name:'Cesar',	                  postalCodePrefix: ['20']},
    {regionCode: 'CH', name:'Chocó',	                  postalCodePrefix: ['27']},
    {regionCode: 'CO', name:'Córdoba',	              postalCodePrefix: ['23']},
    {regionCode: 'CU', name:'Cundinamarca',	          postalCodePrefix: ['25']},
    {regionCode: 'GU', name:'Guainía',	              postalCodePrefix: ['94']},
    {regionCode: 'GV', name:'Guaviare',	              postalCodePrefix: ['95']},
    {regionCode: 'HU', name:'Huila',	                  postalCodePrefix: ['41']},
    {regionCode: 'GJ', name:'La Guajira',	              postalCodePrefix: ['44']},
    {regionCode: 'MG', name:'Magdalena',	              postalCodePrefix: ['47']},
    {regionCode: 'MT', name:'Meta',	                  postalCodePrefix: ['50']},
    {regionCode: 'NA', name:'Nariño',	                  postalCodePrefix: ['52']},
    {regionCode: 'NS', name:'Norte de Santander',	      postalCodePrefix: ['54']},
    {regionCode: 'PU', name:'Putumayo',	              postalCodePrefix: ['86']},
    {regionCode: 'QU', name:'Quindío',	              postalCodePrefix: ['63']},
    {regionCode: 'RI', name:'Risaralda',	              postalCodePrefix: ['66']},
    {regionCode: 'AP', name:'San Andrés y Providencia', postalCodePrefix: ['88']},
    {regionCode: 'SA', name:'Santander',	              postalCodePrefix: ['68']},
    {regionCode: 'SU', name:'Sucre',	                  postalCodePrefix: ['70']},
    {regionCode: 'TO', name:'Tolima',	                  postalCodePrefix: ['73']},
    {regionCode: 'VC', name:'Valle del Cauca',	      postalCodePrefix: ['76']},
    {regionCode: 'VA', name:'Vaupés',	                  postalCodePrefix: ['97']},
    {regionCode: 'VI', name:'Vichada',	              postalCodePrefix: ['99']}
]

for (var i=0; i<provinces.length; i++) {
    var province = provinces[i]
    var provinceCode=countryCode+"-"+province.regionCode;
    print("Saving "+provinceCode)
    dbDest.region.save({
        "iso3166_2" : provinceCode,
        "name" : province.name,
        "superRegion" : country._id,
        "version" : 0,
        "postalCodes" : province.postalCodePrefix,
        "regionType": "STATE"
    });
}