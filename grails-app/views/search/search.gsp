<%@ page import="kuorum.core.model.solr.SolrType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.search" args="[searchParams.word?:'', kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.name]"/></title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="search" />
    %{--<parameter name="idLeftMenu" value="search-filters" />--}%
    %{--<parameter name="idMainContent" value="search-results" />--}%
    <r:require module="search"/>
</head>



<content tag="mainContent">
    <g:render template="searchActionsPagination" model="[searchParams:searchParams,docs:docs]"/>
    %{--<g:if test="${docs.suggest}">--}%
        %{--<p><g:message code="search.spelling"/> <g:link mapping="searcherSearch" params="[word:docs.suggest.suggestedQuery, type:searchParams.solrType]" > ${docs.suggest.suggestedQuery} </g:link>(${docs.suggest.hits})</p>--}%
    %{--</g:if>--}%

    %{--<ul class="${cssClassUL}" id="search-list-id">--}%
    <ul class="search-list clearfix" id="search-list-id">
        <g:render template="searchElement" model="[docs:docs.data]"/>
    </ul>
    <g:render template="searchActionsPagination" model="[searchParams:searchParams,docs:docs]"/>
</content>

<content tag="leftMenu">
    <h1><g:message code="search.filters.title"/></h1>
    <ul class="search-options">
        <li>
            <label>
                <g:link mapping="searcherSearch" params="${params.findAll {k,v-> k!='solrType' && k!='offset' && v}}" class="${!searchParams.solrType?'search-option-active':''}">
                    <g:message code="search.filters.SolrType.ALL"/>
                    %{--<g:set var="hits" value="${docs.facets.type.sum{it.hits}}"/>--}%
                    %{--<g:if test="${hits}">(${hits})</g:if>--}%
                </g:link>
            </label>
        </li>
        <li>
            <label>
                <g:link mapping="searcherSearchKUORUM_USER" params="${params.findAll {k,v-> k!='solrType' && k!='offset' && v}}" class="${searchParams.solrType == kuorum.core.model.solr.SolrType.KUORUM_USER?'search-option-active':''}">
                    <span class="fal fa-user fa-fw"></span>
                    <g:message code="search.filters.SolrType.KUORUM_USER"/>
                    %{--<g:set var="hits" value="${docs.facets.type.find{it.facetName==kuorum.core.model.solr.SolrType.KUORUM_USER.toString()}?.hits?:0}"/>--}%
                    %{--<g:if test="${hits}">(${hits})</g:if>--}%
                </g:link>
            </label>
        </li>
        <g:each in="${_domainActiveCampaigns}" var="activeSolrType">
            <li>
                <label>
                    <g:link mapping="${'searcherSearch'+activeSolrType.toString()}" params="${params.findAll {k,v-> k!='solrType' && k!='offset' && v}}" class="${searchParams.solrType == activeSolrType?'search-option-active':''}">
                        <span class="fal ${activeSolrType.faIcon} fa-fw"></span>
                        <g:message code="search.filters.SolrType.${activeSolrType}"/>
                    %{--<g:set var="hits" value="${docs.facets.type.find{it.facetName==kuorum.core.model.solr.SolrType.POST.toString()}?.hits?:0}"/>--}%
                    %{--<g:if test="${hits}">(${hits})</g:if>--}%
                    </g:link>
                </label>
            </li>
        </g:each>
    </ul>

    <g:set var="relevantCauses" value="${docs.facetCause.findAll{it.hits>0}.sort{-it.hits}.take(10)}"/>
    <g:if test="${relevantCauses}">
        <h2><g:message code="search.filters.causes.title"/></h2>
        <ul class="causes-tags">
            <g:each in="${relevantCauses}" var="tag">
                <li class="cause link-wrapper active" id="cause-participacion">
                    <g:link mapping="searcherSearchByCAUSE" params="${params.findAll {k,v-> k!='searchType' && k!='offset' && v} + [word:tag.name]}" class="sr-only hidden"> Search cause ${tag.name}</g:link>
                    <div class="cause-name" aria-hidden="true" tabindex="104">
                        <span class="fal fa-hashtag"></span>
                        <span>${tag.name} (${tag.hits})</span>
                    </div>
                </li>
            </g:each>
        </ul>

    </g:if>
</content>
</html>