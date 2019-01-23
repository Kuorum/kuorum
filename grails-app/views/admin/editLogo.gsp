<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="admin.menu.domainConfig.uploadLogo.title"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <h1><g:message code="admin.adminPrincipal.title"/></h1>
    <g:render template="/admin/adminMenu" model="[activeMapping:'adminDomainConfigUploadLogo']"/>
</content>
<content tag="titleContent">
    <h1><g:message code="admin.menu.domainConfig.uploadLogo.title"/></h1>
    <h3><g:message code="admin.menu.domainConfig.uploadLogo.subtitle"/></h3>
</content>
<content tag="mainContent">

    <g:uploadForm action="uploadLogo">
        <div class="input-group">
            <label class="input logo" accept=".png">
                <form>
                <span class="btn upload btn-primary"><g:message code="admin.menu.uploadLogo.selectFile"/>
                    <input type="file" style="display: none;" name="logo" accept=".png">
                </span>
                </form>
            </label>
            <button type="submit" class="btn btn-primary" onclick="pageLoadingOn('submitLogo')"><g:message code="admin.menu.uploadLogo.sendFile"/> <i class="fal fa-cloud-upload fa-2" aria-hidden="true"></i></button>
        </div>
    </g:uploadForm>
</content>
