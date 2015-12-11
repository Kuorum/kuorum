<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.editUser"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <g:render template="leftMenu" model="[user:user, activeMapping:'profilePoliticianProfessionalDetails', menu:menu]"/>

</content>
<content tag="mainContent">
    <formUtil:validateForm bean="${command}" form="professionalDetailsForm" />
    <g:form method="POST" mapping="profilePoliticianProfessionalDetails" name="professionalDetailsForm" role="form" class="box-ppal">
    %{--<h1><g:message code="profile.editUser.title"/></h1>--}%

        <fieldset class="row">
            <div class="form-group col-md-6">
                <formUtil:input command="${command}" field="completeName" required="true" showLabel="true"/>
            </div>
            <div class="form-group col-md-6">
                <formUtil:input command="${command}" field="birthPlace" required="true" showLabel="true"/>
            </div>
        </fieldset>

        <fieldset class="row">
            <div class="form-group col-md-6">
                <formUtil:input command="${command}" field="university" required="true" showLabel="true"/>
            </div>
            <div class="form-group col-md-6">
                <formUtil:input command="${command}" field="studies" required="true" showLabel="true"/>
            </div>
            <div class="form-group col-md-6">
                <formUtil:input command="${command}" field="school" required="true" showLabel="true"/>
            </div>
        </fieldset>

        <fieldset class="form-group text-right">
            <a href="#" class="cancel" tabindex="19"><g:message code="profile.emailNotifications.cancel"/></a>
            <input type="submit" value="${g.message(code:'profile.emailNotifications.save')}" class="btn btn-grey btn-lg">
        </fieldset>
    </g:form>
</content>
