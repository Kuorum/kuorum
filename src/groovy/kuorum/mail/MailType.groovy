package kuorum.mail

/**
 * Created by iduetxe on 25/02/14.
 */
public enum MailType {

    REGISTER_VERIFY_ACCOUNT("validationEmail", "registerUser", ["fname","verifyLink"]);


    String nameTemplate
    String tagTemplate
    List<String> requiredBindings
    MailType(String nameTemplate, String tagTemplate, List<String> requiredBindings){
        this.nameTemplate = nameTemplate
        this.tagTemplate = tagTemplate
        this.requiredBindings = requiredBindings
    }

}