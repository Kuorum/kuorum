<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.search"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="search" />
    %{--<parameter name="idLeftMenu" value="search-filters" />--}%
    <parameter name="idMainContent" value="search-results" />
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

    <div class="row">
        <ul class="kakareo-list" id="search-list-id">
            <g:render template="searchElement" model="[docs:docs.elements]"/>
        </ul>
    </div>
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
                        <span class="fa fa-lightbulb-o"></span> <g:message code="search.filters.SolrType.POST"/>
                    </label>
                </div>
            </li>
            <li>
                <div class="checkbox">
                    <label>
                        <input type="checkbox" name="type" id="proyectos" value="${kuorum.core.model.solr.SolrType.PROJECT}" ${searchParams.type == kuorum.core.model.solr.SolrType.PROJECT?'checked':''}>
                        <span class="symbol">#</span> <g:message code="search.filters.SolrType.PROJECT"/>
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
        %{--<ol class="list-unstyled">--}%
            %{--<li>--}%
                %{--<div class="checkbox">--}%
                    %{--<label>--}%
                        %{--<input type="checkbox" id="todo" ${searchParams.subTypes?.size()==kuorum.core.model.solr.SolrSubType.values().size()?'checked':''}>--}%
                        %{--<span class="fa fa-search"></span>--}%
                        %{--<g:message code="search.filters.all"/>--}%
                    %{--</label>--}%
                %{--</div>--}%
                %{--<ul>--}%
                    %{--<g:each in="${kuorum.core.model.solr.SolrType.values()}" var="solrType">--}%
                        %{--<li>--}%
                            %{--<div class="checkbox">--}%
                                %{--<label>--}%
                                    %{--<input value='${solrType}' type="checkbox" id="${solrType}" ${searchParams.subTypes.findAll{it.solrType==solrType}.size()==solrType.solrSubTypes.size()?'checked':''}>--}%
                                    %{--<span class="fa ${postUtil.cssIconSolrType(solrType:solrType)}"></span>--}%
                                    %{--<g:message code="search.filters.SolrType.${solrType}"/>--}%
                                %{--</label>--}%
                            %{--</div>--}%
                            %{--<ul>--}%
                                %{--<g:each in="${solrType.solrSubTypes}" var="solrSubType">--}%
                                    %{--<li>--}%
                                        %{--<div class="checkbox">--}%
                                            %{--<label>--}%
                                                %{--<input name="subTypes" value='${solrSubType}'type="checkbox" class="only" ${searchParams.subTypes?.contains(solrSubType)?'checked':''}>--}%
                                                %{--<span class="fa ${postUtil.cssIconSolrSubType(solrSubType: solrSubType)}"></span>--}%
                                                %{--<g:message code="search.filters.SolrSubType.${solrSubType}"/>--}%
                                            %{--</label>--}%
                                        %{--</div>--}%
                                    %{--</li>--}%
                                %{--</g:each>--}%
                            %{--</ul>--}%
                        %{--</li>--}%
                    %{--</g:each>--}%
                %{--</ul>--}%
            %{--</li>--}%
        %{--</ol>--}%
        <input type="hidden" name="word" value="${searchParams.word}" />
    </g:form>
</content>