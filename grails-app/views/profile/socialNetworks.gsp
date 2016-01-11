<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.socialNetworks"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <g:render template="leftMenu" model="[user:user, activeMapping:'profileSocialNetworks', menu:menu]"/>

</content>

<content tag="titleContent">
    <h1><g:message code="profile.menu.profileSocialNetworks"/></h1>
    <h3><g:message code="profile.menu.profileSocialNetworks.subtitle"/></h3>
</content>
<content tag="mainContent">
    <g:form mapping="profileSocialNetworks" role="form">
        <sec:ifAnyGranted roles="ROLE_POLITICIAN, ROLE_ADMIN">
            <fieldset class="row">
                <div class="form-group col-md-6">
                    <formUtil:url command="${command}" field="officialWebSite" showLabel="true"/>
                </div>
                <div class="form-group col-md-6">
                    <formUtil:url command="${command}" field="institutionalWebSite" showLabel="true"/>
                </div>
            </fieldset>
        </sec:ifAnyGranted>
        <fieldset class="row">
            <div class="form-group col-md-6">
                <formUtil:socialInput command="${command}" field="facebook" cssIcon="fa-facebook"/>
            </div>
            <div class="form-group col-md-6">
                <formUtil:socialInput command="${command}" field="twitter" cssIcon="fa-twitter"/>
            </div>
        </fieldset>
        <fieldset class="row">
            <div class="form-group col-md-6">
                <formUtil:socialInput command="${command}" field="googlePlus" cssIcon="fa-google-plus"/>
            </div>
            <div class="form-group col-md-6">
                <formUtil:socialInput command="${command}" field="blog" cssIcon="fa-rss-square"/>
            </div>
        </fieldset>
        <sec:ifAnyGranted roles="ROLE_POLITICIAN, ROLE_ADMIN">
            <fieldset class="row">
                <div class="form-group col-md-6">
                    <formUtil:socialInput command="${command}" field="linkedIn" cssIcon="fa-linkedin-square"/>
                </div>
                <div class="form-group col-md-6">
                    <formUtil:socialInput command="${command}" field="instagram" cssIcon="fa-instagram"/>
                </div>
            </fieldset>
            <fieldset class="row">
                <div class="form-group col-md-6">
                    <formUtil:socialInput command="${command}" field="youtube" cssIcon="fa-youtube-square"/>
                </div>
                <div class="form-group col-md-6">
                    %{--<formUtil:socialInput command="${command}" field="pinterest" cssIcon="fa-pinterest-square"/>--}%
                </div>
            </fieldset>
        </sec:ifAnyGranted>
        <fieldset class="form-group text-center">
            <input type="submit" value="${g.message(code:'profile.emailNotifications.save')}" class="btn btn-orange btn-lg">
        </fieldset>
    </g:form>
</content>
