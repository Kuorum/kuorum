<g:applyLayout name="mainWidget">

    <r:require module="comparativeChart"/>

    <section id="results-widget-content" class="clearfix">
        <section id="evolution" class="widget dark">
            <header>
                <h1><g:message code="widget.politician.results.title"/></h1>
            </header>
            <div id="comparativeValChart" data-urlJson="${createLink(mapping: 'comparingPoliticianRate', absolute: true, params:params)}">

                Cargando....

            </div>
        </section>

        <section id="real-time" class="widget dark">
            <header>
                <h1>Valoración en tiempo real</h1>
            </header>
            <!-- tabs left -->
            <div class="tabbable tabs-left">
                <ul class="nav nav-tabs">
                    <g:each in="${politicians}" var="politician">
                        <li>
                            <a href="#tab-${politician.alias}" data-toggle="tab" id="tab-link-${politician.alias}">
                                <img src="${image.userImgSrc(user:politician)}" alt="${politician.name}" class="user-img" itemprop="image"/>
                            </a>
                        </li>
                    </g:each>
                    %{--<li class="active"><a href="#a" data-toggle="tab">One</a></li>--}%
                    %{--<li><a href="#b" data-toggle="tab">Two</a></li>--}%
                    %{--<li><a href="#c" data-toggle="tab">Twee</a></li>--}%
                </ul>
                <div class="tab-content">
                    <g:each in="${politicians}" var="politician">
                        <div class="tab-pane" id="tab-${politician.alias}">
                            <div class="col-sm-5 hidden-xs text-center politician-info">
                                <h4>${politician.name}</h4>
                                <h5><userUtil:roleName user="${politician}"/> </h5>
                                <g:link mapping="userShow" params="${politician.encodeAsLinkProperties()}" class="btn btn-blue" target="_blank">
                                    <g:message code="search.list.seeMore"/>
                                </g:link>
                                <hr>
                                <h6>Ultimas noticias</h6>
                                <ul>
                                    <g:each in="${politician.relevantEvents}" var="relevantEvent">
                                        <li>
                                            <abbr title="${relevantEvent.title}">
                                                <g:if test="${relevantEvent.url}">
                                                    <a href="${relevantEvent.url}" target="_blank">
                                                        ${relevantEvent.title}
                                                    </a>
                                                </g:if>
                                                <g:else>
                                                    ${relevantEvent.title}
                                                </g:else>
                                            </abbr>
                                        </li>
                                    </g:each>
                                </ul>
                            </div>
                            <div class="col-sm-6 col-xs-offset-1 col-sm-offset-0 text-center politician-chart">
                                <h4 class="hidden-sm hidden-lg hidden-md text-right">
                                    <g:link mapping="userShow" params="${politician.encodeAsLinkProperties()}" class="btn btn-blue" target="_blank">
                                    <g:message code="search.list.seeMore"/> - ${politician.name}
                                </g:link></h4>
                                <div class="polValChart" data-urljs="${createLink(mapping: 'userHistoricRate', params:politician.encodeAsLinkProperties(), absolute: true)}"></div>
                            </div>

                            %{--</div>--}%

                        </div>
                    </g:each>
                </div>
            </div>
            <!-- /tabs -->
        </section>
        %{--<section id="valuations-widget-content" class="widget">--}%
            %{--<div id="valuation-widget"--}%
                 %{--data-type="ratePolitician"--}%
                 %{--data-userAlias="sanchezcastejon,marianorajoy,pablo_iglesias_,albert_rivera,agarzon"--}%
                 %{--data-width="100%"--}%
                 %{--data-height="300px"></div>--}%
            %{--<script type="text/javascript" src="https://kuorum.org/widget.js?divId=valuation-widget"></script>--}%
        %{--</section>--}%
    </section>
    %{--<footer>--}%
        %{--<div class="container-fluid">--}%
            %{--<section class="links">--}%
                %{--<ul>--}%
                    %{--<li><g:link absolute="true" mapping="footerTechnology" target="_blank"><g:message code="layout.footer.tech"/></g:link> </li>--}%
                    %{--<li><g:link absolute="true" mapping="footerPoliticians" target="_blank"><g:message code="layout.footer.politicians"/></g:link></li>--}%
                    %{--<li><g:link absolute="true" mapping="footerCitizens" target="_blank"><g:message code="layout.footer.citizens"/></g:link></li>--}%
                    %{--<li><g:link absolute="true" mapping="footerDevelopers" target="_blank"><g:message code="layout.footer.editors"/></g:link></li>--}%
                    %{--<li><g:link absolute="true" mapping="footerAboutUs" target="_blank"><g:message code="layout.footer.aboutUs"/></g:link></li>--}%
                    %{--<li><g:link absolute="true" mapping="footerInformation" target="_blank"><g:message code="layout.footer.pressTitle"/></g:link></li>--}%
                    %{--<li><a href="http://kuorumorg.tumblr.com" target="_blank" target="_blank"><g:message code="layout.footer.blog"/></a></li>--}%
                    %{--<li><g:link absolute="true" mapping="footerPrivacyPolicy" target="_blank"><g:message code="layout.footer.privacyPolicy"/></g:link></li>--}%
                    %{--<li><g:link absolute="true" mapping="footerTermsUse" target="_blank"><g:message code="layout.footer.termsUse"/></g:link></li>--}%
                %{--</ul>--}%
            %{--</section>--}%
        %{--</div><!-- /.container-fluid - da ancho máximo y centra -->--}%
    %{--</footer>--}%
    <header>
        <h1>
            <a href="https://kuorum.org" id="brand" class="navbar-brand">
                <img src="${resource(dir: 'images', file: 'logo@3x.png')}" alt="Kuorum.org">
                <span class="hidden">Kuorum.org</span>
            </a>
        </h1>
    </header>
    </body>

</g:applyLayout>