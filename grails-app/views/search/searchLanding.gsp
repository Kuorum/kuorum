<%@ page import="kuorum.core.model.solr.SolrType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.search"/> </title>
    <meta name="layout" content="landingLayout"/>
    <parameter name="showHeadSearch" value="false"/>
    <parameter name="special-cssClass" value="noResults"/>
</head>

<content tag="videoAndRegister">
    <g:render template="/search/searchNoLoggedLanding" model="[searchParams:searchParams]"/>
</content>

<content tag="mainContent">
    <div class="container-fluid" id="results-tag">
        <div class="order-options">
            Order by <span data-toggle="popover" role="button" rel="popover" class="popover-trigger">relevance</span>
            <div class="popover">
                <ul>
                    <li><a href="#" class="active">relevance</a></li>
                    <li><a href="#">proximity</a></li>
                    <li><a href="#">followers</a></li>
                </ul>
            </div>
        </div>

        <div class="row">

            <ul class="politician-list clearfix" id="search-list-id">
                <g:render template="searchElement" model="[docs:docs.elements]"/>
            </ul>
        </div>

        <nav:loadMoreLink
                formId="search-form-loadMore"
                mapping="searcherSearchSeeMore"
                parentId="search-list-id"
                class="searchNoLogged"
                pagination="${searchParams}"
                numElements="${docs.numResults}"
        >
            <input type="hidden" name="word" value="${searchParams.word}" />
            <input type="hidden" name="type" value="${searchParams.type}" />
        </nav:loadMoreLink>
    </div>
</content>
</html>
