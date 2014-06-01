package kuorum.core.model.Law

import kuorum.Region
import kuorum.core.model.Gender
import kuorum.law.AcumulativeVotes

/**
 * Created by iduetxe on 1/06/14.
 */
class LawRegionStats {
    Region region
    AcumulativeVotes totalVotes
    HashMap<Gender, AcumulativeVotes> genderVotes
}
