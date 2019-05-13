import grails.util.Environment
import kuorum.core.exception.KuorumException
import org.springframework.security.access.AccessDeniedException

class UrlMappings {

    static excludes = ['/robots.txt', '/error-page/402.html']

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
                                    // THIS URL IS USED BY kuorum.org to redirect to domain configuration the first time
                                    "/sec/admin/domain/config/registering"(controller: "register", action:"registerRRSSOAuthAjax") {redirectAdminConfig=true}

        name registerSuccess:       "/sign-up/success"          (controller: "register",action:"registerSuccess"){mappingName="registerSuccess"}
        name en_registerSuccess:    "/sign-up/success"          (controller: "register",action:"registerSuccess"){mappingName="registerSuccess"}
        name es_registerSuccess:    "/registro/satisfactorio"   (controller: "register",action:"registerSuccess"){mappingName="registerSuccess"}
        name de_registerSuccess:    "/registrierung/erfolgreich"   (controller: "register",action:"registerSuccess"){mappingName="registerSuccess"}
        name ca_registerSuccess:    "/registre/satisfactori"   (controller: "register",action:"registerSuccess"){mappingName="registerSuccess"}

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

        name searcherSearchPARTICIPATORY_BUDGET:   "/search/participatory-budget"                  (controller: "search", action:"search"){type="PARTICIPATORY_BUDGET"; mappingName="searcherSearchPARTICIPATORY_BUDGET"}
        name en_searcherSearchPARTICIPATORY_BUDGET:"/search/participatory-budget/$word?"           (controller: "search", action:"search"){type="PARTICIPATORY_BUDGET"; mappingName="searcherSearchPARTICIPATORY_BUDGET"}
        name es_searcherSearchPARTICIPATORY_BUDGET:"/buscar/presupuesto-participativo/$word?"      (controller: "search", action:"search"){type="PARTICIPATORY_BUDGET"; mappingName="searcherSearchPARTICIPATORY_BUDGET"}
        name de_searcherSearchPARTICIPATORY_BUDGET:"/suche/burgerhaushalt/$word?"                  (controller: "search", action:"search"){type="PARTICIPATORY_BUDGET"; mappingName="searcherSearchPARTICIPATORY_BUDGET"}
        name ca_searcherSearchPARTICIPATORY_BUDGET:"/cerca/pressupost-participatiu/$word?" (controller: "search", action:"search"){type="PARTICIPATORY_BUDGET"; mappingName="searcherSearchPARTICIPATORY_BUDGET"}

        name searcherSearchPARTICIPATORY_BUDGETByCAUSE:   "/search/participatory-budget/cause/$word?"      (controller: "search", action:"search"){searchType="CAUSE";type="PARTICIPATORY_BUDGET"; mappingName="searcherSearchPARTICIPATORY_BUDGETByCAUSE"}
        name en_searcherSearchPARTICIPATORY_BUDGETByCAUSE:"/search/participatory-budget/cause/$word?"      (controller: "search", action:"search"){searchType="CAUSE";type="PARTICIPATORY_BUDGET"; mappingName="searcherSearchPARTICIPATORY_BUDGETByCAUSE"}
        name es_searcherSearchPARTICIPATORY_BUDGETByCAUSE:"/buscar/presupuesto-participativo/causa/$word?"    (controller: "search", action:"search"){searchType="CAUSE";type="PARTICIPATORY_BUDGET"; mappingName="searcherSearchPARTICIPATORY_BUDGETByCAUSE"}
        name de_searcherSearchPARTICIPATORY_BUDGETByCAUSE:"/suche/burgerhaushalt/themen/$word?"    (controller: "search", action:"search"){searchType="CAUSE";type="PARTICIPATORY_BUDGET"; mappingName="searcherSearchPARTICIPATORY_BUDGETByCAUSE"}
        name ca_searcherSearchPARTICIPATORY_BUDGETByCAUSE:"/cerca/pressupost-participatiu/causa/$word?"    (controller: "search", action:"search"){searchType="CAUSE";type="PARTICIPATORY_BUDGET"; mappingName="searcherSearchPARTICIPATORY_BUDGETByCAUSE"}

