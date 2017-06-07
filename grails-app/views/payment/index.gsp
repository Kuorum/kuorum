<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="funnel.payment.title"/></title>
    <meta name="layout" content="paymentPlainLayout">
    <!-- Schema.org markup for Google+ -->
    <meta itemprop="name" content="${g.message(code:"kuorum.name")}">
    <meta itemprop="description" content="${g.message(code:"layout.head.meta.description")}">
    <meta itemprop="image" content="${resource(dir: 'images', file:'landingSearch-rrss.png', absolute:true)}" />
    <meta itemprop="image" content="${resource(dir: 'images', file: 'logo@2x.png')}" />

</head>

<content tag="mainContent">

    <h1><g:message code="funnel.payment.cycleType.desc1"/></h1>
    <p><g:message code="funnel.payment.cycleType.desc2"/></p>
    <p class="big-margin-top"><g:message code="funnel.payment.cycleType.desc3"/></p>


    <div class="promo-options clearfix">
        <g:each in="${plans}" var="plan"><div class="col-xs-12 col-sm-5 col-md-3 promo ${plan.discount?'recomended':''}">
                <g:if test="${plan.discount}">
                    <span class="tag">${plan.discount}% <span class="discount"><g:message code="funnel.payment.cycleType.discount"/></span></span>
                </g:if>
                <h2><g:message code="org.kuorum.rest.model.payment.SubscriptionCycleDTO.${plan.cycleType}"/></h2>
                <p class="price"><span class="amount"><g:formatNumber number="${plan.monthlyPrice}" maxFractionDigits="0"/></span>€/month</p>
                <g:link mapping="paymentGateway" params="[subscriptionCycle:plan.cycleType]" class="btn"><g:message code="funnel.payment.cycleType.submit"/></g:link>
        </div></g:each>
    </div>

    <ul class="checklist">
        <li>
            <p><i class="fa fa-check" aria-hidden="true"></i> <g:message code="funnel.payment.cycleType.features.1" args="${plans[0].getMaxContacts()}"/> </p>
        </li>
        <li>
            <p><i class="fa fa-check" aria-hidden="true"></i> <g:message code="funnel.payment.cycleType.features.2"/> </p>
        </li>
        <li>
            <p><i class="fa fa-check" aria-hidden="true"></i> <g:message code="funnel.payment.cycleType.features.3"/> </p>
        </li>
        <li>
            <p><i class="fa fa-check" aria-hidden="true"></i> <g:message code="funnel.payment.cycleType.features.4"/></p>
        </li>
    </ul>
</content>