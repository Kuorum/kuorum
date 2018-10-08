import grails.util.Environment
import kuorum.core.exception.KuorumException
import kuorum.core.model.AvailableLanguage
import org.springframework.security.access.AccessDeniedException

class UrlMappings {

    static excludes = ['/robots.txt']

    static List<String> RESERVED_PATHS = ['j_spring_security_facebook_redirect','j_spring_security_exit_user', 'register', 'login','js','images','css', 'fonts']
	static mappings = {

        /**********************/
        /***** I18N URLs ******/
        /**********************/

        /**/
        /** NEW LANDIGNS **/
        name landingServices:       "/"  (controller: "landing", action: "landingServices"){mappingName="landingServices"}
        name en_landingServices:    "/"  (controller: "landing", action: "landingServices"){mappingName="landingServices"}
        name es_landingServices:    "/"  (controller: "landing", action: "landingServices"){mappingName="landingServices"}
        name de_landingServices:    "/"  (controller: "landing", action: "landingServices"){mappingName="landingServices"}
        name ca_landingServices:    "/"  (controller: "landing", action: "landingServices"){mappingName="landingServices"}

        name home:              "/"  (controller: "landing", action:"landingServices"){mappingName="home"}
        name en_home:           "/"  (controller: "landing", action:"landingServices"){mappingName="home"}
        name es_home:           "/"  (controller: "landing", action:"landingServices"){mappingName="home"}
        name de_home:           "/"  (controller: "landing", action:"landingServices"){mappingName="home"}
        name ca_home:           "/"  (controller: "landing", action:"landingServices"){mappingName="home"}

        name footerPrivacyPolicy:   "/legal/privacy-policy"       (controller:"footer", action: "privacyPolicy"){mappingName="footerPrivacyPolicy"}
        name en_footerPrivacyPolicy:"/legal/privacy-policy"       (controller:"footer", action: "privacyPolicy"){mappingName="footerPrivacyPolicy"}
        name es_footerPrivacyPolicy:"/legal/politica-privacidad"  (controller:"footer", action: "privacyPolicy"){mappingName="footerPrivacyPolicy"}
        name de_footerPrivacyPolicy:"/legal/dantensutzpolitik"  (controller:"footer", action: "privacyPolicy"){mappingName="footerPrivacyPolicy"}
        name ca_footerPrivacyPolicy:"/legal/politica-de-privadesa"  (controller:"footer", action: "privacyPolicy"){mappingName="footerPrivacyPolicy"}

        name footerTermsUse:        "/legal/terms-of-use"         (controller:"footer", action: "termsUse")
        name en_footerTermsUse:     "/legal/terms-of-use"         (controller:"footer", action: "termsUse")
        name es_footerTermsUse:     "/legal/condiciones-de-uso"   (controller:"footer", action: "termsUse")
        name de_footerTermsUse:     "/legal/nutzungsbedingungen"  (controller:"footer", action: "termsUse")
        name ca_footerTermsUse:     "/legal/condicions-d-us"      (controller:"footer", action: "termsUse")

        name register:              "/sign-up"          (controller: "register"){action = [GET:"index", POST:"register"]}
        name en_register:           "/sign-up"          (controller: "register"){action = [GET:"index", POST:"register"]}
        name es_register:           "/registro"         (controller: "register"){action = [GET:"index", POST:"register"]}
        name de_register:           "/registrierung"    (controller: "register"){action = [GET:"index", POST:"register"]}
        name ca_register:           "/registre"         (controller: "register"){action = [GET:"index", POST:"register"]}

        name ajaxRegister:          "/ajax/sign-up"     (controller: "register", action:"ajaxRegister")
        name ajaxRegisterCheckEmail:"/ajax/sign-up/checkEmail"(controller: "register", action:"checkEmail")
        name ajaxRegisterRRSSOAuth: "/ajax/sign-up/rrssOAuth"(controller: "register", action:"registerRRSSOAuthAjax")

        name registerSuccess:       "/sign-up/success"          (controller: "register",action:"registerSuccess"){mappingName="registerSuccess"}
        name en_registerSuccess:    "/sign-up/success"          (controller: "register",action:"registerSuccess"){mappingName="registerSuccess"}
        name es_registerSuccess:    "/registro/satisfactorio"   (controller: "register",action:"registerSuccess"){mappingName="registerSuccess"}
        name de_registerSuccess:    "/registrierung/erfolgreich"   (controller: "register",action:"registerSuccess"){mappingName="registerSuccess"}
        name ca_registerSuccess:    "/registre/satisfactori"   (controller: "register",action:"registerSuccess"){mappingName="registerSuccess"}

        name registerPassword:      "/sign-up/set-password"                (controller: "register", action:"selectMyPassword"){mappingName="registerPassword"}
        name en_registerPassword:   "/sign-up/set-password"                (controller: "register", action:"selectMyPassword"){mappingName="registerPassword"}
        name es_registerPassword:   "/registro/establece-password"         (controller: "register", action:"selectMyPassword"){mappingName="registerPassword"}
        name de_registerPassword:   "/registrierung/passwort-einstellen"   (controller: "register", action:"selectMyPassword"){mappingName="registerPassword"}
        name ca_registerPassword:   "/registre/estableix-password"   (controller: "register", action:"selectMyPassword"){mappingName="registerPassword"}

        name registerResendMail:    "/sign-up/no-valid"                 (controller: "register"){action=[GET:"resendRegisterVerification", POST:"resendVerification"];mappingName="registerResendMail"}
        name en_registerResendMail: "/sign-up/no-valid"                 (controller: "register"){action=[GET:"resendRegisterVerification", POST:"resendVerification"];mappingName="registerResendMail"}
        name es_registerResendMail: "/registro/no-verificado"           (controller: "register"){action=[GET:"resendRegisterVerification", POST:"resendVerification"];mappingName="registerResendMail"}
        name de_registerResendMail: "/registrierung/nicht-bestaetigt"   (controller: "register"){action=[GET:"resendRegisterVerification", POST:"resendVerification"];mappingName="registerResendMail"}
        name ca_registerResendMail: "/registre/no-verificat"   (controller: "register"){action=[GET:"resendRegisterVerification", POST:"resendVerification"];mappingName="registerResendMail"}

        name resetPassword:         "/sign-in/recover-password"      (controller: "register"){action=[GET:"forgotPassword", POST:"forgotPasswordPost"];mappingName="resetPassword"}
        name en_resetPassword:      "/sign-in/recover-password"      (controller: "register"){action=[GET:"forgotPassword", POST:"forgotPasswordPost"];mappingName="resetPassword"}
        name es_resetPassword:      "/registro/password-olvidado"    (controller: "register"){action=[GET:"forgotPassword", POST:"forgotPasswordPost"];mappingName="resetPassword"}
        name de_resetPassword:      "/registrierung/passwort-wiederherstellen"    (controller: "register"){action=[GET:"forgotPassword", POST:"forgotPasswordPost"];mappingName="resetPassword"}
        name ca_resetPassword:      "/registre/password-oblidada"    (controller: "register"){action=[GET:"forgotPassword", POST:"forgotPasswordPost"];mappingName="resetPassword"}

        name registerVerifyAccount:         "/register/verifyRegistration"   (controller: "register", action:"verifyRegistration"){mappingName="registerVerifyAccount"}
        name en_registerVerifyAccount:      "/register/verify-registration"  (controller: "register", action:"verifyRegistration"){mappingName="registerVerifyAccount"}
        name es_registerVerifyAccount:      "/registro/verificar-cuenta"     (controller: "register", action:"verifyRegistration"){mappingName="registerVerifyAccount"}
        name de_registerVerifyAccount:      "/registrierung/konto-bestaetigen"     (controller: "register", action:"verifyRegistration"){mappingName="registerVerifyAccount"}
        name ca_registerVerifyAccount:      "/registre/verifica-compte"     (controller: "register", action:"verifyRegistration"){mappingName="registerVerifyAccount"}

        name validateResetPasswordAjax:"/ajax/forgot-password" (controller:"register", action: "ajaxValidationForgotPassword")

        name resetPasswordSent:     "/sign-up/verification-sent"     (controller: "register", action:"forgotPasswordSuccess"){mappingName="resetPasswordSent"}
        name en_resetPasswordSent:  "/sign-up/verification-sent"     (controller: "register", action:"forgotPasswordSuccess"){mappingName="resetPasswordSent"}
        name es_resetPasswordSent:  "/registro/enviada-verificacion" (controller: "register", action:"forgotPasswordSuccess"){mappingName="resetPasswordSent"}
        name de_resetPasswordSent:  "/registrierung/bestaetigung-geschickt" (controller: "register", action:"forgotPasswordSuccess"){mappingName="resetPasswordSent"}
        name ca_resetPasswordSent:  "/registre/enviada-verificacio" (controller: "register", action:"forgotPasswordSuccess"){mappingName="resetPasswordSent"}


        name resetPasswordChange:   "/sign-up/change-pass"       (controller: "register"){action=[GET:"resetPassword", POST:"resetPassword"];mappingName="resetPasswordChange"}
        name en_resetPasswordChange:"/sign-up/change-password"   (controller: "register"){action=[GET:"resetPassword", POST:"resetPassword"];mappingName="resetPasswordChange"}
        name es_resetPasswordChange:"/registro/cambiar-password" (controller: "register"){action=[GET:"resetPassword", POST:"resetPassword"];mappingName="resetPasswordChange"}
        name de_resetPasswordChange:"/registrierung/passwort-aendern" (controller: "register"){action=[GET:"resetPassword", POST:"resetPassword"];mappingName="resetPasswordChange"}
        name ca_resetPasswordChange:"/registre/canvia-password" (controller: "register"){action=[GET:"resetPassword", POST:"resetPassword"];mappingName="resetPasswordChange"}

        name ajaxRequestADemo:      "/ajax/requestADemo"(controller: "register", action: "requestADemo")


        name login:     "/log-in"    (controller:"login", action:"index"){mappingName="login"}
        name en_login:  "/log-in"    (controller:"login", action:"index"){mappingName="login"}
        name es_login:  "/entrar"    (controller:"login", action:"index"){mappingName="login"}
        name de_login:  "/anmelden"    (controller:"login", action:"index"){mappingName="login"}
        name ca_login:  "/accedir"    (controller:"login", action:"index"){mappingName="login"}

        name ajaxLoginCheck:"/ajax/checkLogin"  (controller:"login", action:"checkEmailAndPass")
        name ajaxLoginModal:"/ajax/login/modal-auth"  (controller:"login", action:"modalAuth")

        name loginAuth:     "/sign-in"       (controller:"login", action:"auth"){mappingName="loginAuth"}
        name en_loginAuth:  "/sign-in"       (controller:"login", action:"auth"){mappingName="loginAuth"}
        name es_loginAuth:  "/entra"         (controller:"login", action:"auth"){mappingName="loginAuth"}
        name de_loginAuth:  "/einloggen"     (controller:"login", action:"auth"){mappingName="loginAuth"}
        name ca_loginAuth:  "/accedeix"       (controller:"login", action:"auth"){mappingName="loginAuth"}

        name authError:     "/login/authfail"       (controller:"login", action:"authfail")

        name loginFull:     "/check-user"            (controller:"login", action:"full"){mappingName="loginFull"}
        name en_loginFull:  "/check-user"            (controller:"login", action:"full"){mappingName="loginFull"}
        name es_loginFull:  "/confirmar-usuario"     (controller:"login", action:"full"){mappingName="loginFull"}
        name de_loginFull:  "/benutzer-bestaetigen"     (controller:"login", action:"full"){mappingName="loginFull"}
        name ca_loginFull:  "/confirma-usuari"     (controller:"login", action:"full"){mappingName="loginFull"}

        name logout:    "/logout"       (controller:"logout", action:"index")
                        "/salir"        {controller="redirect"; action= "redirect301"; newMapping='logout'}

        name searcherSearch:        "/search"        (controller: "search", action:"search"){mappingName="searcherSearch"}
        name en_searcherSearch:     "/search/$word?" (controller: "search", action:"search"){mappingName="searcherSearch"}
        name es_searcherSearch:     "/buscar/$word?" (controller: "search", action:"search"){mappingName="searcherSearch"}
        name de_searcherSearch:     "/suche/$word?" (controller: "search", action:"search"){mappingName="searcherSearch"}
        name ca_searcherSearch:     "/cerca/$word?" (controller: "search", action:"search"){mappingName="searcherSearch"}

        name searcherSearchByCAUSE:   "/search/cause"             (controller: "search", action:"search"){searchType="CAUSE"; mappingName="searcherSearchByCAUSE"}
        name en_searcherSearchByCAUSE:"/search/cause/$word?"      (controller: "search", action:"search"){searchType="CAUSE"; mappingName="searcherSearchByCAUSE"}
        name es_searcherSearchByCAUSE:"/buscar/causa/$word?"      (controller: "search", action:"search"){searchType="CAUSE"; mappingName="searcherSearchByCAUSE"}
        name de_searcherSearchByCAUSE:"/suche/themen/$word?"      (controller: "search", action:"search"){searchType="CAUSE"; mappingName="searcherSearchByCAUSE"}
        name ca_searcherSearchByCAUSE:"/cerca/causa/$word?"      (controller: "search", action:"search"){searchType="CAUSE"; mappingName="searcherSearchByCAUSE"}

        name searcherSearchByREGION:   "/search/users/from/$word?"   (controller: "search", action:"search"){searchType="REGION"; mappingName="searcherSearchByREGION"}
        name en_searcherSearchByREGION:"/search/from/$word?"         (controller: "search", action:"search"){searchType="REGION"; mappingName="searcherSearchByREGION"}
        name es_searcherSearchByREGION:"/buscar/en/$word?"           (controller: "search", action:"search"){searchType="REGION"; mappingName="searcherSearchByREGION"}
        name de_searcherSearchByREGION:"/suche/von/$word?"           (controller: "search", action:"search"){searchType="REGION"; mappingName="searcherSearchByREGION"}
        name ca_searcherSearchByREGION:"/cerca/a/$word?"           (controller: "search", action:"search"){searchType="REGION"; mappingName="searcherSearchByREGION"}

        name searcherSearchKUORUM_USER:     "/search/users"             (controller: "search", action:"search"){type="KUORUM_USER"; mappingName="searcherSearchKUORUM_USER"}
        name en_searcherSearchKUORUM_USER:  "/search/users/$word?"      (controller: "search", action:"search"){type="KUORUM_USER"; mappingName="searcherSearchKUORUM_USER"}
        name es_searcherSearchKUORUM_USER:  "/buscar/usuarios/$word?"   (controller: "search", action:"search"){type="KUORUM_USER"; mappingName="searcherSearchKUORUM_USER"}
        name de_searcherSearchKUORUM_USER:  "/suche/benutzer/$word?"   (controller: "search", action:"search"){type="KUORUM_USER"; mappingName="searcherSearchKUORUM_USER"}
        name ca_searcherSearchKUORUM_USER:  "/cerca/usuaris/$word?"   (controller: "search", action:"search"){type="KUORUM_USER"; mappingName="searcherSearchKUORUM_USER"}

        name searcherSearchKUORUM_USERByCAUSE:   "/search/users/cause/$word?"          (controller: "search", action:"search"){searchType="CAUSE";type="KUORUM_USER"; mappingName="searcherSearchKUORUM_USERByCAUSE"}
        name en_searcherSearchKUORUM_USERByCAUSE:"/search/users/cause/$word?"          (controller: "search", action:"search"){searchType="CAUSE";type="KUORUM_USER"; mappingName="searcherSearchKUORUM_USERByCAUSE"}
        name es_searcherSearchKUORUM_USERByCAUSE:"/buscar/usuarios/causa/$word?"       (controller: "search", action:"search"){searchType="CAUSE";type="KUORUM_USER"; mappingName="searcherSearchKUORUM_USERByCAUSE"}
        name de_searcherSearchKUORUM_USERByCAUSE:"/suche/benutzer/themen/$word?"       (controller: "search", action:"search"){searchType="CAUSE";type="KUORUM_USER"; mappingName="searcherSearchKUORUM_USERByCAUSE"}
        name ca_searcherSearchKUORUM_USERByCAUSE:"/cerca/usuaris/causa/$word?"       (controller: "search", action:"search"){searchType="CAUSE";type="KUORUM_USER"; mappingName="searcherSearchKUORUM_USERByCAUSE"}

        name searcherSearchKUORUM_USERByREGION:   "/search/users/from/$word?"          (controller: "search", action:"search"){searchType="REGION";type="KUORUM_USER"; mappingName="searcherSearchKUORUM_USERByREGION"}
        name en_searcherSearchKUORUM_USERByREGION:"/search/users/from/$word?"          (controller: "search", action:"search"){searchType="REGION";type="KUORUM_USER"; mappingName="searcherSearchKUORUM_USERByREGION"}
        name es_searcherSearchKUORUM_USERByREGION:"/buscar/usuarios/en/$word?"         (controller: "search", action:"search"){searchType="REGION";type="KUORUM_USER"; mappingName="searcherSearchKUORUM_USERByREGION"}
        name de_searcherSearchKUORUM_USERByREGION:"/suche/benutzer/von/$word?"         (controller: "search", action:"search"){searchType="REGION";type="KUORUM_USER"; mappingName="searcherSearchKUORUM_USERByREGION"}
        name ca_searcherSearchKUORUM_USERByREGION:"/cerca/usuaris/a/$word?"         (controller: "search", action:"search"){searchType="REGION";type="KUORUM_USER"; mappingName="searcherSearchKUORUM_USERByREGION"}

        name searcherSearchPOST:   "/search/post"                   (controller: "search", action:"search"){type="POST"; mappingName="searcherSearchPOST"}
        name en_searcherSearchPOST:"/search/post/$word?"            (controller: "search", action:"search"){type="POST"; mappingName="searcherSearchPOST"}
        name es_searcherSearchPOST:"/buscar/publicacion/$word?"     (controller: "search", action:"search"){type="POST"; mappingName="searcherSearchPOST"}
        name de_searcherSearchPOST:"/suche/beitraege/$word?"     (controller: "search", action:"search"){type="POST"; mappingName="searcherSearchPOST"}
        name ca_searcherSearchPOST:"/cerca/proposta/$word?"     (controller: "search", action:"search"){type="POST"; mappingName="searcherSearchPOST"}

        name searcherSearchPOSTByCAUSE:   "/search/post/cause/$word?"           (controller: "search", action:"search"){searchType="CAUSE";type="POST"; mappingName="searcherSearchPOSTByCAUSE"}
        name en_searcherSearchPOSTByCAUSE:"/search/post/cause/$word?"           (controller: "search", action:"search"){searchType="CAUSE";type="POST"; mappingName="searcherSearchPOSTByCAUSE"}
        name es_searcherSearchPOSTByCAUSE:"/buscar/publicacion/causa/$word?"    (controller: "search", action:"search"){searchType="CAUSE";type="POST"; mappingName="searcherSearchPOSTByCAUSE"}
        name de_searcherSearchPOSTByCAUSE:"/suche/beitraege/themen/$word?"    (controller: "search", action:"search"){searchType="CAUSE";type="POST"; mappingName="searcherSearchPOSTByCAUSE"}
        name ca_searcherSearchPOSTByCAUSE:"/cerca/proposta/causa/$word?"    (controller: "search", action:"search"){searchType="CAUSE";type="POST"; mappingName="searcherSearchPOSTByCAUSE"}

        name searcherSearchPOSTByREGION:   "/search/post/from/$word?"           (controller: "search", action:"search"){searchType="REGION";type="POST"; mappingName="searcherSearchPOSTByREGION"}
        name en_searcherSearchPOSTByREGION:"/search/post/from/$word?"           (controller: "search", action:"search"){searchType="REGION";type="POST"; mappingName="searcherSearchPOSTByREGION"}
        name es_searcherSearchPOSTByREGION:"/buscar/publicacion/en/$word?"      (controller: "search", action:"search"){searchType="REGION";type="POST"; mappingName="searcherSearchPOSTByREGION"}
        name de_searcherSearchPOSTByREGION:"/suche/beitraege/von/$word?"      (controller: "search", action:"search"){searchType="REGION";type="POST"; mappingName="searcherSearchPOSTByREGION"}
        name ca_searcherSearchPOSTByREGION:"/cerca/proposta/a/$word?"      (controller: "search", action:"search"){searchType="REGION";type="POST"; mappingName="searcherSearchPOSTByREGION"}

        name searcherSearchDEBATE:   "/search/debate"            (controller: "search", action:"search"){type="DEBATE"; mappingName="searcherSearchDEBATE"}
        name en_searcherSearchDEBATE:"/search/debate/$word?"     (controller: "search", action:"search"){type="DEBATE"; mappingName="searcherSearchDEBATE"}
        name es_searcherSearchDEBATE:"/buscar/debate/$word?"     (controller: "search", action:"search"){type="DEBATE"; mappingName="searcherSearchDEBATE"}
        name de_searcherSearchDEBATE:"/suche/debatte/$word?"     (controller: "search", action:"search"){type="DEBATE"; mappingName="searcherSearchDEBATE"}
        name ca_searcherSearchDEBATE:"/cerca/debat/$word?"     (controller: "search", action:"search"){type="DEBATE"; mappingName="searcherSearchDEBATE"}

        name searcherSearchDEBATEByCAUSE:   "/search/debate/cause/$word?"          (controller: "search", action:"search"){searchType="CAUSE";type="DEBATE"; mappingName="searcherSearchDEBATEByCAUSE"}
        name en_searcherSearchDEBATEByCAUSE:"/search/debate/cause/$word?"          (controller: "search", action:"search"){searchType="CAUSE";type="DEBATE"; mappingName="searcherSearchDEBATEByCAUSE"}
        name es_searcherSearchDEBATEByCAUSE:"/buscar/debate/causa/$word?"          (controller: "search", action:"search"){searchType="CAUSE";type="DEBATE"; mappingName="searcherSearchDEBATEByCAUSE"}
        name de_searcherSearchDEBATEByCAUSE:"/suche/debatte/themen/$word?"          (controller: "search", action:"search"){searchType="CAUSE";type="DEBATE"; mappingName="searcherSearchDEBATEByCAUSE"}
        name ca_searcherSearchDEBATEByCAUSE:"/cerca/debat/causa/$word?"          (controller: "search", action:"search"){searchType="CAUSE";type="DEBATE"; mappingName="searcherSearchDEBATEByCAUSE"}

        name searcherSearchDEBATEByREGION:   "/search/debate/from/$word?"           (controller: "search", action:"search"){searchType="REGION";type="DEBATE"; mappingName="searcherSearchDEBATEByREGION"}
        name en_searcherSearchDEBATEByREGION:"/search/debate/from/$word?"           (controller: "search", action:"search"){searchType="REGION";type="DEBATE"; mappingName="searcherSearchDEBATEByREGION"}
        name es_searcherSearchDEBATEByREGION:"/buscar/debate/en/$word?"             (controller: "search", action:"search"){searchType="REGION";type="DEBATE"; mappingName="searcherSearchDEBATEByREGION"}
        name de_searcherSearchDEBATEByREGION:"/suche/debatte/von/$word?"             (controller: "search", action:"search"){searchType="REGION";type="DEBATE"; mappingName="searcherSearchDEBATEByREGION"}
        name ca_searcherSearchDEBATEByREGION:"/cerca/debat/a/$word?"             (controller: "search", action:"search"){searchType="REGION";type="DEBATE"; mappingName="searcherSearchDEBATEByREGION"}


        name searcherSearchEVENT:   "/search/event"             (controller: "search", action:"search"){type="EVENT"; mappingName="searcherSearchEVENT"}
        name en_searcherSearchEVENT:"/search/event/$word?"      (controller: "search", action:"search"){type="EVENT"; mappingName="searcherSearchEVENT"}
        name es_searcherSearchEVENT:"/buscar/evento/$word?"     (controller: "search", action:"search"){type="EVENT"; mappingName="searcherSearchEVENT"}
        name de_searcherSearchEVENT:"/suche/ereignis/$word?"     (controller: "search", action:"search"){type="EVENT"; mappingName="searcherSearchEVENT"}
        name ca_searcherSearchEVENT:"/cerca/esdeveniment/$word?"     (controller: "search", action:"search"){type="EVENT"; mappingName="searcherSearchEVENT"}

        name searcherSearchEVENTByCAUSE:   "/search/event/cause/$word?"          (controller: "search", action:"search"){searchType="CAUSE";type="EVENT"; mappingName="searcherSearchEVENTByCAUSE"}
        name en_searcherSearchEVENTByCAUSE:"/search/event/cause/$word?"          (controller: "search", action:"search"){searchType="CAUSE";type="EVENT"; mappingName="searcherSearchEVENTByCAUSE"}
        name es_searcherSearchEVENTByCAUSE:"/buscar/evento/causa/$word?"         (controller: "search", action:"search"){searchType="CAUSE";type="EVENT"; mappingName="searcherSearchEVENTByCAUSE"}
        name de_searcherSearchEVENTByCAUSE:"/suche/ereignis/themen/$word?"         (controller: "search", action:"search"){searchType="CAUSE";type="EVENT"; mappingName="searcherSearchEVENTByCAUSE"}
        name ca_searcherSearchEVENTByCAUSE:"/cerca/esdeveniment/causa/$word?"         (controller: "search", action:"search"){searchType="CAUSE";type="EVENT"; mappingName="searcherSearchEVENTByCAUSE"}

        name searcherSearchEVENTByREGION:   "/search/event/from/$word?"          (controller: "search", action:"search"){searchType="REGION";type="EVENT"; mappingName="searcherSearchEVENTByREGION"}
        name en_searcherSearchEVENTByREGION:"/search/event/from/$word?"          (controller: "search", action:"search"){searchType="REGION";type="EVENT"; mappingName="searcherSearchEVENTByREGION"}
        name es_searcherSearchEVENTByREGION:"/buscar/evento/en/$word?"           (controller: "search", action:"search"){searchType="REGION";type="EVENT"; mappingName="searcherSearchEVENTByREGION"}
        name de_searcherSearchEVENTByREGION:"/suche/ereignis/von/$word?"           (controller: "search", action:"search"){searchType="REGION";type="EVENT"; mappingName="searcherSearchEVENTByREGION"}
        name ca_searcherSearchEVENTByREGION:"/cerca/esdeveniment/a/$word?"           (controller: "search", action:"search"){searchType="REGION";type="EVENT"; mappingName="searcherSearchEVENTByREGION"}

        name searcherSearchSURVEY:   "/search/survey"               (controller: "search", action:"search"){type="SURVEY"; mappingName="searcherSearchSURVEY"}
        name en_searcherSearchSURVEY:"/search/survey/$word?"        (controller: "search", action:"search"){type="SURVEY"; mappingName="searcherSearchSURVEY"}
        name es_searcherSearchSURVEY:"/buscar/encuesta/$word?"      (controller: "search", action:"search"){type="SURVEY"; mappingName="searcherSearchSURVEY"}
        name de_searcherSearchSURVEY:"/suche/umfrage/$word?"      (controller: "search", action:"search"){type="SURVEY"; mappingName="searcherSearchSURVEY"}
        name ca_searcherSearchSURVEY:"/cerca/enquesta/$word?"      (controller: "search", action:"search"){type="SURVEY"; mappingName="searcherSearchSURVEY"}

        name searcherSearchSURVEYByCAUSE:   "/search/survey/cause/$word?"      (controller: "search", action:"search"){searchType="CAUSE";type="SURVEY"; mappingName="searcherSearchSURVEYByCAUSE"}
        name en_searcherSearchSURVEYByCAUSE:"/search/survey/cause/$word?"      (controller: "search", action:"search"){searchType="CAUSE";type="SURVEY"; mappingName="searcherSearchSURVEYByCAUSE"}
        name es_searcherSearchSURVEYByCAUSE:"/buscar/encuesta/causa/$word?"    (controller: "search", action:"search"){searchType="CAUSE";type="SURVEY"; mappingName="searcherSearchSURVEYByCAUSE"}
        name de_searcherSearchSURVEYByCAUSE:"/suche/umfrage/themen/$word?"    (controller: "search", action:"search"){searchType="CAUSE";type="SURVEY"; mappingName="searcherSearchSURVEYByCAUSE"}
        name ca_searcherSearchSURVEYByCAUSE:"/cerca/enquesta/causa/$word?"    (controller: "search", action:"search"){searchType="CAUSE";type="SURVEY"; mappingName="searcherSearchSURVEYByCAUSE"}

        name searcherSearchSURVEYByREGION:   "/search/survey/from/$word?"      (controller: "search", action:"search"){searchType="REGION";type="SURVEY"; mappingName="searcherSearchSURVEYByREGION"}
        name en_searcherSearchSURVEYByREGION:"/search/survey/from/$word?"      (controller: "search", action:"search"){searchType="REGION";type="SURVEY"; mappingName="searcherSearchSURVEYByREGION"}
        name es_searcherSearchSURVEYByREGION:"/buscar/encuesta/en/$word?"      (controller: "search", action:"search"){searchType="REGION";type="SURVEY"; mappingName="searcherSearchSURVEYByREGION"}
        name de_searcherSearchSURVEYByREGION:"/suche/umfrage/von/$word?"      (controller: "search", action:"search"){searchType="REGION";type="SURVEY"; mappingName="searcherSearchSURVEYByREGION"}
        name ca_searcherSearchSURVEYByREGION:"/cerca/enquesta/a/$word?"      (controller: "search", action:"search"){searchType="REGION";type="SURVEY"; mappingName="searcherSearchSURVEYByREGION"}

        name footerUserGuides:      "/user-guides"          (controller: "footer", action: "footerUserGuides"){mappingName="footerUserGuides"}
        name en_footerUserGuides:   "/user-guides"          (controller: "footer", action: "footerUserGuides"){mappingName="footerUserGuides"}
        name es_footerUserGuides:   "/guias-de-usuario"     (controller: "footer", action: "footerUserGuides"){mappingName="footerUserGuides"}
        name de_footerUserGuides:   "/benutzerhandbuecher"  (controller: "footer", action: "footerUserGuides"){mappingName="footerUserGuides"}
        name ca_footerUserGuides:   "/guies-d-usuari"  (controller: "footer", action: "footerUserGuides"){mappingName="footerUserGuides"}


        /**********************/
        /***** LOGGED URLs ****/ //Language no matters
        /**********************/
        name dashboard:                     "/dashboard" (controller: "dashboard", action:"dashboard")
        name dashboardSkipUploadContacts:   "/dashboard/skipContacts" (controller: "dashboard", action:"skipContacts")
        name dashboardPoliticiansSeeMore:   "/ajax/dashboard/politicians/see-more" (controller: "dashboard", action:"dashboardPoliticians")
        name dashboardCampaignsSeeMore:     "/ajax/dashboard/campaigns/see-more" (controller: "dashboard", action:"dashboardCampaigns")

        name debateCreate:      "/account/debate/new" (controller: "debate"){action = [GET: "create", POST: "saveSettings"]}
        name debateEdit:        "/account/$userAlias/d/$urlTitle-$campaignId/edit-settings" (controller: "debate"){action = [GET: "editSettingsStep", POST: "saveSettings"]}
        name debateEditEvent:   "/account/$userAlias/d/$urlTitle-$campaignId/edit-event" (controller: "event"){action = [GET: "editEvent", POST: "updateEvent"]}
        name debateEditContent: "/account/$userAlias/d/$urlTitle-$campaignId/edit-content" (controller: "debate"){action = [GET: "editContentStep", POST: "saveContent"]}

        name debateRemove:      "/ajax/account/$userAlias/d/$urlTitle-$campaignId/remove" (controller: "debate", action: "remove")
        name debateShow:        "/$userAlias/$urlTitle-$campaignId"         (controller: "campaign", action: "show"){constraints{userAlias(validator:{!UrlMappings.RESERVED_PATHS.contains(it)})}}
                                "/$userAlias/-$campaignId"                  (controller: "campaign", action: "show"){constraints{userAlias(validator:{!UrlMappings.RESERVED_PATHS.contains(it)})}}

        name debateProposalNew:             "/ajax/addProposal"(controller: "debateProposal", action: "addProposal")
        name debateProposalDelete:          "/ajax/deleteProposal"(controller: "debateProposal", action: "deleteProposal")
        name debateProposalPin:             "/ajax/pinProposal"(controller: "debateProposal", action: "pinProposal")
        name debateProposalLike:            "/ajax/likeProposal"(controller: "debateProposal", action: "likeProposal")
        name debateProposalComment:         "/ajax/proposalComment/add"(controller: "debateProposal", action: "addComment")
        name debateProposalDeleteComment:   "/ajax/proposalComment/delete"(controller: "debateProposal", action: "deleteComment")
        name debateProposalVoteComment:     "/ajax/proposalComment/vote"(controller: "debateProposal", action: "voteComment")

        name eventBookTicket:           "/ajax/$userAlias/event/$urlTitle-$campaignId/book"(controller:"event", action: "bookTicket")
        name eventConfirmAssistance:    "/account/event/$campaignId/confirm"(controller:"event", action: "checkIn")
        name eventAssistanceReport:     "/ajax/account/event/$campaignId/report"(controller:"event", action: "sendReport")

        name postLike:              "/ajax/likePost"(controller: "post", action: "likePost")
        name postRemove:            "/ajax/account/$userAlias/p/$urlTitle-$campaignId/remove" (controller: "post", action: "remove")
        name postCreate:            "/account/post/new" (controller: "post"){action = [GET: "create", POST: "saveSettings"]}
        name postEdit:              "/account/$userAlias/p/$urlTitle-$campaignId/edit-settings" (controller: "post"){action = [GET: "editSettingsStep", POST: "saveSettings"]}
        name postEditEvent:         "/account/$userAlias/p/$urlTitle-$campaignId/edit-event" (controller: "event"){action = [GET: "editEvent", POST: "updateEvent"]}
        name postEditContent:       "/account/$userAlias/p/$urlTitle-$campaignId/edit-content" (controller: "post"){action = [GET: "editContentStep", POST: "saveContent"]}
        name postShow:              "/$userAlias/$urlTitle-$campaignId"           (controller: "campaign", action: "show"){constraints{userAlias(validator:{!UrlMappings.RESERVED_PATHS.contains(it)})}}
                                    "/$userAlias/-$campaignId"                    (controller: "campaign", action: "show"){constraints{userAlias(validator:{!UrlMappings.RESERVED_PATHS.contains(it)})}}

        name surveyRemove:          "/ajax/account/$userAlias/s/$urlTitle-$campaignId/remove" (controller: "survey", action: "remove")
        name surveyCreate:          "/account/survey/new" (controller: "survey"){action = [GET: "create", POST: "saveSettings"]}
        name surveyEditSettings:    "/account/$userAlias/s/$urlTitle-$campaignId/edit-settings" (controller: "survey"){action = [GET: "editSettingsStep", POST: "saveSettings"]}
        name surveyEditContent:     "/account/$userAlias/s/$urlTitle-$campaignId/edit-content" (controller: "survey"){action = [GET: "editContentStep", POST: "saveContent"]}
        name surveyEditQuestions:   "/account/$userAlias/s/$urlTitle-$campaignId/edit-questions" (controller: "survey"){action = [GET: "editQuestionsStep", POST: "saveQuestions"]}
        name surveyShow:            "/$userAlias/$urlTitle-$campaignId"                          (controller: "campaign", action: "show"){constraints{userAlias(validator:{!UrlMappings.RESERVED_PATHS.contains(it)})}}
        name surveySaveAnswer:      "/ajax/$userAlias/$urlTitle-$campaignId/saveAnswer"          (controller: "survey", action: "saveAnswer"){constraints{userAlias(validator:{!UrlMappings.RESERVED_PATHS.contains(it)})}}

        name eventCreate:           "/account/event/new" (controller: "event"){action = [GET: "create", POST: "saveSettings"]}
        // REDIRECTS (OLD URLS) - DEPRECATED

        name campaignShow:          "/$userAlias/$urlTitle-$campaignId" (controller: "campaign", action: "show"){constraints{userAlias(validator:{!UrlMappings.RESERVED_PATHS.contains(it)})}}

        name widgetJs:      "/widget.js"(controller: "widget", action:"kuorumWidgetjs")
        name widgetRatePolitician:     "/widget/ratePolitician" (controller: "rating", action:"widgetRatePolitician")
        name widgetComparative:        "/widget/comparation"    (controller: "rating", action:"widgetComparativePoliticianInfo")


        name userShow:              "/$userAlias"           (controller: "kuorumUser", action: "show") {constraints{userAlias(validator:{!UrlMappings.RESERVED_PATHS.contains(it)})}}
        name secUserShow:           "/sec/$userAlias"       (controller: "kuorumUser", action: "secShow")


        name userFollowers:     "/ajax/$userAlias/followers" (controller: "kuorumUser", action: "userFollowers")
        name userFollowing:     "/ajax/$userAlias/following"  (controller: "kuorumUser", action: "userFollowing")
        name userUnsubscribe:   "/unsubscribe/$userId"  (controller: "contacts"){action=[GET:"unsubscribe", POST:"unsubscribeConfirm"]}

        name bulkActionRemoveContactsAjax:          "/ajax/contact/remove" (controller:"contacts", action: "removeContactsBulkAction")
        name bulkActionAddTagsContactsAjax:         "/ajax/contact/addTags" (controller:"contacts", action: "addTagsBulkAction")
        name bulkActionRemoveTagsContactsAjax:      "/ajax/contact/removeTags" (controller: "contacts", action: "removeTagsBulkAction")

        name userFollowAndRegister:          "/$userAlias/subscribe" (controller: "kuorumUser", action: "subscribeTo")
        name ajaxRegisterContact:            "/ajax/contact"(controller: "register", action: "contactRegister");

        name userRate:                  "/ajax/$userAlias/rate"(controller: "rating", action:"ratePolitician")
        name userHistoricRate:          "/ajax/$userAlias/historicRate"(controller: "rating", action:"historicPoliticianRate")
        name userLoadRate:              "/ajax/$userAlias/loadRate"(controller: "rating", action:"loadRating")
        name comparingPoliticianRate:   "/ajax/user/compareRate"(controller: "rating", action:"comparingPoliticianRateData")

        name suggestSearcher:       "/ajax/search/suggestions/all"(controller: "search", action:"suggest")
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

        name customProcessRegisterStep2:            "/edit-profile/sign-up/step2"               (controller: "customRegister"){action = [GET:"step2", POST:"step2Save"]}
        name customProcessRegisterStep3:            "/edit-profile/sign-up/step3"               (controller: "customRegister", action :"step3")
        name customProcessRegisterDomainValidation: "/edit-profile/sign-up/domain-validation"   (controller: "customRegister"){action = [GET:"stepDomainValidation", POST:"stepDomainValidationSave"]}

        name profileValidByDomainChecker:      "/ajax/edit-profile/domain-valid-cheker"  (controller: "profile", action :"domainUserValidChecker")
        name profileValidByDomainValidate:     "/ajax/edit-profile/domain-validate"      (controller: "profile", action :"validateUser")


        name profileMailing : "/notifications/mailing" (controller: "profile", action:"showUserEmails")

        name causeSupport:         "/ajax/cause/$causeName/support" (controller:"causes", action: "supportCause")

        name ajaxHeadNotificationsChecked:  "/ajax/notificaiones/check"(controller:"notification", action:"notificationChecked")
        name ajaxHeadNotificationsSeeMore:  "/ajax/notificaiones/seeMore"(controller:"notification", action:"notificationSeeMore")
        name ajaxHeadMessagesChecked:       "/ajax/mensajes/check"(controller:"layouts", action:"headNotificationsChecked")
        name ajaxFollow:                    "/ajax/kuorumUser/follow"(controller:"kuorumUser", action:"follow")
        name ajaxUnFollow:                  "/ajax/kuorumUser/unFollow"(controller:"kuorumUser", action:"unFollow")
        name ajaxRequestPolitician:         "/ajax/politico/solicitud-kuorum"(controller:"kuorumUser", action:"follow")
        name ajaxCropImage:                 "/ajax/file/crop"(controller:"file", action:"cropImage")
        name ajaxUploadFile:                "/ajax/file/upload" (controller:'file', action:"uploadImage")
        name ajaxUploadFilePDF:             "/ajax/file/uploadPDF" (controller:'file', action:"uploadPDF")

        name adminPrincipal:                        "/sec/admin"                        (controller:"admin", action: "index")
        name adminTestMail:                         "/sec/admin/mailing/test"           (controller:"mailTesting", action: "index")
        name adminSearcherIndex:                    "/sec/admin/searcher/indexar"       (controller:"admin", action: "solrIndex")
        name adminSearcherFullIndex:                "/sec/admin/searcher/full-index"    (controller:"admin", action:"fullIndex")
        name adminSearcherFullIndex:                "/sec/admin/searcher/full-index"    (controller:"admin", action:"fullIndex")
        name adminDomainConfig:                     "/sec/admin/domain"                 (controller:"admin"){action=[GET:"domainConfig",POST:"domainConfigSave"]}
        name adminDomainConfigLanding:              "/sec/admin/domain/landing"         (controller:"admin"){action=[GET:"editLandingInfo",POST:"editLandingInfoSave"]}
        name adminDomainConfigLegalInfo:            "/sec/admin/domain/editLegalInfo"   (controller:"admin"){action=[GET:"editLegalInfo",POST:"updateLegalInfo"]}
        name adminDomainConfigUploadLogo:           "/sec/admin/domain/uploadLogo"      (controller:"admin"){action=[GET:"editLogo",POST:"uploadLogo"]}
        name adminDomainConfigUploadCarouselImages:  "/sec/admin/domain/editCarousel"   (controller:"admin"){action=[GET:"editCarousel",POST:"uploadCarousel"]}


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

        name ajaxDeleteRecommendedUser: "/ajax/kuorumUser/deleteRecommendedUser"(controller: 'recommendedUserInfo', action: 'deleteRecommendedUser')

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
        name politicianContactImportError:              "/account/contacts/import/error" (controller:"contacts", action: "importError")
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
        name politicianMassMailingShow:                 "/account/mass-mailing/$campaignId" (controller:"newsletter"){ action=[GET:"showCampaign", POST:'updateCampaign']}
        name politicianMassMailingSendTest:             "/account/mass-mailing/$campaignId/test" (controller:"newsletter", action: "sendMassMailingTest")
        name politicianMassMailingRemove:               "/ajax/account/mass-mailing/$campaignId/remove" (controller:"newsletter", action:"removeCampaign")
        name politicianMassMailingTrackEvents:          "/ajax/account/mass-mailing/$newsletterId/trackEvents" (controller:"newsletter", action: "showTrackingMails")
        name politicianMassMailingTrackEventsReport:    "/ajax/account/mass-mailing/$newsletterId/trackEvents/report" (controller:"newsletter", action: "sendReport")
        name politicianMassMailingHtml:                 "/account/mass-mailing/$campaignId/html" (controller:"newsletter", action: "showMailCampaign")
        name politicianMassMailingSaveTimeZone:         "/account/mass-mailing/saveTimeZone" (controller: "newsletter"){action = [POST:"saveTimeZone"]}
        name politicianCampaignStatsShow:               "/account/campaign/$campaignId" (controller:"newsletter", action:"showCampaignStats")
        name politicianMassMailingDebateStatsReport:    "/ajax/account/debate/$campaignId/report" (controller:"debate", action: "sendReport")
        name politicianMassMailingSurveyStatsReport:    "/ajax/account/survey/$campaignId/report" (controller:"survey", action: "sendReport")

        name politicianTeamManagement:                  "/account/team-management" (controller:"politician", action: "betaTesterPage")

        "/account/contacts/oauth/$provider/success" (controller: "contactsOAuth", action: "onSuccess")
        "/account/contacts/oauth/$provider/failure" (controller: "contactsOAuth", action: "onFailure")

        "/googleContacts/loadContactsFromGoogle" (controller: "googleContacts", action: "loadContactsFromGoogle")

        "/sec/admin/updateMailChimp" (controller: "admin", action: "updateMailChimp")

        /**********************/
        /***** DEPRECATED *****/
        /**********************/

        name tourStart:           "/tour" (controller:"tour", action: "index")
        name tour_dashboard:      "/tour/dashboard" (controller:"tour", action: "tour_dashboard")

        /**********************/
        /*** END DEPRECATED ***/
        /**********************/

        name sitemapIndex:  "/sitemap" (controller: "siteMap", action: "sitemapIndex")

        name sitemapLandings:   "/sitemap/landings" (controller: "siteMap", action: "sitemapLandings")
        name sitemapFooters:    "/sitemap/footers"   (controller: "siteMap", action: "sitemapFooters")
        name sitemapSearchs:    "/sitemap/searchs"  (controller: "siteMap", action: "sitemapSearchs")
        name sitemapUsersIdx:   "/sitemap/users"    (controller: "siteMap", action: "sitemapUsersIndex")
        name sitemapUsers:      "/sitemap/users/$year/$month"    (controller: "siteMap", action: "sitemapUsers")


        // TODO: REVIEW BASIC URL -> RegisterController:sendConfirmationEmail || Reset password and others
        "/$controller/$action?/$id?"{
            constraints {
                // apply constraints here
            }
        }


        "403" (controller: "error", action: "forbidden")
        "404" (controller: "error", action: "notFound")
        "401" (controller: "error", action: "notAuthorized")


        Environment.executeForCurrentEnvironment {
            development {
                "500" (controller: "error", action: "kuorumExceptionHandler", exception: KuorumException)
                "500" (controller: "error", action: "notAuthorized", exception: AccessDeniedException)
                "500" (controller: "error", action: "internalError")
//                "500"(view:'/error')
            }
            test{
                "500" (controller: "error", action: "kuorumExceptionHandler", exception: KuorumException)
                "500" (controller: "error", action: "notAuthorized", exception: AccessDeniedException)
                "500" (controller: "error", action: "internalError")
            }
            production{
                "500" (controller: "error", action: "kuorumExceptionHandler", exception: KuorumException)
                "500" (controller: "error", action: "notAuthorized", exception: AccessDeniedException)
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