        name searcherSearchPARTICIPATORY_BUDGETByREGION:   "/search/participatory-budget/from/$word?"      (controller: "search", action:"search"){searchType="REGION";type="PARTICIPATORY_BUDGET"; mappingName="searcherSearchPARTICIPATORY_BUDGETByREGION"}
        name en_searcherSearchPARTICIPATORY_BUDGETByREGION:"/search/participatory-budget/from/$word?"      (controller: "search", action:"search"){searchType="REGION";type="PARTICIPATORY_BUDGET"; mappingName="searcherSearchPARTICIPATORY_BUDGETByREGION"}
        name es_searcherSearchPARTICIPATORY_BUDGETByREGION:"/buscar/presupuesto-participativo/en/$word?"      (controller: "search", action:"search"){searchType="REGION";type="PARTICIPATORY_BUDGET"; mappingName="searcherSearchPARTICIPATORY_BUDGETByREGION"}
        name de_searcherSearchPARTICIPATORY_BUDGETByREGION:"/suche/burgerhaushalt/von/$word?"      (controller: "search", action:"search"){searchType="REGION";type="PARTICIPATORY_BUDGET"; mappingName="searcherSearchPARTICIPATORY_BUDGETByREGION"}
        name ca_searcherSearchPARTICIPATORY_BUDGETByREGION:"/cerca/pressupost-participatiu/a/$word?"      (controller: "search", action:"search"){searchType="REGION";type="PARTICIPATORY_BUDGET"; mappingName="searcherSearchPARTICIPATORY_BUDGETByREGION"}


        name searcherSearchDISTRICT_PROPOSAL:   "/search/proposal"                  (controller: "search", action:"search"){type="DISTRICT_PROPOSAL"; mappingName="searcherSearchDISTRICT_PROPOSAL"}
        name en_searcherSearchDISTRICT_PROPOSAL:"/search/proposal/$word?"           (controller: "search", action:"search"){type="DISTRICT_PROPOSAL"; mappingName="searcherSearchDISTRICT_PROPOSAL"}
        name es_searcherSearchDISTRICT_PROPOSAL:"/buscar/propuesta/$word?"      (controller: "search", action:"search"){type="DISTRICT_PROPOSAL"; mappingName="searcherSearchDISTRICT_PROPOSAL"}
        name de_searcherSearchDISTRICT_PROPOSAL:"/suche/vorschlag/$word?"                  (controller: "search", action:"search"){type="DISTRICT_PROPOSAL"; mappingName="searcherSearchDISTRICT_PROPOSAL"}
        name ca_searcherSearchDISTRICT_PROPOSAL:"/cerca/publicacio/$word?" (controller: "search", action:"search"){type="DISTRICT_PROPOSAL"; mappingName="searcherSearchDISTRICT_PROPOSAL"}

        name searcherSearchDISTRICT_PROPOSALByCAUSE:   "/search/proposal/cause/$word?"      (controller: "search", action:"search"){searchType="CAUSE";type="DISTRICT_PROPOSAL"; mappingName="searcherSearchDISTRICT_PROPOSALByCAUSE"}
        name en_searcherSearchDISTRICT_PROPOSALByCAUSE:"/search/proposal/cause/$word?"      (controller: "search", action:"search"){searchType="CAUSE";type="DISTRICT_PROPOSAL"; mappingName="searcherSearchDISTRICT_PROPOSALByCAUSE"}
        name es_searcherSearchDISTRICT_PROPOSALByCAUSE:"/buscar/propuesta/causa/$word?"    (controller: "search", action:"search"){searchType="CAUSE";type="DISTRICT_PROPOSAL"; mappingName="searcherSearchDISTRICT_PROPOSALByCAUSE"}
        name de_searcherSearchDISTRICT_PROPOSALByCAUSE:"/suche/vorschlag/themen/$word?"    (controller: "search", action:"search"){searchType="CAUSE";type="DISTRICT_PROPOSAL"; mappingName="searcherSearchDISTRICT_PROPOSALByCAUSE"}
        name ca_searcherSearchDISTRICT_PROPOSALByCAUSE:"/cerca/publicacio/causa/$word?"    (controller: "search", action:"search"){searchType="CAUSE";type="DISTRICT_PROPOSAL"; mappingName="searcherSearchDISTRICT_PROPOSALByCAUSE"}

