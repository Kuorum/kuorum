import grails.util.Environment
import kuorum.core.exception.KuorumException
import kuorum.core.model.UserType

class UrlMappings {

	static mappings = {

        //TODO Hacer esta �apa en BBDD
//        name tempmoralLink: "/$customLink" (controller: "dashboard", action:"customPostMapping")
        name tempmoralLink: "/medidasvalientes" (controller: "dashboard", action:"customPostMapping")

        //The URLs are duplicated because of the refactor of Law into Project. The name of aliases are changed.
        name home:             "/" (controller: "dashboard", action:"index")
        name dashboardSeeMore: "/ajax/dashboard/ver-mas" (controller: "dashboard", action:"dashboardClucks")
        name discover:                      "/descubre" (controller: "dashboard", action:"discover")
        name discoverProjects:              "/descubre/proyectos"         (controller: "discover", action:"discoverProjects")
                                            "/descubre/leyes"         (controller: "discover", action:"discoverProjects")
        name discoverProjectsByRegion:      "/descubre/proyectos/$regionName" (controller: "discover", action:"discoverProjects")
                                            "/descubre/leyes/$regionName" (controller: "discover", action:"discoverProjects")
        name discoverPoliticians:           "/descubre/politicos"     (controller: "discover", action:"discoverPoliticians")
        name discoverRecentPosts:           "/descubre/publicaciones-recientes" (controller: "discover", action:"discoverRecentPosts")
        name discoverRecommendedPosts:      "/descubre/publicaciones-mas-impulsadas" (controller: "discover", action:"discoverRecommendedPosts")

        name projectCreate:     "/proyectos/nuevo"(controller: "project"){action = [GET:"create", POST:"save"]}
                                "/leyes/nueva"(controller: "project"){action = [GET:"create", POST:"save"]}
        name projects:          "/proyectos/$institutionName?/$commission?" (controller: "project", action:"index")
                                "/leyes/$institutionName?/$commission?" (controller: "project", action:"index")
        name projectShow:       "/proyectos/$institutionName/$commission/$hashtag" (controller: "project", action:"show")
                                "/leyes/$institutionName/$commission/$hashtag" (controller: "project", action:"show")
        name projectStats:      "/proyectos/$institutionName/$commission/$hashtag/ficha-tecnica" (controller: "project", action:"stats")
                                "/leyes/$institutionName/$commission/$hashtag/ficha-tecnica" (controller: "project", action:"stats")
        name projectStatsDataMap:"/ajax/proyectos/$institutionName/$commission/$hashtag/ficha-tecnica/datos-mapa" (controller: "project", action:"statsDataMap")
                                 "/ajax/leyes/$institutionName/$commission/$hashtag/ficha-tecnica/datos-mapa" (controller: "project", action:"statsDataMap")
        name projectStatsPieChart:"/ajax/proyectos/$institutionName/$commission/$hashtag/ficha-tecnica/datos-pieChart" (controller: "project", action:"statsDataPieChart")
                                  "/ajax/leyes/$institutionName/$commission/$hashtag/ficha-tecnica/datos-pieChart" (controller: "project", action:"statsDataPieChart")
        name projectShowSec:    "/sec/proyectos/$institutionName/$commission/$hashtag" (controller: "project", action:"showSecured")
                                "/sec/leyes/$institutionName/$commission/$hashtag" (controller: "project", action:"showSecured")
        name projectVote:       "/ajax/proyectos/$institutionName/$commission/$hashtag/votar"(controller: "project", action:"voteProject")
                                "/ajax/leyes/$institutionName/$commission/$hashtag/votar"(controller: "project", action:"voteProject")
        name projectListClucks: "/ajax/proyectos/$institutionName/$commission/$hashtag/listado-kakareos" (controller: "project", action:"listClucksProject")
                                "/ajax/leyes/$institutionName/$commission/$hashtag/listado-kakareos" (controller: "project", action:"listClucksProject")
        name projectListPostDefends:   "/ajax/proyectos/$institutionName/$commission/$hashtag/listado-post-defendidos" (controller: "project", action:"listClucksProjectDefends")
        name projectListPostVictories: "/ajax/proyectos/$institutionName/$commission/$hashtag/listado-victorias" (controller: "project", action:"listClucksProjectVictories")

        name projectList: "/configuracion-usuario/proyectos"(controller: "tools", action:"listProjects")
        name projectListOfUsers: "/ajax/configuracion-usuario/proyectos"(controller: "tools", action: "ajaxShowProjectListOfUsers")
        name publishProject:   "/configuracion-usuario/proyectos/$hashtag/publicar" (controller:"tools", action: "publishProject")

        name projectUpdate:  "/proyectos/$institutionName/$commission/$hashtag/actualizar"(controller: "project"){action = [GET:"createProjectUpdate", POST:"addProjectUpdate"]}

        name postCreate:    "/proyectos/$institutionName/$commission/$hashtag/nuevo-post"(controller: "post"){action = [GET:"create", POST:"save"]}
                            "/leyes/$institutionName/$commission/$hashtag/nuevo-post"(controller: "post"){action = [GET:"create", POST:"save"]}
//        name postSave:      "/leyes/$institutionName/$commission/$hashtag/guardar-nuevo-post"(controller: "post"){action = [GET:"create", POST:"save"]}
        name postShow:      "/proyectos/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId"(controller: "post", action: "show")
                            "/leyes/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId"(controller: "post", action: "show")
        name postReview:    "/proyectos/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/revisar"(controller: "post", action: "review")
                            "/leyes/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/revisar"(controller: "post", action: "review")
        name postPublish:   "/proyectos/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/publicar"(controller: "post", action:"publish")
                            "/leyes/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/publicar"(controller: "post", action:"publish")
        name postPublished: "/proyectos/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/publicado"(controller: "post", action:"postPublished")
                            "/leyes/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/publicado"(controller: "post", action:"postPublished")
        name postEdit:      "/proyectos/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/editar"(controller: "post"){action = [GET:"edit", POST:"update"]}
                            "/leyes/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/editar"(controller: "post"){action = [GET:"edit", POST:"update"]}
        name postDelete:    "/proyectos/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/eliminar-post"(controller: "post", action: "deletePost")
                            "/leyes/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/eliminar-post"(controller: "post", action: "deletePost")
        name postToggleFavorite:"/ajax/proyectos/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/favorito"(controller: "post",action: "favorite")
                                "/ajax/leyes/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/favorito"(controller: "post",action: "favorite")
        name postDelComment:"/proyectos/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/borrarCommentario"(controller: "post",action: "deleteComment")
                            "/leyes/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/borrarCommentario"(controller: "post",action: "deleteComment")
        name postAddComment:"/proyectos/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/nuevoComentario"(controller: "post",action: "addComment")
                            "/leyes/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/nuevoComentario"(controller: "post",action: "addComment")
        name postVoteComment:"/ajax/proyectos/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/votar-comentario"(controller: "post",action: "voteComment")
                             "/ajax/leyes/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/votar-comentario"(controller: "post",action: "voteComment")
        name postCluckIt:   "/proyectos/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/kakarear"(controller: "post",action: "cluckPost")
                            "/leyes/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/kakarear"(controller: "post",action: "cluckPost")
        name postVoteIt:    "/proyectos/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/impulsar"(controller: "post",action: "votePost")
                            "/leyes/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/impulsar"(controller: "post",action: "votePost")
        name postVotesList: "/ajax/proyectos/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/lista-impulsos"(controller: "post",action: "listVotes")
                            "/ajax/leyes/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/lista-impulsos"(controller: "post",action: "listVotes")
        name postClucksList:"/ajax/proyectos/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/lista-kakareos"(controller: "post",action: "listClucks")
                            "/ajax/leyes/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/lista-kakareos"(controller: "post",action: "listClucks")
        name postPayPost:   "/proyectos/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/promocionar"(controller: "post", action:"promotePost")
                            "/leyes/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/promocionar"(controller: "post", action:"promotePost")
        name postPaiment:   "/proyectos/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/resumen-promocion"(controller: "post", action:"paimentPost")
                            "/leyes/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/resumen-promocion"(controller: "post", action:"paimentPost")
        name postSuccessPay:"/proyectos/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/gracias"(controller: "post", action:"successPromotePost")
                            "/leyes/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/gracias"(controller: "post", action:"successPromotePost")
        name postAddDebate: "/ajax/proyectos/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/addDebate"(controller: "post", action:"addDebate")
                            "/ajax/leyes/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/addDebate"(controller: "post", action:"addDebate")
        name postAddVictory:"/ajax/proyectos/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/victoria"(controller: "post", action:"addVictory")
                            "/ajax/leyes/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/victoria"(controller: "post", action:"addVictory")
        name postAddDefender:"/ajax/proyectos/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/apadrinar"(controller: "post", action:"addDefender")
                             "/ajax/leyes/$institutionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/apadrinar"(controller: "post", action:"addDefender")


        //userShow && users is used for build the urls but is never called because the urls constructed should be like citizenShow, organizationShow, politicianShow
        name userShow:          "/$userTypeUrl/$urlName-$id"   (controller: "kuorumUser", action: "show")
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
        name organizacionShow:  "/organizaciones/$urlName-$id" (controller: "kuorumUser", action: "showOrganization")
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

        name register:            "/registro"(controller: "register"){action = [GET:"index", POST:"register"]}
        name registerSuccess:     "/registro/satisfactorio"(controller: "register",action:"registerSuccess")
        name registerPassword: "/registro/establece-password"(controller: "register", action:"choosePassword")
        name registerResendMail:  "/registro/no-verificado"(controller: "register"){action=[GET:"resendRegisterVerification", POST:"resendVerification"]}
        name resetPassword:       "/registro/password-olvidado"(controller: "register"){action=[GET:"forgotPassword", POST:"forgotPasswordPost"]}
        name resetPasswordSent:   "/registro/enviada-verificacion"(controller: "register", action:"forgotPasswordSuccess")
        name resetPasswordChange: "/registro/cambiar-password"(controller: "register"){action=[GET:"resetPassword", POST:"resetPassword"]}
        name customRegisterStep1: "/registro/paso1"(controller: "customRegister"){action = [GET:"step1", POST:"step1Save"]}
        name customRegisterStep2: "/registro/paso2"(controller: "customRegister"){action = [GET:"step2", POST:"step2Save"]}
        name customRegisterStep3: "/registro/paso3"(controller: "customRegister"){action = [GET:"step3", POST:"step3Save"]}
        name customRegisterStep4: "/registro/paso4"(controller: "customRegister"){action = [GET:"step4", POST:"step4Save"]}
        name customRegisterStep5: "/registro/fin"(controller: "customRegister", action:"step5")

        name searcherSearch:      "/buscar"(controller: "search", action:"search")
        name searcherSearchSeeMore:"/ajax/buscar/seeMore"(controller: "search", action:"searchSeeMore")
        name searcherSearchFilters:"/ajax/buscar/nuevos-filtros"(controller: "search", action:"modifyFilters")
        name searcherSuggests:    "/ajax/buscar/sugerencias"(controller: "search", action:"suggest")

        name profileEditUser:     "/configuracion-usuario"                  (controller: "profile"){action =[GET:"editUser", POST:"editUserSave"]}
        name profileChangePass:   "/configuracion-usuario/cambiar-password" (controller: "profile"){action =[GET:"changePassword", POST:"changePasswordSave"]}
        name profileChangeEmail:  "/configuracion-usuario/cambiar-email"    (controller: "profile"){action =[GET:"changeEmail", POST:"changeEmailSave"]}
        name profileChangeEmailSent:  "/configuracion-usuario/cambiar-email/solicitud-recivida"    (controller: "profile", action :"changeEmailConfirmSent")
        name profileChangeEmailConfirm: "/configuracion-usuario/cambiar-email/confirmar"    (controller: "profile", action: "changeEmailConfirm")
        name profileSocialNetworks:"/configuracion-usuario/redes-sociales"  (controller: "profile"){action=[GET:"socialNetworks",POST:"socialNetworksSave"]}
        name profileEmailNotifications:"/configuracion-usuario/notificaciones-por-email"(controller: "profile"){action=[GET:"configurationEmails",POST:"configurationEmailsSave"]}
        name profileMessages:     "/configuracion-usuario/mensajes"         (controller: "profile", action: "userMessages")
        name profileDeleteAccount:"/configuracion-usuario/eliminar-cuenta"  (controller: "profile"){action=[GET:"deleteAccount", POST:"deleteAccountPost"]}

        name toolsNotifications:  "/configuracion-usuario/notificaciones"   (controller: "tools", action: "userNotifications")
        name toolsFavorites:    "/configuracion-usuario/pendientes-de-leer"(controller: "tools", action: "showFavoritesPosts")
        name toolsMyPosts:      "/configuracion-usuario/mis-posts"        (controller: "tools", action: "showUserPosts")
        name toolsKuorumStore:  "/configuracion-usuario/el-gallinero"     (controller: "tools", action: "kuorumStore")
        name toolsBuyAward:     "/ajax/configuracion-usuario/el-gallinero/comprar"     (controller: "tools", action: "kuorumStoreBuyAward")
        name toolsActivateAward:"/ajax/configuracion-usuario/el-gallinero/activar"     (controller: "tools", action: "kuorumStoreActivateAward")

        name footerWhatIsKuorum:  "/kuorum"                     (controller:"footer", action: "whatIsKuorum" )
        name footerUsingMyVote:   "/kuorum/para-que-sirve-mi-voto"(controller:"footer", action: "usingMyVote" )
        name footerUserGuide:     "/kuorum/guia-del-usuario"    (controller:"footer", action: "userGuide" )
        name footerHistories:     "/kuorum/historias"           (controller:"footer", action: "histories" )
        name footerPurposes:      "/kuorum/propuestas"          (controller:"footer", action: "purposes" )
        name footerQuestions:     "/kuorum/preguntas"           (controller:"footer", action: "questions" )
        name footerCitizens:      "/kuorum/ciudadanos"          (controller:"footer", action: "citizens" )
        name footerOrganizations: "/kuorum/organizaciones"      (controller:"footer", action: "organizations" )
        name footerPoliticians:   "/kuorum/politicos"           (controller:"footer", action: "politicians" )
        name footerDevelopers:    "/kuorum/desarrolladores"     (controller:"footer", action: "developers" )
        name footerKuorumStore:   "/kuorum/el-gallinero"        (controller:"footer", action: "kuorumStore" )
        name footerPrivacyPolicy: "/kuorum/politica-privacidad" (controller:"footer", action: "privacyPolicy")
        name footerTermsUse:      "/kuorum/condiciones-de-uso"  (controller:"footer", action: "termsUse")
        name footerTermsAds:      "/kuorum/normas-publicidad"   (controller:"footer", action: "termsAds")

        name tourStart:           "/tour" (controller:"tour", action: "index")
        name tour_dashboard:      "/tour/dashboard" (controller:"tour", action: "tour_dashboard")
        name tour_project:            "/tour/proyecto"       (controller:"tour", action: "tour_project")
                                  "/tour/ley"       (controller:"tour", action: "tour_project")
        name tour_post:           "/tour/publicacion" (controller:"tour", action: "tour_post")

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

        name login:     "/entrar"       (controller:"login", action:"index")
        name loginAuth: "/autenticarse" (controller:"login", action:"auth")
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
        name adminCreateUser:       "/admin/usuarios/crear-usuario" (controller:"adminUser"){action =[GET:"createUser", POST:"saveUser"]}
        name adminEditUser:         "/admin/usuarios/$userTypeUrl/$urlName-$id/editar" (controller:"adminUser"){action =[GET:"editUser", POST:"updateUser"]}
        name adminStats:            "/admin/estadisticas"           (controller:"adminStats", action: "stats")
        name adminStatsMap:         "/admin/estadisticas/mapa"      (controller:"adminStats", action: "statsDataMap")
        name adminStatsPieChart:    "/admin/estadisticas/pie-chart" (controller:"adminStats", action: "statsDataPieChart")
        name adminCreateRegion:     "/admin/regiones/crear-region"  (controller:"adminRegion"){action =[GET:"createRegion", POST:"saveRegion"]}
        name adminEditRegion:       "/admin/regiones/editar-region" (controller:"adminRegion"){action =[GET:"editRegion", POST:"saveRegion"]}
        name adminListRegions:      "/admin/regiones"               (controller:"adminRegion", action:"listRegions")
        name adminCreateInstitution:"/admin/instituciones/crear"    (controller:"adminRegion"){action =[GET:"createInstitution", POST:"saveInstitution"]}
        name adminEditInstitution:  "/admin/instituciones/editar"   (controller:"adminRegion"){action =[GET:"editInstitution", POST:"saveInstitution"]}
        name adminCreatePoliticalParty:"/admin/partidos-politicos/crear"  (controller:"adminPoliticalParty"){action =[GET:"createPoliticalParty", POST:"savePoliticalParty"]}
        name adminEditPoliticalParty:  "/admin/partidos-politicos/editar" (controller:"adminPoliticalParty"){action =[GET:"editPoliticalParty", POST:"savePoliticalParty"]}
        name adminListPoliticalParty:  "/admin/partidos-politicos"               (controller:"adminPoliticalParty", action:"listPoliticalParties")


        "/sitemap"{
            controller = 'siteMap'
            action = 'siteMap'
        }

        "403" (controller: "error", action: "forbidden")
        "404" (controller: "error", action: "notFound")


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
