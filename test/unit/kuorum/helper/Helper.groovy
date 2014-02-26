package kuorum.helper

import kuorum.Region
import kuorum.core.model.PostType
import kuorum.law.Law
import kuorum.post.Post
import kuorum.users.KuorumUser

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

    public static final KuorumUser createDefaultUser(String email){
        new KuorumUser(
                name:"name",
                email: email,
                password: "XXXX"
        )
    }

    public static final Law createDefaultLaw(String hashtag){
        new Law(
                hashtag: hashtag,
                shortName: "shortName${hashtag}",
                realName: "realName${hashtag}",
                description: "description${hashtag}",
                introduction: "introducction${hashtag}",
                region: creteDefaultRegion()

        )
    }

    public static final Region creteDefaultRegion(){
        new Region(
                name:"Europa",
                iso3166_2:"EU"
        )
    }
}
