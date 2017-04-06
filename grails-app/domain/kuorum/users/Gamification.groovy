package kuorum.users

import kuorum.core.model.gamification.GamificationAward

/**
 * This object abtracts the personal data to store and handle it easily
 */
@Deprecated
class Gamification {

    Integer numEggs = 0
    Integer numPlumes = 0
    Integer numCorns = 0

    GamificationAward activeRole = GamificationAward.ROLE_DEFAULT

    List<GamificationAward> boughtAwards = [GamificationAward.ROLE_DEFAULT ]

    static constraints = {
        numEggs min:0
        numPlumes min:0
        numCorns min:0
    }
}
