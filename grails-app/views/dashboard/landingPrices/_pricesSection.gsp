<%@ page import="org.kuorum.rest.model.payment.SubscriptionCycleDTO; kuorum.core.model.OfferType" %>
<section id="main" role="main" class="homeSub">
    <h3><g:message code="landingPrices.title"/> </h3>
    <div class="promo-options clearfix">
        <table class="kuorum-prices-table">
            <thead>
                <tr class="recommended">
                    <th class="empty"></th>
                    <th class="empty"></th>
                    <th>recomendado</th>
                </tr>
                <tr>
                    <th>Nº Contactos</th>
                    <th>Mensual</th>
                    <th>Anual</th>
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
</section>