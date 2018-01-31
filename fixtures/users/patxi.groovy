import kuorum.core.model.CommissionType
import kuorum.core.model.Gender
import kuorum.core.model.UserType
import kuorum.core.model.gamification.GamificationAward
import kuorum.mail.MailType
import kuorum.notifications.Notice
import kuorum.notifications.NoticeType
import kuorum.users.Gamification
import kuorum.users.KuorumUser
import kuorum.users.PersonData
import kuorum.users.SocialLinks

fixture {


    patxiData(PersonData){
        country = spain
        gender = Gender.MALE
        phonePrefix = "+34"
        postalCode = "28220"
        province = guipuzcua
        provinceCode = "EU-ES-PV-SS"
        userType = UserType.PERSON
    }

    patxiGamification(Gamification){
        numEggs = 1000
        numPlumes = 1000
        numCorns = 1000
        activeRole = GamificationAward.ROLE_DEFAULT
        boughtAwards = [GamificationAward.ROLE_DEFAULT ]
    }

    patxiSocialLinks(SocialLinks){
        facebook = ""
        twitter = ""
        googlePlus = ""
        linkedIn = ""
        youtube = ""
        blog = ""
        instagram = ""
    }

    patxiNotice(Notice){
        firstDashboard = Date.parse("dd/MM/yyyy","20/11/2013")
        noticeType = NoticeType.NOAGEANDGENDER
        reloadDashboard = 28
        timesInMonth = 2

    }

    patxi(KuorumUser) {
        accountExpired = false
        accountLocked = false
        authorities = [roleUser]
        dateCreated = Date.parse("dd/MM/yyyy","20/11/2013")
        email = "patxi@example.com"
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
        name = "Patxi"
        password =  springSecurityService.encodePassword("test")
        passwordExpired = false
        personalData = patxiData
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
        gamification = patxiGamification
        subscribers = []
        activity = patxiActivity
        socialLinks = patxiSocialLinks
        notice = patxiNotice
        alias = "patxi"
    }
}