package payment.contact

import kuorum.core.exception.KuorumException
import kuorum.register.KuorumUserSession

interface IOAuthLoadContacts {

	void loadContacts(KuorumUserSession user, org.scribe.model.Token accessToken) throws KuorumException

}
