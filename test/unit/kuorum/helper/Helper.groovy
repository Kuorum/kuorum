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
class Helper {

    static final void validateConstraints(obj, field, error) {
        def validated = obj.validate()
        if (error && error != 'OK') {
            assert !validated
            assert obj.errors[field]
            assert error == obj.errors[field]
        } else {
            assert !obj.errors[field]
        }
    }

    static final KuorumUser createDefaultUser(String email){
        PersonData personalData = new PersonData(gender: Gender.MALE, studies: Studies.DOCTOR)
        Region userRegion = creteDefaultRegion()
        personalData.provinceCode = userRegion.iso3166_2
        personalData.province = userRegion
        new KuorumUser(
                name:email.split("@")[0],
                email: email,
                password: "XXXX",
                region: creteDefaultRegion(),
                personalData: personalData
        )
    }


    static final Region creteDefaultRegion(){
        new Region(
                name:"Europa",
                iso3166_2:"EU",
                regionType: RegionType.STATE
        )
    }

}
