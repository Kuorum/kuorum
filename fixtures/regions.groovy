import kuorum.Region
import kuorum.core.model.RegionType

fixture {

    europe(Region){
        name ="Europa"
        iso3166_2 = "EU"
        regionType = RegionType.STATE
    }

    spain(Region){
        name ="España"
        iso3166_2 = "EU-ES"
        superRegion = europe
        regionType = RegionType.NATION
    }

    /****** Pais Vasco *******/
    paisVasco(Region){
        name ="Euskadi"
        iso3166_2 = "EU-ES-PV"
        superRegion = spain
        regionType = RegionType.LOCAL
    }


    alava(Region){
        name ="Araba"
        iso3166_2 = "EU-ES-PV-VI" //ES-VI
        superRegion = paisVasco
        postalCodes = "01"
        regionType = RegionType.LOCAL
    }
    vizcaya(Region){
        name ="Bizkaia"
        iso3166_2 = "EU-ES-PV-BI" // ES-BI
        superRegion = paisVasco
        postalCodes = "48"
        regionType = RegionType.LOCAL
    }
    guipuzcua(Region){
        name ="Guipuzkoa"
        iso3166_2 = "EU-ES-PV-SS" // ES-BI
        superRegion = paisVasco
        postalCodes = "20"
        regionType = RegionType.LOCAL
    }

    /****** CASTILLA LA MANCHA *******/
    castillaMancha(Region){
        name ="Castilla la Mancha"
        iso3166_2 = "EU-ES-CM"
        superRegion = spain
        regionType = RegionType.LOCAL
    }
    albacete(Region){
        name ="Albacete"
        iso3166_2 = "EU-ES-CM-AB" // ES-AB
        superRegion = castillaMancha
        postalCodes = "02"
        regionType = RegionType.LOCAL
    }
    guadalajara(Region){
        name ="Guadalajara"
        iso3166_2 = "EU-ES-CM-GU" // ES-GU
        superRegion = castillaMancha
        postalCodes = "19"
        regionType = RegionType.LOCAL
    }
    cuenca(Region){
        name ="Cuenca"
        iso3166_2 = "EU-ES-CM-CU" // ES-CU
        superRegion = castillaMancha
        postalCodes = "16"
        regionType = RegionType.LOCAL
    }
    toledo(Region){
        name ="Toledo"
        iso3166_2 = "EU-ES-CM-TO" // ES-TO
        superRegion = castillaMancha
        postalCodes = "45"
        regionType = RegionType.LOCAL
    }
    ciudadReal(Region){
        name ="Ciudad Real"
        iso3166_2 = "EU-ES-CM-CR"
        superRegion = castillaMancha
        postalCodes = "13"
        regionType = RegionType.LOCAL
    }

    /****** C. Valenciana *******/

    cValenciana(Region){
        name ="Comunidad Valenciana"
        iso3166_2 = "EU-ES-CV"
        superRegion = spain
        regionType = RegionType.LOCAL
    }
    alicante(Region){
        name ="Alicante"
        iso3166_2 = "EU-ES-CV-AC" // ES-A
        superRegion = cValenciana
        postalCodes = "03"
        regionType = RegionType.LOCAL
    }
    valencia(Region){
        name ="Valencia"
        iso3166_2 = "EU-ES-CV-VA" // ES-v
        superRegion = cValenciana
        postalCodes = "46"
        regionType = RegionType.LOCAL
    }
    castellon(Region){
        name ="Castellón"
        iso3166_2 = "EU-ES-CV-CS" //
        superRegion = cValenciana
        postalCodes = "12"
        regionType = RegionType.LOCAL
    }

    /****** Andalucia *******/
    andalucia(Region){
        name ="Andalucia"
        iso3166_2 = "EU-ES-AN"
        superRegion = spain
        regionType = RegionType.LOCAL
    }
    almeria(Region){
        name ="Almería"
        iso3166_2 = "EU-ES-AN-AL" // ES-AL
        superRegion = andalucia
        postalCodes = "04"
        regionType = RegionType.LOCAL
    }
    granada(Region){
        name ="Granada"
        iso3166_2 = "EU-ES-AN-GR" // ES-GR
        superRegion = andalucia
        postalCodes = "18"
        regionType = RegionType.LOCAL
    }
    jaen(Region){
        name ="Jaen"
        iso3166_2 = "EU-ES-AN-JN" // ES-J
        superRegion = andalucia
        postalCodes = "23"
        regionType = RegionType.LOCAL
    }
    cordoba(Region){
        name ="Cordoba"
        iso3166_2 = "EU-ES-AN-CO" // ES-CO
        superRegion = andalucia
        postalCodes = "14"
        regionType = RegionType.LOCAL
    }
    malaga(Region){
        name ="Málaga"
        iso3166_2 = "EU-ES-AN-MA" // ES-MA
        superRegion = andalucia
        postalCodes = "29"
        regionType = RegionType.LOCAL
    }
    cadiz(Region){
        name ="Cádiz"
        iso3166_2 = "EU-ES-AN-CA" // ES-CA
        superRegion = andalucia
        postalCodes = "11"
        regionType = RegionType.LOCAL
    }
    sevilla(Region){
        name ="Sevilla"
        iso3166_2 = "EU-ES-AN-SE" // ES-SE
        superRegion = andalucia
        postalCodes = "41"
        regionType = RegionType.LOCAL
    }
    huelva(Region){
        name ="Huelva"
        iso3166_2 = "EU-ES-AN-HU" // ES-H
        superRegion = andalucia
        postalCodes = "21"
        regionType = RegionType.LOCAL
    }


    /****** Extremadura *******/
    extremadura(Region){
        name ="Extremadura"
        iso3166_2 = "EU-ES-EX"
        superRegion = spain
        regionType = RegionType.LOCAL
    }
    badajoz(Region){
        name ="Badajoz"
        iso3166_2 = "EU-ES-EX-BA" // ES-BA
        superRegion = extremadura
        postalCodes = "06"
        regionType = RegionType.LOCAL
    }
    caceres(Region){
        name ="Cáceres"
        iso3166_2 = "EU-ES-EX-CC" // ES-CC
        superRegion = extremadura
        postalCodes = "10"
        regionType = RegionType.LOCAL
    }
    /****** Castilla y Leon *******/
    castillaLeon(Region){
        name ="Castilla y León"
        iso3166_2 = "EU-ES-CL"
        superRegion = spain
        regionType = RegionType.LOCAL
    }
    avila(Region){
        name ="Ávila"
        iso3166_2 = "EU-ES-CL-AV" // ES-AV
        superRegion = castillaLeon
        postalCodes = "05"
        regionType = RegionType.LOCAL
    }
    segovia(Region){
        name ="Segovia"
        iso3166_2 = "EU-ES-CL-SG"
        superRegion = castillaLeon
        postalCodes = "40"
        regionType = RegionType.LOCAL
    }
    salamanca(Region){
        name ="Salamanca"
        iso3166_2 = "EU-ES-CL-SA" // ES-SA
        superRegion = castillaLeon
        postalCodes = "37"
        regionType = RegionType.LOCAL
    }
    valladolid(Region){
        name ="Valladolid"
        iso3166_2 = "EU-ES-CL-VA" // ES-VA
        superRegion = castillaLeon
        postalCodes = "47"
        regionType = RegionType.LOCAL
    }
    zamora(Region){
        name ="Zamora"
        iso3166_2 = "EU-ES-CL-ZA" // ES-ZA
        superRegion = castillaLeon
        postalCodes = "49"
        regionType = RegionType.LOCAL
    }
    leon(Region){
        name ="León"
        iso3166_2 = "EU-ES-CL-LE" // ES-LE
        superRegion = castillaLeon
        postalCodes = "24"
        regionType = RegionType.LOCAL
    }
    palencia(Region){
        name ="Palencia"
        iso3166_2 = "EU-ES-CL-PA" // ES-P
        superRegion = castillaLeon
        postalCodes = "34"
        regionType = RegionType.LOCAL
    }
    burgos(Region){
        name ="Burgos"
        iso3166_2 = "EU-ES-CL-BU" // ES-BU
        superRegion = castillaLeon
        postalCodes = "09"
        regionType = RegionType.LOCAL
    }
    soria(Region){
        name ="Soria"
        iso3166_2 = "EU-ES-CL-SO" // ES-SO
        superRegion = castillaLeon
        postalCodes = "42"
        regionType = RegionType.LOCAL
    }

    /****** Islas baleares*******/
    iBaleares(Region){
        name ="Islas baleares"
        iso3166_2 = "EU-ES-IB"
        superRegion = spain
        regionType = RegionType.LOCAL
    }
    baleares(Region){
        name ="Baleares"
        iso3166_2 = "EU-ES-IB-IB" // ES-IB
        superRegion = iBaleares
        postalCodes = "07"
        regionType = RegionType.LOCAL
    }
    /****** Cataluña *******/
    catalonia(Region){
        name ="Cataluña"
        iso3166_2 = "EU-ES-CT"
        superRegion = spain
        regionType = RegionType.LOCAL
    }
    barcelona(Region){
        name ="Barcelona"
        iso3166_2 = "EU-ES-CT-BA" // ES-B
        superRegion = catalonia
        postalCodes = "08"
        regionType = RegionType.LOCAL
    }
    girona(Region){
        name ="Girona"
        iso3166_2 = "EU-ES-CT-GE" // ES-GE
        superRegion = catalonia
        postalCodes = "17"
        regionType = RegionType.LOCAL
    }
    tarragona(Region){
        name ="Tarragona"
        iso3166_2 = "EU-ES-CT-TA" // ES-T
        superRegion = catalonia
        postalCodes = "43"
        regionType = RegionType.LOCAL
    }
    lleida(Region){
        name ="Lleida" //Lerida
        iso3166_2 = "EU-ES-CT-LL" // ES-L
        superRegion = catalonia
        postalCodes = "25"
        regionType = RegionType.LOCAL
    }
    /****** Galicia *******/
    galicia(Region){
        name ="Galicia"
        iso3166_2 = "EU-ES-GA"
        superRegion = spain
        regionType = RegionType.LOCAL
    }
    corunia(Region){
        name ="A coruña"
        iso3166_2 = "EU-ES-GA-CO" // ES-C
        superRegion = galicia
        postalCodes = "15"
        regionType = RegionType.LOCAL
    }
    lugo(Region){
        name ="Lugo"
        iso3166_2 = "EU-ES-GA-LU" // ES-LU
        superRegion = galicia
        postalCodes = "27"
        regionType = RegionType.LOCAL
    }
    pontevedra(Region){
        name ="Pontevedra"
        iso3166_2 = "EU-ES-GA-PO" // ES-PO
        superRegion = galicia
        postalCodes = "36"
        regionType = RegionType.LOCAL
    }
    ourense(Region){
        name ="Ourense"
        iso3166_2 = "EU-ES-GA-OR" // ES-OR
        superRegion = galicia
        postalCodes = "32"
        regionType = RegionType.LOCAL
    }

    /****** ARAGON *******/
    aragon(Region){
        name ="Aragón"
        iso3166_2 = "EU-ES-AR"
        superRegion = spain
        regionType = RegionType.LOCAL
    }
    huesca(Region){
        name ="Huesca"
        iso3166_2 = "EU-ES-AR-HU" // ES-HU
        superRegion = aragon
        postalCodes = "22"
        regionType = RegionType.LOCAL
    }
    zaragoza(Region){
        name ="Zaragoza"
        iso3166_2 = "EU-ES-AR-ZA" // ES-Z
        superRegion = aragon
        postalCodes = ["50"]
        regionType = RegionType.LOCAL
    }
    teruel(Region){
        name ="Teruel"
        iso3166_2 = "EU-ES-AR-TE" // ES-TE
        superRegion = aragon
        postalCodes = ["44"]
        regionType = RegionType.LOCAL
    }
    /****** La RIoja *******/
    riojaCA(Region){
        name ="La Rioja"
        iso3166_2 = "EU-ES-RI"
        superRegion = spain
        regionType = RegionType.LOCAL
    }
    rioja(Region){
        name ="La Rioja"
        iso3166_2 = "EU-ES-RI-RI"
        superRegion = riojaCA
        postalCodes = ["26"]
        regionType = RegionType.LOCAL
    }
    /****** Madrid *******/
    madridCA(Region){
        name ="Madrid (Comunidad Autónoma)"
        iso3166_2 = "EU-ES-MD"
        superRegion = spain
        regionType = RegionType.LOCAL
    }
    madrid(Region){
        name ="Madrid"
        iso3166_2 = "EU-ES-MD-MD"
        superRegion = madridCA
        postalCodes = ["28"]
        regionType = RegionType.LOCAL
    }

    /****** Murcia *******/
    murciaCA(Region){
        name ="Region de Murcia"
        iso3166_2 = "EU-ES-MU"
        superRegion = spain
        regionType = RegionType.LOCAL
    }
    murcia(Region){
        name ="Murcia"
        iso3166_2 = "EU-ES-MU-MU"
        superRegion = murciaCA
        postalCodes = ["30"]
        regionType = RegionType.LOCAL
    }
    /****** Navarra *******/
    navarraCA(Region){
        name ="Comunidad Foral de Navarra"
        iso3166_2 = "EU-ES-NA"
        superRegion = spain
        regionType = RegionType.LOCAL
    }
    navarra(Region){
        name ="Navarra"
        iso3166_2 = "EU-ES-NA-NA"
        superRegion = navarraCA
        postalCodes = ["31"]
        regionType = RegionType.LOCAL
    }
    /****** Asturias *******/
    asturiasCA(Region){
        name ="Principado de Asturias"
        iso3166_2 = "EU-ES-AS" // ES-O
        superRegion = spain
        regionType = RegionType.LOCAL
    }
    asturias(Region){
        name ="Asturias"
        iso3166_2 = "EU-ES-AS-AS"
        superRegion = asturiasCA
        postalCodes = ["33"]
        regionType = RegionType.LOCAL
    }

    /****** Islas canarias *******/
    canarias(Region){
        name ="Islas canarias"
        iso3166_2 = "EU-ES-CN" // ES-CN
        superRegion = spain
        regionType = RegionType.LOCAL
    }
    palmas(Region){
        name ="Las Palmas"
        iso3166_2 = "EU-ES-CN-GC" // ES-GC
        superRegion = canarias
        postalCodes = ["35"]
        regionType = RegionType.LOCAL
    }
    santaCruz(Region){
        name ="Santa Cruz De Tenerife"
        iso3166_2 = "EU-ES-CN-TF" // ES-TF
        superRegion = canarias
        postalCodes = ["38"]
        regionType = RegionType.LOCAL
    }

    /****** Cantabria *******/
    cantabriaCA(Region){
        name ="Cantabria (CA)"
        iso3166_2 = "EU-ES-CB" // ES-CB
        superRegion = spain
        regionType = RegionType.LOCAL
    }
    cantabria(Region){
        name ="Cantabria"
        iso3166_2 = "EU-ES-CB-CB" // ES-GC
        superRegion = cantabriaCA
        postalCodes = ["39"]
        regionType = RegionType.LOCAL
    }

    /****** Ceuta *******/
    ceutaCA(Region){
        name ="Ceuta (CA)"
        iso3166_2 = "EU-ES-CE"
        superRegion = spain
        regionType = RegionType.LOCAL
    }
    ceuta(Region){
        name ="Ceuta"
        iso3166_2 = "EU-ES-CE-CE" // ES-CE
        superRegion = ceutaCA
        postalCodes = ["51"]
        regionType = RegionType.LOCAL
    }

    /****** Melilla *******/
    melillaCA(Region){
        name ="Melilla (CA)"
        iso3166_2 = "EU-ES-ML"
        superRegion = spain
        regionType = RegionType.LOCAL
    }
    melilla(Region){
        name ="Melilla"
        iso3166_2 = "EU-ES-ML-ML" // ES-ML
        superRegion = melillaCA
        postalCodes = ["52"]
        regionType = RegionType.LOCAL
    }
}