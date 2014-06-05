<%@ page import="kuorum.mail.MailGroupType; kuorum.mail.MailType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.configurationEmails"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <h1>
        <g:message code="profile.emailNotifications.salutation"/>,
        <br>
        <span class="user-name">${user.name}</span>
    </h1>
    <p><g:message code="profile.emailNotifications.description"/></p>
    <g:render template="leftMenu" model="[user:user, activeMapping:'profileEmailNotifications', menu:menu]"/>

</content>

<content tag="mainContent">
    <h1><g:message code="profile.emailNotifications.title"/></h1>
    <g:form method="POST" mapping="profileEmailNotifications" name="config9" role="form">
        <g:each in="${kuorum.mail.MailGroupType.values().findAll{it.editable}}" var="mailGroup">
            <div class="form-group activityMe">
                <span class="span-label"><g:message code="profile.emailNotifications.${mailGroup}.label"/></span>
                <label class="checkbox-inline pull-right"><input type="checkbox" class="allActivityMails" value=""> marcar todas</label>
                <p class="help-block"><g:message code="profile.emailNotifications.${mailGroup}.helpBlock"/></p>
                <g:each in="${kuorum.mail.MailType.values().findAll{it.mailGroup==mailGroup}}" var="mailType">
                    <div class="checkbox">
                        <label><input type="checkbox" name="availableMails"  value="${mailType}" ${command.availableMails.contains(mailType)?'checked':''}><g:message code="${kuorum.mail.MailType.name}.${mailType}"/></label>
                    </div>
                </g:each>
            </div>
        </g:each>

        <div class="form-group">
            <formUtil:selectMultipleCommissions command="${command}" field="commissions"/>
        </div>

        <div class="form-group">
            <input type="submit" value="${message(code:'profile.emailNotifications.save')}" class="btn btn-grey btn-lg">
            <a href="#" class="cancel">${message(code:'profile.emailNotifications.cancel')}</a>
        </div>
    </g:form>
</content>
