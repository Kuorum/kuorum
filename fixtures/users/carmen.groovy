import kuorum.core.model.CommissionType
import kuorum.core.model.Gender
import kuorum.core.model.UserType
import kuorum.mail.MailType
import kuorum.notifications.Notice
import kuorum.users.KuorumUser
import kuorum.users.PersonData
import kuorum.users.SocialLinks

fixture {

    carmenData(PersonData){
        country = spain
        gender = Gender.FEMALE
        phonePrefix = "+34"
        postalCode = "EU-ES-MD-MD"
        province = madrid
        provinceCode = "EU-ES-PV-SS"
        userType = UserType.PERSON
        year = 1983
    }

    carmenSocialLinks(SocialLinks){
        facebook = ""
        twitter = ""
        googlePlus = ""
        linkedIn = ""
        youtube = ""
        blog = ""
        instagram = ""
    }

    carmenNotice(Notice){
        firstDashboard = Date.parse("dd/MM/yyyy","20/11/2013")
        reloadDashboard = 28
        timesInMonth = 2

    }

    carmen(KuorumUser){
        accountExpired = false
        accountLocked = false
        authorities = [roleUser]
        dateCreated = Date.parse("dd/MM/yyyy","20/11/2013")
        email = "carmen@example.com"
        verified = false
        userType = "PERSON"
        avatar = null
        enabled = true
        followers = []
        following = []
        favorites = []
        numFollowers = 0
        language = "en_EN"
        lastUpdated = Date.parse("dd/MM/yyyy","20/11/2013")
        lastNotificationChecked = Date.parse("dd/MM/yyyy","20/11/2013")
        name = "carmen"
        password =  springSecurityService.encodePassword("test")
        passwordExpired = false
        personalData = carmenData
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
                CommissionType.OTHERS
        ]
        activity = carmenActivity
        socialLinks = carmenSocialLinks
        notice = carmenNotice
    }
}

