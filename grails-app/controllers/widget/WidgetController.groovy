package widget

import kuorum.users.KuorumUser
import kuorum.users.KuorumUserService

class WidgetController {

	KuorumUserService kuorumUserService

	def smallPoliticianInfo = {
		KuorumUser politician = kuorumUserService.findByAlias(params.userAlias)
		[politician:politician]
	}

	def kuorumWidgetjs ={
		[params:params]
	}
}

