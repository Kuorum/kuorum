<%@ page import="kuorum.mail.MailGroupType; kuorum.mail.MailType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.configurationEmails"/> </title>
    <meta name="layout" content="leftMenuConfigLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <g:render template="leftMenuAccount" model="[user:user, activeMapping:'profileEmailNotifications', menu:menu]"/>

</content>

<content tag="titleContent">
    <h1><g:message code="profile.menu.profileEmailNotifications"/></h1>
    <h3><g:message code="profile.menu.profileEmailNotifications.subtitle"/></h3>
</content>


<content tag="mainContent">
    <g:form method="POST" mapping="profileEmailNotifications" name="config9" role="form">
        <div class="box-ppal-section">
            <div class="form-group">
                <span class="span-label"><g:message code="profile.emailNotifications.basic.title"/></span>
                <p class="help-block"><g:message code="profile.emailNotifications.basic.title.helpBlock"/></p>
                <formUtil:checkBox command="${command}" field="followNew" extraClass="clearfix" />
            </div>
            <div class="form-group">
                <span class="span-label"><g:message code="profile.emailNotifications.debate.title"/></span>
                <p class="help-block"><g:message code="profile.emailNotifications.basic.title.helpBlock"/></p>
                <formUtil:checkBox command="${command}" field="debateNewOwner" extraClass="clearfix" />
                <formUtil:checkBox command="${command}" field="proposalNewOwner" extraClass="clearfix" />
                <formUtil:checkBox command="${command}" field="proposalNew" extraClass="clearfix" />
                <formUtil:checkBox command="${command}" field="proposalComment" extraClass="clearfix" />
                <formUtil:checkBox command="${command}" field="proposalLike" extraClass="clearfix" />
                <formUtil:checkBox command="${command}" field="proposalPinned" extraClass="clearfix" />
                <formUtil:checkBox command="${command}" field="mentions" extraClass="clearfix" />
            </div>
            <div class="form-group">
                <span class="span-label"><g:message code="profile.emailNotifications.post.title"/></span>
                <p class="help-block"><g:message code="profile.emailNotifications.basic.title.helpBlock"/></p>
                 <formUtil:checkBox command="${command}" field="postLike" extraClass="clearfix" />
            </div>
            <div class="form-group">
                <span class="span-label"><g:message code="profile.emailNotifications.causes.title"/></span>
                <p class="help-block"><g:message code="profile.emailNotifications.basic.title.helpBlock"/></p>
                <formUtil:checkBox command="${command}" field="debateNewCause" extraClass="clearfix" />
                <formUtil:checkBox command="${command}" field="postNewCause" extraClass="clearfix" />
                <formUtil:checkBox command="${command}" field="eventNewCause" extraClass="clearfix" />
            </div>
        </div>
        <div class="box-ppal-section">
            <fieldset class="form-group text-center">
                <input type="submit" value="${g.message(code:'profile.emailNotifications.save')}" class="btn btn-orange btn-lg">
            </fieldset>
        </div>
    </g:form>
</content>
