<nav:ifPageProperty pageProperty="showHeadSearch">
    <form
            itemprop="potentialAction" itemscope itemtype="http://schema.org/SearchAction"
            action="${createLink(mapping: 'searcherSearch')}"
            id="search-form"
            class="navbar-form navbar-left"
            role="search"
            method="get"
    >
        <meta itemprop="target" content="${createLink(mapping: 'searcherSearch', absolute: true)+'?word={query}'}"/>
        <div class="input-group">

            <g:set var="searchFilterType" value="${params.searchType?:kuorum.core.model.search.SearchType.ALL}"/>
            <div class="open-filter">
                <a data-target="#" href="#" class="dropdown-toggle" id="open-filter-search" data-toggle="dropdown" role="button">
                    <span class="fa fa-kuorumSearchType-${searchFilterType} fa-lg"></span>
                    <span class="sr-only">Filtra tu búsqueda</span>
                </a>
                <ul id="filters" class="dropdown-menu dropdown-menu-left" aria-labelledby="open-filter-search" role="menu">
                    <g:each in="${kuorum.core.model.search.SearchType.values()}" var="searchType">
                        <li>
                            <a href="#${searchType}" class="search-${searchType} ${searchFilterType==searchType?'active':''}">
                                <span class="fa fa-kuorumSearchType-${searchType} fa-lg"></span>
                                <span class="search-filter-text">${message(code:'search.head.placeHolder.'+searchType)}</span>
                            </a>
                        </li>
                    </g:each>
                </ul>
            </div>
            <input
                    type="text"
                    class="form-control"
                    placeholder="${message(code:'search.head.placeHolder')}"
                    itemprop="query-input"
                    name="word"
                    id="srch-term"
                    value="${params.word.encodeAsRemovingHtmlTags()}">
            <div class="input-group-btn">
                <button class="btn search" type="submit"><span class="fa fa-search"></span></button>
            </div>

            <input type="hidden" name="type" id="srch-userType" value="${params.type}"/>
            <input type="hidden" name="searchType" id="srch-type" value="${params.searchType}" />
            <input type="hidden" name="regionCode" id="srch-regionCode" value="${params.regionCode}" />

        </div>
    </form>
</nav:ifPageProperty>