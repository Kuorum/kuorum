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
        <h1><g:message code="profile.socialNetworks.title"/></h1>
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
