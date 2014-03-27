package kuorum.mail

import com.ecwid.mailchimp.MailChimpObject
import com.ecwid.mailchimp.MailChimpObject.Field

/**
 * Created by iduetxe on 27/03/14.
 */
class MailChimpGroup extends MailChimpObject{
//    @Field
//    public Integer id;
    @Field
    public String name
    @Field
    public List<String> groups
}
