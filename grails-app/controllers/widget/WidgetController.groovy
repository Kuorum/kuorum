package widget

import grails.converters.JSON
import kuorum.core.model.UserType
import kuorum.users.KuorumUser
import kuorum.users.KuorumUserService
import kuorum.users.UserReputationService
import org.kuorum.rest.model.kuorumUser.reputation.UserReputationEvolutionRSDTO
import org.kuorum.rest.model.kuorumUser.reputation.UserReputationRSDTO

import javax.servlet.http.HttpServletResponse

class WidgetController {

	KuorumUserService kuorumUserService
	UserReputationService userReputationService

	def ratePolitician = {
		KuorumUser politician = kuorumUserService.findByAlias(params.userAlias)
		[politician:politician]
	}

    def comparativePoliticianInfo = {
        List<KuorumUser> politicians = params.userAlias.collect{kuorumUserService.findByAlias(it)}.findAll{it && it.userType == UserType.POLITICIAN}
        if (!politicians){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        [politicians:politicians]
    }

	def kuorumWidgetjs ={
		[divId:params.divId ]
	}


	def historicPoliticianRate(String userAlias){
		KuorumUser politician = kuorumUserService.findByAlias(userAlias)
		if (!politician || politician.userType != UserType.POLITICIAN){
			response.sendError(HttpServletResponse.SC_NOT_FOUND)
			return;
		}
		UserReputationEvolutionRSDTO evolutionRSDTO = userReputationService.getReputationEvoulution(politician)
		UserReputationRSDTO userReputationRSDTO = userReputationService.getReputation(politician, null)
		def data=  [
				"title":"",
				"average":userReputationRSDTO.userReputation,
				"averageLabel":"${message(code:'politician.valuation.chart.module.average')}",
				"datasets": [
						[
								"name": "${message(code:'politician.valuation.chart.module.realTime')}",
								"data": [],
								"unit": "",
								"type": "spline"
						], [
								"name": "${message(code:'politician.valuation.chart.module.runningAverage')}",
								"data": [],
								"unit": "",
								"type": "spline"
						]
				]
		]

		evolutionRSDTO.reputationSnapshots.each {def reputationSnapshot ->
			data.datasets[0].data << [reputationSnapshot.timestamp, reputationSnapshot.stockValue]
			data.datasets[1].data << [reputationSnapshot.timestamp, reputationSnapshot.runningAverage]
		}

		render data as JSON
	}

	def comparingPoliticianRateData(){
		List<KuorumUser> politicians = params.userAlias.collect{kuorumUserService.findByAlias(it)}.findAll{it && it.userType == UserType.POLITICIAN}
		if (!politicians){
			response.sendError(HttpServletResponse.SC_NOT_FOUND)
			return;
		}

		def data=  [
				"title":"Comparing politicians",
				"datasets": []
		]

		politicians.each {politician ->
			UserReputationEvolutionRSDTO evolutionRSDTO = userReputationService.getReputationEvoulution(politician)
			data.datasets << [
					"name": "${politician.name}",
					"data": evolutionRSDTO.reputationSnapshots.collect{[it.timestamp, it.runningAverage]},
                    "type": "spline"
			]
		}
		render data as JSON
	}

}