        name searcherSearchDISTRICT_PROPOSALByREGION:   "/search/proposal/from/$word?"      (controller: "search", action:"search"){searchType="REGION";type="DISTRICT_PROPOSAL"; mappingName="searcherSearchDISTRICT_PROPOSALByREGION"}
        name en_searcherSearchDISTRICT_PROPOSALByREGION:"/search/proposal/from/$word?"      (controller: "search", action:"search"){searchType="REGION";type="DISTRICT_PROPOSAL"; mappingName="searcherSearchDISTRICT_PROPOSALByREGION"}
        name es_searcherSearchDISTRICT_PROPOSALByREGION:"/buscar/propuesta/en/$word?"      (controller: "search", action:"search"){searchType="REGION";type="DISTRICT_PROPOSAL"; mappingName="searcherSearchDISTRICT_PROPOSALByREGION"}
        name de_searcherSearchDISTRICT_PROPOSALByREGION:"/suche/vorschlag/von/$word?"      (controller: "search", action:"search"){searchType="REGION";type="DISTRICT_PROPOSAL"; mappingName="searcherSearchDISTRICT_PROPOSALByREGION"}
        name ca_searcherSearchDISTRICT_PROPOSALByREGION:"/cerca/publicacio/a/$word?"      (controller: "search", action:"search"){searchType="REGION";type="DISTRICT_PROPOSAL"; mappingName="searcherSearchDISTRICT_PROPOSALByREGION"}

        name searcherSearchPETITION:   "/search/petition"                  (controller: "search", action:"search"){type="PETITION"; mappingName="searcherSearchPETITION"}
        name en_searcherSearchPETITION:"/search/petition/$word?"           (controller: "search", action:"search"){type="PETITION"; mappingName="searcherSearchPETITION"}
        name es_searcherSearchPETITION:"/buscar/peticion/$word?"      (controller: "search", action:"search"){type="PETITION"; mappingName="searcherSearchPETITION"}
        name de_searcherSearchPETITION:"/suche/petition/$word?"                  (controller: "search", action:"search"){type="PETITION"; mappingName="searcherSearchPETITION"}
        name ca_searcherSearchPETITION:"/cerca/peticio/$word?" (controller: "search", action:"search"){type="PETITION"; mappingName="searcherSearchPETITION"}

        name searcherSearchPETITIONByCAUSE:   "/search/petition/cause/$word?"      (controller: "search", action:"search"){searchType="CAUSE";type="PETITION"; mappingName="searcherSearchPETITIONByCAUSE"}
        name en_searcherSearchPETITIONByCAUSE:"/search/petition/cause/$word?"      (controller: "search", action:"search"){searchType="CAUSE";type="PETITION"; mappingName="searcherSearchPETITIONByCAUSE"}
        name es_searcherSearchPETITIONByCAUSE:"/buscar/peticion/causa/$word?"    (controller: "search", action:"search"){searchType="CAUSE";type="PETITION"; mappingName="searcherSearchPETITIONByCAUSE"}
        name de_searcherSearchPETITIONByCAUSE:"/suche/petition/themen/$word?"    (controller: "search", action:"search"){searchType="CAUSE";type="PETITION"; mappingName="searcherSearchPETITIONByCAUSE"}
        name ca_searcherSearchPETITIONByCAUSE:"/cerca/peticio/causa/$word?"    (controller: "search", action:"search"){searchType="CAUSE";type="PETITION"; mappingName="searcherSearchPETITIONByCAUSE"}

