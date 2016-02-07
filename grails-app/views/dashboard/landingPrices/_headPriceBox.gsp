<h2><g:message code="kuorum.core.model.OfferType.${offerType}"/> </h2>
<p class="price">
    <span class="currency"><g:message code="landingPrices.form.currency"/></span>
    <g:set var="price" value="${offerType.price}"/>
    <g:set var="format" value="#"/>
    <g:if test="${offerType.price > 1000}">
        <g:set var="price" value="${(offerType.price/1000)}"/>
        <g:set var="format" value="#0k"/>
    </g:if>
    <!--NO SPACES-->
    <span class="amount" data-basicPrice="${offerType.price}"><g:formatNumber number="${price}" format="${format}"/></span><g:message code="landingPrices.form.perMonth"/>
</p>