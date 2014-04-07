package kuorum.core.model.gamification

/**
 * Created by iduetxe on 24/03/14.
 */
enum GamificationAward {
    ROLE_DEFAULT            (0,     0,    0, false),
    ROLE_MILITANTE          (10,    5,   30, false),
    ROLE_ACTIVISTA          (20,   10,   50, false),
    ROLE_LIDER_OPINION      (30,   25,  500, false),
    ROLE_MAESTRO_JEDI       (50,   50, 2000, false),

    STATS_OWN_HISTORY       (20,   10,  100, true),
    STATS_OWN_QUESTION      (25,    5,  250, true),
    STATS_OWN_PURPOSE       (30,   15,  500, true);

    Integer numEggs
    Integer numPlumes
    Integer numCorns
    Boolean alwaysActive

    GamificationAward(Integer numEggs, Integer numPlumes, Integer numCorns, Boolean alwaysActive){
        this.numCorns = numCorns
        this.numPlumes = numPlumes
        this.numEggs = numEggs
        this.alwaysActive = alwaysActive
    }
}
