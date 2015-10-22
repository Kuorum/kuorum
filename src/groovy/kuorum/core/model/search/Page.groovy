package kuorum.core.model.search

/**
 * This class represents a page of a large result
 */
class Page<T> {

    List<T> data

    Long totalElements

    Long numPages

    Long currentPage

}
