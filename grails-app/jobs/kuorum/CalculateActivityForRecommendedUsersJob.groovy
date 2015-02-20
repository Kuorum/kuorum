package kuorum

import groovy.time.TimeCategory
import kuorum.users.KuorumUserService



class CalculateActivityForRecommendedUsersJob {

    KuorumUserService kuorumUserService

    static triggers = {
        cron name: 'calculateActivityForRecommendedUsers', cronExpression: "0 0 5 * * ?"
    }

    def execute() {
        Date start = new Date()
        log.debug "Start of CalculateActivityForRecommendedUsersJob on $start ..."
        kuorumUserService.recommendedUsersByActivity()
        log.debug "... End of CalculateActivityForRecommendedUsersJob on ${new Date()} with total execution time ${TimeCategory.minus(new Date(),start)}"
    }
}
