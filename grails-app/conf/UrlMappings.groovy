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
        name home:              "/$lang" (controller: "dashboard", action:"landingLeaders"){constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
                                "/" { controller="redirect"; action= "redirect301"; newMapping='home'}

        name landingSearch:     "/$lang/discover"   (controller: "search", action:"searchLanding") {constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
                                "/discover"         { controller="redirect"; action= "redirect301"; newMapping='landingSearch'}
        name landingCitizens:   "/$lang/who-should-i-vote-for" (controller: "dashboard", action:"landingLeaders")
                                "/who-should-i-vote-for" { controller="redirect"; action= "redirect301"; newMapping='landingLeaders'}
                                "/citizens" { controller="redirect"; action= "redirect301"; newMapping='landingLeaders'}
                                "/editors" { controller="redirect"; action= "redirect301"; newMapping='landingLeaders'}
        name landingPrices:     "/$lang/prices" (controller: "dashboard", action:"landingPrices")
                                "/prices" { controller="redirect"; action= "redirect301"; newMapping='landingPrices'}
        name landingPoliticians:"/$lang/win-your-election" (controller: "dashboard", action:"landingLeaders")
                                "/win-your-election" { controller="redirect"; action= "redirect301"; newMapping='landingLeaders'}
                                "/politicians"  { controller="redirect"; action= "redirect301"; newMapping='landingLeaders'}
        name landingOrganizations:  "/$lang/fundraising-tools" (controller: "dashboard", action:"landingOrganizations")
                                    "/fundraising-tools" { controller="redirect"; action= "redirect301"; newMapping='landingOrganizations'}
                                    "/$lang/organizations" { controller="redirect"; action= "redirect301"; newMapping='landingOrganizations'}
                                    "/organizations" { controller="redirect"; action= "redirect301"; newMapping='landingOrganizations'}
        name landingLeaders:        "/$lang/best-email-marketing" (controller: "dashboard", action:"landingLeaders")
                                    "/best-email-marketing" { controller="redirect"; action= "redirect301"; newMapping='landingLeaders'}
                                    "/$lang/leaders" { controller="redirect"; action= "redirect301"; newMapping='landingLeaders'}
                                    "/leaders" { controller="redirect"; action= "redirect301"; newMapping='landingLeaders'}
                                    "/kuorum/citizens"        {controller="redirect"; action= "redirect301"; newMapping='landingLeaders'}
        name landingCorporations:   "/$lang/corporate-innovation" (controller: "dashboard", action:"landingCorporations")
                                    "/corporate-innovation" { controller="redirect"; action= "redirect301"; newMapping='landingCorporations'}
                                    "/$lang/corporations" { controller="redirect"; action= "redirect301"; newMapping='landingCorporations'}
                                    "/corporations" { controller="redirect"; action= "redirect301"; newMapping='landingCorporations'}
        name landingCorporationsBrands:   "/$lang/influential-brands" (controller: "dashboard", action:"landingCorporationsBrands")
                                    "/influential-brands" { controller="redirect"; action= "redirect301"; newMapping='landingCorporationsBrands'}
                                    "/$lang/brands" { controller="redirect"; action= "redirect301"; newMapping='landingCorporationsBrands'}
                                    "/brands" { controller="redirect"; action= "redirect301"; newMapping='landingCorporationsBrands'}
        name footerTechnology:      "/$lang/services/what-is-kuorum"    (controller:"footer", action: "tech" )
                                    "/services/what-is-kuorum"          { controller="redirect"; action= "redirect301"; newMapping='footerTechnology'}
                                    "/kuorum/what-is-kuorum"          { controller="redirect"; action= "redirect301"; newMapping='footerTechnology'}
                                    "/services"                         { controller="redirect"; action= "redirect301"; newMapping='footerTechnology'}
        name footerLeaders:         "/$lang/services/leaders" (controller:"footer", action: "leaders" )
                                    "/services/leaders"       {controller="redirect"; action= "redirect301"; newMapping='footerLeaders'}
        name footerGovernment:      "/$lang/services/organizations"        (controller:"footer", action: "government" )
                                    "/services/organizations"              {controller="redirect"; action= "redirect301"; newMapping='footerGovernment'}
        name footerCitizens:        "/$lang/services/corporations"   (controller:"footer", action: "citizens" )
                                    "/services/corporations"   {controller="redirect"; action= "redirect301"; newMapping='footerCitizens'}
        name footerAboutUs:         "/$lang/about/our-story"             (controller:"footer", action: "aboutUs" )
                                    "/about/our-story"                  {controller="redirect"; action= "redirect301"; newMapping='footerAboutUs'}
                                    "/about"                            {controller="redirect"; action= "redirect301"; newMapping='footerAboutUs'}
        name footerVision:          "/$lang/about/mision-and-vision"    (controller:"footer", action: "vision" )
                                    "/about/mision-and-vision"          {controller="redirect"; action= "redirect301"; newMapping='footerVision'}
        name footerTeam:            "/$lang/about/our-team"             (controller:"footer", action: "team" )
                                    "/about/our-team"                   {controller="redirect"; action= "redirect301"; newMapping='footerTeam'}
        name footerInformation:     "/$lang/press"                      (controller:"footer", action: "information" )
                                    "/press"                            {controller="redirect"; action= "redirect301"; newMapping='footerInformation'}
                                    "/press/information-and-resources"  {controller="redirect"; action= "redirect301"; newMapping='footerInformation'}
        name footerWidget:          "/$lang/press/widget"               (controller:"footer", action: "widget" )
                                    "/press/widget"                     {controller="redirect"; action= "redirect301"; newMapping='footerWidget'}
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

        name registerPressKit:      "/$lang/sign-up/pressKit"   (controller: "register",action:"downloadPressKit")
        name registerStep2:         "/$lang/sign-up/step2"      (controller: "customRegister"){action = [GET:"step2", POST:"step2Save"]}
        name registerStep3:         "/$lang/sign-up/step3"      (controller: "customRegister"){action = [GET:"step3", POST:"step3Save"]}


        name registerSuccess:       "/$lang/sign-up/success"(controller: "register",action:"registerSuccess")
                                    "/registro/satisfactorio"{controller="redirect"; action= "redirect301"; newMapping='registerSuccess'}
        name registerPassword:      "/$lang/sign-up/establece-password"(controller: "register", action:"selectMyPassword")
                                    "/registro/establece-password"{controller="redirect"; action= "redirect301"; newMapping='registerPassword'}
        name registerResendMail:    "/$lang/sign-up/no-valid"(controller: "register"){action=[GET:"resendRegisterVerification", POST:"resendVerification"]}
                                    "/registro/no-verificado"{controller="redirect"; action= "redirect301"; newMapping='registerResendMail'}
        name resetPassword:         "/$lang/sign-in/recover-password"(controller: "register"){action=[GET:"forgotPassword", POST:"forgotPasswordPost"]}
                                    "/sign-in/recover-password"{controller="redirect"; action= "redirect301"; newMapping='resetPassword'}
                                    "/registro/password-olvidado"{controller="redirect"; action= "redirect301"; newMapping='resetPassword'}
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

        //BLOG REDIRECT:
        name blog:          "/$lang/blog/"    (controller: "redirect", action:"blogRedirect")
                            "/blog/"    (controller: "redirect", action:"blogRedirect")
                            "/blog/$articlePath**" (controller: "redirect", action:"blogRedirect")

        /**********************/
        /***** LOGGED URLs ****/ //Language no matters
        /**********************/
        name dashboard:                     "/dashboard" (controller: "dashboard", action:"dashboard")
        name dashboardSkipUploadContacts:   "/dashboard/skipContacts" (controller: "dashboard", action:"skipContacts")
        name dashboardCausesSeeMore:        "/ajax/dashboard/causes/see-more" (controller: "dashboard", action:"dashboardCauses")
        name dashboardPoliticiansSeeMore:   "/ajax/dashboard/politicians/see-more" (controller: "dashboard", action:"dashboardPoliticians")

        name projectCreate:             "/project/new"(controller: "project"){action = [GET:"create", POST:"save"]}
        name projectEdit:               "/project/$userAlias/$hashtag/edit"(controller: "project"){action = [GET:"edit", POST:"update"]}
        name projects:                  "/project/$regionName?/$commission?" (controller: "project", action:"index")

        name debateCreate:      "/account/debate/new" (controller: "debate"){action = [GET: "create", POST: "save"]}
        name debateEditSettings:  "/account/debate/$postId/edit-settings" (controller: "debate"){action = [GET: "editSettingsStep", POST: "saveSettings"]}
        name debateEditContent:   "/account/debate/$postId/edit-content" (controller: "debate"){action = [GET: "editContentStep", POST: "saveContent"]}
        name debateEdit:        "/account/debate/$debateId/edit" (controller: "debate"){action = [GET: "edit", POST: "update"]}
        name debateRemove:      "/ajax/account/debate/$debateId/remove" (controller: "debate", action: "remove")
        name debateShow:        "/$userAlias/d/$title-$debateId"(controller: "debate", action: "show"){constraints{userAlias(validator:{!UrlMappings.RESERVED_PATHS.contains(it) && !UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
                                "/$userAlias/d/-$debateId"      (controller: "debate", action: "show"){constraints{userAlias(validator:{!UrlMappings.RESERVED_PATHS.contains(it) && !UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name debateProposalNew: "/ajax/addProposal"(controller: "debateProposal", action: "addProposal")
        name debateProposalDelete:"/ajax/deleteProposal"(controller: "debateProposal", action: "deleteProposal")
        name debateProposalPin: "/ajax/pinProposal"(controller: "debateProposal", action: "pinProposal")
        name debateProposalLike:"/ajax/likeProposal"(controller: "debateProposal", action: "likeProposal")
        name debateProposalComment: "/ajax/proposalComment/add"(controller: "debateProposal", action: "addComment")
        name debateProposalDeleteComment: "/ajax/proposalComment/delete"(controller: "debateProposal", action: "deleteComment")
        name debateProposalVoteComment: "/ajax/proposalComment/vote"(controller: "debateProposal", action: "voteComment")

        name postCreate:            "/account/post/new" (controller: "post"){action = [GET: "create", POST: "saveSettings"]}
        name postEditSettings:      "/account/post/$postId/edit-settings" (controller: "post"){action = [GET: "editSettingsStep", POST: "saveSettings"]}
        name postEditContent:       "/account/post/$postId/edit-content" (controller: "post"){action = [GET: "editContentStep", POST: "saveContent"]}
        name postShow:              "/$userAlias/p/$title-$postId"  (controller: "post", action: "show"){constraints{userAlias(validator:{!UrlMappings.RESERVED_PATHS.contains(it) && !UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
                                    "/$userAlias/p/-$postId"        (controller: "post", action: "show"){constraints{userAlias(validator:{!UrlMappings.RESERVED_PATHS.contains(it) && !UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name postEdit:              "/account/post/$postId/edit" (controller: "post"){action = [GET: "edit", POST: "update"]}
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

        name projectListOfUsers:        "/ajax/herramientas/project"(controller: "tools", action: "ajaxShowProjectListOfUsers")
        name publishProject:            "/herramientas/project/$hashtag/publicar" (controller:"tools", action: "publishProject")

        name projectUpdate:             "/project/$userAlias/$hashtag/actualizar"(controller: "project"){action = [GET:"createProjectUpdate", POST:"addProjectUpdate"]}

        name langPostShow:  "/$lang/$userAlias/$hashtag/$postBrief-$postId"(controller: "redirect", action: "redirect301Project") {constraints{userAlias(validator:{!UrlMappings.RESERVED_PATHS.contains(it) && !UrlMappings.VALID_LANGUAGE_PATHS.contains(it)}); lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name OldPostShow:      "/$userAlias/$hashtag/$postBrief-$postId"{controller="redirect"; action= "redirect301Project"; constraints{userAlias(validator:{!UrlMappings.RESERVED_PATHS.contains(it) && !UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
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
        name userClucks:        "/ajax/$userAlias/clucks"  (controller: "kuorumUser", action: "userClucks")
        name userPost:          "/ajax/$userAlias/posts"  (controller: "kuorumUser", action: "userPosts")
        name userVictories:     "/ajax/$userAlias/victories"  (controller: "kuorumUser", action: "userVictories")
        name ajaxPoliticianProjects:         "/ajax/$userAlias/politicianProjects"   (controller: "kuorumUser", action: "politicianProjects")
        name ajaxPoliticianDefendedPosts:    "/ajax/$userAlias/defendedPosts"        (controller: "kuorumUser", action: "politicianDefendedPosts")
        name ajaxPoliticianVictoryPosts:     "/ajax/$userAlias/defendedVictoryPosts" (controller: "kuorumUser", action: "politicianDefendedVictories")
        name ajaxRegisterContact:            "/ajax/contact"(controller: "register", action: "contactRegister");

        name userRate:                  "/ajax/$userAlias/rate"(controller: "rating", action:"ratePolitician")
        name userHistoricRate:          "/ajax/$userAlias/historicRate"(controller: "rating", action:"historicPoliticianRate")
        name comparingPoliticianRate:   "/ajax/user/compareRate"(controller: "rating", action:"comparingPoliticianRateData")

        name searcherSearchSeeMore: "/ajax/search/seeMore"(controller: "search", action:"searchSeeMore")
        name searcherSearchFilters: "/ajax/search/new-filters"(controller: "search", action:"modifyFilters")
        name searcherSuggests:      "/ajax/search/suggestions"(controller: "search", action:"suggest")
        name suggestRegions:        "/ajax/search/suggestions/regions"(controller: "search", action:"suggestRegions")
        name suggestTags:           "/ajax/search/suggestions/tags"(controller: "search", action:"suggestTags")

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

        name adminPrincipal:        "/admin"                          (controller:"adminProject", action: "index")
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
        name editorRequestRights:                           "/editor/request"                                           (controller:"editorRecruitment", action: "requestEditor")
        name editorDiscardWarns:                            "/editor/discard"                                           (controller:"editorRecruitment", action: "discardEditor")

        name ajaxDeleteRecommendedUser: "/ajax/kuorumUser/deleteRecommendedUser"(controller: 'recommendedUserInfo', action: 'deleteRecommendedUser')

        name politicianRequestBetaTester:               "/account/request-beta-tester-account" (controller:"politician", action: "requestAPoliticianBetaTester")
        name politicianAnalytics:                       "/account/data-analytics" (controller:"politician", action: "betaTesterPage")
        name politicianContactProfiling:                "/account/contact-profiling" (controller:"politician", action: "betaTesterPage")
        name politicianContacts:                        "/account/contacts" (controller:"contacts", action: "index")
        name politicianContactsSearch:                  "/ajax/account/contacts" (controller:"contacts", action: "searchContacts")
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
        name politicianCampaigns:                       "/account/campaigns" (controller:"massMailing", action: "index")
        name politicianCampaignsNew:                    "/account/campaigns/new" (controller:"massMailing", action: "newCampaign")
        name politicianCampaignsLists:                  "/ajax/account/campaigns/lists" (controller:"campaign", action: "findLiUserCampaigns")
        name politicianCampaignsUploadImages:           "/ajax/account/campaign/$campaignId/uploadImages" (controller:"file", action: "uploadCampaignImages");
        name politicianCampaignsListImages:             "/ajax/account/campaign/$campaignId/listImages" (controller:"file", action: "getCampaignImages");
        name politicianMassMailingNew:                  "/account/mass-mailing/new" (controller:"massMailing"){ action=[GET:"createMassMailing", POST:'saveMassMailingStep1']}
        name politicianMassMailingEdit1:                "/account/mass-mailing/$campaignId/edit-first" (controller: "massMailing"){ action=[GET:"editFirstStep", POST: 'saveMassMailingStep1']}
        name politicianMassMailingEdit2:                "/account/mass-mailing/$campaignId/edit-second" (controller: "massMailing"){ action=[GET:"editSecondStep", POST: 'saveMassMailingStep2']}
        name politicianMassMailingEdit3:                "/account/mass-mailing/$campaignId/edit-third" (controller: "massMailing", action:"editThirdStep")
        name politicianMassMailingEdit3Text:            "/account/mass-mailing/$campaignId/edit-third-text" (controller: "massMailing"){ action=[GET:"editThirdStepText", POST: 'saveMassMailingStep3Text']}
        name politicianMassMailingEdit3Template:        "/account/mass-mailing/$campaignId/edit-third-template" (controller: "massMailing"){ action=[GET:"editThirdStepTemplate", POST: 'saveMassMailingStep3Template']}
        name politicianMassMailingNew:                  "/account/mass-mailing/new" (controller:"massMailing"){ action=[GET:"createMassMailing", POST:'saveMassMailingSettings']}
        name politicianMassMailingSettings:             "/account/mass-mailing/$campaignId/edit-settings" (controller: "massMailing"){ action=[GET:"editSettingsStep", POST: 'saveMassMailingSettings']}
        name politicianMassMailingTemplate:             "/account/mass-mailing/$campaignId/edit-template" (controller: "massMailing"){ action=[GET:"editTemplateStep", POST: 'saveMassMailingTemplate']}
        name politicianMassMailingContent:              "/account/mass-mailing/$campaignId/edit-content" (controller: "massMailing", action:"editContentStep")
        name politicianMassMailingEdit3Text:            "/account/mass-mailing/$campaignId/edit-content-text" (controller: "massMailing"){ action=[GET:"editContentStepText", POST: 'saveMassMailingContentText']}
        name politicianMassMailingEdit3Template:        "/account/mass-mailing/$campaignId/edit-content-template" (controller: "massMailing"){ action=[GET:"editContentStepTemplate", POST: 'saveMassMailingContentTemplate']}
        name politicianMassMailingShow:                 "/account/mass-mailing/$campaignId" (controller:"massMailing"){ action=[GET:"showCampaign",POST:'updateCampaign']}
        name politicianMassMailingSendTest:             "/account/mass-mailing/test" (controller:"massMailing", action: "sendMassMailingTest")
        name politicianMassMailingRemove:               "/ajax/account/mass-mailing/$campaignId/remove" (controller:"massMailing", action:"removeCampaign")
        name politicianMassMailingTrackEvents:          "/ajax/account/mass-mailing/$campaignId/trackEvents" (controller:"massMailing", action: "showTrackingMails")
        name politicianMassMailingHtml:                 "/account/mass-mailing/$campaignId/html" (controller:"massMailing", action: "showMailCampaign")
        name politicianMassMailingSaveTimeZone:         "/account/mass-mailing/saveTimeZone" (controller: "massMailing"){action = [POST:"saveTimeZone"]}

        name politicianTeamManagement:                  "/account/team-management" (controller:"politician", action: "betaTesterPage")

        "/account/contacts/oauth/$provider/success" (controller: "contactsOAuth", action: "onSuccess")
        "/account/contacts/oauth/$provider/failure" (controller: "contactsOAuth", action: "onFailure")

        "/googleContacts/loadContactsFromGoogle" (controller: "googleContacts", action: "loadContactsFromGoogle")

        "/admin/updateMailChimp" (controller: "admin", action: "updateMailChimp")

        /**********************/
        /***** DEPRECATED *****/
        /**********************/
        "/losdatoscuentan" (controller: "dashboard", action:"customPostMapping")
        "/losDatosCuentan" (controller: "dashboard", action:"customPostMapping")
        "/empleoJuvenil" (controller: "dashboard", action:"customPostMappingEmpleoJuvenil")
        "/empleojuvenil" (controller: "dashboard", action:"customPostMappingEmpleoJuvenil")
        "/sayNoToFracking" (controller: "dashboard", action:"customPostMappingSayNoToFracking")
        "/SayNoToFracking" (controller: "dashboard", action:"customPostMappingSayNoToFracking")
        "/saynotofracking" (controller: "dashboard", action:"customPostMappingSayNoToFracking")
        "/immigrationrc" (controller: "dashboard", action:"customPostMappingImmigrationrc")
        "/immigrationRC" (controller: "dashboard", action:"customPostMappingImmigrationrc")

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

        // HERRAMIENTAS (TOOLS)
        name toolsFavorites:        "/herramientas/pendientes-de-leer"(controller: "tools", action: "showFavoritesPosts")
        name toolsMyPosts:          "/herramientas/mis-posts"        (controller: "tools", action: "showUserPosts")
        name toolsKuorumStore:      "/herramientas/el-gallinero"     (controller: "tools", action: "kuorumStore")
        name toolsBuyAward:         "/ajax/herramientas/el-gallinero/comprar"     (controller: "tools", action: "kuorumStoreBuyAward")
        name toolsActivateAward:    "/ajax/herramientas/el-gallinero/activar"     (controller: "tools", action: "kuorumStoreActivateAward")

        name tourStart:           "/tour" (controller:"tour", action: "index")
        name tour_dashboard:      "/tour/dashboard" (controller:"tour", action: "tour_dashboard")

        // PROYECTOS (ADMIN)
        name adminEditProject:      "/admin/project/editar-proyecto/$hashtag" (controller:"adminProject"){action =[GET:"editProject", POST:"updateProject"]}
        name adminPublishProject:   "/admin/project/editar-proyecto/$hashtag/publicar"     (controller:"adminProject", action: "publishProject")
        name adminUnpublishProject: "/admin/project/editar-proyecto/$hashtag/despublicar"  (controller:"adminProject", action: "unPublishProject")
        name adminUnpublishedProjects:"/admin/project/no-publicados"    (controller:"adminProject", action: "unpublishedProjects")

        // ADMIN STATS
        name adminStats:            "/admin/estadisticas"           (controller:"adminStats", action: "stats")
        name adminStatsPieChart:    "/admin/estadisticas/pie-chart" (controller:"adminStats", action: "statsDataPieChart")

        // DEPRECATED SEO - THINK AGAIN
        name discover:                      "/descubre"                                 {controller="redirect"; action= "redirect301"; newMapping='searcherSearch'}
        name discoverProjects:              "/descubre/project"                         {controller="redirect"; action= "redirect301"; newMapping='searcherSearch'}
        name discoverProjectsByRegion:      "/descubre/project/$regionName"             {controller="redirect"; action= "redirect301"; newMapping='searcherSearch'}
        name discoverPoliticians:           "/descubre/politicos"                       {controller="redirect"; action= "redirect301"; newMapping='searcherSearch'}
        name discoverRecentPosts:           "/descubre/publicaciones-recientes"         {controller="redirect"; action= "redirect301"; newMapping='searcherSearch'}
        name discoverRecommendedPosts:      "/descubre/publicaciones-mas-impulsadas"    {controller="redirect"; action= "redirect301"; newMapping='searcherSearch'}

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
