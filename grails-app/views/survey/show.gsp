<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <g:set var="titleMessageCode" value="survey.show.title"/>
    <title><g:message code="${titleMessageCode}" args="[survey.title, _domainName]"/></title>
    <meta name="layout" content="columnCLayout">
    <g:set var="schemaData" value="${[schema:'http://schema.org/Article', name:survey.title]}" scope="request"/>
    <g:render template="/campaigns/showModules/campaignMetaTags" model="[campaign: survey, titleMessageCode:titleMessageCode]"/>
    <r:require modules="survey, forms"/>
</head>

<content tag="mainContent">
    <g:render template="/survey/showModules/mainContent" model="[survey: survey, campaignUser: campaignUser]" />
</content>

<content tag="cColumn">
    <g:render template="/survey/showModules/cCallToAction" model="[survey: survey]"/>
    <g:render template="/campaigns/columnCModules/campaignFiles" model="[campaignFiles:campaignFiles]"/>
    <g:render template="/survey/showModules/cSurveyStats" model="[survey: survey,campaignUser: campaignUser, displayTimeZone:displayTimeZone]"/>
</content>

