<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.socialNetworks"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <g:render template="leftMenu" model="[user:user, activeMapping:'profileSocialNetworks', menu:menu]"/>

</content>

<content tag="mainContent">
    <g:form mapping="profileSocialNetworks" role="form" class="box-ppal">
        %{--<h1><g:message code="profile.socialNetworks.title"/></h1>--}%
        <fieldset class="row">
        <div class="form-group col-md-6">
            <formUtil:socialInput command="${command}" field="facebook" cssIcon="fa-facebook"/>
        </div>
        </fieldset>
        <fieldset class="row">
        <div class="form-group col-md-6">
            <formUtil:socialInput command="${command}" field="twitter" cssIcon="fa-twitter"/>
        </div>
        </fieldset>
        <fieldset class="row">
        <div class="form-group col-md-6">
            <formUtil:socialInput command="${command}" field="blog" cssIcon="fa-rss-square"/>
        </div>
        </fieldset>
        <fieldset class="row">
        <div class="form-group col-md-6">
            <formUtil:socialInput command="${command}" field="googlePlus" cssIcon="fa-google-plus"/>
        </div>
        </fieldset>
        <fieldset class="form-group text-right">
        <div class="form-group">
            <a href="#" class="cancel"><g:message code="profile.emailNotifications.cancel"/></a>
            <input type="submit" value="${g.message(code:'profile.emailNotifications.save')}" class="btn btn-grey btn-lg">
        </div>
        </fieldset>
    </g:form>
</content>
