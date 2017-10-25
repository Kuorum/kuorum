<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.discover"/></title>
    <meta name="layout" content="discoverLayout">
    <!-- Schema.org markup for Google+ -->
    <meta itemprop="name" content="${g.message(code:"kuorum.name")}">
    <meta itemprop="description" content="${g.message(code:"layout.head.meta.description")}">
    <meta itemprop="image" content="${resource(dir: 'images', file:'landingSearch-rrss.png', absolute:true)}" />
    <meta itemprop="image" content="${resource(dir: 'images', file: 'logo@2x.png')}" />
</head>


%{--<content tag="mainContent">--}%

    %{--<g:each in="${relevantProjects}" var="project" status="i">--}%
        %{--<div class="row" style="display:${i!=0?'none':'block'}" id="relevantProject_${i}">--}%
            %{--<section id="main" class="col-xs-12 col-sm-8 col-md-8" role="main">--}%
                %{--<g:render template="/project/projectInfo" model="[project:project, linkToProject:true, hideCallMobileVoteButton:true]"/>--}%
            %{--</section>--}%

            %{--<aside class="col-xs-12 col-sm-4 col-md-4" role="complementary">--}%
                %{--<modulesUtil:projectotes project="${project}"/>--}%
                %{--<modulesUtil:projectActivePeople project="${project}"/>--}%
                %{--<modulesUtil:recommendedPosts project="${project}" title="${message(code:"modules.recommendedProjectPosts.title")}"/>--}%
            %{--</aside>--}%
        %{--</div>--}%
    %{--</g:each>--}%
%{--</content>--}%

<content tag="footerModules">
    %{--<aside class="moreActives" role="complementary">--}%
        %{--<h1><g:message code="discover.module.mostActiveUsers.title"/> </h1>--}%
        %{--<userUtil:showListUsers users="${mostActiveUsers}" visibleUsers="25" messagesPrefix="discover.module.mostActiveUsers.userList"/>--}%
        %{--<p><g:message code="discover.module.mostActiveUsers.footerText"/></p>--}%
    %{--</aside>--}%
    <aside class="row others">
        <modulesUtil:lastCreatedPosts numPost="3" specialCssClass="col-xs-12 col-sm-4 col-md-4"/>


        <div class="col-xs-12 col-sm-4 col-md-4">
            <section>
                <h1><g:message code="discover.module.bestPoliticians.title"/> </h1>
                <userUtil:showListUsers users="${bestPoliticians}" visibleUsers="32" messagesPrefix="discover.module.bestPoliticians.userList"/>
                <p><g:message code="discover.module.bestPoliticians.description"/></p>
            </section>
            <section>
                <h1><g:message code="discover.module.bestSponsors.title"/> </h1>
                <userUtil:showListUsers users="${bestSponsors}" visibleUsers="24" messagesPrefix="discover.module.bestSponsors.userList"/>
                <p><g:message code="discover.module.bestSponsors.description"/> </p>
            </section>
        </div>
    </aside>
</content>

