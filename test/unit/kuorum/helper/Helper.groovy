package kuorum.helper

import kuorum.Institution
import kuorum.Region
import kuorum.core.model.Gender
import kuorum.core.model.PostType
import kuorum.core.model.Studies
import kuorum.law.Law
import kuorum.post.Post
import kuorum.users.KuorumUser
import kuorum.users.PersonData
import kuorum.users.PersonalData

/**
 * Created by iduetxe on 4/02/14.
 */
class Helper {

    public static final void validateConstraints(obj, field, error) {
        def validated = obj.validate()
        if (error && error != 'OK') {
            assert !validated
            assert obj.errors[field]
            assert error == obj.errors[field]
        } else {
            assert !obj.errors[field]
        }
    }

    public static final Post createDefaultPost(KuorumUser owner, Law law){
        new Post(
                owner: owner,
                title:"title",
                text: "Text",
                law:law,
                numVotes: 1,
                numClucks: 1,
                postType: PostType.HISTORY
        )
    }

    public static final Post createDefaultPost(){
        new Post(
                owner: createDefaultUser("postOwner@example.com"),
                title:"title",
                text: "Text",
                law:createDefaultLaw("#hashTagLawDefault"),
                numVotes: 1,
                numClucks: 1,
                postType: PostType.HISTORY
        )
    }

    public static final KuorumUser createDefaultUser(String email){
        PersonData personalData = new PersonData(gender: Gender.MALE, studies: Studies.DOCTOR)
        new KuorumUser(
                name:email.split("@")[0],
                email: email,
                password: "XXXX",
                personalData: personalData
        )
    }

    public static final Law createDefaultLaw(String hashtag){
        new Law(
                hashtag: hashtag,
                shortName: "shortName${hashtag}",
                realName: "realName${hashtag}",
                description: "description${hashtag}",
                introduction: "introducction${hashtag}",
                region: creteDefaultRegion(),
                institution: creteDefaultInstitution()

        )
    }

    public static final Region creteDefaultRegion(){
        new Region(
                name:"Europa",
                iso3166_2:"EU"
        )
    }

    public static final Institution creteDefaultInstitution(){
        new Institution(
                name:"Parlamento europer",
                region: creteDefaultRegion()
        )
    }
}
