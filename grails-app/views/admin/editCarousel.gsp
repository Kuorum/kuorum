<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="admin.menu.domainConfig.uploadCarouselImages.title"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
    <r:require modules="forms"/>
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
        <fieldset class="form-group">
            <label><g:message code="kuorum.web.admin.domain.DomainConfigStep1Command.carousel.label"/>*</label>

            <div class="">
                <div class="col-sm-4">
                    <formUtil:editImage command="${command}" field="slideId1" fileGroup="${kuorum.core.FileGroup.DOMAIN_SLIDE_IMAGE}"/>
                </div>
                <div class="col-sm-4">
                    <formUtil:editImage command="${command}" field="slideId2" fileGroup="${kuorum.core.FileGroup.DOMAIN_SLIDE_IMAGE}"/>
                </div>
                <div class="col-sm-4">
                    <formUtil:editImage command="${command}" field="slideId3" fileGroup="${kuorum.core.FileGroup.DOMAIN_SLIDE_IMAGE}"/>
                </div>
            </div>
        </fieldset>
        <fieldset class="form-group text-center">
            <input type="submit" value="${g.message(code:'default.save')}" class="btn btn-orange btn-lg">
        </fieldset>
    </g:uploadForm>
</content>

