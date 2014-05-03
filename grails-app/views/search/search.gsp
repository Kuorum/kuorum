<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.login"/> </title>
    <meta name="layout" content="leftMenuLayout">
</head>



<content tag="mainContent">
    <div class="clearfix">
        <h1><g:message code="search.head.title.filterType.${seachParams.type}" args="[seachParams.word.encodeAsHTML()]"/> </h1>
        <div id="results"><g:message code="search.head.title.numResults" args="[docs.numResults]"/> </div>
    </div>
    <g:if test="${docs.suggest}">
        <p><g:message code="search.spelling"/> <g:link mapping="searcherSearch" params="[word:docs.suggest.suggestedQuery]" > ${docs.suggest.suggestedQuery} </g:link>(${docs.suggest.hits})</p>
    </g:if>

    <ul id="search-list-id">
        <g:render template="searchElement" model="[docs:docs.elements]"/>
    </ul>

    <div id="load-more" class="text-center">
        <g:link mapping="searcherSearchSeeMore" class="loadMore" data-parent-id="search-list-id" data-form-id="search-form">
            <g:message code="search.list.seeMore"/>
        </g:link>
    </div>
</content>

<content tag="leftMenu">
    <h1><g:message code="search.filters.title"/></h1>
    <form role="form" id="searchFilters">
        <ol class="list-unstyled">
            <li>
                <div class="checkbox">
                    <label>
                        <input type="checkbox" id="todo" ${seachParams.subTypes.size()==kuorum.core.model.solr.SolrSubType.values().size()?'checked':''}>
                        <span class="fa fa-search"></span>
                        <g:message code="search.filters.all"/>
                    </label>
                </div>
                <ul>
                    <g:each in="${kuorum.core.model.solr.SolrType.values()}" var="solrType">
                        <li>
                            <div class="checkbox">
                                <label>
                                    <input type="checkbox" id="${solrType}" ${seachParams.subTypes.findAll{it.solrType==solrType}.size()==solrType.solrSubTypes.size()?'checked':''}>
                                    <span class="fa ${postUtil.cssIconSolrType(solrType:solrType)}"></span>
                                    <g:message code="search.filters.SolrType.${solrType}"/>
                                </label>
                            </div>
                            <ul>
                                <g:each in="${solrType.solrSubTypes}" var="solrSubType">
                                    <li>
                                        <div class="checkbox">
                                            <label>
                                                <input type="checkbox" id="propuestas" class="only" ${seachParams.subTypes.contains(solrSubType)?'checked':''}>
                                                <span class="fa ${postUtil.cssIconSolrSubType(solrSubType: solrSubType)}"></span>
                                                <g:message code="search.filters.SolrSubType.${solrSubType}"/>
                                            </label>
                                        </div>
                                    </li>
                                </g:each>
                            </ul>
                        </li>
                    </g:each>
                </ul>
            </li>
        </ol>
    </form>
</content>