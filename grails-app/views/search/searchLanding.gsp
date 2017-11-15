<%@ page import="kuorum.core.model.solr.SolrType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.landing.discover" args="[searchParams.word?'| '+searchParams.word:'']"/></title>
    <meta name="layout" content="landingLayout"/>
    <parameter name="showHeadSearch" value="false"/>
    <parameter name="special-cssClass" value="noResults"/>
    <parameter name="transparentHead" value="true"/>
    <g:render template="/dashboard/landingMetaTags"
              model="[
                      kuorumTitle:g.message(code:'page.title.landing.discover'),
                      kuorumDescription:g.message(code:'page.title.landing.discover.description'),
                      kuorumImage:r.resource(dir:'images', file:'landingSearch-rrss.png', absolute:true)
//                      kuorumImage:request.siteUrl +r.resource(dir:'images', file:'background-search.jpg')
              ]"/>
    <link rel="canonical" href="${g.createLink(mapping:"landingSearch", params: [])}"/>

    <script type="application/ld+json">
    {
      "@context": "http://schema.org",
      "@type": "WebSite",
      "url": "https://${request.serverName}",
      "potentialAction": {
        "@type": "SearchAction",
        "target": "https://${request.serverName}/search?word={word}",
        "query-input": "required name=word"
      }
    }
    </script>
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
            <ul class="search-list clearfix" id="search-list-id">
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
            <input type="hidden" name="searchType" value="${searchParams.searchType}" />
            <input type="hidden" name="regionCode" value="${params.regionCode}" />
        </nav:loadMoreLink>
    </div>
</content>

<content tag="lastCallToAction">
    <h3><g:message code="landingSearch.lastCallToAction.title"/> </h3>
    <g:link mapping="register" class="btn btn-lg">
        <g:message code="landingPage.fastRegister.start"/>
    </g:link>
</content>

</html>
