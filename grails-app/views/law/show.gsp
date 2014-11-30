<%@ page import="kuorum.core.model.VoteType; kuorum.core.model.PostType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title>${law.shortName}</title>
    <meta name="layout" content="columnCLayout">
    <g:render template="lawMetaTags" model="[law:law]"/>
</head>


<content tag="mainContent">
    <g:render template="/law/lawInfo" model="[law:law, victories: victories, readMore:true]"/>

    <g:render template="/law/lawButtonsCreatePost" model="[law:law]"/>

    <g:if test="${victories}">
        <g:render template="lawVictories" model="[law:law, victories:victories]"/>
    </g:if>
    <g:if test="${clucks}">
        <g:render template="lawClucks" model="[law:law, clucks:clucks, seeMore:seeMore]"/>
    </g:if>
</content>

<content tag="cColumn">

    <modulesUtil:lawVotes law="${law}" social="true" title="true"/>
    <modulesUtil:lawActivePeople law="${law}"/>
    <modulesUtil:recommendedPosts law="${law}" title="${message(code:"modules.recommendedLawPosts.title")}"/>

</content>

<content tag="footerStats">

    <modulesUtil:delayedModule mapping="ajaxModuleLawBottomStats" params="[hashtag:law.hashtag.decodeHashtag()]" elementId="idAjaxModuleLawBottomStats"/>
    %{--<g:include controller="modules" action="bottomLawStats" params="[law:law]"/>--}%
    <a href="#main" class="smooth top">
        <span class="fa fa-caret-up fa-lg"></span>
        <g:message code="law.up"/>
    </a>
</content>
