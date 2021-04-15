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

        /**********************/
        /***** LOGGED URLs ****/ //Language no matters
        /**********************/
        name dashboard:                     "/dashboard" (controller: "dashboard", action:"dashboard")
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
        name surveyCopy:            "/account/survey/$campaignId/copy" (controller:"survey", action:"copyCampaign")

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
        name campaignCheckGroupValidation:"/ajax/$userAlias/$urlTitle-$campaignId/group/check" (controller: "campaign", action: "checkGroupCampaignValidation")
        name campaignPause:         "/ajax/account/$userAlias/$urlTitle-$campaignId/pause" (controller: "newsletter", action: "pauseCampaign")

        name widgetJs:      "/widget.js"(controller: "widget", action:"kuorumWidgetjs")

        name userShow:              "/$userAlias"           (controller: "kuorumUser", action: "show") {constraints{userAlias(validator:{!UrlMappings.RESERVED_PATHS.contains(it)})}}
        name secUserShow:           "/sec/$userAlias"       (controller: "kuorumUser", action: "secShow")


        name userFollowers:     "/ajax/$userAlias/followers" (controller: "kuorumUser", action: "userFollowers")
        name userFollowing:     "/ajax/$userAlias/following" (controller: "kuorumUser", action: "userFollowing")
        name userUnsubscribe:   "/unsubscribe/$userId"       (controller: "contacts"){action=[GET:"unsubscribe", POST:"unsubscribeConfirm"]}
        name userRecomendations:"/ajax/recommendations"      (controller: "kuorumUser", action: "recommendations")

        name bulkActionRemoveContactsAjax:          "/ajax/contact/remove" (controller:"contacts", action: "removeContactsBulkAction")
        name bulkActionAddTagsContactsAjax:         "/ajax/contact/addTags" (controller:"contacts", action: "addTagsBulkAction")
        name bulkActionRemoveTagsContactsAjax:      "/ajax/contact/removeTags" (controller: "contacts", action: "removeTagsBulkAction")
        name bulkActionGeneratePersonalCodeAjax:      "/ajax/contact/genertePersonalTag" (controller: "contacts", action: "generatePersonalCodeBulkAction")
        name bulkActionRemovePersonalCodeAjax:      "/ajax/contact/removePersonalTag" (controller: "contacts", action: "removePersonalCodeBulkAction")

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

        name customProcessRegisterStep2:          "/edit-profile/sign-up/step2"              (controller: "customRegister"){action = [GET:"step2", POST:"step2Save"]}
        name customProcessRegisterStep3:          "/edit-profile/sign-up/step3"              (controller: "customRegister", action :"step3")

        name campaignValidationLinkCheck:         "/sign-up/census"                          (controller: "campaignValidation"){action = [GET:"step0RegisterWithCensusCode", POST:"step0RegisterWithCensusCodeSave"]}
        name campaignValidationCensus:            "/$userAlias/$urlTitle-$campaignId/validation/census"       (controller: "campaignValidation"){action = [GET:"stepCampaignValidationCensus", POST:"stepCampaignValidationCensusSave"]}
        name campaignValidationCode:              "/$userAlias/$urlTitle-$campaignId/validation/customCode"   (controller: "campaignValidation"){action = [GET:"stepCampaignValidationCustomCode", POST:"stepCampaignValidationCustomCodeSave"]}
        name campaignValidationPhoneNumber:       "/$userAlias/$urlTitle-$campaignId/validation/phone/number" (controller: "campaignValidation"){action = [GET:"stepCampaignValidationPhoneNumber"]}
        name campaignValidationPhoneNumberCode:   "/$userAlias/$urlTitle-$campaignId/validation/phone/code"   (controller: "campaignValidation"){action = [GET:"stepCampaignValidationPhoneCode", POST:"stepCampaignValidationPhoneCodeSave"]}

        name profileDomainValidationChecker:            "/ajax/edit-profile/domain-valid-cheker"            (controller: "profile", action :"domainUserValidChecker")
        name profileDomainValidationByCensusValidate:   "/ajax/edit-profile/domain-validate"                (controller: "profile", action :"validateUser")
        name profileDomainValidationByPhoneSendSms:     "/ajax/edit-profile/domain-valid-phone-sendSms"     (controller: "profile", action :"validateUserPhoneSendSMS")
        name profileDomainValidationByPhoneValidate:    "/ajax/edit-profile/domain-valid-phone-validate"    (controller: "profile", action :"validateUserPhone")
        name profileDomainValidationByCodeValidate:     "/ajax/edit-profile/domain-valid-code-validate"    (controller: "profile", action :"validateUserCustomCode")

        name causeSupport:         "/ajax/cause/$causeName/support" (controller:"causes", action: "supportCause")

        name ajaxHeadNotificationsChecked:  "/ajax/notificaiones/check"(controller:"notification", action:"notificationChecked")
        name ajaxHeadNotificationsSeeMore:  "/ajax/notificaiones/seeMore"(controller:"notification", action:"notificationSeeMore")
        name ajaxFollow:                    "/ajax/kuorumUser/follow"(controller:"kuorumUser", action:"follow")
        name ajaxUnFollow:                  "/ajax/kuorumUser/unFollow"(controller:"kuorumUser", action:"unFollow")
        name ajaxRequestPolitician:         "/ajax/politico/solicitud-kuorum"(controller:"kuorumUser", action:"follow")
        name ajaxCropImage:                 "/ajax/file/crop"(controller:"file", action:"cropImage")
        name ajaxUploadFile:                "/ajax/file/upload" (controller:'file', action:"uploadImage")
        name ajaxUploadFilePDF:             "/ajax/file/uploadPDF" (controller:'file', action:"uploadPDF")
        name ajaxUploadCampaignFile:        "/ajax/file/uploadCampaignFile/$userAlias/$urlTitle-$campaignId" (controller:'file', action:"uploadCampaignFile")
        name ajaxDeleteCampaignFile:        "/ajax/file/deleteCampaignFile/$userAlias/$urlTitle-$campaignId" (controller:'file', action:"deleteCampaignFile")
        name ajaxUploadContactFile:         "/ajax/file/uploadContactFile/$userAlias/$contactId" (controller:'file', action:"uploadContactFile")
        name ajaxDeleteContactFile:         "/ajax/file/deleteContactFile/$userAlias/$contactId" (controller:'file', action:"deleteContactFile")
        name ajaxUploadQuestionOptionFile:  "/ajax/file/uploadQuestionOptionFile/$userAlias/$surveyId/$questionId/$questionOptionId" (controller:'file', action:"uploadQuestionOptionFile")
        name ajaxDeleteQuestionOptionFile:  "/ajax/file/deleteQuestionOptionFile/$userAlias/$surveyId/$questionId/$questionOptionId" (controller:'file', action:"deleteQuestionOptionFile")


        name adminPrincipal:                        "/sec/admin"                        (controller:"admin", action: "index")
        name adminTestMail:                         "/sec/admin/mailing/test"           (controller:"mailTesting", action: "index")
        name adminSearcherIndex:                    "/sec/admin/searcher/indexar"       (controller:"admin", action: "solrIndex")
        name adminSearcherFullIndex:                "/sec/admin/searcher/full-index"    (controller:"admin", action:"fullIndex")
        name adminSearcherFullIndex:                "/sec/admin/searcher/full-index"    (controller:"admin", action:"fullIndex")
        name adminRecerateAllCss:                   "/sec/admin/css/recreateAllCss"     (controller:"admin"){action=[GET:"updateDomainCss", POST:"updateDomainCssPost"]}
        name adminDomainConfig:                     "/sec/admin/domain"                 (controller:"admin"){action=[GET:"domainConfig",POST:"domainConfigSave"]}
        name adminDomainConfigLanding:              "/sec/admin/domain/landing"         (controller:"admin"){action=[GET:"editLandingInfo",POST:"editLandingInfoSave"]}
        name adminDomainConfigLegalInfo:            "/sec/admin/domain/editLegalInfo"   (controller:"admin"){action=[GET:"editLegalInfo",POST:"updateLegalInfo"]}
        name adminAuthorizedCampaigns:              "/sec/admin/domain/authorizedCampaigns"(controller:"admin"){action=[GET:"editAuthorizedCampaigns",POST:"updateAuthorizedCampaigns"]}
        name adminDomainConfigUploadLogo:           "/sec/admin/domain/uploadLogo"      (controller:"admin"){action=[GET:"editLogo",POST:"uploadLogo"]}
        name adminDomainConfigUploadCarouselImages: "/sec/admin/domain/editCarousel"    (controller:"admin"){action=[GET:"editCarousel",POST:"uploadCarousel"]}
        name adminDomainConfigRelevantCampagins:    "/sec/admin/domain/relevantCampaigns"(controller:"admin"){action=[GET:"editDomainRelevantCampaigns",POST:"updateDomainRelevantCampaigns"]}
        name adminDomainConfigPlan:                 "/sec/admin/domain/update-plan"     (controller:"admin"){action=[GET:"editDomainPlan",POST:"saveNewDomainPlan"]}
        name adminDomainConfigGoogleValidation:     "/sec/admin/domain/google-validation" (controller:"admin", action:"validateDomain")
        name adminDomainValidation:                 "/sec/admin/domain/validation"      (controller:"admin"){action=[GET:"domainValidation",POST:"domainValidationSave"]}
        name adminRequestEmailSender:               "/sec/admin/domain/requestSender"   (controller: "admin"){action=[GET:"requestedEmailSender", POST: "requestedEmailSenderSend"]}
        name adminEditDomainEmailSender:            "/sec/admin/domain/setSender"       (controller:"admin"){action =[GET:"editDomainEmailSender", POST:"updateDomainEmailSender"]}
        name adminDomainDelete:                     "/sec/admin/domain/delete"          (controller:"admin"){action =[GET:"deleteDomain", POST:"deleteDomainConfirm"]}

        name adminDomainRegisterStep1:              "/sec/admin/domain/config/landing"  (controller:"admin"){action =[GET:"designLandingPage", POST:"saveDesignLandingPage"]}
        name adminDomainRegisterStep2:              "/sec/admin/domain/config/rights"   (controller:"admin"){action =[GET:"userRights", POST:"saveUserRights"]}

        name editorEditUserProfile:                         "/editor/user/$userAlias/edit/profile"                (controller:"editorUser"){action =[GET:"editUser", POST:"updateUser"]}
        name editorKuorumAccountEdit:                       "/editor/user/$userAlias/edit/account-details"        (controller:"editorUser"){action =[GET:"editAdminAccountDetails", POST:"updateAdminAccountDetails"]}
        name editorAdminUserRights:                         "/editor/user/$userAlias/edit/rights"                 (controller:"admin"){action =[GET:"editUserRights", POST:"updateUserRights"]}
        name editorAdminUserInvalidate:                     "/editor/user/$userAlias/edit/invalidate"             (controller:"editorUser", action:'invalidateUser')

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
        name politicianContactActivity:                 "/ajax/account/contacts/$contactId/activity" (controller:"contacts", action: "contactActivity")
        name politicianContactEdit:                     "/account/contacts/$contactId/edit" (controller:"contacts"){action=[GET:"editContact", POST:"updateContact"]}
        name politicianContactPersonalCodeGenerate:     "/account/contacts/$contactId/edit/personalCode/generate" (controller:"contacts", action:"generatePersonalCode")
        name politicianContactPersonalCodeRemove:       "/account/contacts/$contactId/edit/personalCode/remove" (controller:"contacts", action:"removePersonalCode")
        name politicianContactEditUpdateNote:           "/ajax/account/contacts/$contactId/edit/updateNote" (controller:"contacts",action:"updateContactNotes")
        name politicianContactNew:                      "/account/contacts/new" (controller:"contacts"){action =[GET:"newContact", POST:"saveContact"]}
        name politicianContactUnsubscribe:              "/account/contacts/$contactId/unsubscribe" (controller:"contacts", action: "loggedUnsubscribe")
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
        name politicianMassMailingCopy:                 "/account/mass-mailing/$campaignId/copy" (controller:"newsletter", action:"copyCampaign")
        name politicianMassMailingSendTest:             "/account/mass-mailing/$campaignId/test" (controller:"newsletter", action: "sendMassMailingTest")
        name politicianMassMailingRemove:               "/ajax/account/mass-mailing/$campaignId/remove" (controller:"newsletter", action:"removeCampaign")
        name politicianMassMailingTrackEvents:          "/ajax/account/mass-mailing/$newsletterId/trackEvents" (controller:"newsletter", action: "showTrackingMails")
        name politicianMassMailingTrackEventsResend:    "/ajax/account/mass-mailing/$newsletterId/trackEvents/resend/$tackingMailId" (controller:"newsletter", action: "resendEmail")
        name politicianMassMailingTrackEventsReport:    "/ajax/account/mass-mailing/$campaignId/trackEvents/report" (controller:"newsletter", action: "sendReport")
        name politicianMassMailingHtml:                 "/account/mass-mailing/$campaignId/html" (controller:"newsletter", action: "showMailCampaign")
        name politicianMassMailingSaveTimeZone:         "/account/mass-mailing/saveTimeZone" (controller: "newsletter"){action = [POST:"saveTimeZone"]}
        name politicianCampaignStatsShow:               "/account/campaign/$campaignId" (controller:"newsletter", action:"showCampaignStats")
        name politicianCampaignDownloadReport:          "/account/campaign/$campaignId/report/$fileName" (controller:"newsletter", action:"downloadReport")
        name politicianCampaignDeleteReport:            "/account/campaign/$campaignId/report/$fileName/delete" (controller:"newsletter", action:"deleteReport")
        name politicianCampaignRefreshReport:           "/ajax/account/campaign/$campaignId/report" (controller:"newsletter", action:"refreshReports")
        name politicianNewsletterDownloadReport:        "/account/mass-mailing/$campaignId/report/$fileName" (controller:"newsletter", action:"downloadReportNewsletter")
        name politicianNewsletterDeleteReport:          "/account/mass-mailing/$campaignId/report/$fileName/delete" (controller:"newsletter", action:"deleteReportNewsletter")
        name politicianNewsletterRefreshReport:         "/ajax/account/mass-mailing/$campaignId/report" (controller:"newsletter", action:"refreshReportsNewsletter")
        name politicianMassMailingDebateStatsReport:    "/ajax/account/debate/$campaignId/report" (controller:"debate", action: "sendReport")
        name politicianMassMailingSurveyReport:         "/ajax/account/survey/$campaignId/report/$surveyReportType" (controller:"survey", action: "sendReport")
        name politicianMassMailingParticipatoryBudgetReport:"/ajax/account/participatoryBudget/$campaignId/report" (controller:"participatoryBudget", action: "sendProposalsReport")
        name ajaxUploadMassMailingAttachFile:           "/ajax/file/uploadMassMailingFile/$campaignId" (controller:'file', action:"uploadNewsletterAttachedFile")
        name ajaxDeleteMassMailingAttachFile:           "/ajax/file/deleteMassMailingFile/$campaignId" (controller:'file', action:"deleteNewsletterAttachedFile")


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
