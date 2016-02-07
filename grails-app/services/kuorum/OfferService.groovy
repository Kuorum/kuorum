package kuorum

import grails.transaction.Transactional
import kuorum.core.exception.KuorumException
import kuorum.core.model.OfferType
import kuorum.notifications.NotificationService
import kuorum.users.KuorumUser

@Transactional
class OfferService {

    NotificationService notificationService

    OfferPurchased purchaseOffer(KuorumUser user, OfferType offerType, Long kPeople) {
        OfferPurchased offerPurchased = new OfferPurchased()
        offerPurchased.user = user
        offerPurchased.offerType = offerType
        offerPurchased.dateCreated = new Date();
        offerPurchased.kPeople = kPeople
        if (!offerPurchased.save()){
            log.error("Error guardando compra ${offerType} de ${user}. ERRORS => ${offerPurchased.errors}")
            throw new KuorumException("Error guardando compra")
        }
        notificationService.sendOfferPurchasedNotification(user, offerPurchased)
        offerPurchased
    }
}
