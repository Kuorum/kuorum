<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <g:set var="titleMessageCode" value="contest.show.title"/>
    <title><g:message code="${titleMessageCode}" args="[contest.title, _domainName]"/></title>
    <meta name="layout" content="columnCLayout">
    <g:set var="schemaData" value="${[schema: 'http://schema.org/Article', name: contest.title]}" scope="request"/>
    <g:render template="/campaigns/showModules/campaignMetaTags"
              model="[campaign: contest, titleMessageCode: titleMessageCode]"/>
    <r:require modules="contest, forms"/>
</head>

<content tag="mainContent">
    <g:render template="/contest/showModules/mainContent" model="[contest: contest, campaignUser: campaignUser]"/>
</content>

<content tag="cColumn">
    <g:render template="/contest/showModules/cCallToAction"
              model="[contest: contest, campaignUser: campaignUser, hideXs: true]"/>
    <g:render template="/contest/showModules/cContestStatus" model="[contest: contest, campaignUser: campaignUser]"/>
    <g:render template="/campaigns/columnCModules/campaignFiles" model="[campaignFiles: campaignFiles]"/>
    <g:render template="/campaigns/columnCModules/campaignFiles"
              model="[campaignFiles: contactFiles, title: g.message(code: 'campaign.show.contact-files.title'), subtitle: g.message(code: 'campaign.show.contact-files.subtitle')]"/>
    <g:render template="/contest/showModules/cContestStats" model="[contest: contest, campaignUser: campaignUser]"/>
</content>
