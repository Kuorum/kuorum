<%@ page import="kuorum.core.model.OfferType" %>
<g:set var="maxKPeopleList" value="${10}"/>
<script>
    $(function(){
        $(".promo-options select").on("change", function(e){
            var $amount = $(this).parents("form").parent("div").find(".amount")
            var basicPrice = $amount.attr("data-basicPrice")
            var val = basicPrice * $(this).val()
            if (val > 1000){
                console.log(val)
                val = Math.round(val / 1000)
                val = val + "k"
            }
            $amount.html(val)
        })
    })
</script>
<section id="main" role="main" class="homeSub">
    <h3><g:message code="landingPrices.title"/> </h3>
    <div class="promo-options clearfix">
        <div class="col-xs-12 col-sm-12 col-md-3">
            <g:render template="/dashboard/landingPrices/headPriceBox" model="[offerType:OfferType.BASIC]"/>
            <g:form mapping="registerSubscriptionStep1" method="post" name="prices-basic" role="form">
                <input type="hidden" name="offerType" value="${OfferType.BASIC}"/>
                <label class="sr-only" for="basic-people">
                    <g:message code="kuorum.core.model.OfferType.${OfferType.BASIC}"/>
                    (<g:message code="landingPrices.form.currency"/> ${OfferType.BASIC.price}<g:message code="landingPrices.form.perMonth"/>)
                </label>
                <div class="wrap-select">
                    <select name="kpeople" class="form-control" id="basic-people">
                        <g:each in="${(1..maxKPeopleList)}" var="kpeople">
                            <option value="${kpeople}"><g:message code="landingPrices.form.emailablePeople" args="${kpeople}"/></option>
                        </g:each>
                    </select>
                </div>
                <input type="submit" value="${g.message(code:'landingPage.videoAndRegister.startFreeTrial')}" class="btn btn-block">
                <h3><g:message code="landingPrices.form.features.title"/></h3>
                <ul>
                    <li><g:message code="landingPrices.form.features.1profile"/></li>
                    <li><g:message code="landingPrices.form.features.inbox"/></li>
                    <li><g:message code="landingPrices.form.features.massMailing"/></li>
                    <li><g:message code="landingPrices.form.features.socialDataAnalytics"/></li>
                    <li><g:message code="landingPrices.form.features.docManagement"/></li>
                    <li><g:message code="landingPrices.form.features.fundraising"/></li>
                </ul>
            </g:form>
        </div>
        <div class="col-xs-12 col-sm-12 col-md-3 recomended">
            <span class="tag"><g:message code="landingPrices.offerType.recommended"/> </span>
            <g:render template="/dashboard/landingPrices/headPriceBox" model="[offerType:OfferType.PREMIUM]"/>
            <g:form mapping="registerSubscriptionStep1" method="post" name="prices-premium" role="form">
                <input type="hidden" name="offerType" value="${OfferType.PREMIUM}"/>
                <label class="sr-only" for="premium-people">
                    <g:message code="kuorum.core.model.OfferType.${OfferType.PREMIUM}"/>
                    (<g:message code="landingPrices.form.currency"/> ${OfferType.PREMIUM.price}<g:message code="landingPrices.form.perMonth"/>)
                </label>
                <div class="wrap-select">
                    <select name="kpeople" class="form-control" id="premium-people">
                        <g:each in="${(1..maxKPeopleList)}" var="kpeople">
                            <option value="${kpeople}"><g:message code="landingPrices.form.emailablePeople" args="${kpeople}"/></option>
                        </g:each>
                    </select>
                </div>
                <input type="submit" value="${g.message(code:'landingPage.videoAndRegister.startFreeTrial')}" class="btn btn-block">
                <h3><g:message code="landingPrices.form.features.title"/></h3>
                <ul>
                    <li><g:message code="landingPrices.form.features.1profile"/></li>
                    <li><g:message code="landingPrices.form.features.inbox"/></li>
                    <li><g:message code="landingPrices.form.features.semanticAnalysis"/></li>
                    <li><g:message code="landingPrices.form.features.massMailing"/></li>
                    <li><g:message code="landingPrices.form.features.socialDataAnalytics"/></li>
                    <li><g:message code="landingPrices.form.features.docManagement"/></li>
                    <li><g:message code="landingPrices.form.features.fundraising"/></li>
                    <li><g:message code="landingPrices.form.features.events"/></li>
                </ul>
            </g:form>
        </div>
        <div class="col-xs-12 col-sm-12 col-md-3">
            <g:render template="/dashboard/landingPrices/headPriceBox" model="[offerType:OfferType.LOCAL_GROUP]"/>
            <g:form mapping="registerSubscriptionStep1" method="post" name="prices-local" role="form">
                <input type="hidden" name="offerType" value="${OfferType.LOCAL_GROUP}"/>
                <label class="sr-only" for="local-people">
                    <g:message code="kuorum.core.model.OfferType.${OfferType.LOCAL_GROUP}"/>
                    (<g:message code="landingPrices.form.currency"/> ${OfferType.LOCAL_GROUP.price}<g:message code="landingPrices.form.perMonth"/>)
                </label>
                <div class="wrap-select">
                    <select name="kpeople" class="form-control" id="local-people">
                        <g:each in="${(1..maxKPeopleList)}" var="kpeople">
                            <option value="${kpeople}"><g:message code="landingPrices.form.emailablePeople" args="${kpeople}"/></option>
                        </g:each>
                    </select>
                </div>
                <input type="submit" value="${g.message(code:'landingPage.videoAndRegister.startFreeTrial')}" class="btn btn-block">
                <h3><g:message code="landingPrices.form.features.title"/></h3>
                <ul>
                    <li><g:message code="landingPrices.form.features.10profiles"/></li>
                    <li><g:message code="landingPrices.form.features.inbox"/></li>
                    <li><g:message code="landingPrices.form.features.semanticAnalysis"/></li>
                    <li><g:message code="landingPrices.form.features.massMailing"/></li>
                    <li><g:message code="landingPrices.form.features.socialDataAnalytics"/></li>
                    <li><g:message code="landingPrices.form.features.docManagement"/></li>
                    <li><g:message code="landingPrices.form.features.fundraising"/></li>
                    <li><g:message code="landingPrices.form.features.events"/></li>
                </ul>
            </g:form>
        </div>
        <div class="col-xs-12 col-sm-12 col-md-3">
            <g:render template="/dashboard/landingPrices/headPriceBox" model="[offerType:OfferType.CAMPAIGNER]"/>
            <g:form mapping="registerSubscriptionStep1" action="#" method="post" name="prices-campaigner" role="form">
                <input type="hidden" name="offerType" value="${OfferType.CAMPAIGNER}"/>
                <div class="wrap-select">
                    <label><g:message code="landingPrices.form.customSolutions"/> </label>
                </div>
                <input type="submit" value="${g.message(code:'landingPage.videoAndRegister.startFreeTrial')}" class="btn btn-block">
                <h3><g:message code="landingPrices.form.features.title"/></h3>
                <ul>
                    <li><g:message code="landingPrices.form.features.inbox"/></li>
                    <li><g:message code="landingPrices.form.features.semanticAnalysis"/></li>
                    <li><g:message code="landingPrices.form.features.massMailing"/></li>
                    <li><g:message code="landingPrices.form.features.socialDataAnalytics"/></li>
                    <li><g:message code="landingPrices.form.features.docManagement"/></li>
                    <li><g:message code="landingPrices.form.features.fundraising"/></li>
                    <li><g:message code="landingPrices.form.features.events"/></li>
                    <li><g:message code="landingPrices.form.features.projectManager"/></li>
                </ul>
            </g:form>
        </div>
    </div>
</section>