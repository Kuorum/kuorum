<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <g:set var="titleMessageCode" value="petition.show.title"/>
    <title><g:message code="${titleMessageCode}" args="[petition.title, _domainName]"/></title>
    <meta name="layout" content="columnCLayout">
    <g:set var="schemaData" value="${[schema:'http://schema.org/Article', name:petition.title]}" scope="request"/>
    <g:render template="/campaigns/showModules/campaignMetaTags" model="[campaign: petition, titleMessageCode:titleMessageCode]"/>
    <r:require modules="petition"/>

</head>

<content tag="mainContent">
    <g:render template="/petition/showModules/mainContent" model="[petition: petition, petitionUser: petitionUser]" />
</content>

<content tag="cColumn">
    <g:render template="/petition/showModules/cCallToAction" model="[petition: petition, campaignUser: petitionUser]"/>
    <g:render template="/petition/showModules/mainContent/cPetitionStats" model="[petition: petition, campaignUser: petitionUser]"/>
</content>
