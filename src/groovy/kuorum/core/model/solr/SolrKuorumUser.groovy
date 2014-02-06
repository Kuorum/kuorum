package kuorum.core.model.solr

import kuorum.core.model.CommissionType
import kuorum.core.model.Gender
import org.bson.types.ObjectId

/**
 * Created by iduetxe on 1/02/14.
 */
class SolrKuorumUser {

    String id
    String name
    String username
    String email
    Gender gender
    List<CommissionType> relevantCommissions = []

    Date dateCreated
    boolean enabled
}