        name searcherSearchPETITIONByREGION:   "/search/petition/from/$word?"      (controller: "search", action:"search"){searchType="REGION";type="PETITION"; mappingName="searcherSearchPETITIONByREGION"}
        name en_searcherSearchPETITIONByREGION:"/search/petition/from/$word?"      (controller: "search", action:"search"){searchType="REGION";type="PETITION"; mappingName="searcherSearchPETITIONByREGION"}
        name es_searcherSearchPETITIONByREGION:"/buscar/peticion/en/$word?"      (controller: "search", action:"search"){searchType="REGION";type="PETITION"; mappingName="searcherSearchPETITIONByREGION"}
        name de_searcherSearchPETITIONByREGION:"/suche/petition/von/$word?"      (controller: "search", action:"search"){searchType="REGION";type="PETITION"; mappingName="searcherSearchPETITIONByREGION"}
        name ca_searcherSearchPETITIONByREGION:"/cerca/peticio/a/$word?"      (controller: "search", action:"search"){searchType="REGION";type="PETITION"; mappingName="searcherSearchPETITIONByREGION"}

        /**********************/
        /***** LOGGED URLs ****/ //Language no matters
        /**********************/
        name dashboard:                     "/dashboard" (controller: "dashboard", action:"dashboard")
        name dashboardSkipUploadContacts:   "/dashboard/skipContacts" (controller: "dashboard", action:"skipContacts")
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


        name surveyRemove:          "/ajax/account/$userAlias/s/$urlTitle-$campaignId/remove" (controller: "survey", action: "remove")
        name surveyCreate:          "/account/survey/new" (controller: "survey"){action = [GET: "create", POST: "saveSettings"]}
        name surveyEditSettings:    "/account/$userAlias/s/$urlTitle-$campaignId/edit-settings" (controller: "survey"){action = [GET: "editSettingsStep", POST: "saveSettings"]}
        name surveyEditContent:     "/account/$userAlias/s/$urlTitle-$campaignId/edit-content" (controller: "survey"){action = [GET: "editContentStep", POST: "saveContent"]}
        name surveyEditQuestions:   "/account/$userAlias/s/$urlTitle-$campaignId/edit-questions" (controller: "survey"){action = [GET: "editQuestionsStep", POST: "saveQuestions"]}
        name surveyShow:            "/$userAlias/$urlTitle-$campaignId"                          (controller: "campaign", action: "show"){constraints{userAlias(validator:{!UrlMappings.RESERVED_PATHS.contains(it)})}}
        name surveySaveAnswer:      "/ajax/$userAlias/$urlTitle-$campaignId/saveAnswer"          (controller: "survey", action: "saveAnswer"){constraints{userAlias(validator:{!UrlMappings.RESERVED_PATHS.contains(it)})}}

        name eventCreate:           "/account/event/new" (controller: "event"){action = [GET: "create", POST: "saveSettings"]}

