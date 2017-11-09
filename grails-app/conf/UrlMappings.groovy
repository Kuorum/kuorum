import grails.util.Environment
import kuorum.core.exception.KuorumException
import kuorum.core.model.AvailableLanguage
import org.springframework.security.access.AccessDeniedException

class UrlMappings {

    static excludes = ['/robots.txt']

    static List<String> RESERVED_PATHS = ['j_spring_security_facebook_redirect','project', 'proyectos', 'ciudadanos', 'organizaciones', 'politicos', 'register', 'login','js','images','css', 'fonts']
    static List<String> VALID_LANGUAGE_PATHS = AvailableLanguage.values().collect{it.locale.language}
	static mappings = {

        /**********************/
        /***** I18N URLs ******/
        /**********************/

        /**/
        /** NEW LANDIGNS **/
        name landingServices:       "/$lang/leaders-in-engagement" (controller: "landing", action: "landingServices"){constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name landingTechnology:     "/$lang/email-blasts-surveys-and-debates" (controller: "landing", action: "landingTechnology"){constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
                                    "/email-blasts-surveys-and-debates" { controller="redirect"; action= "redirect301"; newMapping='landingTechnology'}
                                    "/technology" { controller="redirect"; action= "redirect301"; newMapping='landingTechnology'}
        name landingEnterprise:     "/$lang/employee-engagement" (controller: "landing", action: "landingEnterprise"){constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
                                    "/employee-engagement" { controller="redirect"; action= "redirect301"; newMapping='landingEnterprise'}
                                    "/enterprises" { controller="redirect"; action= "redirect301"; newMapping='landingEnterprise'}
        name landingGovernments:    "/$lang/transparency-and-participation" (controller: "landing", action: "landingGovernments"){constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
                                    "/transparency-and-participation" { controller="redirect"; action= "redirect301"; newMapping='landingGovernments'}
                                    "/governments" { controller="redirect"; action= "redirect301"; newMapping='landingGovernments'}
        name landingOrganization:   "/$lang/fundraising-tools" (controller: "landing", action: "landingOrganization"){constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
                                    "/fundraising-tools" { controller="redirect"; action= "redirect301"; newMapping='landingOrganization'}
                                    "/organizations" { controller="redirect"; action= "redirect301"; newMapping='landingOrganization'}
        name landingCaseStudy:      "/$lang/successful-stories"                                                     (controller: "landing", action: "landingCaseStudy"){constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
                                    "/successful-stories"                                                           { controller="redirect"; action= "redirect301"; newMapping='landingCaseStudy'}
        name landingCaseStudy001:   "/$lang/successful-stories/toledo-city-council-digitalises-participation"       (controller: "landing", action: "caseStudy001"){constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
                                    "/successful-stories/toledo-city-council-digitalises-participation"             { controller="redirect"; action= "redirect301"; newMapping='landingCaseStudy001'}
        name landingCaseStudy002:   "/$lang/successful-stories/national-media-group-gamifies-user-experience"       (controller: "landing", action: "caseStudy002"){constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
                                    "/successful-stories/national-media-group-gamifies-user-experience"             { controller="redirect"; action= "redirect301"; newMapping='landingCaseStudy002'}
        name landingCaseStudy003:   "/$lang/successful-stories/oxfam-gives-visibility-to-its-advocacy-campaigns"    (controller: "landing", action: "caseStudy003"){constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
                                    "/successful-stories/oxfam-gives-visibility-to-its-advocacy-campaigns"          { controller="redirect"; action= "redirect301"; newMapping='landingCaseStudy003'}
        name landingCaseStudy004:   "/$lang/successful-stories/kaunas-citizens-design-their-cycle-lane"             (controller: "landing", action: "caseStudy004"){constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
                                    "/successful-stories/kaunas-citizens-design-their-cycle-lane"                   { controller="redirect"; action= "redirect301"; newMapping='landingCaseStudy004'}

        name home:              "/$lang" (controller: "landing", action:"landingServices"){constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
                                "/" { controller="redirect"; action= "redirect301"; newMapping='home'}

        name landingSearch:     "/$lang/discover"   (controller: "search", action:"searchLanding") {constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
                                "/discover"         { controller="redirect"; action= "redirect301"; newMapping='landingSearch'}

        name footerAboutKuorum:     "/$lang/what-is-kuorum" (controller: "footer", action: "footerAboutUs"){constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
                                    "/what-is-kuorum" {controller="redirect"; action= "redirect301"; newMapping='footerAboutKuorum'}
        name footerContactUs:       "/$lang/contact" (controller: "footer", action: "footerContactUs"){constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
                                    "/contact" {controller="redirect"; action= "redirect301"; newMapping='footerContactUs'}
        name footerOurTeam:         "/$lang/team" (controller: "footer", action: "footerOurTeam"){constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
                                    "/team" {controller="redirect"; action= "redirect301"; newMapping='footerOurTeam'}
        name footerUserGuides:      "/$lang/user-guides" (controller: "footer", action: "footerUserGuides"){constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
                                    "/user-guides" {controller="redirect"; action= "redirect301"; newMapping='footerUserGuides'}
        name footerPress:           "/$lang/press-and-media" (controller: "footer", action: "footerPress"){constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
                                    "/press-and-media" {controller="redirect"; action= "redirect301"; newMapping='footerPress'}
        name footerHistory:         "/$lang/our-story" (controller: "footer", action: "footerHistory"){constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
                                    "/our-story" {controller="redirect"; action= "redirect301"; newMapping='footerHistory'}
        /**/
        /*** CUSTOM BLOG ARTICLES ***/
        name footerBlog:            "/$lang/blog" (controller: "footer", action: "footerBlog"){constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
                                    "/blog" {controller="redirect"; action= "redirect301"; newMapping='footerBlog'}
        name footerBlog001:         "/$lang/blog/ii-international-conference-on-online-political-communication" (controller: "footer", action: "footerBlog001"){constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
                                    "/blog/ii-international-conference-on-online-political-communication" {controller="redirect"; action= "redirect301"; newMapping='footerBlog001'}
        /* END CUSTOM BLOG ARTICLES */

        name footerPrivacyPolicy:   "/$lang/legal/privacy-policy"       (controller:"footer", action: "privacyPolicy")
                                    "/legal/privacy-policy"             {controller="redirect"; action= "redirect301"; newMapping='footerPrivacyPolicy'}
                                    "/legal"                            {controller="redirect"; action= "redirect301"; newMapping='footerPrivacyPolicy'}
        name footerTermsUse:        "/$lang/legal/terms-of-use"         (controller:"footer", action: "termsUse")
                                    "/legal/terms-of-use"               {controller="redirect"; action= "redirect301"; newMapping='footerTermsUse'}

        name register:              "/$lang/sign-up"    (controller: "register"){action = [GET:"index", POST:"register"]}
                                    "/sign-up"          {controller="redirect"; action= "redirect301"; newMapping='register'}
                                    "/registro"         {controller="redirect"; action= "redirect301"; newMapping='register'}
        name registerAjax:          "/ajax/sign-up"     (controller: "register", action:"ajaxRegister")
        name registerAjaxCheckEmail:"/ajax/sign-up/checkEmail"(controller: "register", action:"checkEmail")
        name registerAjaxRRSSOAuth: "/ajax/sign-up/rrssOAuth"(controller: "register", action:"registerRRSSOAuthAjax")

        name registerPressKit:      "/$lang/sign-up/pressKit"   (controller: "register",action:"downloadCaseStudy")
        name registerStep2:         "/$lang/sign-up/step2"      (controller: "customRegister"){action = [GET:"step2", POST:"step2Save"]}
        name registerStep3:         "/$lang/sign-up/step3"      (controller: "customRegister", action :"step3")


        name registerSuccess:       "/$lang/sign-up/success"(controller: "register",action:"registerSuccess")
                                    "/registro/satisfactorio"{controller="redirect"; action= "redirect301"; newMapping='registerSuccess'}
        name registerPassword:      "/$lang/sign-up/establece-password"(controller: "register", action:"selectMyPassword")
                                    "/registro/establece-password"{controller="redirect"; action= "redirect301"; newMapping='registerPassword'}
        name registerResendMail:    "/$lang/sign-up/no-valid"(controller: "register"){action=[GET:"resendRegisterVerification", POST:"resendVerification"]}
                                    "/registro/no-verificado"{controller="redirect"; action= "redirect301"; newMapping='registerResendMail'}
        name resetPassword:         "/$lang/sign-in/recover-password"(controller: "register"){action=[GET:"forgotPassword", POST:"forgotPasswordPost"]}
                                    "/sign-in/recover-password"{controller="redirect"; action= "redirect301"; newMapping='resetPassword'}
                                    "/registro/password-olvidado"{controller="redirect"; action= "redirect301"; newMapping='resetPassword'}
        name validateResetPasswordAjax:"/ajax/forgot-password" (controller:"register", action: "ajaxValidationForgotPassword")
        name resetPasswordSent:     "/$lang/sign-up/verification-sent"(controller: "register", action:"forgotPasswordSuccess")
                                    "/registro/enviada-verificacion"{controller="redirect"; action= "redirect301"; newMapping='resetPasswordSent'}
        name resetPasswordChange:   "/$lang/sign-up/change-pass"(controller: "register"){action=[GET:"resetPassword", POST:"resetPassword"]}
                                    "/registro/cambiar-password"{controller="redirect"; action= "redirect301"; newMapping='resetPasswordChange'}

        name requestADemo:          "/register/requestADemo"(controller: "register", action: "requestADemo")


        name login:     "/$lang/log-in" (controller:"login", action:"index")
                        "/log-in"       {controller="redirect"; action= "redirect301"; newMapping='login'}
                        "/entrar"       {controller="redirect"; action= "redirect301"; newMapping='login'}
        name loginCheck:"/ajax/checkLogin" (controller:"login", action:"checkEmailAndPass")
        name loginAuth: "/$lang/sign-in"(controller:"login", action:"auth")
                        "/sign-in"      {controller="redirect"; action= "redirect301"; newMapping='loginAuth'}
                        "/autenticarse" {controller="redirect"; action= "redirect301"; newMapping='loginAuth'}
                        "/login/auth"   {controller="redirect"; action= "redirect301"; newMapping='loginAuth'}

        name loginFull: "/$lang/confirmar-usuario"  (controller:"login", action:"full")
                        "/confirmar-usuario"        {controller="redirect"; action= "redirect301"; newMapping='loginFull'}
        name logout:    "/logout"       (controller:"logout", action:"index")
                        "/salir"        {controller="redirect"; action= "redirect301"; newMapping='logout'}

        name searcherSearch:        "/$lang/search"(controller: "search", action:"search")
                                    "/search"{controller="redirect"; action= "redirect301"; newMapping='searcherSearch'}
                                    "/buscar"{controller="redirect"; action= "redirect301"; newMapping='searcherSearch'}

        /**********************/
        /***** LOGGED URLs ****/ //Language no matters
        /**********************/
        name dashboard:                     "/dashboard" (controller: "dashboard", action:"dashboard")
        name dashboardSkipUploadContacts:   "/dashboard/skipContacts" (controller: "dashboard", action:"skipContacts")
        name dashboardCausesSeeMore:        "/ajax/dashboard/causes/see-more" (controller: "dashboard", action:"dashboardCauses")
        name dashboardPoliticiansSeeMore:   "/ajax/dashboard/politicians/see-more" (controller: "dashboard", action:"dashboardPoliticians")
        name dashboardCampaignsSeeMore:     "/ajax/dashboard/campaigns/see-more" (controller: "dashboard", action:"dashboardCampaigns")

        name projectCreate:             "/project/new"(controller: "project"){action = [GET:"create", POST:"save"]}
        name projectEdit:               "/project/$userAlias/$hashtag/edit"(controller: "project"){action = [GET:"edit", POST:"update"]}

        name debateCreate:      "/account/debate/new" (controller: "debate"){action = [GET: "create", POST: "saveSettings"]}
        name debateEdit:        "/account/$userAlias/d/$urlTitle-$debateId/edit-settings" (controller: "debate"){action = [GET: "editSettingsStep", POST: "saveSettings"]}
        name debateEditContent:  "/account/$userAlias/d/$urlTitle-$debateId/edit-content" (controller: "debate"){action = [GET: "editContentStep", POST: "saveContent"]}

        name debateRemove:      "/ajax/account/$userAlias/d/$urlTitle-$debateId/remove" (controller: "debate", action: "remove")
        name debateShow:        "/$userAlias/d/$urlTitle-$debateId"(controller: "debate", action: "show"){constraints{userAlias(validator:{!UrlMappings.RESERVED_PATHS.contains(it) && !UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name langDebateShow:    "/$lang/$userAlias/d/$urlTitle-$debateId"(controller: "debate", action: "show"){constraints{userAlias(validator:{!UrlMappings.RESERVED_PATHS.contains(it) && !UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
                                "/$userAlias/d/-$debateId"      (controller: "debate", action: "show"){constraints{userAlias(validator:{!UrlMappings.RESERVED_PATHS.contains(it) && !UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
                                "/$lang/$userAlias/d/-$debateId"      (controller: "debate", action: "show"){constraints{userAlias(validator:{!UrlMappings.RESERVED_PATHS.contains(it) && !UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name debateProposalNew: "/ajax/addProposal"(controller: "debateProposal", action: "addProposal")
        name debateProposalDelete:"/ajax/deleteProposal"(controller: "debateProposal", action: "deleteProposal")
        name debateProposalPin: "/ajax/pinProposal"(controller: "debateProposal", action: "pinProposal")
        name debateProposalLike:"/ajax/likeProposal"(controller: "debateProposal", action: "likeProposal")
        name debateProposalComment: "/ajax/proposalComment/add"(controller: "debateProposal", action: "addComment")
        name debateProposalDeleteComment: "/ajax/proposalComment/delete"(controller: "debateProposal", action: "deleteComment")
        name debateProposalVoteComment: "/ajax/proposalComment/vote"(controller: "debateProposal", action: "voteComment")

        name postRemove:            "/ajax/account/$userAlias/p/$urlTitle-$postId/remove" (controller: "post", action: "remove")
        name postCreate:            "/account/post/new" (controller: "post"){action = [GET: "create", POST: "saveSettings"]}
        name postEdit:              "/account/$userAlias/p/$urlTitle-$postId/edit-settings" (controller: "post"){action = [GET: "editSettingsStep", POST: "saveSettings"]}
        name postEditContent:       "/account/$userAlias/p/$urlTitle-$postId/edit-content" (controller: "post"){action = [GET: "editContentStep", POST: "saveContent"]}
        name postShow:              "/$userAlias/p/$urlTitle-$postId"  (controller: "post", action: "show"){constraints{userAlias(validator:{!UrlMappings.RESERVED_PATHS.contains(it) && !UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name langPostShow:          "/$lang/$userAlias/p/$urlTitle-$postId"  (controller: "post", action: "show"){constraints{userAlias(validator:{!UrlMappings.RESERVED_PATHS.contains(it) && !UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
                                    "/$userAlias/p/-$postId"        (controller: "post", action: "show"){constraints{userAlias(validator:{!UrlMappings.RESERVED_PATHS.contains(it) && !UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
                                    "/$lang/$userAlias/p/-$postId"        (controller: "post", action: "show"){constraints{userAlias(validator:{!UrlMappings.RESERVED_PATHS.contains(it) && !UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}

        name postLike:              "/ajax/likePost"(controller: "post", action: "likePost")

        name langProjectShow:   "/$lang/$userAlias/$hashtag" (controller: "project", action:"show") {constraints{userAlias(validator:{!UrlMappings.RESERVED_PATHS.contains(it) && !UrlMappings.VALID_LANGUAGE_PATHS.contains(it)}); lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name projectShow:       "/$userAlias/$hashtag"                          {controller="redirect"; action= "redirect301Project"; constraints{userAlias(validator:{!UrlMappings.RESERVED_PATHS.contains(it) && !UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
                                "/hashtag/$hashtag"                             {controller="redirect"; action= "redirect301Project"; }
                                "/proyectos/$regionName/$commission/$hashtag"   {controller="redirect"; action= "redirect301Project"; }
                                "/leyes/$regionName/$commission/$hashtag"       {controller="redirect"; action= "redirect301Project";}

        name projectShowSec:            "/sec/project/$userAlias/$hashtag" (controller: "project", action:"showSecured")
        name projectVote:               "/ajax/project/$userAlias/$hashtag/votar"(controller: "project", action:"voteProject")
        name projectVoteNoTotalUser:    "/sec/project/$userAlias/$hashtag/salvarDatosUsuarioYvotar"(controller: "project", action:"voteProjectAsNonCompleteUser")
        name projectListClucks:         "/ajax/project/$userAlias/$hashtag/listado-kakareos" (controller: "project", action:"listClucksProject")
        name projectListPostDefends:    "/ajax/project/$userAlias/$hashtag/listado-post-defendidos" (controller: "project", action:"listClucksProjectDefends")
        name projectListPostVictories:  "/ajax/project/$userAlias/$hashtag/listado-victorias" (controller: "project", action:"listClucksProjectVictories")

        name projectUpdate:             "/project/$userAlias/$hashtag/actualizar"(controller: "project"){action = [GET:"createProjectUpdate", POST:"addProjectUpdate"]}

        // DEPRECATED
                            "/$lang/$userAlias/$hashtag/$postBrief-$postId"(controller: "redirect", action: "redirect301Project") {constraints{userAlias(validator:{!UrlMappings.RESERVED_PATHS.contains(it) && !UrlMappings.VALID_LANGUAGE_PATHS.contains(it)}); lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
                            "/$userAlias/$hashtag/$postBrief-$postId"{controller="redirect"; action= "redirect301Project"; constraints{userAlias(validator:{!UrlMappings.RESERVED_PATHS.contains(it) && !UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
                            "/proyectos/$regionName/$commission/$hashtag/propuesta/$postBrief-$postId"          {controller="redirect"; action= "redirect301Project";}
                            "/proyectos/$regionName/$commission/$hashtag/$urlPostTypeVieja/$postBrief-$postId"  {controller="redirect"; action= "redirect301Project";}
                            "/leyes/$regionName/$commission/$hashtag/$urlPostTypeVieja/$postBrief-$postId"      {controller="redirect"; action= "redirect301Project";}



        name widgetJs:      "/widget.js"(controller: "widget", action:"kuorumWidgetjs")
        name widgetRatePolitician:     "/widget/ratePolitician" (controller: "rating", action:"widgetRatePolitician")
        name widgetComparative:        "/widget/comparation"    (controller: "rating", action:"widgetComparativePoliticianInfo")


        name langUserShow:          "/$lang/$userAlias"     (controller: "kuorumUser", action: "show") {constraints{userAlias(validator:{!UrlMappings.RESERVED_PATHS.contains(it) && !UrlMappings.VALID_LANGUAGE_PATHS.contains(it)}); lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name userShow:              "/$userAlias"           (controller: "kuorumUser", action: "show") {constraints{userAlias(validator:{!UrlMappings.RESERVED_PATHS.contains(it) && !UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name secUserShow:           "/sec/$userAlias"       (controller: "kuorumUser", action: "secShow")


        name userFollowers:     "/ajax/$userAlias/followers" (controller: "kuorumUser", action: "userFollowers")
        name userFollowing:     "/ajax/$userAlias/following"  (controller: "kuorumUser", action: "userFollowing")
        name userUnsubscribe:   "/unsubscribe/$userId"  (controller: "contacts"){action=[GET:"unsubscribe", POST:"unsubscribeConfirm"]}

        name bulkActionRemoveContactsAjax:         "/ajax/contact/remove" (controller:"contacts", action: "removeContactsBulkAction")
        name bulkActionAddTagsContactsAjax:         "/ajax/contact/addTags" (controller:"contacts", action: "addTagsBulkAction")
        name bulkActionRemoveTagsContactsAjax:      "/ajax/contact/removeTags" (controller: "contacts", action: "removeTagsBulkAction")

        name userFollowAndRegister:          "/$userAlias/subscribe" (controller: "kuorumUser", action: "subscribeTo")
        name ajaxRegisterContact:            "/ajax/contact"(controller: "register", action: "contactRegister");

        name userRate:                  "/ajax/$userAlias/rate"(controller: "rating", action:"ratePolitician")
        name userHistoricRate:          "/ajax/$userAlias/historicRate"(controller: "rating", action:"historicPoliticianRate")
        name comparingPoliticianRate:   "/ajax/user/compareRate"(controller: "rating", action:"comparingPoliticianRateData")

        name searcherSearchSeeMore: "/ajax/search/seeMore"(controller: "search", action:"searchSeeMore")
        name searcherSearchFilters: "/ajax/search/new-filters"(controller: "search", action:"modifyFilters")
        name searcherSuggests:      "/ajax/search/suggestions"(controller: "search", action:"suggest")
        name suggestRegions:        "/ajax/search/suggestions/regions"(controller: "search", action:"suggestRegions")
        name suggestTags:           "/ajax/search/suggestions/tags"(controller: "search", action:"suggestTags")
        name suggestAlias:          "/ajax/search/suggestions/alias"(controller: "search", action:"suggestAlias")

        name profileEditAccountDetails:     "/config/account-details"                                   (controller: "profile"){action =[GET:"editAccountDetails", POST:"updateAccountDetails"]}
        name profileEditUser:               "/edit-profile"                                    (controller: "profile"){action =[GET:"editUser", POST:"editUserSave"]}
        name profileEditCommissions:        "/edit-profile/edit-commission"                    (controller: "profile"){action =[GET:"editCommissions", POST:"editCommissionsSave"]}
        name profileChangePass:             "/edit-profile/change-password"                    (controller: "profile"){action =[GET:"changePassword", POST:"changePasswordSave"]}
        name profileSetPass:                "/edit-profile/create-password"                    (controller: "profile"){action =[GET:"setPassword", POST:"setPasswordSave"]}
        name profileChangeEmail:            "/edit-profile/change-email"                       (controller: "profile"){action =[GET:"changeEmail", POST:"changeEmailSave"]}
        name profileChangeEmailSent:        "/edit-profile/change-email/request-received"      (controller: "profile", action :"changeEmailConfirmSent")
        name profileChangeEmailResend:      "/edit-profile/change-email/resend-email"          (controller: "profile", action :"updateUserEmail")
        name profileChangeEmailConfirm:     "/edit-profile/change-email/confirm"               (controller: "profile", action: "changeEmailConfirm")
        name profileSocialNetworks:         "/edit-profile/social-networks"                    (controller: "profile"){action=[GET:"socialNetworks",POST:"socialNetworksSave"]}
        name profileEmailNotifications:     "/edit-profile/email-notifications"                (controller: "profile"){action=[GET:"configurationEmails",POST:"configurationEmailsSave"]}
        name profileDeleteAccount:          "/edit-profile/delete-account"                     (controller: "profile"){action=[GET:"deleteAccount", POST:"deleteAccountPost"]}
        name profileCauses:                 "/edit-profile/causes"                             (controller: "profile"){action=[GET:"editCauses", POST:"updateCauses"]}
        name profileNews:                   "/edit-profile/news"                               (controller: "profile"){action=[GET:"editNews", POST:"updateNews"]}
        name profileQuickNotes:             "/edit-profile/quick-notes"                        (controller: "profile"){action=[GET:"editQuickNotes", POST:"updateQuickNotes"]}
        name profileProfessionalDetails:    "/edit-profile/professional-details"               (controller: "profile"){action=[GET:"editProfessionalDetails", POST:"updateProfessionalDetails"]}
        name profilePictures:               "/edit-profile/pictures"                           (controller: "profile"){action=[GET:"editPictures", POST: "updatePictures"]}
        name profileNewsletterConfig:       "/config/newsletter-config"                        (controller: "profile"){action=[GET:"editNewsletterConfig", POST: "updateNewsletterConfig"]}
        name profileNewsletterConfigRequestEmailSender:     "/ajax/config/newsletter-config/requestSender"   (controller: "profile"){action=[POST: "requestedEmailSender"]}


        name profileMailing : "/notifications/mailing" (controller: "profile", action:"showUserEmails")

        name causeSupport:         "/ajax/cause/$causeName/support" (controller:"causes", action: "supportCause")
        name causeDiscard:         "/ajax/cause/$causeName/discard" (controller:"causes", action: "discardCause")

        name ajaxHeadNotificationsChecked:  "/ajax/notificaiones/check"(controller:"notification", action:"notificationChecked")
        name ajaxHeadNotificationsSeeMore:  "/ajax/notificaiones/seeMore"(controller:"notification", action:"notificationSeeMore")
        name ajaxHeadMessagesChecked:       "/ajax/mensajes/check"(controller:"layouts", action:"headNotificationsChecked")
        name ajaxFollow:                    "/ajax/kuorumUser/follow"(controller:"kuorumUser", action:"follow")
        name ajaxUnFollow:                  "/ajax/kuorumUser/unFollow"(controller:"kuorumUser", action:"unFollow")
        name ajaxRequestPolitician:         "/ajax/politico/solicitud-kuorum"(controller:"kuorumUser", action:"follow")
        name ajaxCropImage:                 "/ajax/file/crop"(controller:"file", action:"cropImage")
        name ajaxUploadFile:                "/ajax/file/upload" (controller:'file', action:"uploadImage")
        name ajaxUploadFilePDF:             "/ajax/file/uploadPDF" (controller:'file', action:"uploadPDF")

        name ajaxModuleUserCauses:          "/ajax/module/user/causes" (controller:"modules", action: "userCauses")

        name adminPrincipal:        "/admin"                          (controller:"admin", action: "index")
        name adminTestMail:         "/admin/mailing/test"           (controller:"mailTesting", action: "index")
        name adminSearcherIndex:    "/admin/searcher/indexar"       (controller:"admin", action: "solrIndex")
        name adminSearcherFullIndex:"/admin/searcher/full-index"    (controller:"admin", action:"fullIndex")
        name adminEditorsMonitoring:"/admin/editors/monitoring"    (controller:"admin", action:"editorsMonitoring")

        name editorCreatePolitician:                        "/editor/user/politician/create-politician"             (controller:"editorUser"){action =[GET:"createPolitician", POST:"saveCreatePolitician"]}
        name editorEditUserProfile:                         "/editor/user/$userAlias/editar/profile"                (controller:"editorUser"){action =[GET:"editUser", POST:"updateUser"]}
        name editorEditSocialNetwork:                       "/editor/user/$userAlias/editar/social-network"         (controller:"editorUser"){action =[GET:"editUserSocialNetwork", POST:"updateUserSocialNetwork"]}
        name editorEditNews:                                "/editor/user/$userAlias/editar/news"                   (controller:"editorPolitician"){action =[GET:"editNews", POST:"updateNews"]}
        name editorEditPoliticianProfessionalDetails:       "/editor/user/$userAlias/editar/professionalDetails"    (controller:"editorPolitician"){action =[GET:"editProfessionalDetails", POST:"updateProfessionalDetails"]}
        name editorEditPoliticianQuickNotes:                "/editor/user/$userAlias/editar/quick-notes"            (controller:"editorPolitician"){action =[GET:"editQuickNotes", POST:"updateQuickNotes"]}
        name editorEditPoliticianCauses:                    "/editor/user/$userAlias/editar/causes"                 (controller:"editorPolitician"){action =[GET:"editCauses", POST:"updateCauses"]}
        name editorKuorumAccountEdit:                       "/editor/user/$userAlias/editar/account-details"        (controller:"editorUser"){action =[GET:"editAdminAccountDetails", POST:"updateAdminAccountDetails"]}
        name editorAdminUserRights:                         "/editor/user/$userAlias/editar/rights"                 (controller:"admin"){action =[GET:"editUserRights", POST:"updateUserRights"]}
        name editorAdminEmailSender:                         "/editor/user/$userAlias/editar/email-sender"                 (controller:"admin"){action =[GET:"editUserEmailSender", POST:"updateUserEmailSender"]}
        name editorRequestRights:                           "/editor/request"                                           (controller:"editorRecruitment", action: "requestEditor")
        name editorDiscardWarns:                            "/editor/discard"                                           (controller:"editorRecruitment", action: "discardEditor")

        name ajaxDeleteRecommendedUser: "/ajax/kuorumUser/deleteRecommendedUser"(controller: 'recommendedUserInfo', action: 'deleteRecommendedUser')

        name politicianRequestBetaTester:               "/account/request-beta-tester-account" (controller:"politician", action: "requestAPoliticianBetaTester")
        name politicianAnalytics:                       "/account/data-analytics" (controller:"politician", action: "betaTesterPage")
        name politicianContactProfiling:                "/account/contact-profiling" (controller:"politician", action: "betaTesterPage")
        name politicianContacts:                        "/account/contacts" (controller:"contacts", action: "index")
        name politicianContactsSearch:                  "/ajax/account/contacts" (controller:"contacts", action: "searchContacts")
        name politicianContactExport:                   "/account/contacts/export" (controller:"contacts", action: "exportContacts")
        name politicianContactImport:                   "/account/contacts/import" (controller:"contacts", action: "importContacts")
        name politicianContactImportCSV:                "/account/contacts/import/csv" (controller:"contacts"){action =[GET:"importCSVContacts", POST:"importCSVContactsUpload"]}
        name politicianContactImportCSVSave:            "/account/contacts/import/csv_save" (controller:"contacts", action: "importCSVContactsSave")
        name politicianContactImportGmail:              "/account/contacts/import/gmail" (controller:"googleContacts", action: "index")
        name politicianContactImportOutlook:            "/account/contacts/import/outlook" (controller:"outlookContacts", action: "index")
        name politicianContactImportSuccess:            "/account/contacts/import/success" (controller:"contacts", action: "importSuccess")
        name politicianContactTagsAjax:                 "/ajax/account/contacts/tags" (controller:"contacts", action: "contactTags")
        name politicianContactAddTagsAjax:              "/ajax/account/contacts/$contactId/tags" (controller:"contacts", action: "addTagsToContact")
        name politicianContactRemoveAjax:               "/ajax/account/contacts/$contactId/remove" (controller:"contacts", action: "removeContact")
        name politicianContactFilterNew:                "/ajax/account/contacts/filters/new" (controller:"contactFilters", action: "newFilter")
        name politicianContactFilterUpdate:             "/ajax/account/contacts/filters/update" (controller:"contactFilters", action: "updateFilter")
        name politicianContactFilterRefresh:            "/ajax/account/contacts/filters/refresh" (controller:"contactFilters", action: "refreshFilter")
        name politicianContactFilterData:               "/ajax/account/contacts/filters/data" (controller:"contactFilters", action: "getFilterData")
        name politicianContactFilterDelete:             "/ajax/account/contacts/filters/delete" (controller:"contactFilters", action: "deleteFilter")
        name politicianContactEdit:                     "/account/contacts/$contactId/edit" (controller:"contacts"){action=[GET:"editContact", POST:"updateContact"]}
        name politicianContactEditUpdateNote:           "/ajax/account/contacts/$contactId/edit/updateNote" (controller:"contacts",action:"updateContactNotes")
        name politicianContactNew:                      "/account/contacts/new" (controller:"contacts"){action =[GET:"newContact", POST:"saveContact"]}
        name politicianInbox:                           "/account/inbox" (controller:"politician", action: "betaTesterPage")
        name politicianCampaigns:                       "/account/campaigns" (controller:"newsletter", action: "index")
        name politicianCampaignsNew:                    "/account/campaigns/new" (controller:"newsletter", action: "newCampaign")
        name politicianCampaignsLists:                  "/ajax/account/campaigns/lists" (controller:"campaign", action: "findLiUserCampaigns")
        name politicianCampaignsExport:                 "/account/campaigns/export" (controller:"newsletter", action: "exportCampaigns")
        name politicianCampaignsUploadImages:           "/ajax/account/campaign/$campaignId/uploadImages" (controller:"file", action: "uploadCampaignImages");
        name politicianCampaignsListImages:             "/ajax/account/campaign/$campaignId/listImages" (controller:"file", action: "getCampaignImages");
        name politicianMassMailingNew:                  "/account/mass-mailing/new" (controller:"newsletter"){ action=[GET:"createNewsletter", POST:'saveMassMailingSettings']}
        name politicianMassMailingSettings:             "/account/mass-mailing/$campaignId/edit-settings" (controller: "newsletter"){ action=[GET:"editSettingsStep", POST: 'saveMassMailingSettings']}
        name politicianMassMailingTemplate:             "/account/mass-mailing/$campaignId/edit-template" (controller: "newsletter"){ action=[GET:"editTemplateStep", POST: 'saveMassMailingTemplate']}
        name politicianMassMailingContent:              "/account/mass-mailing/$campaignId/edit-content" (controller: "newsletter") { action=[GET:"editContentStep", POST: 'saveMassMailingContent']}
//        name politicianMassMailingContentText:          "/account/mass-mailing/$campaignId/edit-content-text" (controller: "massMailing"){ action=[GET:"editContentStepText", POST: 'saveMassMailingContentText']}
//        name politicianMassMailingContentTemplate:      "/account/mass-mailing/$campaignId/edit-content-template" (controller: "massMailing"){ action=[GET:"editContentStepTemplate", POST: 'saveMassMailingContent']}
        name politicianMassMailingShow:                 "/account/mass-mailing/$campaignId" (controller:"newsletter"){ action=[GET:"showCampaign", POST:'updateCampaign']}
        name politicianMassMailingSendTest:             "/account/mass-mailing/$campaignId/test" (controller:"newsletter", action: "sendMassMailingTest")
        name politicianMassMailingRemove:               "/ajax/account/mass-mailing/$campaignId/remove" (controller:"newsletter", action:"removeCampaign")
        name politicianMassMailingTrackEvents:          "/ajax/account/mass-mailing/$campaignId/trackEvents" (controller:"newsletter", action: "showTrackingMails")
        name politicianMassMailingTrackEventsReport:    "/ajax/account/mass-mailing/$campaignId/trackEvents/report" (controller:"newsletter", action: "sendReport")
        name politicianMassMailingHtml:                 "/account/mass-mailing/$campaignId/html" (controller:"newsletter", action: "showMailCampaign")
        name politicianMassMailingSaveTimeZone:         "/account/mass-mailing/saveTimeZone" (controller: "newsletter"){action = [POST:"saveTimeZone"]}
        name politicianDebateStatsShow:                 "/account/debate/$debateId" (controller:"newsletter", action:"showDebateStats")
        name politicianMassMailingDebateStatsReport:    "/ajax/account/debate/$debateId/report" (controller:"debate", action: "sendReport")

        name politicianTeamManagement:                  "/account/team-management" (controller:"politician", action: "betaTesterPage")

        name paymentStart:                              "/payment" (controller:"payment")
        name paymentGateway:                            "/payment/gateway" (controller:"payment"){action=[GET:'paymentGateway',POST:'paymentGatewaySubmitSubscription']}
        name paymentGatewayPlan:                        "/ajax/payment/gateway/plan" (controller:"payment", action: "getInfoPlan")
        name paymentGatewaySavePaymentMethod:           "/ajax/payment/gateway/paymentMethod" (controller:"payment", action: "savePaymentMethod")
        name paymentSuccess:                            "/payment/success" (controller:"payment", action: 'paymentSuccess')
        name paymentPromotionalCodeValidation:          "/ajax/payment/promotional-code" (controller: "payment"){action = [POST:"promotionalCodeValidation"]}

        "/account/contacts/oauth/$provider/success" (controller: "contactsOAuth", action: "onSuccess")
        "/account/contacts/oauth/$provider/failure" (controller: "contactsOAuth", action: "onFailure")

        "/googleContacts/loadContactsFromGoogle" (controller: "googleContacts", action: "loadContactsFromGoogle")

        "/admin/updateMailChimp" (controller: "admin", action: "updateMailChimp")

        /**********************/
        /***** DEPRECATED *****/
        /**********************/

        // REGISTRO DE POLITICO
        name registerSubscriptionStep1:  "/subscribe/step1" (controller: "customRegister", action:"subscriptionStep1")
        name registerSubscriptionStep1Save:  "/subscribe/step1-save" (controller: "customRegister"){action = [GET:"subscriptionStep1", POST:"subscriptionStep1Save"]}
        name registerSubscriptionStep3:  "/subscribe/step3" (controller: "customRegister", action:"subscriptionStep3")

        // Registros parciales
        name customRegisterCountryAndPostalCode: "/registro/countryAndPostalCode"(controller: "customRegister", action:"countryAndPostalCode")
        name customRegisterAgeAndGender: "/registro/ageAndGender"(controller: "customRegister", action:"ageAndGender")
        name customRegisterTelephone: "/registro/telephone"(controller: "customRegister", action:"telephone")

        // Funel antiguo de compra de politico
        name funnelSuccessfulStories:      "/la-nueva-politica"  (controller:"funnel", action:"funnelSuccessfulStories")

        name tourStart:           "/tour" (controller:"tour", action: "index")
        name tour_dashboard:      "/tour/dashboard" (controller:"tour", action: "tour_dashboard")

        // ADMIN STATS
        name adminStats:            "/admin/estadisticas"           (controller:"adminStats", action: "stats")
        name adminStatsPieChart:    "/admin/estadisticas/pie-chart" (controller:"adminStats", action: "statsDataPieChart")

        /**********************/
        /*** END DEPRECATED ***/
        /**********************/

        name sitemapIndex:  "/$lang/sitemapIndex" (controller: "siteMap", action: "sitemapIndex"){constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
                            "/sitemapIndex"{ controller="redirect"; action= "redirect301"; newMapping='sitemapIndex'}

        name sitemap:       "/$lang/sitemap" (controller: "siteMap", action: "sitemap"){constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
                            "/sitemap"{ controller="redirect"; action= "redirect301"; newMapping='sitemap'}

        name sitemapCountry:"/$lang/sitemapCountry" (controller: "siteMap", action: "sitemapCountry"){constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
                            "/sitemapCountry"{ controller="redirect"; action= "redirect301"; newMapping='sitemapCountry'}

        "403" (controller: "error", action: "forbidden")
        "404" (controller: "error", action: "notFound")
        "401" (controller: "error", action: "notAuthorized")


       "/$controller/$action?/$id?"{
           constraints {
                        // apply constraints here
                    }
                }

        Environment.executeForCurrentEnvironment {
            development {
                "500" (controller: "error", action: "kuorumExceptionHandler", exception: KuorumException)
                "500" (controller: "error", action: "notAuthorized", exception: AccessDeniedException)
                "500" (controller: "error", action: "internalError")
//                "500"(view:'/error')
            }
            test{
                "500" (controller: "error", action: "kuorumExceptionHandler", exception: KuorumException)
                "500" (controller: "error", action: "internalError")
            }
            production{
                "500" (controller: "error", action: "kuorumExceptionHandler", exception: KuorumException)
                "500" (controller: "error", action: "internalError")
            }


        }
//        "500"(view:'/error')
	}

    static exceptionMappings = {
        "Access is denied" org.springframework.security.access.AccessDeniedException { ex ->
            controller = "error"
            action = "notAuthorized"
            exception = ex
        }
    }
}
