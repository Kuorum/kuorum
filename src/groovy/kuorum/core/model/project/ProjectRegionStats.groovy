package kuorum.core.model.project

import kuorum.Region
import kuorum.core.model.Gender
import kuorum.project.AcumulativeVotes

/**
 * Created by iduetxe on 1/06/14.
 */
class ProjectRegionStats {
    Region region
    AcumulativeVotes totalVotes
    HashMap<Gender, AcumulativeVotes> genderVotes
}
