<%@ page import="kuorum.core.model.VoteType; kuorum.core.model.PostType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title>${project.shortName}</title>
    <meta name="layout" content="columnCLayout">
    <g:render template="projectMetaTags" model="[project:project]"/>
</head>


<content tag="subHeader">
    <g:render template="/project/projectSubHeader" model="[project: project, projectStats:projectStats, regionStats:regionStats, userVote:userVote]"/>
</content>


<content tag="mainContent">
    <g:render template="/project/projectInfo" model="[project:project, victories: victories, readMore:true]"/>
    <g:render template="/project/projectUpdates" model="[project:project]"/>

    <g:if test="${victories}">
        <g:render template="projectVictories" model="[project:project, victories:victories]"/>
    </g:if>
    <g:if test="${clucks}">
        <g:render template="projectClucks" model="[project:project, clucks:clucks, seeMore:seeMore]"/>
    </g:if>
</content>

<content tag="cColumn">

    <modulesUtil:projectVotes project="${project}" social="true" title="true"/>
    <modulesUtil:projectActivePeople project="${project}"/>
    <modulesUtil:recommendedPosts project="${project}" title="${message(code:"modules.recommendedProjectPosts.title")}"/>

</content>

<content tag="footerStats">

    <modulesUtil:delayedModule mapping="ajaxModuleProjectBottomStats" params="[hashtag:project.hashtag.decodeHashtag()]" elementId="idAjaxModuleProjectBottomStats"/>
    %{--<g:include controller="modules" action="bottomProjectStats" params="[project:project]"/>--}%
    <a href="#main" class="smooth top">
        <span class="fa fa-caret-up fa-lg"></span>
        <g:message code="project.up"/>
    </a>
</content>


