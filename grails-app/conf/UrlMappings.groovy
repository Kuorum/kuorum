import kuorum.core.model.PostType
import org.springframework.security.core.context.SecurityContextHolder as SCH
class UrlMappings {

	static mappings = {

        name home: "/" (controller: "dashboard", action:"index")

        name lawCreate:     "/leyes/nueva"(controller: "law"){action = [GET:"create", POST:"save"]}
        name lawShow:       "/leyes/$regionName/$commision/$hashtag" (controller: "law", action:"show")
        name lawEdit:       "/leyes/$regionName/$commision/$hashtag/editar"(controller: "law", action:"edit")
        name lawUpdate:     "/leyes/$regionName/$commision/$hashtag/actualizar"(controller: "law"){action = [GET:"edit", POST:"update"]}
        name lawVote:       "/leyes/$regionName/$commision/$hashtag/votar"(controller: "law", action:"voteLaw")

        name postCreate:    "/leyes/$regionName/$commision/$hashtag/nuevo-post"(controller: "post", action: "create")
        name postSave:      "/leyes/$regionName/$commision/$hashtag/guardar-nuevo-post"(controller: "post"){action = [GET:"create", POST:"save"]}
        name postShow:      "/leyes/$regionName/$commision/$hashtag/$postTypeUrl/$postId"(controller: "post", action: "show")
        name postReview:    "/leyes/$regionName/$commision/$hashtag/$postTypeUrl/$postId/revisar"(controller: "post", action: "review")
        name postPublish:   "/leyes/$regionName/$commision/$hashtag/$postTypeUrl/$postId/publicar"(controller: "post", action:"publish")
        name postPromoteYourPost:"/leyes/$regionName/$commision/$hashtag/$postTypeUrl/$postId/promocionar"(controller: "post", action:"promoteYourPost")
        name postEdit:      "/leyes/$regionName/$commision/$hashtag/$postTypeUrl/$postId/editar"(controller: "post", action:"edit")
        name postUpdate:    "/leyes/$regionName/$commision/$hashtag/$postTypeUrl/$postId/actualizar"(controller: "post"){action = [GET:"edit", POST:"update"]}
        name postAddFavorite:"/leyes/$regionName/$commision/$hashtag/$postTypeUrl/$postId/leerDespues"(controller: "post",action: "favorite")
        name postDelFavorite:"/leyes/$regionName/$commision/$hashtag/$postTypeUrl/$postId/yaLeido"(controller: "post",action: "unfavorite")
        name postDelComment:"/leyes/$regionName/$commision/$hashtag/$postTypeUrl/$postId/borrarCommentario"(controller: "post",action: "deleteComment")
        name postAddComment:"/leyes/$regionName/$commision/$hashtag/$postTypeUrl/$postId/nuevoComentario"(controller: "post",action: "addComment")




        name userShow: "/ciudadano/$id" (controller: "kuorumUser", action: "show")

        name cluckCreate: "/cluck/$postId" (controller: "cluck", action: "createCluck")

        //Espacio para que funcione 3 mapping distintos a la misma url????

        name register:            "/registro"(controller: "register"){action = [GET:"index", POST:"register"]}
        name registerSuccess:     "/registro/satisfactorio"(controller: "register",action:"registerSuccess")
        name resetPassword:       "/registro/passwordOlvidado"(controller: "register"){action=[GET:"forgotPassword", POST:"forgotPasswordPost"]}
        name resetPasswordSent:   "/registro/enviadaVerificacion"(controller: "register", action:"forgotPasswordSuccess")
        name resetPasswordChange: "/registro/cambiarPassword"(controller: "register"){action=[GET:"resetPassword", POST:"resetPassword"]}
        name customRegisterStep1: "/registro/paso1"(controller: "customRegister"){action = [GET:"step1", POST:"step1Save"]}
        name customRegisterStep2: "/registro/paso2"(controller: "customRegister"){action = [GET:"step2", POST:"step2Save"]}
        name customRegisterStep3: "/registro/paso3"(controller: "customRegister"){action = [GET:"step3", POST:"step3Save"]}
        name customRegisterStep4: "/registro/paso4"(controller: "customRegister"){action = [GET:"step4", POST:"step4Save"]}
        name customRegisterStep5: "/registro/fin"(controller: "customRegister", action:"step5")

        name searcherSearch:      "/buscar"(controller: "search", action:"search")
        name searcherSearch:      "/buscar/resultados"(controller: "search", action:"search")
        name searcherSearch:      "/buscar/sugerencias"(controller: "search", action:"suggest")

        name profileEditUser:     "/mi-perfil"                  (controller: "profile", action: "editUser")
        name profileChangePass:   "/mi-perfil/cambiar-password" (controller: "profile", action: "changePassword")
        name profileChangeEmail:  "/mi-perfil/cambiar-email"    (controller: "profile", action: "changeEmail")
        name profileSocialNetworks:"/mi-perfil/redes-sociales"  (controller: "profile", action: "socialNetworks")
        name profileEmailNotifications:"/mi-perfil/notificaciones-por-email"(controller: "profile", action: "configurationEmails")
        name profileFavorites:    "/mi-perfil/pendientes-de-leer"(controller: "profile", action: "showFavoritesPosts")
        name profileMyPosts:      "/mi-perfil/mis-posts"        (controller: "profile", action: "showUserPosts")
        name profileKuorumStore:  "/mi-perfil/el-gallinero"     (controller: "profile", action: "kuorumStore")
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
        name footerTermsUse:      "/kuorum/condicionesDeUso"    (controller:"footer", action: "termsUse")
        name footerTermsAds:      "/kuorum/normas-publicidad"   (controller:"footer", action: "termsAds")

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
