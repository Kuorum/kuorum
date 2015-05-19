<%@ page import="kuorum.core.model.solr.SolrType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.search"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="search" />
    %{--<parameter name="idLeftMenu" value="search-filters" />--}%
    %{--<parameter name="idMainContent" value="search-results" />--}%
</head>



<content tag="mainContent">
    <div class="clearfix">
        <g:if test="${searchParams.word}">
            <h1><g:message code="search.head.title.filterType.${searchParams.type}" args="[searchParams.word?.encodeAsHTML()]"/> </h1>
            <div id="results"><g:message code="search.head.title.numResults" args="[docs.numResults]"/> </div>
        </g:if>
        <g:else>
            <h1><g:message code="search.head.title.noWord"/> </h1>
        </g:else>
    </div>
    <g:if test="${docs.suggest}">
        <p><g:message code="search.spelling"/> <g:link mapping="searcherSearch" params="[word:docs.suggest.suggestedQuery, type:searchParams.type]" > ${docs.suggest.suggestedQuery} </g:link>(${docs.suggest.hits})</p>
    </g:if>

    <g:set var="cssClassUL" value=""/>
    <g:if test="${searchParams.type == SolrType.PROJECT}">
        <g:set var="cssClassUL" value="kakareo-list project clearfix"/>
    </g:if>
    <g:elseif test="${searchParams.type == SolrType.KUORUM_USER || searchParams.type == SolrType.POLITICIAN}">
        <g:set var="cssClassUL" value="politician-list clearfix"/>
    </g:elseif>
    <g:else>
        <g:set var="cssClassUL" value="kakareo-list"/>
    </g:else>

    <ul class="${cssClassUL}" id="search-list-id">
        <g:render template="searchElement" model="[docs:docs.elements]"/>
    </ul>

    <nav:loadMoreLink
            formId="search-form-loadMore"
            mapping="searcherSearchSeeMore"
            parentId="search-list-id"
        pagination="${searchParams}"
        numElements="${docs.numResults}"
    >
        <input type="hidden" name="word" value="${searchParams.word}" />
        <input type="hidden" name="type" value="${searchParams.type}" />
    </nav:loadMoreLink>
</content>

<content tag="leftMenu">
    <h1><g:message code="search.filters.title"/></h1>
    %{--<g:form mapping="searcherSearchFilters" role="form" name="searchFilters" data-updateElementId="search-list-id" rel="nofollow">--}%
    <g:form mapping="searcherSearch" role="form" name="searchFilters" rel="nofollow" method="GET">
        <ul>
            <li>
                <div class="checkbox">
                    <label>
                        <input type="checkbox" name="type" id="propuestas" value="${kuorum.core.model.solr.SolrType.POST}" ${searchParams.type == kuorum.core.model.solr.SolrType.POST?'checked':''}>
                        &nbsp;<span class="fa fa-lightbulb-o"></span>&nbsp;&nbsp;<g:message code="search.filters.SolrType.POST"/>
                    </label>
                </div>
            </li>
            <li>
                <div class="checkbox">
                    <label>
                        <input type="checkbox" name="type" id="proyectos" value="${kuorum.core.model.solr.SolrType.PROJECT}" ${searchParams.type == kuorum.core.model.solr.SolrType.PROJECT?'checked':''}>
                        <span class="fa fa-briefcase"></span> <g:message code="search.filters.SolrType.PROJECT"/>
                    </label>
                </div>
            </li>
            <li>
                <div class="checkbox">
                    <label>
                        <input type="checkbox" name="type" id="politicos" value="${kuorum.core.model.solr.SolrType.POLITICIAN}" ${searchParams.type == kuorum.core.model.solr.SolrType.POLITICIAN?'checked':''}>
                        <span class="icon-user"></span> <g:message code="search.filters.SolrType.POLITICIAN"/>
                    </label>
                </div>
            </li>
            <li>
                <div class="checkbox">
                    <label>
                        <input type="checkbox" name="type" id="ciudadanos" value="${kuorum.core.model.solr.SolrType.KUORUM_USER}" ${searchParams.type == kuorum.core.model.solr.SolrType.KUORUM_USER?'checked':''}>
                        <span class="icon-user"></span> <g:message code="search.filters.SolrType.KUORUM_USER"/>
                    </label>
                </div>
            </li>
        </ul>
        <input type="hidden" name="word" value="${searchParams.word}" />
    </g:form>
</content>