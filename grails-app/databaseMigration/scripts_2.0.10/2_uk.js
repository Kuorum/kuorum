var dbDest = dbDest || connect("localhost:27017/Kuorum");

//SUPER REGION
var parentRegionCode ="EU";


//COUNTRY
var countryPostFixCode = "UK";
var regionName="United Kindom";
var countryType="NATION"

var countryCode=parentRegionCode+"-"+countryPostFixCode;
var parentRegion = dbDest.region.find({iso3166_2:parentRegionCode}).next()


dbDest.region.save({
    "iso3166_2" : countryCode,
    "name" : regionName,
    "superRegion" : parentRegion._id,
    "version" : 0,
    "postalCodes" : [],
    "prefixPhone" : "+44",
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
    {regionCode:'AB', postalCodePrefix:['AB'], name:'Aberdeen'},
    {regionCode:'AL', postalCodePrefix:['AL'], name:'St Albans'},
    {regionCode:'BI', postalCodePrefix:['B'],  name:'Birmingham'},
    {regionCode:'BA', postalCodePrefix:['BA'], name:'Bath'},
    {regionCode:'BB', postalCodePrefix:['BB'], name:'Blackburn'},
    {regionCode:'BD', postalCodePrefix:['BD'], name:'Bradford'},
    {regionCode:'BH', postalCodePrefix:['BH'], name:'Bournemouth'},
    {regionCode:'BL', postalCodePrefix:['BL'], name:'Bolton'},
    {regionCode:'BN', postalCodePrefix:['BN'], name:'Brighton'},
    {regionCode:'BR', postalCodePrefix:['BR'], name:'Bromley'},
    {regionCode:'BS', postalCodePrefix:['BS'], name:'Bristol'},
    {regionCode:'BT', postalCodePrefix:['BT'], name:'Belfast'},
    {regionCode:'CA', postalCodePrefix:['CA'], name:'Carlisle'},
    {regionCode:'CB', postalCodePrefix:['CB'], name:'Cambridge'},
    {regionCode:'CF', postalCodePrefix:['CF'], name:'Cardiff'},
    {regionCode:'CH', postalCodePrefix:['CH'], name:'Chester'},
    {regionCode:'CM', postalCodePrefix:['CM'], name:'Chelmsford'},
    {regionCode:'CO', postalCodePrefix:['CO'], name:'Colchester'},
    {regionCode:'CR', postalCodePrefix:['CR'], name:'Croydon'},
    {regionCode:'CT', postalCodePrefix:['CT'], name:'Canterbury'},
    {regionCode:'CV', postalCodePrefix:['CV'], name:'Coventry'},
    {regionCode:'CW', postalCodePrefix:['CW'], name:'Crewe'},
    {regionCode:'DA', postalCodePrefix:['DA'], name:'Dartford'},
    {regionCode:'DD', postalCodePrefix:['DD'], name:'Dundee'},
    {regionCode:'DE', postalCodePrefix:['DE'], name:'Derby'},
    {regionCode:'DG', postalCodePrefix:['DG'], name:'Dumfries'},
    {regionCode:'DH', postalCodePrefix:['DH'], name:'Durham'},
    {regionCode:'DL', postalCodePrefix:['DL'], name:'Darlington'},
    {regionCode:'DN', postalCodePrefix:['DN'], name:'Doncaster'},
    {regionCode:'DT', postalCodePrefix:['DT'], name:'Dorchester'},
    {regionCode:'DY', postalCodePrefix:['DY'], name:'Dudley'},
    {regionCode:'EL', postalCodePrefix:['E'], name:'East London'},
    {regionCode:'EC', postalCodePrefix:['EC'], name:'East Central London'},
    {regionCode:'EH', postalCodePrefix:['EH'], name:'Edinburgh'},
    {regionCode:'EN', postalCodePrefix:['EN'], name:'Enfield'},
    {regionCode:'EX', postalCodePrefix:['EX'], name:'Exeter'},
    {regionCode:'FK', postalCodePrefix:['FK'], name:'Falkirk'},
    {regionCode:'FY', postalCodePrefix:['FY'], name:'Blackpool'},
    {regionCode:'GW', postalCodePrefix:['G'], name:'Glasgow'},
    {regionCode:'GL', postalCodePrefix:['GL'], name:'Gloucester'},
    {regionCode:'GU', postalCodePrefix:['GU'], name:'Guildford'},
    {regionCode:'HA', postalCodePrefix:['HA'], name:'Harrow'},
    {regionCode:'HD', postalCodePrefix:['HD'], name:'Huddersfield'},
    {regionCode:'HG', postalCodePrefix:['HG'], name:'Harrogate'},
    {regionCode:'HP', postalCodePrefix:['HP'], name:'Hemel Hempstead'},
    {regionCode:'HR', postalCodePrefix:['HR'], name:'Hereford'},
    {regionCode:'HS', postalCodePrefix:['HS'], name:'Outer Hebrides'},
    {regionCode:'HU', postalCodePrefix:['HU'], name:'Hull'},
    {regionCode:'HX', postalCodePrefix:['HX'], name:'Halifax'},
    {regionCode:'IG', postalCodePrefix:['IG'], name:'Ilford'},
    {regionCode:'IP', postalCodePrefix:['IP'], name:'Ipswich'},
    {regionCode:'IV', postalCodePrefix:['IV'], name:'Inverness'},
    {regionCode:'KA', postalCodePrefix:['KA'], name:'Kilmarnock'},
    {regionCode:'KT', postalCodePrefix:['KT'], name:'Kingston upon Thames'},
    {regionCode:'KW', postalCodePrefix:['KW'], name:'Kirkwall'},
    {regionCode:'KY', postalCodePrefix:['KY'], name:'Kirkcaldy'},
    {regionCode:'LI', postalCodePrefix:['L']	,name:'Liverpool'},
    {regionCode:'LA', postalCodePrefix:['LA'], name:'Lancaster'},
    {regionCode:'LD', postalCodePrefix:['LD'], name:'Llandrindod Wells'},
    {regionCode:'LE', postalCodePrefix:['LE'], name:'Leicester'},
    {regionCode:'LL', postalCodePrefix:['LL'], name:'Llandudno'},
    {regionCode:'LN', postalCodePrefix:['LN'], name:'Lincoln'},
    {regionCode:'LS', postalCodePrefix:['LS'], name:'Leeds'},
    {regionCode:'LU', postalCodePrefix:['LU'], name:'Luton'},
    {regionCode:'MA', postalCodePrefix:['M'], name:'Manchester'},
    {regionCode:'ME', postalCodePrefix:['ME'], name:'Rochester'},
    {regionCode:'MK', postalCodePrefix:['MK'], name:'Milton Keynes'},
    {regionCode:'ML', postalCodePrefix:['ML'], name:'Motherwell'},
    {regionCode:'NL', postalCodePrefix:['N'], name:'North London'},
    {regionCode:'NE', postalCodePrefix:['NE'], name:'Newcastle upon Tyne'},
    {regionCode:'NG', postalCodePrefix:['NG'], name:'Nottingham'},
    {regionCode:'NN', postalCodePrefix:['NN'], name:'Northampton'},
    {regionCode:'NP', postalCodePrefix:['NP'], name:'Newport'},
    {regionCode:'NR', postalCodePrefix:['NR'], name:'Norwich'},
    {regionCode:'NW', postalCodePrefix:['NW'], name:'North West London'},
    {regionCode:'OL', postalCodePrefix:['OL'], name:'Oldham'},
    {regionCode:'OX', postalCodePrefix:['OX'], name:'Oxford'},
    {regionCode:'PA', postalCodePrefix:['PA'], name:'Paisley'},
    {regionCode:'PE', postalCodePrefix:['PE'], name:'Peterborough'},
    {regionCode:'PH', postalCodePrefix:['PH'], name:'Perth'},
    {regionCode:'PL', postalCodePrefix:['PL'], name:'Plymouth'},
    {regionCode:'PO', postalCodePrefix:['PO'], name:'Portsmouth'},
    {regionCode:'PR', postalCodePrefix:['PR'], name:'Preston'},
    {regionCode:'RG', postalCodePrefix:['RG'], name:'Reading'},
    {regionCode:'RH', postalCodePrefix:['RH'], name:'Redhill'},
    {regionCode:'RM', postalCodePrefix:['RM'], name:'Romford'},
    {regionCode:'SH', postalCodePrefix:['S'],  name:'Sheffield'},
    {regionCode:'SA', postalCodePrefix:['SA'], name:'Swansea'},
    {regionCode:'SE', postalCodePrefix:['SE'], name:'South East London'},
    {regionCode:'SG', postalCodePrefix:['SG'], name:'Stevenage'},
    {regionCode:'SK', postalCodePrefix:['SK'], name:'Stockport'},
    {regionCode:'SL', postalCodePrefix:['SL'], name:'Slough'},
    {regionCode:'SM', postalCodePrefix:['SM'], name:'Sutton'},
    {regionCode:'SN', postalCodePrefix:['SN'], name:'Swindon'},
    {regionCode:'SO', postalCodePrefix:['SO'], name:'Southampton'},
    {regionCode:'SP', postalCodePrefix:['SP'], name:'Salisbury'},
    {regionCode:'SR', postalCodePrefix:['SR'], name:'Sunderland'},
    {regionCode:'SS', postalCodePrefix:['SS'], name:'Southend-on-Sea'},
    {regionCode:'ST', postalCodePrefix:['ST'], name:'Stoke-on-Trent'},
    {regionCode:'SW', postalCodePrefix:['SW'], name:'South West London'},
    {regionCode:'SY', postalCodePrefix:['SY'], name:'Shrewsbury'},
    {regionCode:'TA', postalCodePrefix:['TA'], name:'Taunton'},
    {regionCode:'TD', postalCodePrefix:['TD'], name:'Galashiels'},
    {regionCode:'TF', postalCodePrefix:['TF'], name:'Telford'},
    {regionCode:'TN', postalCodePrefix:['TN'], name:'Tonbridge'},
    {regionCode:'TQ', postalCodePrefix:['TQ'], name:'Torquay'},
    {regionCode:'TR', postalCodePrefix:['TR'], name:'Truro'},
    {regionCode:'TS', postalCodePrefix:['TS'], name:'Cleveland'},
    {regionCode:'TW', postalCodePrefix:['TW'], name:'Twickenham'},
    {regionCode:'UB', postalCodePrefix:['UB'], name:'Southall'},
    {regionCode:'WL', postalCodePrefix:['W'],  name:'West London'},
    {regionCode:'WA', postalCodePrefix:['WA'], name:'Warrington'},
    {regionCode:'WC', postalCodePrefix:['WC'], name:'Western Central London'},
    {regionCode:'WD', postalCodePrefix:['WD'], name:'Watford'},
    {regionCode:'WF', postalCodePrefix:['WF'], name:'Wakefield'},
    {regionCode:'WN', postalCodePrefix:['WN'], name:'Wigan'},
    {regionCode:'WR', postalCodePrefix:['WR'], name:'Worcester'},
    {regionCode:'WS', postalCodePrefix:['WS'], name:'Walsall'},
    {regionCode:'WV', postalCodePrefix:['WV'], name:'Wolverhampton'},
    {regionCode:'YO', postalCodePrefix:['YO'], name:'York'},
    {regionCode:'ZE', postalCodePrefix:['ZE'], name:'Lerwick'}
]

for (var i=0; i<provinces.length; i++) {
    var province = provinces[i]
    var provinceCode=countryCode+"-"+province.regionCode;
    print("Saving "+provinceCode)
    dbDest.region.save({
        "iso3166_2" : provinceCode,
        "name" : province.name,
        "superRegion" : parentRegion._id,
        "version" : 0,
        "postalCodes" : province.postalCodePrefix,
        "regionType": "STATE"
    });
}