var dbDest = dbDest || connect("localhost:27017/Kuorum");

//SUPER REGION
var parentRegionCode ="UE";


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
    {regionCode:'AB', postalCode:['AB'], name:'	Aberdeen                    '},
    {regionCode:'AL', postalCode:['AL'], name:'	St Albans                   '},
    {regionCode:'BI', postalCode:['B']	, name:'Birmingham                   '},
    {regionCode:'BA', postalCode:['BA'], name:'	Bath                        '},
    {regionCode:'BB', postalCode:['BB'], name:'	Blackburn                   '},
    {regionCode:'BD', postalCode:['BD'], name:'	Bradford                    '},
    {regionCode:'BH', postalCode:['BH'], name:'	Bournemouth                 '},
    {regionCode:'BL', postalCode:['BL'], name:'	Bolton                      '},
    {regionCode:'BN', postalCode:['BN'], name:'	Brighton                    '},
    {regionCode:'BR', postalCode:['BR'], name:'	Bromley                     '},
    {regionCode:'BS', postalCode:['BS'], name:'	Bristol                     '},
    {regionCode:'BT', postalCode:['BT'], name:'	Belfast                     '},
    {regionCode:'CA', postalCode:['CA'], name:'	Carlisle                    '},
    {regionCode:'CB', postalCode:['CB'], name:'	Cambridge                   '},
    {regionCode:'CF', postalCode:['CF'], name:'	Cardiff                     '},
    {regionCode:'CH', postalCode:['CH'], name:'	Chester                     '},
    {regionCode:'CM', postalCode:['CM'], name:'	Chelmsford                  '},
    {regionCode:'CO', postalCode:['CO'], name:'	Colchester                  '},
    {regionCode:'CR', postalCode:['CR'], name:'	Croydon                     '},
    {regionCode:'CT', postalCode:['CT'], name:'	Canterbury                  '},
    {regionCode:'CV', postalCode:['CV'], name:'	Coventry                    '},
    {regionCode:'CW', postalCode:['CW'], name:'	Crewe                       '},
    {regionCode:'DA', postalCode:['DA'], name:'	Dartford                    '},
    {regionCode:'DD', postalCode:['DD'], name:'	Dundee                      '},
    {regionCode:'DE', postalCode:['DE'], name:'	Derby                       '},
    {regionCode:'DG', postalCode:['DG'], name:'	Dumfries                    '},
    {regionCode:'DH', postalCode:['DH'], name:'	Durham                      '},
    {regionCode:'DL', postalCode:['DL'], name:'	Darlington                  '},
    {regionCode:'DN', postalCode:['DN'], name:'	Doncaster                   '},
    {regionCode:'DT', postalCode:['DT'], name:'	Dorchester                  '},
    {regionCode:'DY', postalCode:['DY'], name:'	Dudley                      '},
    {regionCode:'EL',  postalCode:['E']	, name:'East London                  '},
    {regionCode:'EC', postalCode:['EC'], name:'	East Central London         '},
    {regionCode:'EH', postalCode:['EH'], name:'	Edinburgh                   '},
    {regionCode:'EN', postalCode:['EN'], name:'	Enfield                     '},
    {regionCode:'EX', postalCode:['EX'], name:'	Exeter                      '},
    {regionCode:'FK', postalCode:['FK'], name:'	Falkirk                     '},
    {regionCode:'FY', postalCode:['FY'], name:'	Blackpool                   '},
    {regionCode:'GW',  postalCode:['G']	, name:'Glasgow                      '},
    {regionCode:'GL', postalCode:['GL'], name:'	Gloucester                  '},
    {regionCode:'GU', postalCode:['GU'], name:'	Guildford                   '},
    {regionCode:'HA', postalCode:['HA'], name:'	Harrow                      '},
    {regionCode:'HD', postalCode:['HD'], name:'	Huddersfield                '},
    {regionCode:'HG', postalCode:['HG'], name:'	Harrogate                   '},
    {regionCode:'HP', postalCode:['HP'], name:'	Hemel Hempstead             '},
    {regionCode:'HR', postalCode:['HR'], name:'	Hereford                    '},
    {regionCode:'HS', postalCode:['HS'], name:'	Outer Hebrides              '},
    {regionCode:'HU', postalCode:['HU'], name:'	Hull                        '},
    {regionCode:'HX', postalCode:['HX'], name:'	Halifax                     '},
    {regionCode:'IG', postalCode:['IG'], name:'	Ilford                      '},
    {regionCode:'IP', postalCode:['IP'], name:'	Ipswich                     '},
    {regionCode:'IV', postalCode:['IV'], name:'	Inverness                   '},
    {regionCode:'KA', postalCode:['KA'], name:'	Kilmarnock                  '},
    {regionCode:'KT', postalCode:['KT'], name:'	Kingston upon Thames        '},
    {regionCode:'KW', postalCode:['KW'], name:'	Kirkwall                    '},
    {regionCode:'KY', postalCode:['KY'], name:'	Kirkcaldy                   '},
    {regionCode:'LI',  postalCode:['L']	, name:'Liverpool                    '},
    {regionCode:'LA', postalCode:['LA'], name:'	Lancaster                   '},
    {regionCode:'LD', postalCode:['LD'], name:'	Llandrindod Wells           '},
    {regionCode:'LE', postalCode:['LE'], name:'	Leicester                   '},
    {regionCode:'LL', postalCode:['LL'], name:'	Llandudno                   '},
    {regionCode:'LN', postalCode:['LN'], name:'	Lincoln                     '},
    {regionCode:'LS', postalCode:['LS'], name:'	Leeds                       '},
    {regionCode:'LU', postalCode:['LU'], name:'	Luton                       '},
    {regionCode:'MA',  postalCode:['M']	, name:'Manchester                   '},
    {regionCode:'ME', postalCode:['ME'], name:'	Rochester                   '},
    {regionCode:'MK', postalCode:['MK'], name:'	Milton Keynes               '},
    {regionCode:'ML', postalCode:['ML'], name:'	Motherwell                  '},
    {regionCode:'NL',  postalCode:['N']	, name:'North London                 '},
    {regionCode:'NE', postalCode:['NE'], name:'	Newcastle upon Tyne         '},
    {regionCode:'NG', postalCode:['NG'], name:'	Nottingham                  '},
    {regionCode:'NN', postalCode:['NN'], name:'	Northampton                 '},
    {regionCode:'NP', postalCode:['NP'], name:'	Newport                     '},
    {regionCode:'NR', postalCode:['NR'], name:'	Norwich                     '},
    {regionCode:'NW', postalCode:['NW'], name:'	North West London           '},
    {regionCode:'OL', postalCode:['OL'], name:'	Oldham                      '},
    {regionCode:'OX', postalCode:['OX'], name:'	Oxford                      '},
    {regionCode:'PA', postalCode:['PA'], name:'	Paisley                     '},
    {regionCode:'PE', postalCode:['PE'], name:'	Peterborough                '},
    {regionCode:'PH', postalCode:['PH'], name:'	Perth                       '},
    {regionCode:'PL', postalCode:['PL'], name:'	Plymouth                    '},
    {regionCode:'PO', postalCode:['PO'], name:'	Portsmouth                  '},
    {regionCode:'PR', postalCode:['PR'], name:'	Preston                     '},
    {regionCode:'RG', postalCode:['RG'], name:'	Reading                     '},
    {regionCode:'RH', postalCode:['RH'], name:'	Redhill                     '},
    {regionCode:'RM', postalCode:['RM'], name:'	Romford                     '},
    {regionCode:'SH', postalCode:['S']	, name:'Sheffield                    '},
    {regionCode:'SA', postalCode:['SA'], name:'	Swansea                     '},
    {regionCode:'SE', postalCode:['SE'], name:'	South East London           '},
    {regionCode:'SG', postalCode:['SG'], name:'	Stevenage                   '},
    {regionCode:'SK', postalCode:['SK'], name:'	Stockport                   '},
    {regionCode:'SL', postalCode:['SL'], name:'	Slough                      '},
    {regionCode:'SM', postalCode:['SM'], name:'	Sutton                      '},
    {regionCode:'SN', postalCode:['SN'], name:'	Swindon                     '},
    {regionCode:'SO', postalCode:['SO'], name:'	Southampton                 '},
    {regionCode:'SP', postalCode:['SP'], name:'	Salisbury                   '},
    {regionCode:'SR', postalCode:['SR'], name:'	Sunderland                  '},
    {regionCode:'SS', postalCode:['SS'], name:'	Southend-on-Sea             '},
    {regionCode:'ST', postalCode:['ST'], name:'	Stoke-on-Trent              '},
    {regionCode:'SW', postalCode:['SW'], name:'	South West London           '},
    {regionCode:'SY', postalCode:['SY'], name:'	Shrewsbury                  '},
    {regionCode:'TA', postalCode:['TA'], name:'	Taunton                     '},
    {regionCode:'TD', postalCode:['TD'], name:'	Galashiels                  '},
    {regionCode:'TF', postalCode:['TF'], name:'	Telford                     '},
    {regionCode:'TN', postalCode:['TN'], name:'	Tonbridge                   '},
    {regionCode:'TQ', postalCode:['TQ'], name:'	Torquay                     '},
    {regionCode:'TR', postalCode:['TR'], name:'	Truro                       '},
    {regionCode:'TS', postalCode:['TS'], name:'	Cleveland                   '},
    {regionCode:'TW', postalCode:['TW'], name:'	Twickenham                  '},
    {regionCode:'UB', postalCode:['UB'], name:'	Southall                    '},
    {regionCode:'WL', postalCode:['W']	, name:'West London                  '},
    {regionCode:'WA', postalCode:['WA'], name:'	Warrington                  '},
    {regionCode:'WC', postalCode:['WC'], name:'	Western Central London      '},
    {regionCode:'WD', postalCode:['WD'], name:'	Watford                     '},
    {regionCode:'WF', postalCode:['WF'], name:'	Wakefield                   '},
    {regionCode:'WN', postalCode:['WN'], name:'	Wigan                       '},
    {regionCode:'WR', postalCode:['WR'], name:'	Worcester                   '},
    {regionCode:'WS', postalCode:['WS'], name:'	Walsall                     '},
    {regionCode:'WV', postalCode:['WV'], name:'	Wolverhampton               '},
    {regionCode:'YO', postalCode:['YO'], name:'	York                        '},
    {regionCode:'ZE', postalCode:['ZE'], name:'	Lerwick                     '}
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