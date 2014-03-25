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
    Long offset = 0

    public Integer getMax(){
        Math.min(max ?: 10, 100)
    }

    public Integer getOffset() {
        offset?offset:0
    }
}