        name participatoryBudgetCreate:            "/account/participatory-budget/new" (controller: "participatoryBudget"){action = [GET: "create", POST: "saveSettings"]}
        name participatoryBudgetEditSettings:      "/account/$userAlias/pb/$urlTitle-$campaignId/edit-settings" (controller: "participatoryBudget"){action = [GET: "editSettingsStep", POST: "saveSettings"]}
        name participatoryBudgetEditDistricts:     "/account/$userAlias/pb/$urlTitle-$campaignId/edit-districts"(controller: "participatoryBudget"){action = [GET: "editDistricts", POST: "saveDistricts"]}
        name participatoryBudgetEditDeadlines:     "/account/$userAlias/pb/$urlTitle-$campaignId/edit-deadlines"(controller: "participatoryBudget"){action = [GET: "editDeadlines", POST: "saveDeadlines"]}
        name participatoryBudgetEditContent:       "/account/$userAlias/pb/$urlTitle-$campaignId/edit-content"  (controller: "participatoryBudget"){action = [GET: "editContentStep", POST: "saveContent"]}
        name participatoryBudgetRemove:            "/ajax/account/$userAlias/pb/$urlTitle-$campaignId/remove"    (controller: "participatoryBudget", action:"remove")
        name participatoryBudgetShow:              "/$userAlias/$urlTitle-$campaignId"           (controller: "campaign", action: "show"){constraints{userAlias(validator:{!UrlMappings.RESERVED_PATHS.contains(it)})}}
        name participatoryBudgetList:              "/ajax/account/participatory-budgets" (controller: "participatoryBudget", action:"listActiveParticipativeBudgets")
        name participatoryBudgetEditStatus:        "/ajax/account/$userAlias/pb/$urlTitle-$campaignId/edit-status" (controller: "participatoryBudget", action:"editStatus")
        name participatoryBudgetDistrictProposals: "/ajax/$userAlias/$urlTitle-$campaignId/district-$districtId/proposals" (controller: "participatoryBudget", action:"findDistrictProposals")
        name participatoryBudgetDistrictProposalSupport: "/ajax/$userAlias/$participatoryBudgetTitle-$participatoryBudgetId/$urlTitle-$campaignId/support" (controller: "participatoryBudget", action:"supportDistrictProposal")
        name participatoryBudgetDistrictProposalVote:    "/ajax/$userAlias/$participatoryBudgetTitle-$participatoryBudgetId/$urlTitle-$campaignId/vote"    (controller: "participatoryBudget", action:"voteDistrictProposal")
        name participatoryBudgetDistrictProposalsPagination:      "/ajax/$userAlias/$urlTitle-$campaignId/proposals"                  (controller: "participatoryBudget", action:"paginateParticipatoryBudgetProposalsJson")
        name participatoryBudgetDistrictProposalsTechnicalReview: "/ajax/$userAlias/$urlTitle-$campaignId/proposals/technicalReview"  (controller: "participatoryBudget", action:"updateTechnicalReview")

        name districtProposalCreate:            "/account/$userAlias/pb/$urlTitle-$campaignId/new-proposal"         (controller: "districtProposal"){action = [GET: "create", POST: "saveNewProposal"]}
        name districtProposalCreateContent:     "/account/$userAlias/pb/$urlTitle-$campaignId/new-proposal-content" (controller: "districtProposal"){action = [GET: "createByContent", POST: "saveNewProposalByContent"]}
        name districtProposalEditSettings:      "/account/$userAlias/pb/$participatoryBudgetTitle-$participatoryBudgetId/$urlTitle-$campaignId/edit-settings" (controller: "districtProposal"){action = [GET: "editSettingsStep", POST: "saveSettings"]}
        name districtProposalEditDistrict:      "/account/$userAlias/pb/$participatoryBudgetTitle-$participatoryBudgetId/$urlTitle-$campaignId/edit-district" (controller: "districtProposal"){action = [GET: "editDistrict", POST: "saveDistrict"]}
        name districtProposalEditContent:       "/account/$userAlias/pb/$participatoryBudgetTitle-$participatoryBudgetId/$urlTitle-$campaignId/edit-content" (controller: "districtProposal"){action = [GET: "editContentStep", POST: "saveContent"]}
        name districtProposalShow:              "/$userAlias/$participatoryBudgetTitle-$participatoryBudgetId/$urlTitle-$campaignId"           (controller: "campaign", action: "show"){constraints{userAlias(validator:{!UrlMappings.RESERVED_PATHS.contains(it)})}}
        name districtProposalRemove:            "/ajax/account/$userAlias/pb/$participatoryBudgetTitle-$participatoryBudgetId/$urlTitle-$campaignId/remove" (controller: "districtProposal", action: "remove")

