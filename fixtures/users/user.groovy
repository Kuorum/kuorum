import kuorum.users.User

fixture {


    user(User){
        accountExpired = false
        accountLocked = false
        authorities = [roleUser]
        dateCreated = Date.parse("dd/MM/yyyy","20/11/2013")
        defend = ""
        enabled = true
        followingEnterprises = []
        friends = []
        langauge ="es_ES"
        lastUpdated = Date.parse("dd/MM/yyyy","01/11/2013")
        name ="Usuario normal"
        password = springSecurityService.encodePassword("test")
        passwordExpired = false
        pathAvatar = null
        personalData = userData
        privacy = Privacy.PUBLIC
        relevantCommissions = [
                CommissionType.JUSTICE,
                CommissionType.CONSTITUTIONAL,
                CommissionType.AGRICULTURE,
                CommissionType.NUTRITION_AND_ENVIRONMENT,
                CommissionType.FOREIGN_AFFAIRS,
                CommissionType.IMPROVING,
                CommissionType.CULTURE,
                CommissionType.DEFENSE,
                CommissionType.ECONOMY,
                CommissionType.EDUCATION_SPORTS,
                CommissionType.EMPLOY_AND_HEALTH_SERVICE,
                CommissionType.DEVELOPING,
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
        requestedFriends = []
        socialLinks = userSocialLinks
        username = "user@kuorum.org"
    }
}
