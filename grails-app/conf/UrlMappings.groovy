import grails.util.Environment
import kuorum.core.exception.KuorumException
import kuorum.core.model.UserType

class UrlMappings {

    static excludes = ['/robots.txt']

	static mappings = {

        //TODO Hacer esta chapu en BBDD
//        name tempmoralLink: "/$customLink" (controller: "dashboard", action:"customPostMapping")
        name tempmoralLink: "/losdatoscuentan" (controller: "dashboard", action:"customPostMapping")
        "/losDatosCuentan" (controller: "dashboard", action:"customPostMapping")
        "/empleoJuvenil" (controller: "dashboard", action:"customPostMappingEmpleoJuvenil")
        "/empleojuvenil" (controller: "dashboard", action:"customPostMappingEmpleoJuvenil")
        "/sayNoToFracking" (controller: "dashboard", action:"customPostMappingSayNoToFracking")
        "/SayNoToFracking" (controller: "dashboard", action:"customPostMappingSayNoToFracking")
        "/saynotofracking" (controller: "dashboard", action:"customPostMappingSayNoToFracking")
        "/immigrationrc" (controller: "dashboard", action:"customPostMappingImmigrationrc")
        "/immigrationRC" (controller: "dashboard", action:"customPostMappingImmigrationrc")

        //The URLs are duplicated because of the refactor of Law into Project. The name of aliases are changed.
        name home:             "/" (controller: "dashboard", action:"index")
        name dashboard:             "/dashboard" (controller: "dashboard", action:"dashboard")
        name landingEditors:   "/editors" (controller: "dashboard", action:"landingEditors")
        name dashboardSeeMore: "/ajax/dashboard/ver-mas" (controller: "dashboard", action:"dashboardClucks")
        name discover:                      "/descubre" (controller: "discover", action:"discoverProjects")
        name discoverProjects:              "/descubre/proyectos"         (controller: "discover", action:"discoverProjects")
                                            "/descubre/leyes"         (controller: "discover", action:"discoverProjects")
        name discoverProjectsByRegion:      "/descubre/proyectos/$regionName" (controller: "discover", action:"discoverProjects")
                                            "/descubre/leyes/$regionName" (controller: "discover", action:"discoverProjects")
        name discoverPoliticians:           "/descubre/politicos"     (controller: "discover", action:"discoverPoliticians")
        name discoverRecentPosts:           "/descubre/publicaciones-recientes" (controller: "discover", action:"discoverRecentPosts")
        name discoverRecommendedPosts:      "/descubre/publicaciones-mas-impulsadas" (controller: "discover", action:"discoverRecommendedPosts")

        name projectCreate:             "/proyectos/nuevo"(controller: "project"){action = [GET:"create", POST:"save"]}
                                        "/leyes/nueva"(controller: "project"){action = [GET:"create", POST:"save"]}
        name projectEdit:               "/proyectos/$regionName/$commission/$hashtag/edit"(controller: "project"){action = [GET:"edit", POST:"update"]}
        name projects:                  "/proyectos/$regionName?/$commission?" (controller: "project", action:"index")
                                        "/leyes/$regionName?/$commission?" (controller: "project", action:"index")
        name projectShow:               "/proyectos/$regionName/$commission/$hashtag" (controller: "project", action:"show")
        name projectShow:               "/hashtag/$hashtag" (controller: "project", action:"show")
                                        "/leyes/$regionName/$commission/$hashtag" (controller: "project", action:"show")
        name projectStats:              "/proyectos/$regionName/$commission/$hashtag/ficha-tecnica" (controller: "project", action:"stats")
                                        "/leyes/$regionName/$commission/$hashtag/ficha-tecnica" (controller: "project", action:"stats")
        name projectStatsDataMap:       "/ajax/proyectos/$regionName/$commission/$hashtag/ficha-tecnica/datos-mapa" (controller: "project", action:"statsDataMap")
        name projectStatsPieChart:      "/ajax/proyectos/$regionName/$commission/$hashtag/ficha-tecnica/datos-pieChart" (controller: "project", action:"statsDataPieChart")
        name projectShowSec:            "/sec/proyectos/$regionName/$commission/$hashtag" (controller: "project", action:"showSecured")
        name projectVote:               "/ajax/proyectos/$regionName/$commission/$hashtag/votar"(controller: "project", action:"voteProject")
        name projectVoteNoTotalUser:    "/sec/proyectos/$regionName/$commission/$hashtag/salvarDatosUsuarioYvotar"(controller: "project", action:"voteProjectAsNonCompleteUser")
        name projectListClucks:         "/ajax/proyectos/$regionName/$commission/$hashtag/listado-kakareos" (controller: "project", action:"listClucksProject")
        name projectListPostDefends:    "/ajax/proyectos/$regionName/$commission/$hashtag/listado-post-defendidos" (controller: "project", action:"listClucksProjectDefends")
        name projectListPostVictories:  "/ajax/proyectos/$regionName/$commission/$hashtag/listado-victorias" (controller: "project", action:"listClucksProjectVictories")

        name projectList: "/herramientas/proyectos"(controller: "tools", action:"listProjects")
        name projectListOfUsers: "/ajax/herramientas/proyectos"(controller: "tools", action: "ajaxShowProjectListOfUsers")
        name publishProject:   "/herramientas/proyectos/$hashtag/publicar" (controller:"tools", action: "publishProject")

        name projectUpdate:  "/proyectos/$regionName/$commission/$hashtag/actualizar"(controller: "project"){action = [GET:"createProjectUpdate", POST:"addProjectUpdate"]}

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
//        name postAddDefender:"/ajax/proyectos/$regionName/$commission/$hashtag/propuesta/$postBrief-$postId/apadrinar"(controller: "post", action:"addDefender")


        //userShow && users is used for build the urls but is never called because the urls constructed should be like citizenShow, organizationShow, politicianShow
        name userShow:          "/$userTypeUrl/$urlName-$id"   (controller: "kuorumUser", action: "show"){
            constraints {
                userTypeUrl inList: ["ciudadanos", "organizaciones", "politicos"]
            }
        }
        name userShowWithAlias: "/$userAlias"   (controller: "kuorumUser", action: "showWithAlias"){
            constraints{
                userAlias (validator: { !['j_spring_security_facebook_redirect', 'proyectos', 'ciudadanos', 'organizaciones', 'politicos'].contains(it) })
            }
        }
        name secUserShow:       "/sec/$userTypeUrl/$urlName-$id"   (controller: "kuorumUser", action: "secShow")
        name users:             "/$userTypeUrl"     (controller: "kuorumUser", action: "index"){
            constraints {
                userTypeUrl inList: ["ciudadanos", "organizaciones", "politicos"]
            }
        }

        name citizenShow:       "/ciudadanos/$urlName-$id"     (controller: "kuorumUser", action: "showCitizen")
        name citizens:          "/ciudadanos"     {
            controller = "kuorumUser"
            action ="index"
            userTypeUrl = UserType.PERSON
        }
        name organizacionShow:  "/organizaciones/$urlName-$id" (controller: "kuorumUser", action: "showCitizen")
        name organizations:     "/organizaciones"  {
            controller = "kuorumUser"
            action ="index"
            userTypeUrl = UserType.ORGANIZATION
        }
        name politicianShow:    "/politicos/$urlName-$id"      (controller: "kuorumUser", action: "showPolitician")
        name politicians:       "/politicos"  {
            controller = "kuorumUser"
            action ="politicians"
            userTypeUrl = UserType.POLITICIAN
        }
        name userFollowers:     "/$userTypeUrl/$urlName-$id/seguidores" (controller: "kuorumUser", action: "userFollowers")
        name userFollowing:     "/$userTypeUrl/$urlName-$id/siguiendo"  (controller: "kuorumUser", action: "userFollowing")
        name userClucks:        "/ajax/$userTypeUrl/$urlName-$id/clucks"  (controller: "kuorumUser", action: "userClucks")
        name userPost:          "/ajax/$userTypeUrl/$urlName-$id/posts"  (controller: "kuorumUser", action: "userPosts")
        name userVictories:     "/ajax/$userTypeUrl/$urlName-$id/victories"  (controller: "kuorumUser", action: "userVictories")
        name ajaxPoliticianProjects:         "/ajax/$userTypeUrl/$urlName-$id/politicianProjects"   (controller: "kuorumUser", action: "politicianProjects")
        name ajaxPoliticianDefendedPosts:    "/ajax/$userTypeUrl/$urlName-$id/defendedPosts"        (controller: "kuorumUser", action: "politicianDefendedPosts")
        name ajaxPoliticianVictoryPosts:     "/ajax/$userTypeUrl/$urlName-$id/defendedVictoryPosts" (controller: "kuorumUser", action: "politicianDefendedVictories")
        name ajaxRegisterContact:            "/ajax/contact"(controller: "register", action: "contactRegister");

        name register:            "/sing-up"(controller: "register"){action = [GET:"index", POST:"register"]}
                                  "/registro"(controller: "register"){action = [GET:"index", POST:"register"]}
        name registerPressKit:    "/sing-up/pressKit"(controller: "register",action:"downloadPressKit")
        name registerStep2:       "/sing-up/step2"(controller: "customRegister"){action = [GET:"step2", POST:"step2Save"]}
        name registerStep3:       "/sing-up/step3"(controller: "customRegister", action:"step3")
        name registerSuccess:     "/registro/satisfactorio"(controller: "register",action:"registerSuccess")
        name registerPassword: "/registro/establece-password"(controller: "register", action:"selectMyPassword")
        name registerResendMail:  "/registro/no-verificado"(controller: "register"){action=[GET:"resendRegisterVerification", POST:"resendVerification"]}
        name resetPassword:       "/registro/password-olvidado"(controller: "register"){action=[GET:"forgotPassword", POST:"forgotPasswordPost"]}
        name resetPasswordSent:   "/registro/enviada-verificacion"(controller: "register", action:"forgotPasswordSuccess")
        name resetPasswordChange: "/registro/cambiar-password"(controller: "register"){action=[GET:"resetPassword", POST:"resetPassword"]}

        name customRegisterCountryAndPostalCode: "/registro/countryAndPostalCode"(controller: "customRegister", action:"countryAndPostalCode")
        name customRegisterAgeAndGender: "/registro/ageAndGender"(controller: "customRegister", action:"ageAndGender")
        name customRegisterTelephone: "/registro/telephone"(controller: "customRegister", action:"telephone")

        name searcherSearch:        "/search"(controller: "search", action:"search")
                                    "/buscar"(controller: "search", action:"search")
        name searcherSearchSeeMore: "/ajax/buscar/seeMore"(controller: "search", action:"searchSeeMore")
        name searcherSearchFilters: "/ajax/buscar/nuevos-filtros"(controller: "search", action:"modifyFilters")
        name searcherSuggests:      "/ajax/buscar/sugerencias"(controller: "search", action:"suggest")
        name suggestRegions:        "/ajax/buscar/regiones/sugerencias"(controller: "search", action:"suggestRegions")
        name suggestTags:           "/ajax/buscar/tags/sugerencias"(controller: "search", action:"suggestTags")

        name profileEditUser:     "/configuracion-usuario"                  (controller: "profile"){action =[GET:"editUser", POST:"editUserSave"]}
        name profileEditAccountDetails:     "/config/account-details"                  (controller: "profile"){action =[GET:"editAccountDetails", POST:"updateAccountDetails"]}
        name profileEditCommissions:     "/configuracion-usuario/cambiar-areas-interes"                  (controller: "profile"){action =[GET:"editCommissions", POST:"editCommissionsSave"]}
        name profileChangePass:   "/configuracion-usuario/cambiar-password" (controller: "profile"){action =[GET:"changePassword", POST:"changePasswordSave"]}
        name profileSetPass:      "/configuracion-usuario/crear-password" (controller: "profile"){action =[GET:"setPassword", POST:"setPasswordSave"]}
        name profileChangeEmail:  "/configuracion-usuario/cambiar-email"    (controller: "profile"){action =[GET:"changeEmail", POST:"changeEmailSave"]}
        name profileChangeEmailSent:  "/configuracion-usuario/cambiar-email/solicitud-recivida"    (controller: "profile", action :"changeEmailConfirmSent")
        name profileChangeEmailResend:  "/configuracion-usuario/cambiar-email/resend-email" (controller: "profile", action :"updateUserEmail")
        name profileChangeEmailConfirm: "/configuracion-usuario/cambiar-email/confirmar"    (controller: "profile", action: "changeEmailConfirm")
        name profileSocialNetworks:"/configuracion-usuario/redes-sociales"  (controller: "profile"){action=[GET:"socialNetworks",POST:"socialNetworksSave"]}
        name profileEmailNotifications:"/configuracion-usuario/notificaciones-por-email"(controller: "profile"){action=[GET:"configurationEmails",POST:"configurationEmailsSave"]}
        name profileMessages:     "/configuracion-usuario/mensajes"         (controller: "profile", action: "userMessages")
        name profileDeleteAccount:"/configuracion-usuario/eliminar-cuenta"  (controller: "profile"){action=[GET:"deleteAccount", POST:"deleteAccountPost"]}

        name profilePoliticianExternalActivity: "/configuracion-politico/external-activity" (controller: "politicianProfile"){action=[GET:"editExternalActivity", POST:"updateExternalActivity"]}
        name profilePoliticianRelevantEvents: "/configuracion-politico/known-for"     (controller: "politicianProfile"){action=[GET:"editRelevantEvents", POST:"updateRelevantEvents"]}
        name profilePoliticianProfessionalDetails: "/configuracion-politico/professional-details"     (controller: "politicianProfile"){action=[GET:"editProfessionalDetails", POST:"updateProfessionalDetails"]}
        name profilePoliticianQuickNotes: "/configuracion-politico/quick-notes"     (controller: "politicianProfile"){action=[GET:"editQuickNotes", POST:"updateQuickNotes"]}
        name profilePoliticianCauses: "/configuracion-politico/causas"     (controller: "politicianProfile"){action=[GET:"editCauses", POST:"updateCauses"]}
        name profilePoliticianExperience: "/configuracion-politico/experiencia"     (controller: "politicianProfile"){action=[GET:"editPoliticalExperience", POST:"updatePoliticalExperience"]}

        name profileMailing : "/notifications/mailing" (controller: "profile", action:"showUserEmails")

        name toolsNotifications:  "/herramientas/notificaciones"   (controller: "tools", action: "userNotifications")
        name toolsFavorites:    "/herramientas/pendientes-de-leer"(controller: "tools", action: "showFavoritesPosts")
        name toolsMyPosts:      "/herramientas/mis-posts"        (controller: "tools", action: "showUserPosts")
        name toolsKuorumStore:  "/herramientas/el-gallinero"     (controller: "tools", action: "kuorumStore")
        name toolsBuyAward:     "/ajax/herramientas/el-gallinero/comprar"     (controller: "tools", action: "kuorumStoreBuyAward")
        name toolsActivateAward:"/ajax/herramientas/el-gallinero/activar"     (controller: "tools", action: "kuorumStoreActivateAward")

        name footerAboutUs:       "/kuorum"                         (controller:"footer", action: "aboutUs" )
        name footerVision:        "/kuorum/mision-and-vision"        (controller:"footer", action: "vision" )
                                  "/kuorum/vision-y-valores"        (controller:"footer", action: "vision" )
        name footerTeam:          "/kuorum/our-team"          (controller:"footer", action: "team" )
                                  "/kuorum/nuestro-equipo"          (controller:"footer", action: "team" )
        name footerTechnology:    "/kuorum/what-is-kuorum"          (controller:"footer", action: "tech" )
        name footerCitizens:      "/kuorum/citizens"              (controller:"footer", action: "citizens" )
                                  "/kuorum/ciudadanos"              (controller:"footer", action: "citizens" )
        name footerImpact:        "/kuorum/impact-measurement"      (controller:"footer", action: "impact" )
        name footerPoliticians:   "/kuorum/politicians"               (controller:"footer", action: "politicians" )
                                  "/kuorum/politicos"               (controller:"footer", action: "politicians" )
        name footerDevelopers:    "/kuorum/editors"         (controller:"footer", action: "developers" )
        name footerInformation:   "/kuorum/press"  (controller:"footer", action: "information" )
                                  "/kuorum/informacion-y-recursos"  (controller:"footer", action: "information" )
        name footerPrivacyPolicy: "/kuorum/privacy-policy"     (controller:"footer", action: "privacyPolicy")
                                  "/kuorum/politica-privacidad"     (controller:"footer", action: "privacyPolicy")
        name footerTermsUse:      "/kuorum/terms-of-use"      (controller:"footer", action: "termsUse")
                                  "/kuorum/condiciones-de-uso"      (controller:"footer", action: "termsUse")

        name funnelSuccessfulStories:      "/la-nueva-politica"  (controller:"funnel", action:"funnelSuccessfulStories")
        name funnelOffers:                 "/ofertas"            (controller:"funnel", action:"funnelOffers")
        name funnelPay:                    "/suscripcion"        (controller:"funnel", action:"funnelPay")
        name funnelSubscription:           "/registro-politico"  (controller:"funnel", action:"funnelSubscription")
        name funnelLoggin:                 "/login-politico"     (controller:"funnel", action:"funnelLogin")
        name funnelPaySuccess:    "/pago-satisfactorio" (controller:"funnel", action:"funnelSuccess")
        name funnelUpdatePersonalData:    "/actualizar-tlf-politico" (controller:"funnel", action:"funnelUpdatePersonalData")

        name tourStart:           "/tour" (controller:"tour", action: "index")
        name tour_dashboard:      "/tour/dashboard" (controller:"tour", action: "tour_dashboard")
        name tour_project:            "/tour/proyecto"       (controller:"tour", action: "tour_project")
                                  "/tour/ley"       (controller:"tour", action: "tour_project")
        name tour_post:           "/tour/publicacion" (controller:"tour", action: "tour_post")

        name campaignPoll:        "/campaign/poll" (controller: "campaign", action: "saveCitizenPriorities")

        name ajaxHeadNotificationsChecked: "/ajax/notificaiones/check"(controller:"notification", action:"notificationChecked")
        name ajaxPostponeAlert: "/ajax/notificaiones/posponer/$id"(controller:"notification", action:"postponeAlert")
        name ajaxHeadMessagesChecked: "/ajax/mensajes/check"(controller:"layouts", action:"headNotificationsChecked")
        name ajaxFollow: "/ajax/kuorumUser/follow"(controller:"kuorumUser", action:"follow")
        name ajaxUnFollow: "/ajax/kuorumUser/unFollow"(controller:"kuorumUser", action:"unFollow")
        name ajaxRequestPolitician: "/ajax/politico/solicitud-kuorum"(controller:"kuorumUser", action:"follow")
        name ajaxCropImage: "/ajax/file/crop"(controller:"file", action:"cropImage")
        name ajaxUploadFile: "/ajax/file/upload" (controller:'file', action:"uploadImage")
        //New endpoint. Upload a PDF file
        name ajaxUploadFilePDF: "/ajax/file/uploadPDF" (controller:'file', action:"uploadPDF")

        name ajaxModuleProjectBottomStats: '/ajax/project/bottomProjectStats' (controller:'modules', action: 'bottomProjectStats')
                                       '/ajax/law/bottomLawStats' (controller:'modules', action: 'bottomProjectStats')

        name login:     "/log-in"       (controller:"login", action:"index")
                        "/entrar"       (controller:"login", action:"index")
        name loginAuth: "/sign-in" (controller:"login", action:"auth")
                        "/autenticarse" (controller:"login", action:"auth")
        name loginFull: "/confirmar-usuario" (controller:"login", action:"full")
        name logout:    "/salir"        (controller:"logout", action:"index")

        name adminPrincipal:        "/admin"                          (controller:"adminProject", action: "index")
        name adminCreateProject:    "/admin/proyectos/crear-proyecto" (controller:"adminProject"){action =[GET:"createProject", POST:"saveProject"]}
                                    "/admin/leyes/crear-ley"        (controller:"adminProject"){action =[GET:"createProject", POST:"saveProject"]}
        name adminEditProject:      "/admin/proyectos/editar-proyecto/$hashtag" (controller:"adminProject"){action =[GET:"editProject", POST:"updateProject"]}
                                    "/admin/leyes/editar-ley/$hashtag" (controller:"adminProject"){action =[GET:"editProject", POST:"updateProject"]}
        name adminPublishProject:   "/admin/proyectos/editar-proyecto/$hashtag/publicar"     (controller:"adminProject", action: "publishProject")
                                    "/admin/leyes/editar-ley/$hashtag/publicar"     (controller:"adminProject", action: "publishProject")
        name adminUnpublishProject: "/admin/proyectos/editar-proyecto/$hashtag/despublicar"  (controller:"adminProject", action: "unPublishProject")
                                    "/admin/leyes/editar-ley/$hashtag/despublicar"  (controller:"adminProject", action: "unPublishProject")
        name adminUnpublishedProjects:"/admin/proyectos/no-publicados"    (controller:"adminProject", action: "unpublishedProjects")
                                      "/admin/leyes/no-publicadas"    (controller:"adminProject", action: "unpublishedProjects")
        name adminTestMail:         "/admin/mailing/test"           (controller:"mailTesting", action: "index")
        name adminSearcherIndex:    "/admin/searcher/indexar"       (controller:"admin", action: "solrIndex")
        name adminSearcherFullIndex:"/admin/searcher/full-index"    (controller:"admin", action:"fullIndex")
//        name adminCreateUser:       "/admin/usuarios/crear-usuario" (controller:"adminUser"){action =[GET:"createUser", POST:"saveUser"]}
        name adminEditUser:         "/admin/usuarios/$userTypeUrl/$urlName-$id/editar" (controller:"adminUser"){action =[GET:"editUser", POST:"updateUser"]}
        name adminEditPoliticianExternalActivity: "/admin/usuarios/$userTypeUrl/$urlName-$id/editar/externalActivity" (controller:"adminPolitician"){action =[GET:"editExternalActivity", POST:"updateExternalActivity"]}
        name adminEditPoliticianRelevantEvents: "/admin/usuarios/$userTypeUrl/$urlName-$id/editar/relevantEvents" (controller:"adminPolitician"){action =[GET:"editRelevantEvents", POST:"updateRelevantEvents"]}
        name adminEditPoliticianProfessionalDetails: "/admin/usuarios/$userTypeUrl/$urlName-$id/editar/professionalDetails" (controller:"adminPolitician"){action =[GET:"editProfessionalDetails", POST:"updateProfessionalDetails"]}
        name adminEditPoliticianQuickNotes: "/admin/usuarios/$userTypeUrl/$urlName-$id/editar/quick-notes" (controller:"adminPolitician"){action =[GET:"editQuickNotes", POST:"updateQuickNotes"]}
        name adminEditPoliticianCauses: "/admin/usuarios/$userTypeUrl/$urlName-$id/editar/causes" (controller:"adminPolitician"){action =[GET:"editCauses", POST:"updateCauses"]}
        name adminEditPoliticianExperience: "/admin/usuarios/$userTypeUrl/$urlName-$id/editar/experiencia" (controller:"adminPolitician"){action =[GET:"editPoliticalExperience", POST:"updatePoliticalExperience"]}
        name adminKuorumAccountEdit: "/admin/usuarios/$userTypeUrl/$urlName-$id/editar/account-details" (controller:"adminUser"){action =[GET:"editAdminAccountDetails", POST:"updateAdminAccountDetails"]}
        name adminStats:            "/admin/estadisticas"           (controller:"adminStats", action: "stats")
        name adminStatsMap:         "/admin/estadisticas/mapa"      (controller:"adminStats", action: "statsDataMap")
        name adminStatsPieChart:    "/admin/estadisticas/pie-chart" (controller:"adminStats", action: "statsDataPieChart")
        name ajaxDeleteRecommendedUser: "/ajax/kuorumUser/deleteRecommendedUser"(controller: 'recommendedUserInfo', action: 'deleteRecommendedUser')

        "/sitemap"{
            controller = 'siteMap'
            action = 'siteMap'
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
}
