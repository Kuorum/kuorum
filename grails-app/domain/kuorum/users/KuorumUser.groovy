package kuorum.users

import com.mongodb.WriteConcern
import grails.plugin.springsecurity.SpringSecurityService
import kuorum.KuorumFile
import kuorum.core.model.AvailableLanguage
import kuorum.core.model.CommissionType
import kuorum.core.model.UserType
import kuorum.mail.MailType
import kuorum.notifications.Notice
import kuorum.users.extendedPoliticianData.*
import org.bson.types.ObjectId

/**
 * Represents the user in kuorum
 *
 * Is not separated with inheritance (Politician, Organization, Person) because an user can switch between them,
 * and is a nightmare handle it
 */
class KuorumUser {

    ObjectId id
    String name
    String email
    String alias
    String bio
//    String username
    String password
    AvailableLanguage language = AvailableLanguage.es_ES
    Boolean verified = Boolean.FALSE

    KuorumFile avatar
    KuorumFile imageProfile

    PersonalData personalData = new PersonData()
    UserType userType = UserType.PERSON

    Boolean requestedPolitician = Boolean.FALSE
    Boolean requestedPoliticianBetaTester = Boolean.FALSE
    EditorRules editorRules

    List<CommissionType> relevantCommissions = CommissionType.values()
    List<ObjectId> following  = [] // KuorumUsersId => Is an id instead a KuorumUser because gorm updates all the following users
    List<ObjectId> followers = [] // KuorumUsersId => Is an id instead a KuorumUser because gorm updates all the following users
    List<ObjectId> subscribers = [] // KuorumUsersId => Is an id instead a KuorumUser because gorm updates all the following users

    Integer numFollowers = 0

    List<ObjectId> favorites = [] //PostIds => Is the id instead of Post because gorm updates all

    List<MailType> availableMails = MailType.values()

    Gamification gamification = new Gamification()
    Activity activity = new Activity()
    SocialLinks socialLinks = new SocialLinks()
    KuorumUser organization

    List<String> tags

    Notice notice

//    static hasMany = [following:KuorumUser,followers:KuorumUser,subscribers:KuorumUser]

    static embedded = [
            'personalData',
            'authorities',
            'gamification',
            'avatar',
            'activity',
            'politicianActivity',
            'imageProfile',
            'socialLinks',
            'notice',
            'externalPoliticianActivities',
            'timeLine',
            'relevantEvents',
            'politicianLeaning',
            'professionalDetails',
            'politicianExtraInfo',
            'careerDetails',
            'institutionalOffice',
            'politicalOffice',
            'editorRules'
    ]

    /**
     * Represents the last time that the user checked the notifications
     */
    Date lastNotificationChecked = new Date()

    //Politician FIELDS
//    PoliticalParty politicalParty
//    Institution institution
//    @Deprecated //Use ProfesionalDetails.region
//    Region politicianOnRegion
    PoliticianActivity politicianActivity

    List<ExternalPoliticianActivity> externalPoliticianActivities
    List<PoliticianRelevantEvent> relevantEvents
    List<PoliticianTimeLine> timeLine
    PoliticianLeaning politicianLeaning = new PoliticianLeaning()
    ProfessionalDetails professionalDetails
    CareerDetails careerDetails
    PoliticianExtraInfo politicianExtraInfo
    OfficeDetails institutionalOffice
    OfficeDetails politicalOffice




    //Spring fields
    SpringSecurityService springSecurityService
    def grailsApplication

    boolean enabled = Boolean.TRUE
    boolean accountExpired = Boolean.FALSE
    boolean accountLocked = Boolean.FALSE
    boolean passwordExpired = Boolean.FALSE
    Date dateCreated
    Date lastUpdated
    Set<RoleUser> authorities

    Integer activityForRecommendation = 0


    public static final transient ALIAS_REGEX = "[a-zA-Z0-9_]{1,15}"
    static constraints = {
        name nullable:false
        email nullable: false, email: true
        alias nullable:true, unique:true, maxSize: 15, matches: ALIAS_REGEX
        password nullable:true
        bio nullable:true
        avatar nullable:true
        imageProfile nullable:true
        userType nullable: false
        notice nullable: true
        editorRules nullable:true

        //POLITICIAN VALIDATION
//        institution nullable:true
        requestedPolitician nullable:true
        requestedPoliticianBetaTester nullable:true
        organization nullable: true
        politicianActivity nullable:true
        externalPoliticianActivities nullable: true
        relevantEvents nulable:true
        timeLine nulable:true
        politicianLeaning nullable:true
        professionalDetails nullable:true
        institutionalOffice nullable:true
        politicalOffice nullable:true
        careerDetails nullable:true
        politicianExtraInfo nullable:true
    }

    static mapping = {
        email index:true, indexAttributes: [unique:true]
        alias index:true, indexAttributes: [unique:true, sparse:true]
//        following cascade:"refresh"
//        followers cascade:"refresh"
//        subscribers cascade:"refresh"


        writeConcern WriteConcern.FSYNC_SAFE
    }


