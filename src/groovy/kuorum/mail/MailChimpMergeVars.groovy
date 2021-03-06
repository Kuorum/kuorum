package kuorum.mail

import com.ecwid.mailchimp.MailChimpObject
import com.ecwid.mailchimp.MailChimpObject.Field

/**
 * Created by iduetxe on 16/02/14.
 */
class MailChimpMergeVars extends MailChimpObject {

    @Field public String EMAIL;
    @Field public String FNAME
    @Field public String LNAME
    @Field public String BIRTHDAY
    @Field public String POSTALCODE
    @Field public String GENDER
    @Field public String STUDIES
    @Field public String WORKINGSEC
    @Field public String USERTYPE
    @Field public String COUNTRY_C
    @Field public String LOCATION_C
    @Field public String mc_language;

    @Field
    public List<MailChimpGroup> groupings

    public MailChimpMergeVars() {
    }

//    public MailChimpMergeVars(String email, String fname, String birthday, String postalCode) {
//        this.EMAIL = email;
//        this.FNAME = fname;
//        this.MC_BIRTHDAY = birthday;
//        this.MC_POSTALCODE=postalCode
//    }
}
