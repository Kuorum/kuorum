<%@ page import="kuorum.core.model.UserType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.login"
                      args="[kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.name]"/></title>
    <meta name="layout" content="centerColumn1Layout">
    <parameter name="showHeadSearch" value="false"/>
    <parameter name="showNavBar" value="false"/>
    <parameter name="extraHeadCss" value="landing"/>
    <parameter name="disableLogoLink" value="true"/>
    <r:require modules="forms"/>
</head>

<content tag="title">
    <g:message code="customRegister.step2.title"/>
</content>

<content tag="mainContent">
    <ol class="stepsSign">
        <li class=""><div class="step-label"><g:message code="customRegister.fillProfile.basicData"/></div></li>
        <li class=""><div class="step-label"><g:message code="customRegister.fillProfile.images"/></div></li>
        <li class=""><div class="step-label"><g:message code="customRegister.fillProfile.files"/></div></li>
        <li class="active"><div class="step-label"><g:message code="customRegister.fillProfile.social"/></div></li>
    </ol>
    <formUtil:validateForm bean="${command}" form="stepFillSocial" autocomplete="off"/>
    <form name="stepFillSocial" role="form" method="POST" autocomplete="off"
          class="signup step2">

        <g:render template="formSocialNetworks" model="[command: command, showPoliticianFields: showPoliticianFields]"/>

        <fieldset aria-live="polite" class="row">
            <div class="form-group text-center option-buttons">
                <g:link mapping="funnelFillFiles" params="[campaignId: campaignId]"
                        class="btn btn-lg btn-grey-light"><g:message code="default.paginate.back"/></g:link>
                <input type="submit" value="${g.message(code: 'customRegister.step2.submit')}" class="btn btn-lg">
            </div>
        </fieldset>
    </form>
</content>

