package kuorum.admin

import kuorum.Institution
import kuorum.Region
import org.bson.types.ObjectId

class AdminRegionController extends  AdminController {

    def createRegion() {
        [regions: Region.findAll(), command:new Region(), institutions:[]]
    }

    def saveRegion(Region region){
        def postalCodes = new Region(params).postalCodes
        region.postalCodes = postalCodes //Chpu rápida para no comerme la cabeza
        String regionCode = region.iso3166_2.split("-").last()
        region.iso3166_2 = region.superRegion.iso3166_2+"-"+regionCode
        if (!region.save()){
            region.iso3166_2 = region.iso3166_2.split("-").last()
            render view:'/adminRegion/createRegion', model: [
                    command:region,
                    regions:Region.findAll(),
                    institutions:Institution.findAllByRegion(region)
            ]
            return
        }
        flash.message="Region ${region.name} salvada"
        redirect mapping:"adminEditRegion", params: [iso3166_2:region.iso3166_2]
    }

    def listRegions(){
        [regions:Region.findAll()]
    }

    def editRegion(String iso3166_2) {
        Region region = Region.findByIso3166_2(iso3166_2)
        [regions: Region.findAll(), command:region, institutions:Institution.findAllByRegion(region)]
    }

    def editInstitution(String id){
        Institution institution = Institution.get(new ObjectId(id))
        [command:institution, regions: Region.findAll()]
    }

    def saveInstitution(Institution institution){
        if (!institution.save()){
            render view:'/adminRegion/editInstitution', model: [
                    command:institution,
                    regions:Region.findAll()
            ]
            return
        }
        flash.message="Institucion ${institution.name} salvada"
        redirect mapping:"adminEditInstitution", params: [id:institution.id]
    }

    def createInstitution(String iso3166_2){
        Region region = Region.findByIso3166_2(iso3166_2)
        Institution institution = new Institution(region:region)
        [command:institution, regions: Region.findAll()]
    }
}
