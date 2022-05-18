<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title><g:message code="customRegister.stepDomainValidation.title"/></title>
    <meta name="layout" content="centerColumn1Layout">
    <parameter name="showHeadSearch" value="false"/>
    <parameter name="showNavBar" value="false"/>
    <parameter name="extraHeadCss" value="landing"/>
    <parameter name="disableLogoLink" value="true"/>

</head>

<content tag="mainContent">

    <div class="container-fluid box-ppal box-error">
        <div class="box-ppal-title">
            <h3><g:message code="campaign.closed.token.error.title"/></h3>
        </div>

        <div class="box-ppal-section">
            <g:if test="${campaign.startDate}">
                <g:set var="startDate"><g:formatDate format="dd/MM/yyyy HH:mm zzz" date="${campaign.startDate}"
                                                     timeZone="${campaign.user.timeZone}"/></g:set>
                <g:set var="endDate"><g:formatDate format="dd/MM/yyyy HH:mm zzz" date="${campaign.endDate}"
                                                   timeZone="${campaign.user.timeZone}"/></g:set>
                <g:message code="campaign.closed.token.error.message.startDate"
                           args="[contact.name, campaign.title, startDate, endDate]"/>
            </g:if>
            <g:else>
                <g:set var="endDate"><g:formatDate format="dd/MM/yyyy HH:mm" date="${campaign.endDate}"
                                                   timeZone="${campaign.user.timeZone}"/></g:set>
                <g:message code="campaign.closed.token.error.message.endDate"
                           args="[contact.name, campaign.title, endDate]"/>
            </g:else>
        </div>
    </div>
</content>

</html>