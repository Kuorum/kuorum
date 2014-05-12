import kuorum.core.model.UserType

class UrlMappings {

	static mappings = {

        name home:             "/" (controller: "dashboard", action:"index")
        name dashboardSeeMore: "/dashboard/ver-mas" (controller: "dashboard", action:"dashboardClucks")
        name discover:         "/descubre" (controller: "dashboard", action:"discover")

        name lawCreate:     "/leyes/nueva"(controller: "law"){action = [GET:"create", POST:"save"]}
        name laws:          "/leyes/$regionName?/$commission?" (controller: "law", action:"index")
        name lawShow:       "/leyes/$regionName/$commission/$hashtag" (controller: "law", action:"show")
        name lawEdit:       "/leyes/$regionName/$commission/$hashtag/editar"(controller: "law", action:"edit")
        name lawUpdate:     "/leyes/$regionName/$commission/$hashtag/actualizar"(controller: "law"){action = [GET:"edit", POST:"update"]}
        name lawVote:       "/ajax/leyes/$regionName/$commission/$hashtag/votar"(controller: "law", action:"voteLaw")

        name postCreate:    "/leyes/$regionName/$commission/$hashtag/nuevo-post"(controller: "post"){action = [GET:"create", POST:"save"]}
//        name postSave:      "/leyes/$regionName/$commission/$hashtag/guardar-nuevo-post"(controller: "post"){action = [GET:"create", POST:"save"]}
        name postShow:      "/leyes/$regionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId"(controller: "post", action: "show")
        name postReview:    "/leyes/$regionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/revisar"(controller: "post", action: "review")
        name postPublish:   "/leyes/$regionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/publicar"(controller: "post", action:"publish")
        name postPublished: "/leyes/$regionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/publicado"(controller: "post", action:"promoteYourPost")
        name postEdit:      "/leyes/$regionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/editar"(controller: "post"){action = [GET:"edit", POST:"update"]}
        name postToggleFavorite:"/ajax/leyes/$regionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/favorito"(controller: "post",action: "favorite")
        name postDelComment:"/leyes/$regionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/borrarCommentario"(controller: "post",action: "deleteComment")
        name postAddComment:"/leyes/$regionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/nuevoComentario"(controller: "post",action: "addComment")
        name postCluckIt:   "/leyes/$regionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/kakarear"(controller: "post",action: "cluckPost")
        name postVoteIt:    "/leyes/$regionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/impulsar"(controller: "post",action: "votePost")
        name postPayPost:   "/leyes/$regionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/promocionar"(controller: "post", action:"promotePost")
        name postPaiment:   "/leyes/$regionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/resumen-promocion"(controller: "post", action:"paimentPost")
        name postSuccessPay:"/leyes/$regionName/$commission/$hashtag/$postTypeUrl/$postBrief-$postId/gracias"(controller: "post", action:"successPromotePost")


        //userShow && users is used for build the urls but is never called because the urls constructed should be like citizenShow, organizationShow, politicianShow
        name userShow:          "/$userTypeUrl/$urlName-$id"   (controller: "kuorumUser", action: "show")
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
            action ="index"
            userTypeUrl = UserType.POLITICIAN
        }
        name userFollowers:     "/$userTypeUrl/$urlName-$id/seguidores" (controller: "kuorumUser", action: "userFollowers")
        name userFollowing:     "/$userTypeUrl/$urlName-$id/siguiendo"  (controller: "kuorumUser", action: "userFollowing")

        name register:            "/registro"(controller: "register"){action = [GET:"index", POST:"register"]}
        name registerSuccess:     "/registro/satisfactorio"(controller: "register",action:"registerSuccess")
        name registerResendMail:  "/registro/no-verificado"(controller: "register",action:"resendRegisterVerification")
        name resetPassword:       "/registro/password-olvidado"(controller: "register"){action=[GET:"forgotPassword", POST:"forgotPasswordPost"]}
        name resetPasswordSent:   "/registro/enviada-verificacion"(controller: "register", action:"forgotPasswordSuccess")
        name resetPasswordChange: "/registro/cambiarPassword"(controller: "register"){action=[GET:"resetPassword", POST:"resetPassword"]}
        name customRegisterStep1: "/registro/paso1"(controller: "customRegister"){action = [GET:"step1", POST:"step1Save"]}
        name customRegisterStep2: "/registro/paso2"(controller: "customRegister"){action = [GET:"step2", POST:"step2Save"]}
        name customRegisterStep3: "/registro/paso3"(controller: "customRegister"){action = [GET:"step3", POST:"step3Save"]}
        name customRegisterStep4: "/registro/paso4"(controller: "customRegister"){action = [GET:"step4", POST:"step4Save"]}
        name customRegisterStep5: "/registro/fin"(controller: "customRegister", action:"step5")

