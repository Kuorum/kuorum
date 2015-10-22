package kuorum.core.model.search

/**
 * This class represents a page of a large result
 */
class Page<T> {

    public Page(List<T> data, Pagination pagination, Long totalElements){
        this.data = data
        this.totalElements = totalElements
        this.numPages = Math.round( (totalElements/pagination.max)+0.5)
        this.currentPage = pagination.offset
    }

    List<T> data

    Long totalElements

    Long numPages

    Long currentPage

}
