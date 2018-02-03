package kuorum.users

import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import kuorum.core.model.UserType
import kuorum.web.widget.AverageWidgetType
import net.sf.ehcache.search.aggregator.Average
import org.kuorum.rest.model.kuorumUser.reputation.ReputationSnapshotRSDTO
import org.kuorum.rest.model.kuorumUser.reputation.UserReputationEvolutionRSDTO
import org.kuorum.rest.model.kuorumUser.reputation.UserReputationRSDTO

import javax.servlet.http.HttpServletResponse
import java.text.SimpleDateFormat

class RatingController {

    SpringSecurityService springSecurityService
    KuorumUserService kuorumUserService
    UserReputationService userReputationService
    PoliticianService politicianService

    def ratePolitician(String userAlias){
        KuorumUser politician = kuorumUserService.findByAlias(userAlias)
        Integer rate = Integer.parseInt(params.rate)
        UserReputationRSDTO userReputationRSDTO = userReputationService.addReputation(politician,rate)
        render userReputationRSDTO as JSON
    }

    def widgetComparativePoliticianInfo = {
        List<KuorumUser> politicians = params.userAlias.collect{kuorumUserService.findByAlias(it)}.findAll{politicianService.isPolitician(it)}
        if (!politicians){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        UserReputationEvolutionRSDTO.Interval interval = getIntervalFromParams(params)
        AverageWidgetType averageWidgetType = getAverageTypeFromParams(params)
        [politicians:politicians, interval:interval, averageWidgetType:averageWidgetType, startDate:params.startDate, endDate:params.endDate]
    }

    def widgetRatePolitician = {
        List<KuorumUser> politicians = params.userAlias.collect{kuorumUserService.findByAlias(it)}.findAll{it && it.userType == UserType.POLITICIAN}
        Map<String, UserReputationRSDTO> rates = [:]
        politicians.each{politician ->
            UserReputationRSDTO userReputationRSDTO = userReputationService.getReputation(politician)
            rates.put(politician.alias, userReputationRSDTO)
        }
        [politicians :politicians, rates:rates ]
    }

    def historicPoliticianRate(String userAlias){
        KuorumUser politician = kuorumUserService.findByAlias(userAlias)
        if (!politicianService.isPolitician(politician)){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }
        UserReputationEvolutionRSDTO.Interval interval = getIntervalFromParams(params)
        AverageWidgetType averageWidgetType = getAverageTypeFromParams(params)
        def range = getRangeDate(params)
        UserReputationEvolutionRSDTO evolutionRSDTO = userReputationService.getReputationEvoulution(politician,interval, range.startDate, range.endDate)
        UserReputationRSDTO userReputationRSDTO = userReputationService.getReputation(politician)
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
                                "name": """${message(code:"politician.valuation.chart.module.${averageWidgetType}")}""",
                                "data": [],
                                "unit": "",
                                "type": "spline"
                        ]
                ]
        ]

        evolutionRSDTO.reputationSnapshots.each {def reputationSnapshot ->
            data.datasets[0].data << [reputationSnapshot.timestamp, reputationSnapshot.stockValue]
            data.datasets[1].data << [reputationSnapshot.timestamp, getProperAverage(averageWidgetType,reputationSnapshot)]
        }

        render data as JSON
    }

    def comparingPoliticianRateData(){
        List<KuorumUser> politicians = params.userAlias.collect{kuorumUserService.findByAlias(it)}.findAll{politicianService.isPolitician(it)}
        if (!politicians){
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            return;
        }

        def data=  [
                "title":"Comparing politicians",
                "datasets": []
        ]
        UserReputationEvolutionRSDTO.Interval interval = getIntervalFromParams(params)
        AverageWidgetType averageWidgetType = getAverageTypeFromParams(params)
        def range = getRangeDate(params)
        politicians.each {politician ->
            UserReputationEvolutionRSDTO evolutionRSDTO = userReputationService.getReputationEvoulution(politician, interval, range.startDate, range.endDate)
            data.datasets << [
                    "name": "${politician.name}",
                    "alias":"${politician.alias}",
                    "data": evolutionRSDTO.reputationSnapshots.collect{[it.timestamp, getProperAverage(averageWidgetType,it)]},
                    "type": "spline"
            ]
        }
        render data as JSON
    }


    def getRangeDate(def params){
        Date startDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/Madrid"));
        try{
            startDate = sdf.parse(params.startDate)
        }catch (Exception e){}
        Date endDate= null;
        try{
            endDate = sdf.parse(params.endDate)
        }catch (Exception e){}
        return [endDate:endDate, startDate: startDate]
    }
    private UserReputationEvolutionRSDTO.Interval getIntervalFromParams(def params){
        UserReputationEvolutionRSDTO.Interval interval = UserReputationEvolutionRSDTO.Interval.HOUR
        if (params.interval){
            try{
                interval = params.interval as UserReputationEvolutionRSDTO.Interval
            }catch(java.lang.IllegalArgumentException wrongIntervalException){
                interval = UserReputationEvolutionRSDTO.Interval.HOUR
            }
        }
        return interval;
    }

    private AverageWidgetType getAverageTypeFromParams(def params){
        AverageWidgetType averageWidgetType = AverageWidgetType.RUNNING_AVERAGE
        if (params.averageWidgetType){
            try{
                averageWidgetType = params.averageWidgetType as AverageWidgetType
            }catch(java.lang.IllegalArgumentException wrongAverageTypeException){
                averageWidgetType = AverageWidgetType.RUNNING_AVERAGE
            }
        }
        return averageWidgetType;
    }

    private Double getProperAverage(AverageWidgetType averageWidgetType, def reputationSnapshotRSDTO){
        if (AverageWidgetType.GLOBAL_AVERAGE== averageWidgetType){
            return reputationSnapshotRSDTO.averageEvaluation
        }else{
            return reputationSnapshotRSDTO.runningAverage
        }
    }

    def loadRating(String userAlias){
        KuorumUser user = KuorumUser.findByAlias(userAlias)
        UserReputationRSDTO userReputationRSDTO = userReputationService.getReputationWithCache(user)
        render template: "/kuorumUser/popoverUserRating", model: [user:user,userReputation: userReputationRSDTO]
    }

}
