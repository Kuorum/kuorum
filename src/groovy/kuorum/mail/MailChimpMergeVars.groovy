package kuorum.mail

import com.ecwid.mailchimp.MailChimpObject
import com.ecwid.mailchimp.MailChimpObject.Field

/**
 * Created by iduetxe on 16/02/14.
 */
class MailChimpMergeVars extends MailChimpObject {

    @Field
    public String EMAIL, FNAME, BIRTHDAY, POSTALCODE, GENDER, PROVINCE,STUDIES,WORKINGSEC,USERTYPE;

    @Field
    public List<MailChimpGroup> groupings

    public MailChimpMergeVars() {
    }

//    public MailChimpMergeVars(String email, String fname, String birthday, String postalCode) {
//        this.EMAIL = email;
//        this.FNAME = fname;
//        this.BIRTHDAY = birthday;
//        this.POSTALCODE=postalCode
//    }
}
