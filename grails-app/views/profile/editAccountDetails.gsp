<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.profileEditAccountDetails"/> </title>
    <meta name="layout" content="leftMenuConfigLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <g:render template="leftMenuAccount" model="[user:user, activeMapping:'profileEditAccountDetails', menu:menu]"/>
    <r:require modules="forms"/>
</content>

<content tag="titleContent">
    <h1><g:message code="profile.menu.profileEditAccountDetails"/></h1>
    <h3><g:message code="profile.menu.profileEditAccountDetails.subtitle"/></h3>
</content>
<content tag="mainContent">
    <formUtil:validateForm bean="${command}" form="accountDetailsForm" dirtyControl="true"/>
    <g:form method="POST" mapping="profileEditAccountDetails" name="accountDetailsForm" role="form" class="submitOrangeButton" autocomplete="noFill">
        <div class="box-ppal-section">
            <g:render template="accountDetailsForm" model="[command:command]"/>
            <fieldset class="row">
                <div class="form-group col-md-6">
                    <input type="text" name="autocompleteNameOff" style="display:none" data-ays-ignore="true"/>
                    <formUtil:password command="${command}" field="password" required="true" showLabel="true"/>
                </div>
            </fieldset>
        </div>
        <div class="box-ppal-section">
            <fieldset class="form-group text-center">
                <input type="submit" value="${g.message(code:'profile.emailNotifications.save')}" class="btn btn-orange btn-lg">
            </fieldset>
        </div>
    </g:form>
</content>
