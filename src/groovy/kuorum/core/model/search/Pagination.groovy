package kuorum.core.model.search

import grails.validation.Validateable

/**
 * Created by iduetxe on 25/03/14.
 */
@Validateable
class Pagination {

    /**
     * Num max of elements
     */
    Long max = 10

    /**
     * Offset
     */
    Long offset = 0L

    public Integer getMax(){
        Math.min(max != null && max >= 0? max: 10, 100)
    }

    public Long getOffset() {
        offset?offset:0
    }

    static constraints = {
    }
}
