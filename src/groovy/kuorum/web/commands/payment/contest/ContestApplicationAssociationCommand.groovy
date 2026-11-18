package kuorum.web.commands.payment.contest

import grails.validation.Validateable

/**
 * "Association" step of the contest application wizard (collaborator variant only).
 * The registration sheet files are not part of this command: they are campaign files uploaded
 * through the generic campaign files uploader and checked separately by the controller.
 */
@Validateable
class ContestApplicationAssociationCommand {
    String associationName
    String associationImage

    // Error code 'nullable' also for blank values (empty/whitespace strings are not turned into null when the command is not bound from a
    // request), so the existing "...cannot be empty" messages apply
    static constraints = {
        associationName nullable: false, validator: { val -> if (!val?.trim()) return 'nullable' }
        associationImage nullable: false, validator: { val -> if (!val?.trim()) return 'nullable' }
    }
}
