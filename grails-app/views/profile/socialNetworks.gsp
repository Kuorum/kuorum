<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.socialNetworks"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <h1>
        <g:message code="profile.socialNetworks.salutation"/>,
        <br>
        <span class="user-name">${user.name}</span>
    </h1>
    <p><g:message code="profile.socialNetworks.description"/></p>
    <g:render template="leftMenu" model="[user:user, activeMapping:'profileSocialNetworks', menu:menu]"/>

</content>

<content tag="mainContent">
    <h1><g:message code="profile.socialNetworks.title"/></h1>
    <g:form mapping="profileSocialNetworks" role="form">
        <div class="form-group">
            <formUtil:socialInput command="${command}" field="facebook" cssIcon="fa-facebook"/>
        </div>
        <div class="form-group">
            <formUtil:socialInput command="${command}" field="twitter" cssIcon="fa-twitter"/>
        </div>
        <div class="form-group">
            <formUtil:socialInput command="${command}" field="blog" cssIcon="fa-rss-square"/>
        </div>
        <div class="form-group">
            <formUtil:socialInput command="${command}" field="googlePlus" cssIcon="fa-google-plus"/>
        </div>
        <div class="form-group">
            <input type="submit" value="Guardar" class="btn btn-grey btn-lg">
            <a href="#" class="cancel">Cancelar</a>
        </div>
    </g:form>
</content>