        name petitionSign:              "/ajax/$userAlias/pt/$urlTitle-$campaignId/sign"(controller: "petition", action: "signPetition")
        name petitionRemove:            "/ajax/account/$userAlias/pt/$urlTitle-$campaignId/remove" (controller: "petition", action: "remove")
        name petitionCreate:            "/account/petition/new" (controller: "petition"){action = [GET: "create", POST: "saveSettings"]}
        name petitionEdit:              "/account/$userAlias/pt/$urlTitle-$campaignId/edit-settings" (controller: "petition"){action = [GET: "editSettingsStep", POST: "saveSettings"]}
        name petitionEditContent:       "/account/$userAlias/pt/$urlTitle-$campaignId/edit-content" (controller: "petition"){action = [GET: "editContentStep", POST: "saveContent"]}
        name petitionShow:              "/$userAlias/$urlTitle-$campaignId"           (controller: "campaign", action: "show"){constraints{userAlias(validator:{!UrlMappings.RESERVED_PATHS.contains(it)})}}


        name campaignShow:          "/$userAlias/$urlTitle-$campaignId" (controller: "campaign", action: "show"){constraints{userAlias(validator:{!UrlMappings.RESERVED_PATHS.contains(it)})}}

        name widgetJs:      "/widget.js"(controller: "widget", action:"kuorumWidgetjs")

        name userShow:              "/$userAlias"           (controller: "kuorumUser", action: "show") {constraints{userAlias(validator:{!UrlMappings.RESERVED_PATHS.contains(it)})}}
        name secUserShow:           "/sec/$userAlias"       (controller: "kuorumUser", action: "secShow")


        name userFollowers:     "/ajax/$userAlias/followers" (controller: "kuorumUser", action: "userFollowers")
        name userFollowing:     "/ajax/$userAlias/following"  (controller: "kuorumUser", action: "userFollowing")
        name userUnsubscribe:   "/unsubscribe/$userId"  (controller: "contacts"){action=[GET:"unsubscribe", POST:"unsubscribeConfirm"]}

        name bulkActionRemoveContactsAjax:          "/ajax/contact/remove" (controller:"contacts", action: "removeContactsBulkAction")
        name bulkActionAddTagsContactsAjax:         "/ajax/contact/addTags" (controller:"contacts", action: "addTagsBulkAction")
        name bulkActionRemoveTagsContactsAjax:      "/ajax/contact/removeTags" (controller: "contacts", action: "removeTagsBulkAction")

        name ajaxRegisterContact:            "/ajax/kuorum-user/contact"(controller: "kuorumUser", action: "contactUser")

        name userRate:                  "/ajax/$userAlias/rate"(controller: "rating", action:"ratePolitician")
        name userLoadRate:              "/ajax/$userAlias/loadRate"(controller: "rating", action:"loadRating")

        name suggestSearcher:       "/ajax/search/suggestions/all"(controller: "search", action:"suggest")
        name suggestRegions:        "/ajax/search/suggestions/regions"(controller: "search", action:"suggestRegions")
        name suggestTags:           "/ajax/search/suggestions/tags"(controller: "search", action:"suggestTags")
        name suggestAlias:          "/ajax/search/suggestions/alias"(controller: "search", action:"suggestAlias")

        name profileEditAccountDetails:     "/config/account-details"                                   (controller: "profile"){action =[GET:"editAccountDetails", POST:"updateAccountDetails"]}
        name profileEditUser:               "/edit-profile"                                    (controller: "profile"){action =[GET:"editUser", POST:"editUserSave"]}
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
        name profilePictures:               "/edit-profile/pictures"                           (controller: "profile"){action=[GET:"editPictures", POST: "updatePictures"]}
        name profileNewsletterConfig:       "/config/newsletter-config"                        (controller: "profile"){action=[GET:"editNewsletterConfig", POST: "updateNewsletterConfig"]}

