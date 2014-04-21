import kuorum.core.model.CommissionType
import kuorum.core.model.Gender
import kuorum.core.model.UserType
import kuorum.core.model.gamification.GamificationAward
import kuorum.users.Gamification
import kuorum.users.KuorumUser
import kuorum.users.PersonData

fixture {

    noeGamification(Gamification){
        numEggs = 0
        numPlumes = 0
        numCorns = 0
        activeRole = GamificationAward.ROLE_DEFAULT
        boughtAwards = [GamificationAward.ROLE_DEFAULT ]
    }

    noeData(PersonData){
        gender =  Gender.FEMALE
        userType = UserType.PERSON
        postalCode = "08003"
        provinceCode = "EU-ES-CT-BA"
        province = barcelona
        birthday = Date.parse("dd/MM/yyyy","09/10/1983")
    }

    noe(KuorumUser){
        email = "noe@example.com"
        name ="Noelia De todos los santos"
        personalData = noeData
        verified = Boolean.FALSE
        password = springSecurityService.encodePassword("test")
        relevantCommissions = [
                CommissionType.JUSTICE,
                CommissionType.CONSTITUTIONAL,
                CommissionType.AGRICULTURE,
                CommissionType.NUTRITION_AND_ENVIRONMENT,
                CommissionType.FOREIGN_AFFAIRS,
                CommissionType.RESEARCH_DEVELOP,
                CommissionType.CULTURE,
                CommissionType.DEFENSE,
                CommissionType.ECONOMY,
                CommissionType.EDUCATION_SPORTS,
                CommissionType.EMPLOY_AND_HEALTH_SERVICE,
                CommissionType.PUBLIC_WORKS,
                CommissionType.TAXES,
                CommissionType.INDUSTRY,
                CommissionType.DOMESTIC_POLICY,
                CommissionType.BUDGETS,
                CommissionType.HEALTH_CARE,
                CommissionType.EUROPE_UNION,
                CommissionType.DISABILITY,
                CommissionType.ROAD_SAFETY,
                CommissionType.SUSTAINABLE_MOBILITY,
                CommissionType.OTHERS
        ]
        language ="es_ES"
        userType = UserType.PERSON
        favorites = []
        numFollowers = 0
        gamification = noeGamification
        lastNotificationChecked = Date.parse("dd/MM/yyyy","09/09/2012")

        accountExpired = false
        accountLocked = false
        authorities = [roleUser]
        dateCreated = Date.parse("dd/MM/yyyy","09/09/2013")
        enabled = true
        lastUpdated = Date.parse("dd/MM/yyyy","01/11/2013")
        passwordExpired = false
    }
}

