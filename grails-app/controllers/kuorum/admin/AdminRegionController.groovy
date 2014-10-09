package kuorum.admin

import kuorum.Region

class AdminRegionController extends  AdminController {

    def createRegion() {
        [regions: Region.findAll(), command:new Region()]
    }

    def saveRegion(Region region){
        def postalCodes = new Region(params).postalCodes
        region.postalCodes = postalCodes //Chpu rápida para no comerme la cabeza
        String regionCode = region.iso3166_2.split("-").last()
        region.iso3166_2 = region.superRegion.iso3166_2+"-"+regionCode
        if (!region.save()){
            render view:'/adminRegion/createRegion', model: [
                    command:region,
                    regions:Region.findAll()
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
        [regions: Region.findAll(), command:region]
    }
}