        name customProcessRegisterStep2:            "/edit-profile/sign-up/step2"               (controller: "customRegister"){action = [GET:"step2", POST:"step2Save"]}
        name customProcessRegisterStep3:            "/edit-profile/sign-up/step3"               (controller: "customRegister", action :"step3")
        name customProcessRegisterDomainValidation: "/edit-profile/sign-up/domain-validation"   (controller: "customRegister"){action = [GET:"stepDomainValidation", POST:"stepDomainValidationSave"]}

        name profileValidByDomainChecker:      "/ajax/edit-profile/domain-valid-cheker"  (controller: "profile", action :"domainUserValidChecker")
        name profileValidByDomainValidate:     "/ajax/edit-profile/domain-validate"      (controller: "profile", action :"validateUser")

        name causeSupport:         "/ajax/cause/$causeName/support" (controller:"causes", action: "supportCause")

        name ajaxHeadNotificationsChecked:  "/ajax/notificaiones/check"(controller:"notification", action:"notificationChecked")
        name ajaxHeadNotificationsSeeMore:  "/ajax/notificaiones/seeMore"(controller:"notification", action:"notificationSeeMore")
        name ajaxFollow:                    "/ajax/kuorumUser/follow"(controller:"kuorumUser", action:"follow")
        name ajaxUnFollow:                  "/ajax/kuorumUser/unFollow"(controller:"kuorumUser", action:"unFollow")
        name ajaxRequestPolitician:         "/ajax/politico/solicitud-kuorum"(controller:"kuorumUser", action:"follow")
        name ajaxCropImage:                 "/ajax/file/crop"(controller:"file", action:"cropImage")
        name ajaxUploadFile:                "/ajax/file/upload" (controller:'file', action:"uploadImage")
        name ajaxUploadFilePDF:             "/ajax/file/uploadPDF" (controller:'file', action:"uploadPDF")
        name ajaxUploadCampaignFile:             "/ajax/file/uploadCampaignFile/$userAlias/$urlTitle-$campaignId" (controller:'file', action:"uploadCampaignFile")
        name ajaxDeleteCampaignFile:             "/ajax/file/deleteCampaignFile/$userAlias/$urlTitle-$campaignId" (controller:'file', action:"deleteCampaignFile")

        name adminPrincipal:                        "/sec/admin"                        (controller:"admin", action: "index")
        name adminTestMail:                         "/sec/admin/mailing/test"           (controller:"mailTesting", action: "index")
        name adminSearcherIndex:                    "/sec/admin/searcher/indexar"       (controller:"admin", action: "solrIndex")
        name adminSearcherFullIndex:                "/sec/admin/searcher/full-index"    (controller:"admin", action:"fullIndex")
        name adminSearcherFullIndex:                "/sec/admin/searcher/full-index"    (controller:"admin", action:"fullIndex")
        name adminDomainConfig:                     "/sec/admin/domain"                 (controller:"admin"){action=[GET:"domainConfig",POST:"domainConfigSave"]}
        name adminDomainConfigLanding:              "/sec/admin/domain/landing"         (controller:"admin"){action=[GET:"editLandingInfo",POST:"editLandingInfoSave"]}
        name adminDomainConfigLegalInfo:            "/sec/admin/domain/editLegalInfo"   (controller:"admin"){action=[GET:"editLegalInfo",POST:"updateLegalInfo"]}
        name adminAuthorizedCampaigns:              "/sec/admin/domain/authorizedCampaigns"(controller:"admin"){action=[GET:"editAuthorizedCampaigns",POST:"updateAuthorizedCampaigns"]}
        name adminDomainConfigUploadLogo:           "/sec/admin/domain/uploadLogo"      (controller:"admin"){action=[GET:"editLogo",POST:"uploadLogo"]}
        name adminDomainConfigUploadCarouselImages: "/sec/admin/domain/editCarousel"    (controller:"admin"){action=[GET:"editCarousel",POST:"uploadCarousel"]}
        name adminDomainConfigRelevantCampagins:    "/sec/admin/domain/relevantCampaigns"(controller:"admin"){action=[GET:"editDomainRelevantCampaigns",POST:"updateDomainRelevantCampaigns"]}
        name adminDomainConfigPlan:                 "/sec/admin/domain/update-plan"     (controller:"admin"){action=[GET:"editDomainPlan",POST:"saveNewDomainPlan"]}
        name adminDomainConfigGoogleValidation:     "/sec/admin/domain/google-validation" (controller:"admin", action:"validateDomain")
        name adminRequestEmailSender:               "/sec/admin/domain/requestSender"   (controller: "admin"){action=[GET:"requestedEmailSender", POST: "requestedEmailSenderSend"]}
        name adminEditDomainEmailSender:            "/sec/admin/domain/setSender"       (controller:"admin"){action =[GET:"editDomainEmailSender", POST:"updateDomainEmailSender"]}
        name adminDomainDelete:                     "/sec/admin/domain/delete"          (controller:"admin"){action =[GET:"deleteDomain", POST:"deleteDomainConfirm"]}

