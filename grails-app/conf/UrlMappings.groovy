import grails.util.Environment
import kuorum.core.exception.KuorumException
import kuorum.core.model.AvailableLanguage
import kuorum.core.model.UserType
import org.springframework.security.access.AccessDeniedException

class UrlMappings {

    static excludes = ['/robots.txt']

    static List<String> RESERVED_PATHS = ['j_spring_security_facebook_redirect', 'proyectos', 'ciudadanos', 'organizaciones', 'politicos']
    static List<String> VALID_LANGUAGE_PATHS = ["es","en","lt","de","it"]
	static mappings = {

        /**********************/
        /***** I18N URLs ******/
        /**********************/
        name home:              "/$lang" (controller: "search", action:"searchLanding"){constraints{lang (validator:{UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
                                "/" { controller="redirect"; action= "redirect301"; newMapping='home'}


        name landingSearch:     "/$lang"(controller: "search", action:"searchLanding")
                                "/$lang/discover"   (controller: "search", action:"searchLanding")
                                "/discover"         { controller="redirect"; action= "redirect301"; newMapping='landingSearch'}
        name landingCitizens:   "/$lang/who-should-i-vote-for" (controller: "dashboard", action:"landingCitizens")
                                "/who-should-i-vote-for" { controller="redirect"; action= "redirect301"; newMapping='landingCitizens'}
                                "/citizens" { controller="redirect"; action= "redirect301"; newMapping='landingCitizens'}
                                "/editors" { controller="redirect"; action= "redirect301"; newMapping='landingCitizens'}
        name landingPrices:     "/$lang/prices" (controller: "dashboard", action:"landingPrices")
                                "/prices" { controller="redirect"; action= "redirect301"; newMapping='landingPrices'}
        name landingPoliticians:"/$lang/win-your-election" (controller: "dashboard", action:"landingPoliticians")
                                "/win-your-election" { controller="redirect"; action= "redirect301"; newMapping='landingPoliticians'}
                                "/politicians"  { controller="redirect"; action= "redirect301"; newMapping='landingPoliticians'}
        name landingOrganizations:  "/$lang/advocate-better" (controller: "dashboard", action:"landingOrganizations")
                                    "/advocate-better" { controller="redirect"; action= "redirect301"; newMapping='landingOrganizations'}
                                    "/organizations" { controller="redirect"; action= "redirect301"; newMapping='landingOrganizations'}

        name footerTechnology:      "/$lang/services/what-is-kuorum"    (controller:"footer", action: "tech" )
                                    "/services/what-is-kuorum"          { controller="redirect"; action= "redirect301"; newMapping='footerTechnology'}
                                    "/services"                         { controller="redirect"; action= "redirect301"; newMapping='footerTechnology'}
        name footerPoliticians:     "/$lang/services/win-your-election" (controller:"footer", action: "politicians" )
                                    "/services/win-your-election"       {controller="redirect"; action= "redirect301"; newMapping='footerPoliticians'}
                                    "/services/politicians"             {controller="redirect"; action= "redirect301"; newMapping='footerPoliticians'}
        name footerGovernment:      "/$lang/services/government"        (controller:"footer", action: "government" )
                                    "/services/government"              {controller="redirect"; action= "redirect301"; newMapping='footerGovernment'}
        name footerCitizens:        "/$lang/services/who-should-i-vote-for"   (controller:"footer", action: "citizens" )
                                    "/services/who-should-i-vote-for"   {controller="redirect"; action= "redirect301"; newMapping='footerCitizens'}
                                    "/services/citizens"                {controller="redirect"; action= "redirect301"; newMapping='footerCitizens'}
        name footerDevelopers:      "/$lang/services/editors"           (controller:"footer", action: "developers" )
                                    "/services/editors"                 {controller="redirect"; action= "redirect301"; newMapping='footerDevelopers'}
        name footerAboutUs:         "/$lang/about/our-story"             (controller:"footer", action: "aboutUs" )
                                    "/about/our-story"                  {controller="redirect"; action= "redirect301"; newMapping='footerAboutUs'}
                                    "/about"                            {controller="redirect"; action= "redirect301"; newMapping='footerAboutUs'}
        name footerVision:          "/$lang/about/mision-and-vision"    (controller:"footer", action: "vision" )
                                    "/about/mision-and-vision"          {controller="redirect"; action= "redirect301"; newMapping='footerVision'}
        name footerImpact:          "/$lang/about/impact"               (controller:"footer", action: "impact" )
                                    "/about/impact"                     {controller="redirect"; action= "redirect301"; newMapping='footerImpact'}
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

        name registerPressKit:          "/$lang/sign-up/pressKit"(controller: "register",action:"downloadPressKit")
        name registerStep2:             "/$lang/sign-up/step2"(controller: "customRegister"){action = [GET:"step2", POST:"step2Save"]}
        name registerStep3:             "/$lang/sign-up/step3"(controller: "customRegister"){action = [GET:"step3", POST:"step3Save"]}


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


        name login:     "/$lang/log-in" (controller:"login", action:"index")
                        "/log-in"       {controller="redirect"; action= "redirect301"; newMapping='login'}
                        "/entrar"       {controller="redirect"; action= "redirect301"; newMapping='login'}
        name loginAuth: "/$lang/sign-in"(controller:"login", action:"auth")
                        "/sign-in"      {controller="redirect"; action= "redirect301"; newMapping='loginAuth'}
                        "/autenticarse" {controller="redirect"; action= "redirect301"; newMapping='loginAuth'}
        name loginFull: "/$lang/confirmar-usuario"  (controller:"login", action:"full")
                        "/confirmar-usuario"        {controller="redirect"; action= "redirect301"; newMapping='loginFull'}
        name logout:    "/logout"       (controller:"logout", action:"index")
                        "/salir"        {controller="redirect"; action= "redirect301"; newMapping='logout'}

        name userShow:              "/$lang/$userAlias"     (controller: "kuorumUser", action: "show") {constraints{lang (inList:UrlMappings.VALID_LANGUAGE_PATHS)}}
        name userShowPlain:         "/$userAlias"           (controller: "kuorumUser", action: "show") {constraints{userAlias(validator:{!UrlMappings.RESERVED_PATHS.contains(it) && !UrlMappings.VALID_LANGUAGE_PATHS.contains(it)})}}
        name secUserShow:           "/sec/$userAlias"       (controller: "kuorumUser", action: "secShow")

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
        name dashboardCausesSeeMore:        "/ajax/dashboard/causes/see-more" (controller: "dashboard", action:"dashboardCauses")
        name dashboardPoliticiansSeeMore:   "/ajax/dashboard/politicians/see-more" (controller: "dashboard", action:"dashboardPoliticians")

        name projectCreate:             "/proyectos/nuevo"(controller: "project"){action = [GET:"create", POST:"save"]}
        name projectEdit:               "/proyectos/$regionName/$commission/$hashtag/edit"(controller: "project"){action = [GET:"edit", POST:"update"]}
        name projects:                  "/proyectos/$regionName?/$commission?" (controller: "project", action:"index")
        name projectShow:               "/hashtag/$hashtag" (controller: "project", action:"show")
                                        "/proyectos/$regionName/$commission/$hashtag" (controller: "project", action:"show")
                                        "/leyes/$regionName/$commission/$hashtag" (controller: "project", action:"show")
        name projectShowSec:            "/sec/proyectos/$regionName/$commission/$hashtag" (controller: "project", action:"showSecured")
        name projectVote:               "/ajax/proyectos/$regionName/$commission/$hashtag/votar"(controller: "project", action:"voteProject")
        name projectVoteNoTotalUser:    "/sec/proyectos/$regionName/$commission/$hashtag/salvarDatosUsuarioYvotar"(controller: "project", action:"voteProjectAsNonCompleteUser")
        name projectListClucks:         "/ajax/proyectos/$regionName/$commission/$hashtag/listado-kakareos" (controller: "project", action:"listClucksProject")
        name projectListPostDefends:    "/ajax/proyectos/$regionName/$commission/$hashtag/listado-post-defendidos" (controller: "project", action:"listClucksProjectDefends")
        name projectListPostVictories:  "/ajax/proyectos/$regionName/$commission/$hashtag/listado-victorias" (controller: "project", action:"listClucksProjectVictories")

        name projectList:               "/herramientas/proyectos"(controller: "tools", action:"listProjects")
        name projectListOfUsers:        "/ajax/herramientas/proyectos"(controller: "tools", action: "ajaxShowProjectListOfUsers")
        name publishProject:            "/herramientas/proyectos/$hashtag/publicar" (controller:"tools", action: "publishProject")

        name projectUpdate:             "/proyectos/$regionName/$commission/$hashtag/actualizar"(controller: "project"){action = [GET:"createProjectUpdate", POST:"addProjectUpdate"]}

        name postCreate:    "/proyectos/$regionName/$commission/$hashtag/nuevo-post"(controller: "post"){action = [GET:"create", POST:"save"]}
                            "/leyes/$regionName/$commission/$hashtag/nuevo-post"(controller: "post"){action = [GET:"create", POST:"save"]}
//        name postSave:      "/leyes/$regionName/$commission/$hashtag/guardar-nuevo-post"(controller: "post"){action = [GET:"create", POST:"save"]}
        name postShow:      "/proyectos/$regionName/$commission/$hashtag/propuesta/$postBrief-$postId"(controller: "post", action: "show")
                            "/proyectos/$regionName/$commission/$hashtag/$urlPostTypeVieja/$postBrief-$postId"(controller: "post", action: "show")
                            "/leyes/$regionName/$commission/$hashtag/$urlPostTypeVieja/$postBrief-$postId"(controller: "post", action: "show")
        name postReview:    "/proyectos/$regionName/$commission/$hashtag/propuesta/$postBrief-$postId/revisar"(controller: "post", action: "review")
        name postPublish:   "/proyectos/$regionName/$commission/$hashtag/propuesta/$postBrief-$postId/publicar"(controller: "post", action:"publish")
        name postPublished: "/proyectos/$regionName/$commission/$hashtag/propuesta/$postBrief-$postId/publicado"(controller: "post", action:"postPublished")
        name postEdit:      "/proyectos/$regionName/$commission/$hashtag/propuesta/$postBrief-$postId/editar"(controller: "post"){action = [GET:"edit", POST:"update"]}
        name postDelete:    "/proyectos/$regionName/$commission/$hashtag/propuesta/$postBrief-$postId/eliminar-post"(controller: "post", action: "deletePost")
        name postToggleFavorite:"/ajax/proyectos/$regionName/$commission/$hashtag/propuesta/$postBrief-$postId/favorito"(controller: "post",action: "favorite")
        name postDelComment:"/proyectos/$regionName/$commission/$hashtag/propuesta/$postBrief-$postId/borrarCommentario"(controller: "post",action: "deleteComment")
        name postAddComment:"/proyectos/$regionName/$commission/$hashtag/propuesta/$postBrief-$postId/nuevoComentario"(controller: "post",action: "addComment")
        name postVoteComment:"/ajax/proyectos/$regionName/$commission/$hashtag/propuesta/$postBrief-$postId/votar-comentario"(controller: "post",action: "voteComment")
        name postCluckIt:   "/proyectos/$regionName/$commission/$hashtag/propuesta/$postBrief-$postId/kakarear"(controller: "post",action: "cluckPost")
        name postVoteIt:    "/proyectos/$regionName/$commission/$hashtag/propuesta/$postBrief-$postId/impulsar"(controller: "post",action: "votePost")
        name postVoteAndRegister:    "/proyectos/$regionName/$commission/$hashtag/propuesta/$postBrief-$postId/impulsar-registro"(controller: "post",action: "votePostWithRegister")
        name postVotesList: "/ajax/proyectos/$regionName/$commission/$hashtag/propuesta/$postBrief-$postId/lista-impulsos"(controller: "post",action: "listVotes")
        name postClucksList:"/ajax/proyectos/$regionName/$commission/$hashtag/propuesta/$postBrief-$postId/lista-kakareos"(controller: "post",action: "listClucks")
        name postPayPost:   "/proyectos/$regionName/$commission/$hashtag/propuesta/$postBrief-$postId/promocionar"(controller: "post", action:"promotePost")
                            "/leyes/$regionName/$commission/$hashtag/propuesta/$postBrief-$postId/promocionar"(controller: "post", action:"promotePost")
        name postPaiment:   "/proyectos/$regionName/$commission/$hashtag/propuesta/$postBrief-$postId/resumen-promocion"(controller: "post", action:"paimentPost")
                            "/leyes/$regionName/$commission/$hashtag/propuesta/$postBrief-$postId/resumen-promocion"(controller: "post", action:"paimentPost")
        name postSuccessPay:"/proyectos/$regionName/$commission/$hashtag/propuesta/$postBrief-$postId/gracias"(controller: "post", action:"successPromotePost")
        name postAddDebate: "/ajax/proyectos/$regionName/$commission/$hashtag/propuesta/$postBrief-$postId/addDebate"(controller: "post", action:"addDebate")
        name postAddVictory:"/ajax/proyectos/$regionName/$commission/$hashtag/propuesta/$postBrief-$postId/victoria"(controller: "post", action:"addVictory")
        name postAddDefender:"/ajax/proyectos/propuesta/apadrinar"(controller: "post", action:"addDefender")


        name widgetJs:      "/widget.js"(controller: "widget", action:"kuorumWidgetjs")
        name widgetRatePolitician:     "/widget/ratePolitician" (controller: "rating", action:"widgetRatePolitician")
        name widgetComparative:        "/widget/comparation"    (controller: "rating", action:"widgetComparativePoliticianInfo")

        name userFollowers:     "/ajax/$userAlias/seguidores" (controller: "kuorumUser", action: "userFollowers")
        name userFollowing:     "/ajax/$userAlias/siguiendo"  (controller: "kuorumUser", action: "userFollowing")

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

        name searcherSearchSeeMore: "/ajax/buscar/seeMore"(controller: "search", action:"searchSeeMore")
        name searcherSearchFilters: "/ajax/buscar/nuevos-filtros"(controller: "search", action:"modifyFilters")
        name searcherSuggests:      "/ajax/buscar/sugerencias"(controller: "search", action:"suggest")
        name suggestRegions:        "/ajax/buscar/regiones/sugerencias"(controller: "search", action:"suggestRegions")
        name suggestTags:           "/ajax/buscar/tags/sugerencias"(controller: "search", action:"suggestTags")

        name profileEditAccountDetails:     "/config/account-details"                                   (controller: "profile"){action =[GET:"editAccountDetails", POST:"updateAccountDetails"]}
        name profileEditUser:               "/edit-profile"                                    (controller: "profile"){action =[GET:"editUser", POST:"editUserSave"]}
        name profileEditCommissions:        "/edit-profile/cambiar-areas-interes"              (controller: "profile"){action =[GET:"editCommissions", POST:"editCommissionsSave"]}
        name profileChangePass:             "/edit-profile/cambiar-password"                   (controller: "profile"){action =[GET:"changePassword", POST:"changePasswordSave"]}
        name profileSetPass:                "/edit-profile/crear-password"                     (controller: "profile"){action =[GET:"setPassword", POST:"setPasswordSave"]}
        name profileChangeEmail:            "/edit-profile/cambiar-email"                      (controller: "profile"){action =[GET:"changeEmail", POST:"changeEmailSave"]}
        name profileChangeEmailSent:        "/edit-profile/cambiar-email/solicitud-recivida"   (controller: "profile", action :"changeEmailConfirmSent")
        name profileChangeEmailResend:      "/edit-profile/cambiar-email/resend-email"         (controller: "profile", action :"updateUserEmail")
        name profileChangeEmailConfirm:     "/edit-profile/cambiar-email/confirmar"            (controller: "profile", action: "changeEmailConfirm")
        name profileSocialNetworks:         "/edit-profile/redes-sociales"                     (controller: "profile"){action=[GET:"socialNetworks",POST:"socialNetworksSave"]}
        name profileEmailNotifications:     "/edit-profile/notificaciones-por-email"           (controller: "profile"){action=[GET:"configurationEmails",POST:"configurationEmailsSave"]}
        name profileMessages:               "/edit-profile/mensajes"                           (controller: "profile", action: "userMessages")
        name profileDeleteAccount:          "/edit-profile/eliminar-cuenta"                    (controller: "profile"){action=[GET:"deleteAccount", POST:"deleteAccountPost"]}
        name profileCauses:                 "/edit-profile/causas"                             (controller: "profile"){action=[GET:"editCauses", POST:"updateCauses"]}
        name profileNews:                   "/edit-profile/news"                               (controller: "profile"){action=[GET:"editNews", POST:"updateNews"]}
        name profileQuickNotes:             "/edit-profile/quick-notes"                        (controller: "profile"){action=[GET:"editQuickNotes", POST:"updateQuickNotes"]}
        name profileProfessionalDetails:    "/edit-profile/professional-details"               (controller: "profile"){action=[GET:"editProfessionalDetails", POST:"updateProfessionalDetails"]}


        name profileMailing : "/notifications/mailing" (controller: "profile", action:"showUserEmails")

        name causeSupport:         "/ajax/cause/$causeName/support" (controller:"causes", action: "supportCause")
        name causeDiscard:         "/ajax/cause/$causeName/discard" (controller:"causes", action: "discardCause")

        name campaignPoll:        "/campaign/poll" (controller: "massMailing", action: "saveCitizenPriorities")

        name ajaxHeadNotificationsChecked:  "/ajax/notificaiones/check"(controller:"notification", action:"notificationChecked")
        name ajaxPostponeAlert:             "/ajax/notificaiones/posponer/$id"(controller:"notification", action:"postponeAlert")
        name ajaxHeadMessagesChecked:       "/ajax/mensajes/check"(controller:"layouts", action:"headNotificationsChecked")
        name ajaxFollow:                    "/ajax/kuorumUser/follow"(controller:"kuorumUser", action:"follow")
        name ajaxUnFollow:                  "/ajax/kuorumUser/unFollow"(controller:"kuorumUser", action:"unFollow")
        name ajaxRequestPolitician:         "/ajax/politico/solicitud-kuorum"(controller:"kuorumUser", action:"follow")
        name ajaxCropImage:                 "/ajax/file/crop"(controller:"file", action:"cropImage")
        name ajaxUploadFile:                "/ajax/file/upload" (controller:'file', action:"uploadImage")
        name ajaxUploadFilePDF:             "/ajax/file/uploadPDF" (controller:'file', action:"uploadPDF")

        name ajaxModuleUserCauses:          "/ajax/module/user/causes" (controller:"modules", action: "userCauses")

        name adminPrincipal:        "/admin"                          (controller:"adminProject", action: "index")
        name adminEditProject:      "/admin/proyectos/editar-proyecto/$hashtag" (controller:"adminProject"){action =[GET:"editProject", POST:"updateProject"]}
        name adminPublishProject:   "/admin/proyectos/editar-proyecto/$hashtag/publicar"     (controller:"adminProject", action: "publishProject")
        name adminUnpublishProject: "/admin/proyectos/editar-proyecto/$hashtag/despublicar"  (controller:"adminProject", action: "unPublishProject")
        name adminUnpublishedProjects:"/admin/proyectos/no-publicados"    (controller:"adminProject", action: "unpublishedProjects")
        name adminTestMail:         "/admin/mailing/test"           (controller:"mailTesting", action: "index")
        name adminSearcherIndex:    "/admin/searcher/indexar"       (controller:"admin", action: "solrIndex")
        name adminSearcherFullIndex:"/admin/searcher/full-index"    (controller:"admin", action:"fullIndex")
        name adminEditorsMonitoring:"/admin/editors/monitoring"    (controller:"admin", action:"editorsMonitoring")

        name editorCreatePolitician:                        "/editor/usuarios/politician/create-politician" (controller:"editorUser"){action =[GET:"createPolitician", POST:"saveCreatePolitician"]}
        name editorEditUserProfile:                         "/editor/usuarios/$userAlias/editar/profile" (controller:"editorUser"){action =[GET:"editUser", POST:"updateUser"]}
        name editorEditSocialNetwork:                       "/editor/usuarios/$userAlias/editar/social-network" (controller:"editorUser"){action =[GET:"editUserSocialNetwork", POST:"updateUserSocialNetwork"]}
        name editorEditNews:                                "/editor/usuarios/$userAlias/editar/news" (controller:"editorPolitician"){action =[GET:"editNews", POST:"updateNews"]}
        name editorEditPoliticianProfessionalDetails:       "/editor/usuarios/$userAlias/editar/professionalDetails" (controller:"editorPolitician"){action =[GET:"editProfessionalDetails", POST:"updateProfessionalDetails"]}
        name editorEditPoliticianQuickNotes:                "/editor/usuarios/$userAlias/editar/quick-notes" (controller:"editorPolitician"){action =[GET:"editQuickNotes", POST:"updateQuickNotes"]}
        name editorEditPoliticianCauses:                    "/editor/usuarios/$userAlias/editar/causes" (controller:"editorPolitician"){action =[GET:"editCauses", POST:"updateCauses"]}
        name editorKuorumAccountEdit:                       "/editor/usuarios/$userAlias/editar/account-details" (controller:"editorUser"){action =[GET:"editAdminAccountDetails", POST:"updateAdminAccountDetails"]}
        name editorAdminUserRights:                         "/editor/usuarios/$userAlias/editar/rights" (controller:"admin"){action =[GET:"editUserRights", POST:"updateUserRights"]}
        name editorRequestRights:                           "/editor/request" (controller:"editorRecruitment", action: "requestEditor")
        name editorDiscardWarns:                            "/editor/discard" (controller:"editorRecruitment", action: "discardEditor")
        name adminStats:            "/admin/estadisticas"           (controller:"adminStats", action: "stats")
        name adminStatsMap:         "/admin/estadisticas/mapa"      (controller:"adminStats", action: "statsDataMap")
        name adminStatsPieChart:    "/admin/estadisticas/pie-chart" (controller:"adminStats", action: "statsDataPieChart")
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
        name politicianMassMailing:                     "/account/mass-mailing" (controller:"massMailing", action: "index")
        name politicianMassMailingNew:                  "/account/mass-mailing/new" (controller:"massMailing"){ action=[GET:"createMassMailing",POST:'saveMassMailing']}
        name politicianMassMailingShow:                 "/account/mass-mailing/$campaignId" (controller:"massMailing"){ action=[GET:"showCampaign",POST:'updateCampaign']}
        name politicianMassMailingSendTest:             "/account/mass-mailing/test" (controller:"massMailing", action: "sendMassMailingTest")
        name politicianMassMailingRemove:               "/ajax/account/mass-mailing/$campaignId/remove" (controller:"massMailing", action:"removeCampaign")
        name politicianMassMailingTrackEvents:          "/ajax/account/mass-mailing/$campaignId/trackEvents" (controller:"massMailing", action: "showTrackingMails")
        name politicianMassMailingHtml:                 "/account/mass-mailing/$campaignId/html" (controller:"massMailing", action: "showMailCampaign")
        name politicianTeamManagement:                  "/account/team-management" (controller:"politician", action: "betaTesterPage")

        "/account/contacts/oauth/$provider/success" (controller: "contactsOAuth", action: "onSuccess")
        "/account/contacts/oauth/$provider/failure" (controller: "contactsOAuth", action: "onFailure")

        /**********************/
        /***** PENSAR SEO *****/
        /**********************/
        name discover:                      "/descubre" (controller: "discover", action:"discoverProjects")
        name discoverProjects:              "/descubre/proyectos"         (controller: "discover", action:"discoverProjects")
        name discoverProjectsByRegion:      "/descubre/proyectos/$regionName" (controller: "discover", action:"discoverProjects")
        name discoverPoliticians:           "/descubre/politicos"     (controller: "discover", action:"discoverPoliticians")
        name discoverRecentPosts:           "/descubre/publicaciones-recientes" (controller: "discover", action:"discoverRecentPosts")
        name discoverRecommendedPosts:      "/descubre/publicaciones-mas-impulsadas" (controller: "discover", action:"discoverRecommendedPosts")
        /**********************/
        /*** END PENSAR SEO ***/
        /**********************/


        /**********************/
        /***** DEPRECATED *****/
        /**********************/
        //TODO Hacer esta chapu en BBDD
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
        name toolsNotifications:    "/herramientas/notificaciones"   (controller: "tools", action: "userNotifications")
        name toolsFavorites:        "/herramientas/pendientes-de-leer"(controller: "tools", action: "showFavoritesPosts")
        name toolsMyPosts:          "/herramientas/mis-posts"        (controller: "tools", action: "showUserPosts")
        name toolsKuorumStore:      "/herramientas/el-gallinero"     (controller: "tools", action: "kuorumStore")
        name toolsBuyAward:         "/ajax/herramientas/el-gallinero/comprar"     (controller: "tools", action: "kuorumStoreBuyAward")
        name toolsActivateAward:    "/ajax/herramientas/el-gallinero/activar"     (controller: "tools", action: "kuorumStoreActivateAward")

        name tourStart:           "/tour" (controller:"tour", action: "index")
        name tour_dashboard:      "/tour/dashboard" (controller:"tour", action: "tour_dashboard")
        /**********************/
        /*** END DEPRECATED ***/
        /**********************/

        "/sitemapIndex"{
            controller = 'siteMap'
            action = 'sitemapIndex'
        }

        "/sitemap"{
            controller = 'siteMap'
            action = 'sitemap'
        }

        "/sitemapCountry"{
            controller = 'siteMap'
            action = 'sitemapCountry'
        }

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
