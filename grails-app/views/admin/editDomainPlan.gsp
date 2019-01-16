<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="admin.menu.domainConfig.authorizedCampaigns.title"/> </title>
    <meta name="layout" content="leftMenuConfigLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <h1><g:message code="admin.adminPrincipal.title"/></h1>
    <g:render template="/admin/adminMenu" model="[activeMapping:'adminDomainConfigPlan']"/>
</content>
<content tag="titleContent">
    <h1><g:message code="admin.menu.domainConfig.adminDomainConfigPlan.title"/></h1>
    <h3><g:message code="admin.menu.domainConfig.adminDomainConfigPlan.subtitle"/></h3>
</content>

<content tag="mainContent">
    <h1> Current plan </h1>
    <ul>
        <li>${domainPaymentInfo.billingAmountUsersRange}</li>
        <li>${domainPaymentInfo.billingCycle}</li>
    </ul>
    <div class="promo-options clearfix">
        <g:each in="${plans}" var="plan">
            <g:form method="POST" mapping="adminDomainConfigPlan">
                <div class="col-xs-12 col-sm-5 col-md-3 ${domainPaymentInfo.domainPlan==plan.plan?'highlighted':''}">
                    <h2><g:message code="org.kuorum.rest.model.domain.DomainPlanRSDTO.${plan.plan}"/></h2>
                    <p class="price clearfix">
                        <g:if test="${domainPaymentInfo.billingCycle == org.kuorum.rest.model.domain.BillingCycleRDTO.MONTHLY}">
                            <span class="amount"><g:formatNumber number="${plan.monthlyPrice}" type='currency' currencyCode="EUR"  maxFractionDigits="0"/></span> / per month
                        </g:if>
                        <g:else>
                            <span class="amount"><g:formatNumber number="${plan.yearlyPrice}" type='currency' currencyCode="EUR" maxFractionDigits="0" /></span> / per year
                        </g:else>
                        <input type="hidden" value="${plan.plan}" name="plan"/>
                        <input type="submit" class="btn" value="${g.message(code:'funnel.payment.cycleType.submit')}"/>
                    </p>
                </div>
            </g:form>
        </g:each>
    </div>
</content>
