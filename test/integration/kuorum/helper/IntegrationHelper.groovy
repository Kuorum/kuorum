package kuorum.helper

import kuorum.Region
import kuorum.core.model.Gender
import kuorum.core.model.RegionType
import kuorum.core.model.Studies
import kuorum.users.KuorumUser
import kuorum.users.PersonData

/**
 * Created by iduetxe on 4/02/14.
 */
class IntegrationHelper {

    public static final KuorumUser createDefaultUser(String email){
        PersonData personalData = new PersonData(gender: Gender.MALE, studies: Studies.DOCTOR)
        Region userRegion = Region.findByIso3166_2AndRegionType("EU", RegionType.STATE) ?:creteDefaultRegion()
        personalData.provinceCode = userRegion.iso3166_2
        personalData.province = userRegion
        new KuorumUser(
                name:email.split("@")[0],
                email: email,
                password: "XXXX",
                region: userRegion,
                personalData: personalData
        )
    }

    public static final Region creteDefaultRegion(){
        new Region(
                name:"Europa",
                iso3166_2:"EU",
                regionType: RegionType.STATE
        )
    }

}
