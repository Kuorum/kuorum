package kuorum.core.model.Law

import kuorum.Region
import kuorum.law.AcumulativeVotes

/**
 * Created by iduetxe on 1/06/14.
 */
class LawRegionStats {
    Region region
    AcumulativeVotes totalVotes
    AcumulativeVotes femaleVotes
    AcumulativeVotes maleVotes
    AcumulativeVotes organizationVotes
}
