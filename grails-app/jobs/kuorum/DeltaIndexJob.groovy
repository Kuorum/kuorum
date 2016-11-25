package kuorum

import kuorum.solr.IndexSolrService

class DeltaIndexJob {
    static triggers = {
      simple repeatInterval: 60000l // execute job once in 60 seconds
    }

    IndexSolrService indexSolrService

    def execute() {
        indexSolrService.deltaIndex()
    }
}
