<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.socialNetworks"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
    <r:require modules="forms"/>
</head>

<content tag="leftMenu">
    <g:render template="leftMenu" model="[activeMapping:'profileSocialNetworks', menu:menu]"/>

</content>

<content tag="titleContent">
    <h1><g:message code="profile.menu.profileSocialNetworks"/></h1>
    <h3><g:message code="profile.menu.profileSocialNetworks.subtitle"/></h3>
</content>
<content tag="mainContent">
    <formUtil:validateForm bean="${command}" form="socialForm" dirtyControl="true"/>
    <g:form mapping="profileSocialNetworks" role="form" dirtyControl="true" name="socialForm">
        <g:set var="showPoliticianFields" value="${false}"/>
        <sec:ifAnyGranted roles="ROLE_POLITICIAN">
            <g:set var="showPoliticianFields" value="${true}"/>
        </sec:ifAnyGranted>
        <div class="box-ppal-section">
            <g:render template="formSocialNetworks"
                      model="[command: command, showPoliticianFields: showPoliticianFields]"/>
        </div>

        <div class="box-ppal-section">
            <fieldset aria-live="polite" class="form-group text-center">
                <input type="submit" value="${g.message(code: 'default.save')}" class="btn btn-orange btn-lg">
            </fieldset>
        </div>
    </g:form>
</content>
