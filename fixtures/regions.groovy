import kuorum.Region

fixture {

    europe(Region){
        name ="Europa"
        iso3166_2 = "EU"
    }

    spain(Region){
        name ="España"
        iso3166_2 = "EU-ES"
        superRegion = europe
    }

    /****** Pais Vasco *******/
    paisVasco(Region){
        name ="Euskadi"
        iso3166_2 = "EU-ES-PV"
        superRegion = spain
    }


    alava(Region){
        name ="Araba"
        iso3166_2 = "EU-ES-PV-VI" //ES-VI
        superRegion = paisVasco
        postalCode = "01"
    }
    vizcaya(Region){
        name ="Bizkaia"
        iso3166_2 = "EU-ES-PV-BI" // ES-BI
        superRegion = paisVasco
        postalCode = "48"
    }
    guipuzcua(Region){
        name ="Guipuzkoa"
        iso3166_2 = "EU-ES-PV-SS" // ES-BI
        superRegion = paisVasco
        postalCode = "20"
    }

    /****** CASTILLA LA MANCHA *******/
    castillaMancha(Region){
        name ="Castilla la Mancha"
        iso3166_2 = "EU-ES-CM" // ES-BI
        superRegion = spain
    }
    albacete(Region){
        name ="Albacete"
        iso3166_2 = "EU-ES-CM-AB" // ES-AB
        superRegion = castillaMancha
        postalCode = "02"
    }
    guadalajara(Region){
        name ="Guadalajara"
        iso3166_2 = "EU-ES-CM-GU" // ES-GU
        superRegion = castillaMancha
        postalCode = "19"
    }
    cuenca(Region){
        name ="Cuenca"
        iso3166_2 = "EU-ES-CM-CU" // ES-CU
        superRegion = castillaMancha
        postalCode = "16"
    }
    toledo(Region){
        name ="Toledo"
        iso3166_2 = "EU-ES-CM-TO" // ES-TO
        superRegion = castillaMancha
        postalCode = "45"
    }
    ciudadReal(Region){
        name ="Ciudad Real"
        iso3166_2 = "EU-ES-CM-CR" // ES-TO
        superRegion = castillaMancha
        postalCode = "13"
    }

    /****** C. Valenciana *******/

    cValenciana(Region){
        name ="Comunidad Valenciana"
        iso3166_2 = "EU-ES-CV"
        superRegion = spain
    }
    alicante(Region){
        name ="Alicante"
        iso3166_2 = "EU-ES-CV-AC" // ES-A
        superRegion = cValenciana
        postalCode = "03"
    }
    valencia(Region){
        name ="Valencia"
        iso3166_2 = "EU-ES-CV-VA" // ES-v
        superRegion = cValenciana
        postalCode = "46"
    }
    castellon(Region){
        name ="Castellon"
        iso3166_2 = "EU-ES-CV-AC" // ES-A
        superRegion = cValenciana
        postalCode = "12"
    }

    /****** Andalucia *******/
    andalucia(Region){
        name ="Andalucia"
        iso3166_2 = "EU-ES-AN"
        superRegion = spain
    }
    almeria(Region){
        name ="Almería"
        iso3166_2 = "EU-ES-AN-AL" // ES-AL
        superRegion = andalucia
        postalCode = "04"
    }
    granada(Region){
        name ="Granada"
        iso3166_2 = "EU-ES-AN-GR" // ES-GR
        superRegion = andalucia
        postalCode = "18"
    }
    jaen(Region){
        name ="Jaen"
        iso3166_2 = "EU-ES-AN-JN" // ES-J
        superRegion = andalucia
        postalCode = "23"
    }
    cordoba(Region){
        name ="Cordoba"
        iso3166_2 = "EU-ES-AN-CO" // ES-CO
        superRegion = andalucia
        postalCode = "14"
    }
    malaga(Region){
        name ="Málaga"
        iso3166_2 = "EU-ES-AN-MA" // ES-MA
        superRegion = andalucia
        postalCode = "29"
    }
    cadiz(Region){
        name ="Cádiz"
        iso3166_2 = "EU-ES-AN-CA" // ES-CA
        superRegion = andalucia
        postalCode = "11"
    }
    sevilla(Region){
        name ="Sevilla"
        iso3166_2 = "EU-ES-AN-SE" // ES-SE
        superRegion = andalucia
        postalCode = "41"
    }
    huelva(Region){
        name ="Huelva"
        iso3166_2 = "EU-ES-AN-HU" // ES-H
        superRegion = andalucia
        postalCode = "21"
    }


    /****** Extremadura *******/
    extremadura(Region){
        name ="Extremadura"
        iso3166_2 = "EU-ES-EX"
        superRegion = spain
    }
    badajoz(Region){
        name ="Badajoz"
        iso3166_2 = "EU-ES-EX-BA" // ES-BA
        superRegion = extremadura
        postalCode = "06"
    }
    caceres(Region){
        name ="Cáceres"
        iso3166_2 = "EU-ES-EX-CC" // ES-CC
        superRegion = extremadura
        postalCode = "10"
    }
    /****** Castilla y Leon *******/
    castillaLeon(Region){
        name ="Castilla y León"
        iso3166_2 = "EU-ES-CL"
        superRegion = spain
    }
    avila(Region){
        name ="Ávila"
        iso3166_2 = "EU-ES-CL-AV" // ES-AV
        superRegion = castillaLeon
        postalCode = "05"
    }
    segovia(Region){
        name ="Segovia"
        iso3166_2 = "EU-ES-CL-SO" // ES-SO
        superRegion = castillaLeon
        postalCode = "40"
    }
    salamanca(Region){
        name ="Salamanca"
        iso3166_2 = "EU-ES-CL-SA" // ES-SA
        superRegion = castillaLeon
        postalCode = "37"
    }
    valladolid(Region){
        name ="Valladolid"
        iso3166_2 = "EU-ES-CL-VA" // ES-VA
        superRegion = castillaLeon
        postalCode = "47"
    }
    zamora(Region){
        name ="Zamora"
        iso3166_2 = "EU-ES-CL-ZA" // ES-ZA
        superRegion = castillaLeon
        postalCode = "49"
    }
    leon(Region){
        name ="León"
        iso3166_2 = "EU-ES-CL-LE" // ES-LE
        superRegion = castillaLeon
        postalCode = "24"
    }
    palencia(Region){
        name ="Palencia"
        iso3166_2 = "EU-ES-CL-PA" // ES-P
        superRegion = castillaLeon
        postalCode = "34"
    }
    burgos(Region){
        name ="Burgos"
        iso3166_2 = "EU-ES-CL-BU" // ES-BU
        superRegion = castillaLeon
        postalCode = "09"
    }
    soria(Region){
        name ="Soria"
        iso3166_2 = "EU-ES-CL-SO" // ES-SO
        superRegion = castillaLeon
        postalCode = "42"
    }

    /****** Islas baleares*******/
    iBaleares(Region){
        name ="Islas baleares"
        iso3166_2 = "EU-ES-IB"
        superRegion = spain
    }
    baleares(Region){
        name ="Baleares"
        iso3166_2 = "EU-ES-IB-IB" // ES-IB
        superRegion = iBaleares
        postalCode = "07"
    }
    /****** Cataluña *******/
    catalonia(Region){
        name ="Cataluña"
        iso3166_2 = "EU-ES-CT"
        superRegion = spain
    }
    barcelona(Region){
        name ="Barcelona"
        iso3166_2 = "EU-ES-CT-BA" // ES-B
        superRegion = catalonia
        postalCode = "08"
    }
    girona(Region){
        name ="Girona"
        iso3166_2 = "EU-ES-CT-GE" // ES-GE
        superRegion = catalonia
        postalCode = "17"
    }
    tarragona(Region){
        name ="Tarragona"
        iso3166_2 = "EU-ES-CT-TA" // ES-T
        superRegion = catalonia
        postalCode = "43"
    }
    lleida(Region){
        name ="Lleida" //Lerida
        iso3166_2 = "EU-ES-CT-LL" // ES-L
        superRegion = catalonia
        postalCode = "25"
    }
    /****** Galicia *******/
    galicia(Region){
        name ="Galicia"
        iso3166_2 = "EU-ES-GA"
        superRegion = spain
    }
    corunia(Region){
        name ="A coruña"
        iso3166_2 = "EU-ES-GA-CO" // ES-C
        superRegion = galicia
        postalCode = "15"
    }
    lugo(Region){
        name ="Lugo"
        iso3166_2 = "EU-ES-GA-LU" // ES-LU
        superRegion = galicia
        postalCode = "27"
    }
    pontevedra(Region){
        name ="Pontevedra"
        iso3166_2 = "EU-ES-GA-PO" // ES-PO
        superRegion = galicia
        postalCode = "36"
    }
    ourense(Region){
        name ="Ourense"
        iso3166_2 = "EU-ES-GA-OR" // ES-OR
        superRegion = galicia
        postalCode = "32"
    }

    /****** Galicia *******/
    aragon(Region){
        name ="Aragón"
        iso3166_2 = "EU-ES-AR"
        superRegion = spain
    }
    huesca(Region){
        name ="Huesca"
        iso3166_2 = "EU-ES-AR-HU" // ES-HU
        superRegion = aragon
        postalCode = "22"
    }
    zaragoza(Region){
        name ="Zaragoza"
        iso3166_2 = "EU-ES-AR-ZA" // ES-Z
        superRegion = aragon
        postalCode = "50"
    }
    teruel(Region){
        name ="Teruel"
        iso3166_2 = "EU-ES-AR-TE" // ES-TE
        superRegion = aragon
        postalCode = "44"
    }
    /****** La RIoja *******/
    riojaCA(Region){
        name ="La Rioja (Comunidad Autónoma)"
        iso3166_2 = "EU-ES-RI"
        superRegion = spain
    }
    rioja(Region){
        name ="La Rioja"
        iso3166_2 = "EU-ES-RI-RI"
        superRegion = riojaCA
        postalCode = "26"
    }
    /****** Madrid *******/
    madridCA(Region){
        name ="Madrid (Comunidad Autónoma)"
        iso3166_2 = "EU-ES-MD"
        superRegion = spain
    }
    madrid(Region){
        name ="Madrid"
        iso3166_2 = "EU-ES-MD-MD"
        superRegion = madridCA
        postalCode = "28"
    }

    /****** Murcia *******/
    murciaCA(Region){
        name ="Region de Murcia"
        iso3166_2 = "EU-ES-MU"
        superRegion = spain
    }
    murcia(Region){
        name ="Murcia"
        iso3166_2 = "EU-ES-MU-MU"
        superRegion = murciaCA
        postalCode = "30"
    }
    /****** Navarra *******/
    navarraCA(Region){
        name ="Comunidad Floral de Navarra"
        iso3166_2 = "EU-ES-NA"
        superRegion = spain
    }
    navarra(Region){
        name ="Navarra"
        iso3166_2 = "EU-ES-NA-NA"
        superRegion = navarraCA
        postalCode = "31"
    }
    /****** Asturias *******/
    asturiasCA(Region){
        name ="Principado de Asturias"
        iso3166_2 = "EU-ES-AS" // ES-O
        superRegion = spain
    }
    asturias(Region){
        name ="Asturias"
        iso3166_2 = "EU-ES-AS-AS"
        superRegion = asturiasCA
        postalCode = "33"
    }

    /****** Islas canarias *******/
    canarias(Region){
        name ="Islas canarias"
        iso3166_2 = "EU-ES-CN" // ES-CN
        superRegion = spain
    }
    palmas(Region){
        name ="Las Palmas"
        iso3166_2 = "EU-ES-CN-GC" // ES-GC
        superRegion = canarias
        postalCode = "35"
    }
    santaCruz(Region){
        name ="Santa Cruz De Tenerife"
        iso3166_2 = "EU-ES-CN-TF" // ES-TF
        superRegion = canarias
        postalCode = "38"
    }

    /****** Cantabria *******/
    cantabriaCA(Region){
        name ="Cantabria (CA)"
        iso3166_2 = "EU-ES-CB" // ES-CB
        superRegion = spain
    }
    cantabria(Region){
        name ="Cantabria"
        iso3166_2 = "EU-ES-CB-CB" // ES-GC
        superRegion = cantabriaCA
        postalCode = "39"
    }

    /****** Ceuta *******/
    ceutaCA(Region){
        name ="ceuta (CA)"
        iso3166_2 = "EU-ES-CE"
        superRegion = spain
    }
    ceuta(Region){
        name ="Cantabria"
        iso3166_2 = "EU-ES-CE-CE" // ES-CE
        superRegion = ceutaCA
        postalCode = "51"
    }

    /****** Melilla *******/
    melillaCA(Region){
        name ="ceuta (CA)"
        iso3166_2 = "EU-ES-ML"
        superRegion = spain
    }
    melilla(Region){
        name ="Cantabria"
        iso3166_2 = "EU-ES-ML-ML" // ES-ML
        superRegion = melillaCA
        postalCode = "52"
    }
}