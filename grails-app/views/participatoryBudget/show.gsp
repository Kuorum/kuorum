<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <g:set var="titleMessageCode" value="participatoryBudget.show.title"/>
    <title><g:message code="${titleMessageCode}" args="[participatoryBudget.title, _domainName]"/></title>
    <meta name="layout" content="columnCLayout">
    <g:set var="schemaData" value="${[schema:'http://schema.org/Article', name:participatoryBudget.title]}" scope="request"/>
    <g:render template="/campaigns/showModules/campaignMetaTags" model="[campaign: participatoryBudget, titleMessageCode:titleMessageCode]"/>
    <r:require modules="participatoryBudget"/>
</head>

<content tag="mainContent">
    <g:render template="/participatoryBudget/showModules/mainContent" model="[participatoryBudget: participatoryBudget, campaignUser: campaignUser]" />
</content>

<content tag="cColumn">
    <g:render template="/participatoryBudget/showModules/cCallToAction" model="[participatoryBudget: participatoryBudget, campaignUser: campaignUser]"/>
    <g:render template="/participatoryBudget/showModules/cParticipatoryBudgetStats" model="[participatoryBudget: participatoryBudget, campaignUser: campaignUser]"/>
    <g:render template="/participatoryBudget/showModules/cParticpatoryBudgetStatus" model="[participatoryBudget: participatoryBudget, campaignUser: campaignUser]"/>
</content>

