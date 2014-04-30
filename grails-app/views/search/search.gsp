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
    <h1>Buscar en</h1>

</content>