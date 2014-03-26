package kuorum.core.model.search

/**
 * Created by iduetxe on 25/03/14.
 */
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
        Math.min(max ?: 10, 100)
    }

    public Long getOffset() {
        offset?offset:0
    }
}
