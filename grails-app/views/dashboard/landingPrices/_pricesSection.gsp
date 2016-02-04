<g:set var="maxKPeopleList" value="${10}"/>
<section id="main" role="main" class="homeSub">
    <h1><g:message code="landingPrices.title"/> </h1>
    <div class="promo-options clearfix">
        <div class="col-xs-12 col-sm-12 col-md-3">
            <h2><g:message code="${kuorum.core.model.OfferType.BASIC}"/> </h2>
            <p class="price"><span class="currency"><g:message code="landingPrices.form.currency"/></span> <span class="amount">9</span>/month</p>
            <form action="#" method="post" name="prices-basic" id="prices-basic" role="form">
                <label class="sr-only" for="basic-people">Basic (€ 9/month) with...</label>
                <div class="wrap-select">
                    <select class="form-control" id="basic-people">
                        <g:each in="${(1..maxKPeopleList)}" var="kpeople">
                            <option value="${kpeople}"><g:message code="landingPrices.form.emailablePeople" args="${kpeople}"/></option>
                        </g:each>
                    </select>
                </div>
                <input type="submit" value="${g.message(code:'landingPage.videoAndRegister.startFreeTrial')}" class="btn btn-block">
                <h3><g:message code="landingPrices.form.features.title"/></h3>
                <ul>
                    <li>1 profile</li>
                    <li>Inbox Automation</li>
                    <li>Mass Mailing</li>
                    <li>Social Data Analytics</li>
                    <li>Doc. Management</li>
                    <li>Fundraising</li>
                </ul>
            </form>
        </div>
        <div class="col-xs-12 col-sm-12 col-md-3 recomended">
            <span class="tag">Recomended</span>
            <h2>Premium</h2>
            <p class="price"><span class="currency"><g:message code="landingPrices.form.currency"/></span> <span class="amount">29</span>/month</p>
            <form action="#" method="post" name="prices-premium" id="prices-premium" role="form">
                <label class="sr-only" for="premium-people">Premium (€ 29/month) with...</label>
                <div class="wrap-select">
                    <select class="form-control" id="premium-people">
                        <g:each in="${(1..maxKPeopleList)}" var="kpeople">
                            <option value="${kpeople}"><g:message code="landingPrices.form.emailablePeople" args="${kpeople}"/></option>
                        </g:each>
                    </select>
                </div>
                <input type="submit" value="${g.message(code:'landingPage.videoAndRegister.startFreeTrial')}" class="btn btn-block">
                <h3><g:message code="landingPrices.form.features.title"/></h3>
                <ul>
                    <li>1 profile</li>
                    <li>Inbox Automation</li>
                    <li>Semantic Analysis</li>
                    <li>Mass Mailing</li>
                    <li>Social Data Analytics</li>
                    <li>Doc. Management</li>
                    <li>Fundraising</li>
                    <li>Events &amp; Ticketing</li>
                </ul>
            </form>
        </div>
        <div class="col-xs-12 col-sm-12 col-md-3">
            <h2>Local group</h2>
            <p class="price"><span class="currency"><g:message code="landingPrices.form.currency"/></span> <span class="amount">199</span>/month</p>
            <form action="#" method="post" name="prices-local" id="prices-local" role="form">
                <label class="sr-only" for="local-people">Local group (€ 199/month) with...</label>
                <div class="wrap-select">
                    <select class="form-control" id="local-people">
                        <g:each in="${(1..maxKPeopleList)}" var="kpeople">
                            <option value="${kpeople}"><g:message code="landingPrices.form.emailablePeople" args="${kpeople}"/></option>
                        </g:each>
                    </select>
                </div>
                <input type="submit" value="${g.message(code:'landingPage.videoAndRegister.startFreeTrial')}" class="btn btn-block">
                <h3><g:message code="landingPrices.form.features.title"/></h3>
                <ul>
                    <li>10 profile</li>
                    <li>Inbox Automation</li>
                    <li>Semantic Analysis</li>
                    <li>Mass Mailing</li>
                    <li>Social Data Analytics</li>
                    <li>Doc. Management</li>
                    <li>Fundraising</li>
                    <li>Events &amp; Ticketing</li>
                </ul>
            </form>
        </div>
        <div class="col-xs-12 col-sm-12 col-md-3">
            <h2>Campaigner</h2>
            <p class="price"><span class="currency"><g:message code="landingPrices.form.currency"/></span> <span class="amount">5k</span>/month</p>
            <form action="#" method="post" name="prices-campaigner" id="prices-campaigner" role="form">
                <div class="wrap-select">
                    <label>Customizable solution</label>
                </div>
                <input type="submit" value="${g.message(code:'landingPage.videoAndRegister.startFreeTrial')}" class="btn btn-block">
                <h3><g:message code="landingPrices.form.features.title"/></h3>
                <ul>
                    <li>Inbox Automation</li>
                    <li>Semantic Analysis</li>
                    <li>Mass Mailing</li>
                    <li>Social Data Analytics</li>
                    <li>Doc. Management</li>
                    <li>Fundraising</li>
                    <li>Events &amp; Ticketing</li>
                    <li>Project Manager</li>
                </ul>
            </form>
        </div>
    </div>
</section>