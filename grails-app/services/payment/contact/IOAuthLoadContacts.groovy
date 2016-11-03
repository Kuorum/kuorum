package payment.contact

import kuorum.core.exception.KuorumException
import kuorum.users.KuorumUser

interface IOAuthLoadContacts {

	void loadContacts(KuorumUser user, org.scribe.model.Token accessToken) throws KuorumException

}
