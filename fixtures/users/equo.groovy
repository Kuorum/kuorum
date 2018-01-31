import kuorum.core.model.CommissionType
import kuorum.core.model.EnterpriseSector
import kuorum.core.model.Gender
import kuorum.core.model.UserType
import kuorum.core.model.gamification.GamificationAward
import kuorum.mail.MailType
import kuorum.notifications.Notice
import kuorum.notifications.NoticeType
import kuorum.users.Gamification
import kuorum.users.KuorumUser
import kuorum.users.OrganizationData
import kuorum.users.SocialLinks

fixture {

    equoGamification(Gamification){
        numEggs = 0
        numPlumes =0
        numCorns = 0
        activeRole = GamificationAward.ROLE_DEFAULT
        boughtAwards = [GamificationAward.ROLE_DEFAULT]
    }

    equoData(OrganizationData){
        country = spain
        gender = Gender.ORGANIZATION
        phonePrefix = "+34"
        postalCode = "EU-ES-MD-MD"
        province = madrid
        provinceCode = "EU-ES-PV-SS"
        userType = UserType.ORGANIZATION
        enterpriseSector = EnterpriseSector.NGO
    }

    equoSocialLinks(SocialLinks){
        facebook = ""
        twitter = ""
        googlePlus = ""
        linkedIn = ""
        youtube = ""
        blog = ""
        instagram = ""
    }

    equoNotice(Notice){
        firstDashboard = Date.parse("dd/MM/yyyy","20/11/2013")
        noticeType = NoticeType.NOAGEANDGENDER
        reloadDashboard = 28
        timesInMonth = 2

    }

    equo(KuorumUser){
        accountExpired = false
        accountLocked = false
        authorities = [roleUser]
        dateCreated = Date.parse("dd/MM/yyyy","20/11/2013")
        email = "equo@example.com"
        verified = false
        userType = UserType.ORGANIZATION
        avatar = null
        enabled = true
        followers = []
        following = []
        favorites = []
        numFollowers = 0
        language = "en_EN"
        lastUpdated = Date.parse("dd/MM/yyyy","20/11/2013")
        lastNotificationChecked = Date.parse("dd/MM/yyyy","20/11/2013")
        name = "equo"
        password =  springSecurityService.encodePassword("test")
        passwordExpired = false
        personalData = equoData
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
        availableMails = [
                MailType.REGISTER_VERIFY_EMAIL,
                MailType.REGISTER_RESET_PASSWORD,
                MailType.REGISTER_RRSS,
                MailType.REGISTER_ACCOUNT_COMPLETED,
                MailType.NOTIFICATION_CLUCK,
                MailType.NOTIFICATION_FOLLOWER,
                MailType.NOTIFICATION_PUBLIC_MILESTONE,
                MailType.NOTIFICATION_DEBATE_USERS,
                MailType.NOTIFICATION_DEBATE_AUTHOR,
                MailType.NOTIFICATION_DEBATE_POLITICIAN,
                MailType.NOTIFICATION_DEFENDED_USERS,
                MailType.NOTIFICATION_DEFENDED_AUTHOR,
                MailType.NOTIFICATION_DEFENDED_BY_POLITICIAN,
                MailType.NOTIFICATION_DEFENDED_POLITICIANS,
                MailType.NOTIFICATION_VICTORY_USERS,
                MailType.NOTIFICATION_VICTORY_DEFENDER,
                MailType.POST_CREATED_1,
                MailType.POST_CREATED_2,
                MailType.POST_CREATED_3,
                MailType.POST_CREATED_4,
                MailType.NOTIFICATION_COMMENTED_POST_OWNER,
                MailType.NOTIFICATION_VICTORY_USERS,
                MailType.NOTIFICATION_COMMENTED_POST_USERS,
                MailType.PROJECT_CREATED_NOTIFICATION,
        ]
        gamification = equoGamification
        subscribers = []
        activity = equoActivity
        socialLinks = equoSocialLinks
        notice = equoNotice
    }
}

