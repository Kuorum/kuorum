package payment.contact

import kuorum.core.exception.KuorumException
import kuorum.users.KuorumUser

/**
 * Created by alex on 27/10/16.
 */
interface IOAuthLoadContacts {

	void loadContacts(KuorumUser user, org.scribe.model.Token accessToken) throws KuorumException
}
