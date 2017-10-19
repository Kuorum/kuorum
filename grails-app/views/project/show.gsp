<%@ page import="kuorum.core.model.VoteType; kuorum.core.model.PostType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title>${project.shortName}</title>
    <meta name="layout" content="columnCLayout">
    <link rel="canonical" href="${g.createLink(mapping:"projectShow", params: project.encodeAsLinkProperties(), absolute:true)}"/>
</head>
<content tag="metaData">
    <g:render template="projectMetaTags" model="[project:project]"/>
</content>

<content tag="subHeader">
    <g:render template="/project/projectSubHeader" model="[project: project, projectBasicStats:projectBasicStats, userVote:userVote]"/>
</content>


<content tag="mainContent">
    <g:render template="/project/projectInfo" model="[project:project, victories: victories, readMore:true, projectBasicStats:projectBasicStats, projectStats:projectStats]"/>
    <g:render template="/project/projectUpdates" model="[project:project]"/>

    %{--<g:if test="${victories}">--}%
        %{--<g:render template="projectVictories" model="[project:project, victories:victories]"/>--}%
    %{--</g:if>--}%
    <g:if test="${posts}">
        <g:render
                template="projectClucks"
                model="[
                        project:project,
                        posts:posts,
                        seeMorePosts:seeMorePosts,
                        numPosts:numPosts,
                        victories:victories,
                        seeMoreVictories:seeMoreVictories,
                        numVictories:numVictories,
                        defends:defends,
                        numDefends:numDefends,
                        seeMoreDefends:seeMoreDefends]"/>
    </g:if>
</content>

<content tag="cColumn">

    <modulesUtil:projectVotes project="${project}" social="true" title="true" basicPersonalDataCommand="${basicPersonalDataCommand}"/>
    <modulesUtil:projectActivePeople project="${project}"/>

</content>