        name searcherSearch:      "/buscar"(controller: "search", action:"search")
        name searcherSearchSeeMore:"/ajax/buscar/seeMore"(controller: "search", action:"searchSeeMore")
        name searcherSearchFilters:"/ajax/buscar/nuevos-filtros"(controller: "search", action:"modifyFilters")
        name searcherSuggests:    "/buscar/sugerencias"(controller: "search", action:"suggest")

        name profileEditUser:     "/mi-perfil"                  (controller: "profile"){action =[GET:"editUser", POST:"editUserSave"]}
        name profileChangePass:   "/mi-perfil/cambiar-password" (controller: "profile"){action =[GET:"changePassword", POST:"changePasswordSave"]}
        name profileChangeEmail:  "/mi-perfil/cambiar-email"    (controller: "profile", action: "changeEmail")
        name profileSocialNetworks:"/mi-perfil/redes-sociales"  (controller: "profile", action: "socialNetworks")
        name profileEmailNotifications:"/mi-perfil/notificaciones-por-email"(controller: "profile"){action=[GET:"configurationEmails",POST:"configurationEmailsSave"]}
        name profileFavorites:    "/mi-perfil/pendientes-de-leer"(controller: "profile", action: "showFavoritesPosts")
        name profileMyPosts:      "/mi-perfil/mis-posts"        (controller: "profile", action: "showUserPosts")
        name profileKuorumStore:  "/mi-perfil/el-gallinero"     (controller: "profile", action: "kuorumStore")
        name profileBuyAward:     "/mi-perfil/el-gallinero/comprar"     (controller: "profile", action: "kuorumStoreBuyAward")
        name profileActivateAward:"/mi-perfil/el-gallinero/activar"     (controller: "profile", action: "kuorumStoreActivateAward")
        name profileNotifications:"/mi-perfil/notificaciones"   (controller: "profile", action: "userNotifications")
        name profileMessages:     "/mi-perfil/mensajes"         (controller: "profile", action: "userMessages")

        name footerWhatIsKuorum:  "/kuorum/queEs"               (controller:"footer", action: "whatIsKuorum" )
        name footerUsingMyVote:   "/kuorum/para-que-sirve-mi-voto"(controller:"footer", action: "usingMyVote" )
        name footerUserGuide:     "/kuorum/guia-del-usuario"    (controller:"footer", action: "userGuide" )
        name footerHistories:     "/kuorum/historias"           (controller:"footer", action: "histories" )
        name footerPurposes:      "/kuorum/propuestas"          (controller:"footer", action: "purposes" )
        name footerQuestions:     "/kuorum/preguntas"           (controller:"footer", action: "questions" )
        name footerCitizens:      "/kuorum/ciudadanos"          (controller:"footer", action: "citizens" )
        name footerOrganizations: "/kuorum/organizaciones"      (controller:"footer", action: "organizations" )
        name footerPoliticians:   "/kuorum/politicos"           (controller:"footer", action: "politicians" )
        name footerDevelopers:    "/kuorum/desarrolladores"     (controller:"footer", action: "developers" )
        name footerPrivacyPolicy: "/kuorum/politica-privacidad" (controller:"footer", action: "privacyPolicy")
        name footerTermsUse:      "/kuorum/condiciones-de-uso"  (controller:"footer", action: "termsUse")
        name footerTermsAds:      "/kuorum/normas-publicidad"   (controller:"footer", action: "termsAds")

        name tourStart:           "/tour" (controller:"tour", action: "index")

        name ajaxHeadNotificationsChecked: "/ajax/notificaiones/check"(controller:"notification", action:"notificationChecked")
        name ajaxPostponeAlert: "/ajax/notificaiones/posponer/$id"(controller:"notification", action:"postponeAlert")
        name ajaxHeadMessagesChecked: "/ajax/mensajes/check"(controller:"layouts", action:"headNotificationsChecked")
        name ajaxFollow: "/ajax/kuorumUser/follow"(controller:"kuorumUser", action:"follow")
        name ajaxUnFollow: "/ajax/kuorumUser/unFollow"(controller:"kuorumUser", action:"unFollow")
        name ajaxRequestPolitician: "/ajax/politico/solicitud-kuorum"(controller:"kuorumUser", action:"follow")
        name ajaxCropImage: "/ajax/file/crop"(controller:"file", action:"cropImage")

        name ajaxModuleLawBottomStats: '/ajax/law/bottomLawStats' (controller:'modules', action: 'bottomLawStats')

        name login: "/entrar" (controller:"login", action:"index")
        name logout: "/salir" (controller:"logout", action:"index")

        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }


        "500"(view:'/error')
	}
}
