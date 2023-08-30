package kuorum.web.commands.editor

import grails.validation.Validateable
import kuorum.core.model.AvailableLanguage
import kuorum.users.KuorumUser
import kuorum.web.commands.profile.AccountDetailsCommand
import kuorum.web.commands.profile.EditUserProfileCommand

@Validateable
class EditorAccountCommand {

    EditorAccountCommand() {
        // Default constructor for command validation flow
    }

    EditorAccountCommand(KuorumUser user) {
        this.name = user.name
        this.surname = user.surname
        this.email = user.email
        this.language = user.language
        this.timeZoneId = user.timeZone?.getID()
        this.bio = user.bio
    }
    String name
    String surname
    String email
    AvailableLanguage language
    String timeZoneId
    String bio
    static constraints = {
        importFrom AccountDetailsCommand, include: ["name", "surname", "language", "timeZoneId"]
        importFrom EditUserProfileCommand, include: ["bio"]
        email nullable: false, email: true
    }
}
