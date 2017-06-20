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
                      kuorumImage:request.siteUrl +r.resource(dir:'images', file:'background-prices.jpg')
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
                    <th class="recommended">Recomendado</th>
                </tr>
                <tr>
                    <th>Nº Contactos</th>
                    <th>Mensual</th>
                    <th class="recommended">Anual</th>
                </tr>
                </thead>
                <tbody>
                <g:each in="${plans}" var="planGroup">
                    <tr>
                        <td>${planGroup.key}</td>
                        <td><g:formatNumber number="${planGroup.value.find{it.cycleType == org.kuorum.rest.model.payment.SubscriptionCycleDTO.MONTHLY}.monthlyPrice}"/> €<g:message code="landingPrices.form.perMonth"/></td>
                        <td><g:formatNumber number="${planGroup.value.find{it.cycleType == org.kuorum.rest.model.payment.SubscriptionCycleDTO.YEARLY}.monthlyPrice}"/> €<g:message code="landingPrices.form.perMonth"/></td>
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
