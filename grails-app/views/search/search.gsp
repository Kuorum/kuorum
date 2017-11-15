<%@ page import="kuorum.core.model.solr.SolrType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.search" args="[searchParams.word]"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="search" />
    %{--<parameter name="idLeftMenu" value="search-filters" />--}%
    %{--<parameter name="idMainContent" value="search-results" />--}%
</head>



<content tag="mainContent">
    <g:render template="searchActionsPagination" model="[searchParams:searchParams,docs:docs]"/>
    %{--<g:if test="${docs.suggest}">--}%
        %{--<p><g:message code="search.spelling"/> <g:link mapping="searcherSearch" params="[word:docs.suggest.suggestedQuery, type:searchParams.type]" > ${docs.suggest.suggestedQuery} </g:link>(${docs.suggest.hits})</p>--}%
    %{--</g:if>--}%

    <g:set var="cssClassUL" value=""/>
    <g:if test="${searchParams.type == SolrType.DEBATE}">
        <g:set var="cssClassUL" value="campaign-list clearfix"/>
    </g:if>
    <g:elseif test="${searchParams.type == SolrType.POST}">
        <g:set var="cssClassUL" value="campaign-list clearfix"/>
    </g:elseif>
    <g:else>
        <g:set var="cssClassUL" value="politician-list clearfix"/>
    </g:else>

    %{--<ul class="${cssClassUL}" id="search-list-id">--}%
    <ul class="search-list clearfix" id="search-list-id">
        <g:render template="searchElement" model="[docs:docs.elements]"/>
    </ul>

    <g:render template="searchActionsPagination" model="[searchParams:searchParams,docs:docs]"/>
    %{--<nav:loadMoreLink--}%
            %{--formId="search-form-loadMore"--}%
            %{--mapping="searcherSearchSeeMore"--}%
            %{--parentId="search-list-id"--}%
        %{--pagination="${searchParams}"--}%
        %{--numElements="${docs.numResults}"--}%
    %{-->--}%
        %{--<input type="hidden" name="word" value="${searchParams.word}" />--}%
        %{--<input type="hidden" name="type" value="${searchParams.type}" />--}%
        %{--<input type="hidden" name="searchType" value="${searchParams.searchType}" />--}%
        %{--<input type="hidden" name="regionCode" value="${params.regionCode}" />--}%
    %{--</nav:loadMoreLink>--}%
</content>

<content tag="leftMenu">
    <h1><g:message code="search.filters.title"/></h1>
    %{--<g:form mapping="searcherSearchFilters" role="form" name="searchFilters" data-updateElementId="search-list-id" rel="nofollow">--}%
    <g:form mapping="searcherSearch" role="form" name="searchFilters" rel="nofollow" method="GET">
        <ul>
            <li>
                <div class="checkbox">
                    <label>
                        <input type="checkbox" name="type" id="ciudadanos" value="${kuorum.core.model.solr.SolrType.KUORUM_USER}" ${searchParams.type == kuorum.core.model.solr.SolrType.KUORUM_USER?'checked':''}>
                        <span class="icon-user"></span> <g:message code="search.filters.SolrType.KUORUM_USER"/> (${docs.facets.type.find{it.facetName==kuorum.core.model.solr.SolrType.KUORUM_USER.toString()}?.hits?:0})
                    </label>
                </div>
            </li>
            <li>
                <div class="checkbox">
                    <label>
                        <input type="checkbox" name="type" id="search-post" value="${kuorum.core.model.solr.SolrType.POST}" ${searchParams.type == kuorum.core.model.solr.SolrType.POST?'checked':''}>
                        <span class="fa fa-newspaper-o"></span> <g:message code="search.filters.SolrType.POST"/> (${docs.facets.type.find{it.facetName==kuorum.core.model.solr.SolrType.POST.toString()}?.hits?:0})
                    </label>
                </div>
            </li>
            <li>
                <div class="checkbox">
                    <label>
                        <input type="checkbox" name="type" id="search-debates" value="${kuorum.core.model.solr.SolrType.DEBATE}" ${searchParams.type == kuorum.core.model.solr.SolrType.DEBATE?'checked':''}>
                        <span class="fa fa-comments-o"></span> <g:message code="search.filters.SolrType.DEBATE"/>(${docs.facets.type.find{it.facetName==kuorum.core.model.solr.SolrType.DEBATE.toString()}?.hits?:0})
                    </label>
                </div>
            </li>
        </ul>
        <input type="hidden" name="word" value="${searchParams.word}" />
        <input type="hidden" name="searchType" value="${searchParams.searchType}" />
        <input type="hidden" name="regionCode" value="${params.regionCode}" />
    </g:form>
</content>
</html>