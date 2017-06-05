package kuorum.helper

import kuorum.KuorumFile
import kuorum.Region
import kuorum.core.FileGroup
import kuorum.core.model.*
import kuorum.post.Post
import kuorum.project.Project
import kuorum.users.KuorumUser
import kuorum.users.PersonData

/**
 * Created by iduetxe on 4/02/14.
 */
class IntegrationHelper {

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

    public static final Post createDefaultPost(KuorumUser owner, Project project){
        new Post(
                owner: owner,
                ownerPersonalData: owner.personalData,
                title:"title",
                text: "Text",
                project:project,
                numVotes: 1,
                numClucks: 1,
                postType: PostType.HISTORY
        )
    }

    public static final Post createDefaultPost(){
        KuorumUser owner = createDefaultUser("postOwner@example.com")
        Project project = createDefaultProject("#hashTagProjectDefault")
        return createDefaultPost(owner, project)
    }

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

    public static final Project createDefaultProject(String hashtag){
        Region region = Region.findByIso3166_2AndRegionType("EU", RegionType.STATE) ?:creteDefaultRegion()
        KuorumUser owner = KuorumUser.findByEmail("projectowner@example.com") ?: createDefaultUser("projectOwner@example.com").save(flush:true)
        KuorumFile pdfFile = new KuorumFile(
                fileGroup: FileGroup.PDF,
                temporal: true,
                user: owner,
                userId: owner.id ,
                url: "http://kuorum.org",
                local: true,
                storagePath: "/tmp",
                fileName: "test.pdf"
        )
        KuorumFile urlYoutube = new KuorumFile(
                fileGroup: FileGroup.PROJECT_IMAGE,
                temporal: true,
                user: owner,
                url: "http://kuorum.org",
                local: false
        )
        new Project(
                hashtag: hashtag,
                shortName: "shortName${hashtag}",
                realName: "realName${hashtag}",
                description: "description${hashtag}",
                commissions: [CommissionType.OTHERS],
                region: region,
//                institution: creteDefaultInstitution(),
                availableStats: Boolean.TRUE,
                deadline: new Date() + 10,
                pdfFile: pdfFile,
                urlYoutube: urlYoutube,
                shortUrl:new URL('http://ow.ly'),
                owner: owner
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
