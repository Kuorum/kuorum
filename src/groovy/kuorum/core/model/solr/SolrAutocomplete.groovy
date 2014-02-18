package kuorum.core.model.solr

/**
 * Created by iduetxe on 18/02/14.
 */
class SolrAutocomplete {
    Long numResults
    ArrayList<String> suggests
    ArrayList<SolrLaw> laws
    ArrayList<SolrKuorumUser> kuorumUsers
}
