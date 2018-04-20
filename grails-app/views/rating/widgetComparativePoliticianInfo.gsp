<g:applyLayout name="mainWidget">

    <head>
        <r:require module="comparativeChart"/>
        <style>
            ${raw(params.customCss)}
        </style>
    </head>



    <section id="results-widget-content" class="clearfix">
        <section id="evolution" class="widget dark">
            <header>
                <h1><g:message code="widget.politician.results.title"/></h1>
            </header>
            <div id="comparativeValChart" data-urlJson="${createLink(mapping: 'comparingPoliticianRate', absolute: true, params:params)}">
                <g:message code="widget.loading"/>
            </div>
        </section>

        <section id="real-time" class="widget dark">
            <header>
                <h1><g:message code="widget.politician.realTime.title"/></h1>
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
                                <h6><g:message code="widget.politician.realTime.latestNews"/></h6>
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
                                <div class="polValChart" data-urljs="${createLink(mapping: 'userHistoricRate', params:politician.encodeAsLinkProperties() + [interval:interval,averageWidgetType:averageWidgetType,startDate:startDate, endDate:endDate], absolute: true)}"></div>
                            </div>

                            %{--</div>--}%

                        </div>
                    </g:each>
                </div>
            </div>
            <!-- /tabs -->
        </section>
    </section>
    <header>
        <h1>
            <a href="https://${kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.domain}" id="brand" class="navbar-brand">
                <img src="${resource(dir: 'images', file: 'logo@3x.png')}" alt="Kuorum.org">
                <span class="hidden">${kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.name}</span>
            </a>
        </h1>
    </header>
    </body>

</g:applyLayout>