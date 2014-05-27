<%@ page import="kuorum.core.model.VoteType; kuorum.core.model.PostType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title>${law.shortName}</title>
    <meta name="layout" content="columnCLayout">
    <g:render template="lawMetaTags" model="[law:law]"/>
</head>


<content tag="mainContent">
    <g:render template="/law/lawInfo" model="[law:law, victories: victories, readMore:true]"/>

    <aside class="participate">
        <h1><g:message code="law.createPost.title"/> </h1>
        <p><g:message code="law.createPost.description"/> </p>
        <div class="row">
            <div class="col-xs-12 col-sm-4 col-md-4">
                <g:link mapping="postCreate" params="${law.encodeAsLinkProperties()+[postType:PostType.HISTORY]}">
                    <span class="fa fa-comment fa-2x"></span><br>
                    <g:message code="${kuorum.core.model.PostType.class.name}.${kuorum.core.model.PostType.HISTORY}"/>
                </g:link>
                <p><g:message code="law.createPost.${kuorum.core.model.PostType.HISTORY}.description"/> </P>
            </div>
            <div class="col-xs-12 col-sm-4 col-md-4">
                <g:link mapping="postCreate" params="${law.encodeAsLinkProperties()+[postType:PostType.QUESTION]}">
                    <span class="fa fa-question-circle fa-2x"></span><br>
                    <g:message code="${kuorum.core.model.PostType.class.name}.${kuorum.core.model.PostType.QUESTION}"/>
                </g:link>
                <p><g:message code="law.createPost.${kuorum.core.model.PostType.QUESTION}.description"/> </P>
            </div>
            <div class="col-xs-12 col-sm-4 col-md-4">
                <g:link mapping="postCreate" params="${law.encodeAsLinkProperties()+[postType:PostType.PURPOSE]}">
                    <span class="fa fa-lightbulb-o fa-2x"></span><br>
                    <g:message code="${kuorum.core.model.PostType.class.name}.${kuorum.core.model.PostType.PURPOSE}"/>
                </g:link>
                <p><g:message code="law.createPost.${kuorum.core.model.PostType.PURPOSE}.description"/> </P>
            </div>
        </div>
    </aside>

    <g:if test="${victories}">
        <g:render template="lawVictories" model="[law:law, victories:victories]"/>
    </g:if>
    <aside class="participAll">
        <h1><g:message code="law.clucks.title"/> </h1>
        <p><g:message code="law.clucks.description"/> </p>
        <!-- COMIENZA LISTA DE KAKAREOS -->
        <g:set var="urlLoadMore" value="${createLink(mapping: 'lawListClucks', params: law.encodeAsLinkProperties())}"/>
        <g:render template="/cluck/listClucks" model="[clucks:clucks, urlLoadMore:urlLoadMore, seeMore:seeMore]"/>
        %{--<ul class="kakareo-list" role="log" aria-live="assertive" aria-relevant="additions">--}%
            %{--<g:each in="${clucks}" var="cluck">--}%
                %{--<g:render template="/cluck/cluck" model="[cluck:cluck]"/>--}%
            %{--</g:each>--}%
        %{--</ul>--}%
    </aside>
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
