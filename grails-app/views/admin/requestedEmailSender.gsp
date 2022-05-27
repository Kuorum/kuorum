<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="admin.menu.domainConfig.requestCustomDomain.title"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <h1><g:message code="admin.adminPrincipal.title"/></h1>
    <g:render template="/admin/adminMenu" model="[activeMapping:'adminRequestEmailSender']"/>
</content>
<content tag="titleContent">
    <h1><g:message code="admin.menu.domainConfig.requestCustomDomain.title"/></h1>
    <h3><g:message code="admin.menu.domainConfig.requestCustomDomain.subtitle"/></h3>
</content>
<content tag="mainContent">


    <g:if test="${isRequested && !emailSender}">
        <g:set var="textInfo" value="${g.message(code:'kuorum.web.commands.profile.NewsletterConfigCommand.customEmailSender.success.requested')}"/>
        <g:set var="buttonState" value="disabled='disabled'"/>
    </g:if>
    <g:elseif test="${emailSender}">
        <g:set var="textInfo" value="${g.message(code:'kuorum.web.commands.profile.NewsletterConfigCommand.customEmailSender.success.working', args: [emailSender])}"/>
        <g:set var="buttonVisibility" value="hidden"/>
    </g:elseif>
    <g:else>
        <g:set var="textInfo" value="${g.message(code:'kuorum.web.commands.profile.NewsletterConfigCommand.customEmailSender.success.requested')}"/>
        <g:set var="checkVisibility" value="hidden"/>
    </g:else>


    <div class="box-ppal-section">
        <div class="form-group">
            <span class="span-label"><g:message
                    code="kuorum.web.commands.profile.NewsletterConfigCommand.customEmailSender.label"/></span>

            <p class="help-block"><g:message
                    code="kuorum.web.commands.profile.NewsletterConfigCommand.customEmailSender.label.helpBlock"/></p>
        </div>
    </div>

    <div class="box-ppal-section">
        <fieldset aria-live="polite" class="form-group text-center ${buttonVisibility}">
            <input id="requestCustomSender" type="submit"
                   value="${g.message(code: 'kuorum.web.commands.profile.NewsletterConfigCommand.customEmailSender.button.request')}"
                   class="btn btn-orange btn-lg" ${buttonState}
                   data-ajaxRequestSender="${g.createLink(mapping: 'adminRequestEmailSender')}">
        </fieldset>
        <fieldset aria-live="polite" class="validate">
            <div class="col-xs-12 valid ${checkVisibility}">
                <i class="fal fa-check fa-2x"></i><span>${textInfo}</span>
            </div>
        </fieldset>
    </div>
</content>
