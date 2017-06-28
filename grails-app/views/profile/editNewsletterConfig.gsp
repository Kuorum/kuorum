<%@ page import="kuorum.mail.MailGroupType; kuorum.mail.MailType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.newsletterConfig"/> </title>
    <meta name="layout" content="leftMenuConfigLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <g:render template="leftMenuAccount" model="[user:user, activeMapping:'profileNewsletterConfig', menu:menu]"/>

</content>

<content tag="titleContent">
    <h1><g:message code="profile.menu.profileNewsletterConfig"/></h1>
    <h3><g:message code="profile.menu.profileNewsletterConfig.subtitle"/></h3>
</content>


<content tag="mainContent">
    <formUtil:validateForm bean="${command}" form="configNewsletter"/>
    <g:form method="POST" mapping="profileNewsletterConfig" name="configNewsletter" role="form">
        <div class="box-ppal-section">
            <div class="form-group">
                <formUtil:input command="${command}" field="mainUrl" extraClass="clearfix"  showLabel="true"/>
            </div>
            <div class="form-group address">
                <span class="span-label"><g:message code="kuorum.web.commands.profile.NewsletterConfigCommand.address1.label"/></span>
                <p class="help-block"><g:message code="kuorum.web.commands.profile.NewsletterConfigCommand.address1.label.helpBlock"/></p>

                <formUtil:input command="${command}" field="address1" showLabel="false"/>
                <formUtil:input command="${command}" field="address2" showLabel="false"/>
                <formUtil:input command="${command}" field="address3" showLabel="false"/>
            </div>
        </div>
        <div class="box-ppal-section">
            <fieldset class="form-group text-center">
                <input type="submit" value="${g.message(code:'profile.emailNotifications.save')}" class="btn btn-orange btn-lg">
            </fieldset>
        </div>
    </g:form>
</content>
