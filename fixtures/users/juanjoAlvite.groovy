import kuorum.core.model.CommissionType
import kuorum.core.model.Gender
import kuorum.core.model.UserType
import kuorum.core.model.gamification.GamificationAward
import kuorum.users.Gamification
import kuorum.users.KuorumUser
import kuorum.users.PersonData

fixture {

    log.info "Creando usuario 'juanjo alvite' "


    juanjoGamification(Gamification){
        numEggs = 10
        numPlumes = 10
        numCorns = 20
        activeRole = GamificationAward.ROLE_ACTIVISTA
        boughtAwards = [GamificationAward.ROLE_DEFAULT,  GamificationAward.ROLE_ACTIVISTA]
    }

    juanjoPersonalData(PersonData){
        gender =  Gender.MALE
        userType = UserType.PERSON
        postalCode = "28001"
        provinceCode = "EU-SP-MD-MD"
        province = madrid
        birthday = Date.parse("dd/MM/yyyy","09/10/1980")
    }

    juanjoAlvite(KuorumUser){
        email = "juanjoAlvite@example.com"
        name ="Juanjo Alvite1"
        personalData = juanjoPersonalData
        password = springSecurityService.encodePassword("test")
        following = []
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
        gamification = juanjoGamification
        lastNotificationChecked = Date.parse("dd/MM/yyyy","09/09/2012")

        accountExpired = false
        accountLocked = false
        authorities = [roleUser,rolePremium]
        dateCreated = Date.parse("dd/MM/yyyy","20/11/2013")
        enabled = true
        lastUpdated = Date.parse("dd/MM/yyyy","01/11/2013")
        passwordExpired = false
    }
}
