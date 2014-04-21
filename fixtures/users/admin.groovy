import kuorum.core.model.CommissionType
import kuorum.core.model.Gender
import kuorum.core.model.UserType
import kuorum.core.model.gamification.GamificationAward
import kuorum.users.Gamification
import kuorum.users.KuorumUser
import kuorum.users.PersonData

fixture {

    adminGamification(Gamification){
        numEggs = 0
        numPlumes =0
        numCorns = 0
        activeRole = GamificationAward.ROLE_DEFAULT
        boughtAwards = [GamificationAward.ROLE_DEFAULT]
    }

    adminData(PersonData){
        gender =  Gender.FEMALE
        userType = UserType.PERSON
        postalCode = "28004"
        provinceCode = "EU-SP-MD-MD"
        province = madrid
        birthday = Date.parse("dd/MM/yyyy","21/04/1982")
    }

    admin(KuorumUser){
        email = "Admin@example.com"
        name ="Admin"
        personalData = adminData
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
        gamification = adminGamification
        lastNotificationChecked = Date.parse("dd/MM/yyyy","09/09/2012")

        accountExpired = false
        accountLocked = false
        authorities = [roleUser, roleAdmin]
        dateCreated = Date.parse("dd/MM/yyyy","09/09/2012")
        enabled = true
        lastUpdated = Date.parse("dd/MM/yyyy","01/11/2012")
        passwordExpired = false
    }
}

