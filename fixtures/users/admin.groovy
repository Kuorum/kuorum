import kuorum.core.model.CommissionType
import kuorum.core.model.Gender
import kuorum.users.Person
import kuorum.users.PersonalData

fixture {

    adminData(PersonalData){
        gender =  Gender.FEMALE
        postalCode = "28004"
        regionCode = "EU-SP-MD"
        birthday = Date.parse("dd/MM/yyyy","21/04/1982")
    }

    admin(Person){
        email = "Admin@example.com"
        name ="Admin"
        personalData = adminData
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

        accountExpired = false
        accountLocked = false
        authorities = [roleUser, roleAdmin]
        dateCreated = Date.parse("dd/MM/yyyy","09/09/2012")
        enabled = true
        lastUpdated = Date.parse("dd/MM/yyyy","01/11/2012")
        passwordExpired = false
    }
}

