<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.profileProfessionalDetails"/> </title>
    <meta name="layout" content="leftMenuConfigLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <h1><g:message code="admin.adminPrincipal.title"/></h1>
    <g:render template="/admin/adminMenu" model="[activeMapping:'adminDomainConfigUploadCarouselImages']"/>
</content>
<content tag="titleContent">
    <h1><g:message code="admin.menu.domainConfig.uploadCarouselImages.title"/></h1>
    <h3><g:message code="admin.menu.domainConfig.uploadCarouselImages.subtitle"/></h3>
</content>
<content tag="mainContent">

    <g:uploadForm mapping="adminDomainConfigUploadCarouselImages">
        <fieldset class="form-group image fondoperfil" accept=".jpg" data-multimedia-switch="on" data-multimedia-type="IMAGE">
            <formUtil:editImage command="${command}" accept=".jpg,.jpeg" field="slideId1" fileGroup="${kuorum.core.FileGroup.DOMAIN_SLIDE_IMAGE}"/>
        </fieldset>
        <fieldset class="form-group image fondoperfil" accept=".jpg" data-multimedia-switch="on" data-multimedia-type="IMAGE">
            <formUtil:editImage command="${command}" accept=".jpg,.jpeg" field="slideId2" fileGroup="${kuorum.core.FileGroup.DOMAIN_SLIDE_IMAGE}"/>
        </fieldset>
        <fieldset class="form-group image fondoperfil" accept=".jpg" data-multimedia-switch="on" data-multimedia-type="IMAGE">
            <formUtil:editImage command="${command}" accept=".jpg,.jpeg" field="slideId3" fileGroup="${kuorum.core.FileGroup.DOMAIN_SLIDE_IMAGE}"/>
        </fieldset>
        <fieldset class="form-group text-center">
            <input type="submit" value="${g.message(code:'profile.emailNotifications.save')}" class="btn btn-orange btn-lg">
        </fieldset>
    </g:uploadForm>

</content>

