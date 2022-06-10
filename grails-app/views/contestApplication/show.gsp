<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <g:set var="titleMessageCode" value="contestApplication.show.title"/>
    <title><g:message code="${titleMessageCode}" args="[contestApplication.title, _domainName]"/></title>
    <meta name="layout" content="columnCLayout">
    <g:set var="schemaData" value="${[schema: 'http://schema.org/Article', name: contestApplication.title]}"
           scope="request"/>
    <g:render template="/campaigns/showModules/campaignMetaTags"
              model="[campaign: contestApplication, titleMessageCode: titleMessageCode]"/>
    %{--    <r:require modules="participatoryBudget, districtProposal, forms"/>--}%
</head>

<content tag="mainContent">
    <g:render template="/contestApplication/showModules/mainContent"
              model="[contestApplication: contestApplication, campaignUser: campaignUser]"/>
</content>

<content tag="cColumn">
    <g:render template="/contestApplication/showModules/cCallToAction"
              model="[contestApplication: contestApplication, campaignUser: campaignUser, hideXs: true]"/>
    <g:render template="/campaigns/columnCModules/campaignFiles" model="[campaignFiles: campaignFiles]"/>
    <g:render template="/campaigns/columnCModules/campaignFiles" model="[campaignFiles: contactFiles]"/>

    <g:render template="/contestApplication/showModules/cContestApplicationStats"
              model="[contestApplication: contestApplication, campaignUser: campaignUser]"/>
</content>

