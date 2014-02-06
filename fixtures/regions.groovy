import kuorum.Region

fixture {

    europe(Region){
        name ="Europa"
        iso3166_2 = "EU"
    }

    spain(Region){
        name ="España"
        iso3166_2 = "EU-SP"
        superRegion = europe
    }

    madrid(Region){
        name ="Madrid"
        iso3166_2 = "EU-SP-MD"
        superRegion = spain
    }

    catalonia(Region){
        name ="Cataluña"
        iso3166_2 = "EU-SP-CT"
        superRegion = spain
    }
}