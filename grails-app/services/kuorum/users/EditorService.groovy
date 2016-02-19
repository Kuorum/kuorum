package kuorum.users

import grails.transaction.Transactional

@Transactional
class EditorService {

    KuorumUser requestEditorRights(KuorumUser user) {

        if (!user.editorRules){
            user.editorRules = new EditorRules();
        }
        user.editorRules.requestedEditor = true;
        user.save()
    }

    KuorumUser discardEditorWarns(KuorumUser user) {

        if (!user.editorRules){
            user.editorRules = new EditorRules();
        }
        user.editorRules.discardEditorWarns = true;
        user.save()

    }
}
