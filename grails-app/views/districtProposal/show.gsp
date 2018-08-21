<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <g:set var="titleMessageCode" value="districtProposal.show.title"/>
    <title><g:message code="${titleMessageCode}" args="[districtProposal.title, _domainName]"/></title>
    <meta name="layout" content="columnCLayout">
    <g:set var="schemaData" value="${[schema:'http://schema.org/Article', name:districtProposal.title]}" scope="request"/>
    <g:render template="/campaigns/showModules/campaignMetaTags" model="[campaign: districtProposal, titleMessageCode:titleMessageCode]"/>
    <r:require modules="districtProposal"/>
</head>

<content tag="mainContent">
    <g:render template="/districtProposal/showModules/mainContent" model="[districtProposal: districtProposal, campaignUser: campaignUser]"/>
</content>

<content tag="cColumn">
    <g:render template="/districtProposal/showModules/cCallToAction" model="[districtProposal: districtProposal, campaignUser: campaignUser]"/>
</content>

