<g:applyLayout name="main">
%{--Es igual al layout de columnC pero cambia la cabecera--}%
    <head>
        <title><g:message code="tour.tour_project.title"/></title>
    </head>

    <body itemscope itemtype="http://schema.org/WebPage">
    <g:render template="headTourFake" model="[user:user]"/>
    <div class="row main">
        <div class="container-fluid">
            <div class="row" >
                <section id="main" class="col-xs-12 col-sm-12 col-md-8" role="main">
                    <g:render template="/project/projectInfo" model="[project:project, victories: victories, readMore:true]"/>
                    %{--<g:render template="/project/projectButtonsCreatePost" model="[project:project]"/>--}%
                    <g:if test="${victories}">
                        <g:render template="/project/projectVictories" model="[project:project, victories:victories]"/>
                    </g:if>
                    <g:render template="/project/projectClucks" model="[project:project, clucks:clucks, seeMore:false]"/>
                </section>

                <aside class="col-xs-12 col-sm-12 col-md-4" role="complementary">
                    <g:render template="fakeProjectVote" model="[project:project,necessaryVotesForKuorum:necessaryVotesForKuorum]"/>

                    <modulesUtil:projectActivePeople project="${project}"/>
                    <modulesUtil:recommendedPosts project="${project}" title="${message(code:"modules.recommendedProjectPosts.title")}"/>
                </aside>
            </div>
        </div><!-- /.conatiner-fluid -->
    </div><!-- /#main -->

    <g:render template="/layouts/footer/footer"/>

    <r:require module="tour"/>

    </body>

</g:applyLayout>