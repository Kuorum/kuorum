<g:applyLayout name="main">
%{--Es igual al layout de columnC pero cambia la cabecera--}%
    <head>
        <title><g:message code="tour.tour_law.title"/></title>
    </head>

    <body itemscope itemtype="http://schema.org/WebPage">
    <g:render template="headTourFake" model="[user:user]"/>
    <div class="row main">
        <div class="container-fluid">
            <div class="row" >
                <section id="main" class="col-xs-12 col-sm-12 col-md-8" role="main">
                    <g:render template="/law/lawInfo" model="[law:law, victories: victories, readMore:true]"/>
                    <g:render template="/law/lawButtonsCreatePost" model="[law:law]"/>
                    <g:if test="${victories}">
                        <g:render template="/law/lawVictories" model="[law:law, victories:victories]"/>
                    </g:if>
                    <g:render template="/law/lawClucks" model="[law:law, clucks:clucks, seeMore:false]"/>
                </section>

                <aside class="col-xs-12 col-sm-12 col-md-4" role="complementary">
                    <g:render template="fakeLawVote" model="[law:law,necessaryVotesForKuorum:necessaryVotesForKuorum]"/>

                    <modulesUtil:lawActivePeople law="${law}"/>
                    <modulesUtil:recommendedPosts law="${law}" title="${message(code:"modules.recommendedLawPosts.title")}"/>
                </aside>
            </div>
        </div><!-- /.conatiner-fluid -->
    </div><!-- /#main -->

    <g:render template="/layouts/footer/footer"/>

    <r:require module="tour"/>

    </body>

</g:applyLayout>