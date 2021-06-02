<div class="clearfix search-actions">
    <div class="col-md-6 search-text">
        <g:if test="${searchParams.word}">
            <h5 id="results"><g:message code="search.head.title.filterType.${searchParams.solrType}" args="[searchParams.word?.encodeAsHTML()]"/> <g:message code="search.head.title.numResults" args="[docs.total]"/> </h5>
        </g:if>
        <g:else>
        %{--<h1><g:message code="search.head.title.noWord"/> </h1>--}%
        </g:else>
    </div>
    <div class="pag-list-search col-md-6">
        <nav:contactPagination
                link="${g.createLink(mapping:"searcherSearch", params:[word:searchParams.word, type:searchParams.solrType, searchType:searchParams.searchType, regionCode:params.regionCode], absolute:true)}"
                currentPage="${Math.floor(searchParams.offset/searchParams.max)}"
                sizePage="${searchParams.max}"
                ulClasss="paginationTop"
                total="${docs.total}"/>
    </div>
</div>