package kuorum.admin

import grails.plugin.springsecurity.annotation.Secured
import kuorum.PoliticalParty
import org.bson.types.ObjectId

@Secured(['ROLE_ADMIN'])
class AdminPoliticalPartyController   extends  AdminController{


    def createPoliticalParty() {
        [command:new PoliticalParty()]
    }

    def savePoliticalParty(PoliticalParty politicalParty){
        if (!politicalParty.save()){
            render view:'/adminRegion/createRegion', model: [command:politicalParty]
            return
        }
        flash.message="Salvado correctamente ${politicalParty.name}"
        redirect mapping:"adminEditPoliticalParty", params: [id:politicalParty.id]
    }

    def editPoliticalParty(String id){
        PoliticalParty politicalParty = PoliticalParty.get(new ObjectId(id))
        [command:politicalParty]
    }

    def listPoliticalParties(){
        [politicalParties: PoliticalParty.list()]
    }
}
