<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="landingPrices.page.title"/></title>
    <meta name="layout" content="paymentPlainLayout">
    <parameter name="normalHead" value="true"/>
    <parameter name="special-cssClass" value="prices"/>
    <g:render template="/dashboard/landingMetaTags"
              model="[
                      kuorumTitle:g.message(code:'landingPrices.page.title'),
                      kuorumDescription:g.message(code:'landingPrices.page.description'),
                      kuorumImage:r.resource(dir:'images', file:'background-prices.jpg', absolute:true)
              ]"/>
</head>

<content tag="mainContent">
    <div class="box-ppal" id="payment">
        <h1><g:message code="landingPrices.title"/></h1>
        <div class="promo-options clearfix">
            <table class="kuorum-prices-table">
                <thead>
                <tr>
                    <th class="empty"></th>
                    <th class="empty"></th>
                    <th class="recommended"><g:message code="landingPrices.offerType.recommended"/></th>
                </tr>
                <tr>
                    <th><g:message code="landingPrices.offerType.numberContacts"/> </th>
                    <th><g:message code="org.kuorum.rest.model.payment.SubscriptionCycleDTO.MONTHLY"/> </th>
                    <th class="recommended"><g:message code="org.kuorum.rest.model.payment.SubscriptionCycleDTO.YEARLY"/></th>
                </tr>
                </thead>
                <tbody>
                <g:set var="iter" value="${0}"/>
                <g:each in="${plans}" var="planGroup">
                    <g:set var="index" value="${iter = iter + 1}"/>
                    <g:if test="${index == plans.size()}">
                        <g:set var="label_overMax" value="${g.message(code: 'landingPrices.form.overMax.label')}"/>
                    </g:if>
                    <tr>
                        <td>${planGroup.key}</td>
                        <td><span class="maximum">${label_overMax}</span> <g:formatNumber number="${planGroup.value.find{it.cycleType == org.kuorum.rest.model.payment.SubscriptionCycleDTO.MONTHLY}.monthlyPrice}"/> €<g:message code="landingPrices.form.perMonth"/></td>
                        <td><span class="maximum">${label_overMax}</span> <g:formatNumber number="${planGroup.value.find{it.cycleType == org.kuorum.rest.model.payment.SubscriptionCycleDTO.YEARLY}.monthlyPrice}"/> €<g:message code="landingPrices.form.perMonth"/></td>
                    </tr>
                </g:each>
                </tbody>
            </table>
        </div>
    </div>
</content>

%{--<content tag="mainContent">--}%
    %{--<g:render template="landingPrices/weceInfo"/>--}%
%{--</content>--}%
