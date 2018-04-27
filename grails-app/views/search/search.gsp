<%@ page import="kuorum.core.model.solr.SolrType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.search" args="[searchParams.word?:'', kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.name]"/></title>
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

    %{--<ul class="${cssClassUL}" id="search-list-id">--}%
    <ul class="search-list clearfix" id="search-list-id">
        <g:render template="searchElement" model="[docs:docs.data]"/>
    </ul>
    <g:render template="searchActionsPagination" model="[searchParams:searchParams,docs:docs]"/>
</content>

<content tag="leftMenu">
    <h1><g:message code="search.filters.title"/></h1>
    <g:form mapping="searcherSearch" role="form" name="searchFilters" rel="nofollow" method="GET">
        <ul>
            <li>
                <div class="checkbox">
                    <label>
                        <input type="checkbox" name="type" id="all-types" value="" ${!searchParams.type?'checked':''}/>
                        <g:link mapping="searcherSearch" params="${params.findAll {k,v-> k!='type' && k!='offset' && v}}">
                            <span class="icon-user"></span>
                            <g:message code="search.filters.SolrType.ALL"/>
                            %{--<g:set var="hits" value="${docs.facets.type.sum{it.hits}}"/>--}%
                            %{--<g:if test="${hits}">(${hits})</g:if>--}%
                        </g:link>
                    </label>
                </div>
            </li>
            <li>
                <div class="checkbox">
                    <label>
                        <input type="checkbox" name="type" id="ciudadanos" value="${kuorum.core.model.solr.SolrType.KUORUM_USER}" ${searchParams.type == kuorum.core.model.solr.SolrType.KUORUM_USER?'checked':''}>
                        <g:link mapping="searcherSearchKUORUM_USER" params="${params.findAll {k,v-> k!='type' && k!='offset' && v}}">
                            <span class="icon-user"></span>
                            <g:message code="search.filters.SolrType.KUORUM_USER"/>
                            %{--<g:set var="hits" value="${docs.facets.type.find{it.facetName==kuorum.core.model.solr.SolrType.KUORUM_USER.toString()}?.hits?:0}"/>--}%
                            %{--<g:if test="${hits}">(${hits})</g:if>--}%
                        </g:link>
                    </label>
                </div>
            </li>
            <li>
                <div class="checkbox">
                    <label>
                        <input type="checkbox" name="type" id="search-post" value="${kuorum.core.model.solr.SolrType.POST}" ${searchParams.type == kuorum.core.model.solr.SolrType.POST?'checked':''}>
                        <g:link mapping="searcherSearchPOST" params="${params.findAll {k,v-> k!='type' && k!='offset' && v}}">
                            <span class="fa fa-newspaper-o"></span>
                            <g:message code="search.filters.SolrType.POST"/>
                            %{--<g:set var="hits" value="${docs.facets.type.find{it.facetName==kuorum.core.model.solr.SolrType.POST.toString()}?.hits?:0}"/>--}%
                            %{--<g:if test="${hits}">(${hits})</g:if>--}%
                        </g:link>
                    </label>
                </div>
            </li>
            <li>
                <div class="checkbox">
                    <label>
                        <input type="checkbox" name="type" id="search-debates" value="${kuorum.core.model.solr.SolrType.DEBATE}" ${searchParams.type == kuorum.core.model.solr.SolrType.DEBATE?'checked':''}>
                        <g:link mapping="searcherSearchDEBATE" params="${params.findAll {k,v-> k!='type' && k!='offset' && v}}">
                            <span class="fa fa-comments-o"></span>
                            <g:message code="search.filters.SolrType.DEBATE"/>
                            %{--<g:set var="hits" value="${docs.facets.type.find{it.facetName==kuorum.core.model.solr.SolrType.DEBATE.toString()}?.hits?:0}"/>--}%
                            %{--<g:if test="${hits}">(${hits})</g:if>--}%
                        </g:link>
                    </label>
                </div>
            </li>
            <li>
                <div class="checkbox">
                    <label>
                        <input type="checkbox" name="type" id="search-surveys" value="${kuorum.core.model.solr.SolrType.SURVEY}" ${searchParams.type == kuorum.core.model.solr.SolrType.SURVEY?'checked':''}>
                        <g:link mapping="searcherSearchSURVEY" params="${params.findAll {k,v-> k!='type' && k!='offset' && v}}">
                            <span class="fa fa-pie-chart"></span>
                            <g:message code="search.filters.SolrType.SURVEY"/>
                        </g:link>
                    </label>
                </div>
            </li>
            <li>
                <div class="checkbox">
                    <label>
                        <input type="checkbox" name="type" id="search-debates" value="${kuorum.core.model.solr.SolrType.EVENT}" ${searchParams.type == kuorum.core.model.solr.SolrType.EVENT?'checked':''}>
                        <g:link mapping="searcherSearchEVENT" params="${params.findAll {k,v-> k!='type' && k!='offset' && v}}">
                            <span class="fa fa-calendar-check-o"></span>
                            <g:message code="search.filters.SolrType.EVENT"/>
                            %{--<g:set var="hits" value="${docs.facets.type.find{it.facetName==kuorum.core.model.solr.SolrType.DEBATE.toString()}?.hits?:0}"/>--}%
                            %{--<g:if test="${hits}">(${hits})</g:if>--}%
                        </g:link>
                    </label>
                </div>
            </li>
        </ul>
        <input type="hidden" name="word" value="${searchParams.word}" />
        <input type="hidden" name="searchType" value="${searchParams.searchType}" />
        <input type="hidden" name="regionCode" value="${params.regionCode}" />
    </g:form>

    <g:set var="relevantCauses" value="${docs.facetCause.findAll{it.hits>0}.sort{-it.hits}.take(10)}"/>
    <g:if test="${relevantCauses}">
        <h2><g:message code="search.filters.causes.title"/></h2>
        <ul class="causes-tags">
            <g:each in="${relevantCauses}" var="tag">
                <li class="cause link-wrapper active" id="cause-participacion">
                    <g:link mapping="searcherSearchByCAUSE" params="${params.findAll {k,v-> k!='searchType' && k!='offset' && v} + [word:tag.name]}" class="sr-only hidden"> Search cause ${tag.name}</g:link>
                    <div class="cause-name" aria-hidden="true" tabindex="104">
                        <span class="fa fa-hashtag"></span>
                        <span>${tag.name} (${tag.hits})</span>
                    </div>
                </li>
            </g:each>
        </ul>

    </g:if>
</content>
</html>