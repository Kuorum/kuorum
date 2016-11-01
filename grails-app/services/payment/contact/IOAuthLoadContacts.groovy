package payment.contact

import kuorum.core.exception.KuorumException
import kuorum.users.KuorumUser

interface IOAuthLoadContacts {

	void loadContacts(KuorumUser user, accessToken) throws KuorumException

}
