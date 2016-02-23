<%@ page import="kuorum.core.model.solr.SolrType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.search"/> </title>
    <meta name="layout" content="landingLayout"/>
    <parameter name="showHeadSearch" value="false"/>
    <parameter name="special-cssClass" value="noResults"/>
    <parameter name="transparentHead" value="true"/>
    <g:render template="/dashboard/landingMetaTags"/>
</head>

<content tag="videoAndRegister">
    <g:render template="/search/searchNoLoggedLanding" model="[searchParams:searchParams]"/>
</content>

<content tag="mainContent">
    <div class="container-fluid" id="results-tag">
            <div class="order-options">
                <g:if env="development">
                    <g:message code="landingSearch.orderBy.text"/>
                    <span data-toggle="popover" role="button" rel="popover" class="popover-trigger">
                        <g:message code="landingSearch.orderBy.option.relevance"/>
                    </span>
                    <div class="popover">
                        <ul>
                            <li><a href="#" class="active"><g:message code="landingSearch.orderBy.option.relevance"/></a></li>
                            <li><a href="#"><g:message code="landingSearch.orderBy.option.proximity"/></a></li>
                            <li><a href="#"><g:message code="landingSearch.orderBy.option.followers"/></a></li>
                        </ul>
                    </div>
                </g:if>
            </div>

        <div class="row">

            <g:set var="columnsCss" value="col-xs-12 col-sm-6 col-md-4"/>
            <ul class="politician-list clearfix" id="search-list-id">
                <g:render template="searchElement" model="[docs:docs.elements, columnsCss:'col-xs-12 col-sm-6 col-md-4']"/>
            </ul>
        </div>

        <nav:loadMoreLink
                formId="search-form-loadMore"
                mapping="searcherSearchSeeMore"
                parentId="search-list-id"
                cssClass="landingSearch"
                pagination="${searchParams}"
                numElements="${docs.numResults}"
        >
            <input type="hidden" name="word" value="${searchParams.word}" />
            <input type="hidden" name="type" value="${searchParams.type}" />
            <input type="hidden" name="columnsCss" value="${columnsCss}" />
        </nav:loadMoreLink>
    </div>
</content>
</html>
