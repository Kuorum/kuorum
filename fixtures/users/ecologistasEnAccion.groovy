import kuorum.core.model.CommissionType
import kuorum.core.model.Gender
import kuorum.core.model.UserType
import kuorum.users.KuorumUser
import kuorum.users.OrganizationData

fixture {


    ecologistasData(OrganizationData){
        gender =  Gender.ORGANIZATION
        userType = UserType.ORGANIZATION
        provinceCode = "EU-ES"
        country = spain
    }

    ecologistasEnAccion(KuorumUser){
//        username = "ecologistasEnAccion"
        email = "ecologistas@example.com"
        name ="Ecologistas en acción"
        personalData = ecologistasData
        verified = Boolean.TRUE
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
//                CommissionType.SUSTAINABLE_MOBILITY,
                CommissionType.OTHERS
        ]
        language ="es_ES"
        favorites = []
        userType = UserType.ORGANIZATION
        numFollowers = 0

        accountExpired = false
        accountLocked = false
        authorities = [roleUser]
        dateCreated = Date.parse("dd/MM/yyyy","20/11/2013")
        enabled = true
        lastUpdated = Date.parse("dd/MM/yyyy","01/11/2013")
        passwordExpired = false
    }
}