        name adminDomainRegisterStep1:              "/sec/admin/domain/config/landing"  (controller:"admin"){action =[GET:"designLandingPage", POST:"saveDesignLandingPage"]}
        name adminDomainRegisterStep2:              "/sec/admin/domain/config/rights"   (controller:"admin"){action =[GET:"userRights", POST:"saveUserRights"]}

        name editorEditUserProfile:                         "/editor/user/$userAlias/edit/profile"                (controller:"editorUser"){action =[GET:"editUser", POST:"updateUser"]}
        name editorKuorumAccountEdit:                       "/editor/user/$userAlias/edit/account-details"        (controller:"editorUser"){action =[GET:"editAdminAccountDetails", POST:"updateAdminAccountDetails"]}
        name editorAdminUserRights:                         "/editor/user/$userAlias/edit/rights"                 (controller:"admin"){action =[GET:"editUserRights", POST:"updateUserRights"]}

        name ajaxDeleteRecommendedUser: "/ajax/kuorumUser/deleteRecommendedUser"(controller: 'recommendedUserInfo', action: 'deleteRecommendedUser')

        name politicianContactProfiling:                "/account/contact-profiling" (controller:"politician", action: "betaTesterPage")
        name politicianContacts:                        "/account/contacts" (controller:"contacts", action: "index")
        name politicianContactsSearch:                  "/ajax/account/contacts" (controller:"contacts", action: "searchContacts")
        name politicianContactExport:                   "/account/contacts/export" (controller:"contacts", action: "exportContacts")
        name politicianContactImport:                   "/account/contacts/import" (controller:"contacts", action: "importContacts")
        name politicianContactImportCSV:                "/account/contacts/import/csv" (controller:"contacts"){action =[GET:"importCSVContacts", POST:"importCSVContactsUpload"]}
        name politicianContactImportCSVSave:            "/account/contacts/import/csv_save" (controller:"contacts", action: "importCSVContactsSave")
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
        name politicianCampaignsUploadImages:           "/ajax/account/campaign/$campaignId/uploadImages" (controller:"file", action: "uploadCampaignImages")
        name politicianCampaignsListImages:             "/ajax/account/campaign/$campaignId/listImages" (controller:"file", action: "getCampaignImages")
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
        name politicianMassMailingParticipatoryBudgetReport:"/ajax/account/participatoryBudget/$campaignId/report" (controller:"participatoryBudget", action: "sendProposalsReport")

        name politicianTeamManagement:                  "/account/team-management" (controller:"politician", action: "betaTesterPage")

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

        "Lost cookie" org.springframework.security.web.authentication.rememberme.CookieTheftException {ex ->
            controller = "error"
            action = "cookieLost"
            exception = ex
        }
    }
}
