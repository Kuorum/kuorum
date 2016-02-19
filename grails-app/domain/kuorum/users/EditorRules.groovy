package kuorum.users

/**
 * This object contains the editors data
 *
 * Like: People that can edit or if the user has requested the editor option
 */
class EditorRules {

    Boolean discardEditorWarns = Boolean.FALSE
    Boolean requestedEditor = Boolean.FALSE

    static constraints = {
        requestedEditor nullable:false
        discardEditorWarns nullable:false
    }
}
