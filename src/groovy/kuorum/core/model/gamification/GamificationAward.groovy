package kuorum.core.model.gamification

/**
 * Created by iduetxe on 24/03/14.
 */
enum GamificationAward {
    ROLE_DEFAULT(0, 0, 0),
    ROLE_MILITANTE(10, 5, 30),
    ROLE_ACTIVISTA(20, 10, 50),
    ROLE_LIDER_OPINION(30, 25, 500),
    ROLE_MAESTRO_JEDI(50,50,2000),

    STATS_OWN_HISTORY(20,10,100),
    STATS_OWN_QUESTION(25,5,250),
    STATS_OWN_PURPOSE(30,15,500);

    Integer numEggs
    Integer numPlumes
    Integer numCorns

    GamificationAward(Integer numEggs, Integer numPlumes, Integer numCorns){
        this.numCorns = numCorns
        this.numPlumes = numPlumes
        this.numEggs = numEggs
    }
}
