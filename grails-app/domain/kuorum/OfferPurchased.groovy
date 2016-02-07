package kuorum

import kuorum.core.model.OfferType
import kuorum.users.KuorumUser

/**
 * Created by iduetxe on 7/03/15.
 */
class OfferPurchased {
    OfferType offerType
    Long kPeople
    KuorumUser user
    Date dateCreated
}
