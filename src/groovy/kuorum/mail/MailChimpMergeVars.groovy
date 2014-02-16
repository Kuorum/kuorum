package kuorum.mail

import com.ecwid.mailchimp.MailChimpObject
import com.ecwid.mailchimp.MailChimpObject.Field

/**
 * Created by iduetxe on 16/02/14.
 */
class MailChimpMergeVars extends MailChimpObject {

    @Field
    public String EMAIL, FNAME, LNAME;

    public MailChimpMergeVars() {
    }

    public MailChimpMergeVars(String email, String fname, String lname) {
        this.EMAIL = email;
        this.FNAME = fname;
        this.LNAME = lname;
    }
}
