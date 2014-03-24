import kuorum.core.model.PostType
import org.springframework.security.core.context.SecurityContextHolder as SCH
class UrlMappings {

	static mappings = {

        name home: "/" (controller: "dashboard", action:"index")

        name lawCreate:     "/leyes/nueva"(controller: "law"){action = [GET:"create", POST:"save"]}
        name lawShow:       "/leyes/$regionName/$commision/$hashtag" (controller: "law", action:"show")
        name lawEdit:       "/leyes/$regionName/$commision/$hashtag/editar"(controller: "law", action:"edit")
        name lawUpdate:     "/leyes/$regionName/$commision/$hashtag/actualizar"(controller: "law"){action = [GET:"edit", POST:"update"]}
        name postShow:      "/leyes/$regionName/$commision/$hashtag/$postTypeUrl/$postId"(controller: "post", action: "show")
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

        name customRegisterStep1: "/registro/paso1"(controller: "customRegister"){action = [GET:"step1", POST:"step1Save"]}
        name customRegisterStep2: "/registro/paso2"(controller: "customRegister"){action = [GET:"step2", POST:"step2Save"]}
        name customRegisterStep3: "/registro/paso3"(controller: "customRegister"){action = [GET:"step3", POST:"step3Save"]}
        name customRegisterStep4: "/registro/paso4"(controller: "customRegister"){action = [GET:"step4", POST:"step4Save"]}
        name customRegisterStep5: "/registro/fin"(controller: "customRegister", action:"step5")
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
