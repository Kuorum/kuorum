package kuorum.web.commands.payment.contact

import grails.validation.Validateable

@Validateable
class NewContactsCommand {

    List<NewContactCommand> newContactCommands = []

    static constraints = {
        newContactCommands nullable: false, minSize: 1, maxSize: 20
    }
}
