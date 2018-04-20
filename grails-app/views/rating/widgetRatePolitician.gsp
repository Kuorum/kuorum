<g:applyLayout name="mainWidget">
    <head>
        <title><g:message code="widget.politician.valuation.rate.title"/></title>
        <style>
            ${raw(params.customCss)}
        </style>
    </head>
    <body>
        <section id="valuations-widget-content" class="widget">
            <header>
                <h1><g:message code="widget.politician.valuation.rate.title"/></h1>
                %{--<a href="https://kuorum.org" target="_blank">Kuorum.org</a>--}%
            </header>
            <ul class="user-list-followers">
                <g:each in="${politicians}" var="politician">
                    <g:set var="politicianRate" value="${Math.round(rates[politician.alias].userReputation)}"/>
                    <g:set var="userPoliticianRated" value="${rates[politician.alias].evaluation}"/>
                    <li itemtype="http://schema.org/Person" itemscope class="user widget-user-rating" id="${politician.alias}">
                        <g:link mapping="userShow" params="${politician.encodeAsLinkProperties()}" target="_blank">
                            <img src="${image.userImgSrc(user:politician)}" alt="${politician.name}" class="user-img" itemprop="image"><span itemprop='name'>${politician.name}</span>
                        </g:link>
                        <!-- politician valuation -->
                        <form action="${g.createLink(mapping:'userRate', params:politician.encodeAsLinkProperties(), absolute:true)}" method="post" class="rating user-rate">
                            <fieldset class="rating">
                                <legend><g:message code="politician.valuation.rate"/></legend>
                                <g:each in="${(1..5).reverse()}" var="i">
                                    <input id="star${i}_userRate_${politician.alias}" type="radio" name="rating" value="${i}" ${userPoliticianRated==i?'checked':''}>
                                    <label for="star${i}_userRate_${politician.alias}">
                                        <g:message code="politician.valuation.rate.value.starts" args="[i]"/>
                                        <span class="rate-message"><g:message code="politician.valuation.rate.value.text.${i}"/></span>
                                    </label>
                                </g:each>
                            </fieldset>
                        </form>
                        <form class="rating">
                            <fieldset class="rating">
                                <legend><g:message code="politician.valuation.rate"/></legend>
                                <g:each in="${(1..5).reverse()}" var="i">
                                    <input id="star${i}_rate_${politician.alias}" type="radio" name="rating" value="${i}" ${politicianRate==i?'checked':''}>
                                    <label for="star${i}_rate_${politician.alias}">
                                        <g:message code="politician.valuation.rate.value.starts" args="[i]"/>
                                        <span class="rate-message"><g:message code="politician.valuation.rate.value.text.${i}"/></span>
                                    </label>
                                </g:each>
                            </fieldset>
                        </form>
                    </li>
                    <g:render template="/kuorumUser/userShowTemplates/modals/modalRatingShare"
                              model="[
                                      user:politician,
                                      titleModal:message(code:'widget.politician.valuation.rate.modal.share.title', args: [politician.name]),
                                      paragraphModal:message(code:'widget.politician.valuation.rate.modal.share.description', args: [politician.name])
                              ]"/>
                </g:each>
            </ul>
            %{--<footer>--}%
                %{--<a href="https://kuorum.org" target="_blank">Ver resultados</a>--}%
            %{--</footer>--}%
            <footer>
                <h1>
                    <a href="https://${kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.domain}" id="brand" class="navbar-brand" target="_blank">
                        <img src="${resource(dir: 'images', file: 'logo@3x.png')}" alt="Kuorum.org">
                        <span class="hidden">${kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.name}</span>
                    </a>
                </h1>
            </footer>
        </section>
    </body>

</g:applyLayout>