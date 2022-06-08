<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <g:set var="titleMessageCode" value="contestApplication.show.title"/>
    <title><g:message code="${titleMessageCode}" args="[contestApplication.title, _domainName]"/></title>
    <meta name="layout" content="columnCLayout">
    <g:set var="schemaData" value="${[schema: 'http://schema.org/Article', name: contestApplication.title]}"
           scope="request"/>
    <g:render template="/campaigns/showModules/campaignMetaTags"
              model="[campaign: contest, titleMessageCode: titleMessageCode]"/>
    %{--    <r:require modules="participatoryBudget, districtProposal, forms"/>--}%
</head>

<content tag="mainContent">
    <g:render template="/contestApplication/showModules/mainContent"
              model="[contestApplication: contestApplication, campaignUser: campaignUser]"/>
</content>

<content tag="cColumn">
    <g:render template="/contestApplication/showModules/cCallToAction"
              model="[contest: contestApplication, campaignUser: campaignUser, hideXs: true]"/>
    <g:render template="/campaigns/columnCModules/campaignFiles" model="[campaignFiles: campaignFiles]"/>
    <g:render template="/contestApplication/showModules/cContestApplicationStats"
              model="[contest: contestApplication, campaignUser: campaignUser]"/>
</content>

