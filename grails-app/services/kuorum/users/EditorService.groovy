package kuorum.users

import grails.transaction.Transactional
import kuorum.notifications.NotificationService

@Transactional
class EditorService {

    NotificationService notificationService

    KuorumUser requestEditorRights(KuorumUser user) {

        if (!user.editorRules){
            user.editorRules = new EditorRules();
        }
        if (user.editorRules.requestedEditor){
            return user;
        }
        user.editorRules.requestedEditor = true;
        user.save()
        notificationService.sendEditorPurchaseNotification(user)
        user
    }

    KuorumUser discardEditorWarns(KuorumUser user) {

        if (!user.editorRules){
            user.editorRules = new EditorRules();
        }
        user.editorRules.discardEditorWarns = true;
        user.save()

    }
}
