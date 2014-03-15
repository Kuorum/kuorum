import kuorum.core.model.PostType
import org.springframework.security.core.context.SecurityContextHolder as SCH
class UrlMappings {

	static mappings = {

        name home: "/" (controller: "dashboard", action:"index")

        name lawCreate:     "/ley/nueva"(controller: "law"){action = [GET:"create", POST:"save"]}
        name lawShow:       "/ley/$hashtag" (controller: "law", action:"show")
        name lawEdit:       "/ley/$hashtag/editar"(controller: "law", action:"edit")
        name lawUpdate:     "/ley/$hashtag/actualizar"(controller: "law"){action = [GET:"edit", POST:"update"]}
        name postShow:      "/ley/$hashtag/$postTypeUrl/$postId"(controller: "post", action: "show"){
            constraints {
                //Only on show. Is not important that the type don't match. Is only here because is the most important
                postTypeUrl(validator: {
                    return (it in PostType.values().collect{it.urlText})
                })
            }
        }
        name postCreate:    "/ley/$hashtag/nuevoPost"(controller: "post", action: "create")
        name postSave:      "/ley/$hashtag/guardar"(controller: "post"){action = [GET:"create", POST:"save"]}
        name postReview:    "/ley/$hashtag/$postTypeUrl/$postId/revisar"(controller: "post", action: "review")
        name postPublish:   "/ley/$hashtag/$postTypeUrl/$postId/publicar"(controller: "post", action:"publish")
        name postPromoteYourPost:"/ley/$hashtag/$postTypeUrl/$postId/promocionar"(controller: "post", action:"promoteYourPost")
        name postEdit:      "/ley/$hashtag/$postTypeUrl/$postId/editar"(controller: "post", action:"edit")
        name postUpdate:    "/ley/$hashtag/$postTypeUrl/$postId/actualizar"(controller: "post"){action = [GET:"edit", POST:"update"]}



        name userShow: "/user/$id" (controller: "kuorumUser", action: "show")

        name cluckCreate: "/cluck/$postId" (controller: "cluck", action: "createCluck")

        //Espacio para que funcione 3 mapping distintos a la misma url????

        name customRegisterStep2: "/registro/paso2"(controller: "customRegister"){action = [GET:"step2", POST:"step2Save"]}
        name customRegisterStep1: "/registro/paso1"(controller: "customRegister"){action = [GET:"step1", POST:"step1Save"]}
//        name customRegisterStep1: "/registro/paso1"(controller: "customRegister",action:"step1")
//        name customRegisterStep1Save: "/registro/paso1/save"(controller: "customRegister",action:"step1Save")
//        name customRegisterStep2: "/registro/paso2"(controller: "customRegister", action: "step2")

        //name landingPage: "/landingPage" (controller: "dashboard", action:"landingPage")
        //name dashboard: "/dashboard" (controller: "dashboard", action:"dashboard")

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
