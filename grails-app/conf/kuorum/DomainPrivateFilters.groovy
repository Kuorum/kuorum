package kuorum

class DomainPrivateFilters {

    // The censusLoginFilter should go before because it redirects to an allowed page if the user is not logged
    def dependsOn=[CensusLoginFilters]


    def domainService

    def filters = {
        customRegisterFilter(controller:'customRegister|logout|login|error|register|layouts|landing|footer|campaignValidation', invert: true) {
            before = {
                if (!domainService.showPrivateContent()){
//                        It is for redirect when the user is not logged and the the domain is private
//                    flash.message("Plataforma protegida. Por favor ingrese sus credenciales")
                    redirect(mapping: 'login')

                    return false
                }
            }

            after = { Map model ->

            }
            afterView = { Exception e ->

            }
        }
    }
}