    String toString(){
        name
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (o==null) return false
        if (!(o instanceof KuorumUser)) return false

        KuorumUser that = (KuorumUser) o

        if (email != that.email) return false

        return true
    }

    static transients = ["springSecurityService", 'activityForRecommendation', 'grailsApplication']

//    static mapping = {
//       password column: '`password`'
//    }

    def beforeInsert() {
        updateDenormalizedData()
        audit()

    }

    def beforeUpdate() {
        log.debug("Se ha actualizado el usuario ${id}")
        updateDenormalizedData()
        audit()
    }

    //Is not private for call it from service. I'm not proud for that
    void updateDenormalizedData(){
        email = email.toLowerCase()
        alias = alias?.toLowerCase()
        if (!followers) followers = []
        numFollowers = followers.size()
        if (!personalData){
            this.personalData = new PersonalData()
        }
        personalData.userType = userType
        if (UserType.POLITICIAN.equals(userType) && politicianLeaning?.liberalIndex==null){
            politicianLeaning.liberalIndex = 50
        }
        if (politicianLeaning?.liberalIndex){
            politicianLeaning?.liberalIndex = Math.min(100, politicianLeaning.liberalIndex) // MAX 100
            politicianLeaning?.liberalIndex = Math.max(0, politicianLeaning.liberalIndex) // min 0
        }
    }

    private void audit(){
        try{
            KuorumUserAudit kuorumUserAudit = new KuorumUserAudit()
            if (springSecurityService.isLoggedIn()){
                kuorumUserAudit.editor = springSecurityService.currentUser
            }
            if (this != kuorumUserAudit.editor) {

                kuorumUserAudit.user = this
                //            kuorumUserAudit.snapshotUser = this
                kuorumUserAudit.dateCreated = new Date()
                Map<String, String> res = getPropertyValues(this, listPropertyNames(this), "")
                def obj = this
                embedded.each { fieldName ->
                    if (obj."${fieldName}" instanceof java.util.List || obj."${fieldName}" instanceof java.util.Set) {
                        obj."${fieldName}".eachWithIndex { listObj, idx ->
                            res.putAll(getPropertyValues(listObj, listPropertyNames(listObj), "${fieldName}[$idx]."))
                        }
                    } else if (obj."${fieldName}" != null) {
                        res.putAll(getPropertyValues(obj."${fieldName}", listPropertyNames(obj."${fieldName}"), "${fieldName}."))
                    }
                }
                kuorumUserAudit.snapshot = res
                        .inject([:]) {
                    map, v -> map << [(v.key.toString().replaceAll("\\.", "_")): v.value?.toString()]
                }.findAll {
                    it.key != "lastUpdated"
                }
                def audits = KuorumUserAudit.findAllByUser(this, [max: 1, sort: "dateCreated", order: "desc"])
                if (audits) {
                    //Filter not edited fields
                    kuorumUserAudit.updates = [:]
                    Map prevSnapshot = audits.first().snapshot
                    kuorumUserAudit.snapshot.each { k, v ->
                        if (prevSnapshot.get(k) != v) {
                            kuorumUserAudit.updates.put(k, v)
                        }
                    }
                } else {
                    kuorumUserAudit.updates = kuorumUserAudit.snapshot
                }
                if (kuorumUserAudit.updates) {
                    kuorumUserAudit.save()
                }
            }else{
                //The user is editing himself
                log.debug("The user ${this} is editing himself. No audit save.")
            }
        }catch (Throwable e){
            log.warn("Not audit save for user ${this}", e)
        }
    }

    Map<String,String> getPropertyValues(def obj, def properties, String prefix){
        def res = [:]
        properties.each{ fieldName ->
            if (embedded.contains(fieldName) &&
                    (obj."${fieldName}" instanceof java.util.List || obj."${fieldName}" instanceof java.util.Set )) {
                obj."${fieldName}".eachWithIndex { listObj, idx ->
                    res.putAll(getPropertyValues(listObj,listPropertyNames(listObj), "${prefix}${fieldName}[$idx]."))
                }
            }else if (embedded.contains(fieldName) && obj."${fieldName}" && grailsApplication.isDomainClass(obj."${fieldName}".class)) {
                res.putAll(getPropertyValues(obj."${fieldName}", listPropertyNames(obj."${fieldName}"), "${prefix}${fieldName}."))
            }else {
                //Basic Data
                res << ["${prefix}${fieldName}":obj."${fieldName}"?.toString()?:null]
            }
        }
        res
    }

    private def listPropertyNames(def obj){
        if (obj){
            new org.codehaus.groovy.grails.commons.DefaultGrailsDomainClass(obj.class).persistentProperties.collect{it.name}
        }else{
            []
        }
    }

    int hashCode() {
        return id?id.hashCode():email.hashCode()
    }
}
