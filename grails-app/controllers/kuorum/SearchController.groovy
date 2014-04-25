package kuorum

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import kuorum.core.model.CommissionType
import kuorum.core.model.search.SearchParams
import kuorum.core.model.solr.*
import org.springframework.beans.factory.InitializingBean

class SearchController implements InitializingBean {

    def searchSolrService
    def indexSolrService
//    def messageSource

    private messageEnumJson(def type){
        [
                type:type.toString(),
//                i18n:messageSource.getMessage("${type.class.name}.${type}")
                i18n:g.message(code: "${type.class.name}.${type}")
        ]
    }

   private userRoleJson(SolrKuorumUser user){
       def type = user.role.toString()
       def i18n = g.message(code: "${user.role.class.name}.${user.role}.${user.gender}")
       if (user.subType == SolrSubType.POLITICIAN){
           type = user.subType.toString()
           i18n = g.message(code: "${user.subType.class.name}.${user.subType}")
       }
       if (user.gender)
        [
            type:type,
            i18n:i18n
        ]
   }

    public void afterPropertiesSet() throws Exception {
        JSON.createNamedConfig('suggest') {
            it.registerObjectMarshaller( SolrType)          { SolrType solrType             -> messageEnumJson(solrType)}
            it.registerObjectMarshaller( SolrSubType)       { SolrSubType solrSubType       -> messageEnumJson(solrSubType)}
            it.registerObjectMarshaller( CommissionType )   { CommissionType commissionType -> messageEnumJson(commissionType)}
            it.registerObjectMarshaller( SolrLaw )   { SolrLaw solrLaw ->
                [
                        status:solrLaw.subType,
                        title:solrLaw.name,
                        hashtag:solrLaw.hashtag
                ]
            }
            it.registerObjectMarshaller( SolrKuorumUser )   { SolrKuorumUser solrKuorumUser ->
                [
                        name:solrKuorumUser.name,
                        role:userRoleJson(solrKuorumUser),
                        urlAvatar:solrKuorumUser.urlImage
                ]
            }
            it.registerObjectMarshaller(SolrAutocomplete){SolrAutocomplete solrAutocomplete ->
                [
                        suggests : solrAutocomplete.suggests,
                        users: solrAutocomplete.kuorumUsers,
                        laws: solrAutocomplete.laws
                ]
            }
        }
    }

    def search(SearchParams searchParams) {

        def docs = searchSolrService.search(searchParams)
        [docs:docs]

    }

    @Secured(['ROLE_ADMIN'])
    def fullIndex(){
         indexSolrService.fullIndex()
        render "Ok"
    }

    def suggest(String word){
        SearchParams searchParams = new SearchParams(word:word)
        SolrAutocomplete res = searchSolrService.suggest(searchParams)
        JSON.use('suggest') {
            render res as JSON
        }
    }
}
