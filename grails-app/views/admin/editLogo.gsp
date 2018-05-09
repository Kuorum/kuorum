<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.profileProfessionalDetails"/> </title>
    <meta name="layout" content="leftMenuConfigLayout">
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
            <label class="input logo">
                <form>
                <span class="btn upload btn-primary">Select file
                    <input type="file" style="display: none;" name="logo" accept="image/*">
                </span>
                </form>
            </label>
            <button type="submit" class="btn btn-primary">Send <i class="fa fa-cloud-upload fa-2" aria-hidden="true"></i></button>
            %{--<button type="submit" class="btn btn-primary" id="load" data-loading-text=<i class="'fa fa-circle-o-notch fa-spin'></i> Processing Order">Send <i class="fa fa-cloud-upload fa-2" aria-hidden="true"></i></button>--}%
        </div>
    </g:uploadForm>

    %{--<form>--}%
        %{--<span><i class="fa fa-cloud-upload fa-2" aria-hidden="true"></i></span>--}%
        %{--<input class="btn btn-lg" type="file" name="pic" accept="image/*">--}%
        %{--<input type="submit">--}%
    %{--</form>--}%

    %{--<formUtil:validateForm form="domainConfigForm" bean="${command}" dirtyControl="true"/>--}%
    %{--<g:form method="POST" mapping="adminDomainConfig" name="domainConfigForm" role="form">--}%
        %{--<g:render template="domainConfigForm" model="[command:command]"/>--}%
    %{--</g:form>--}%
</content>
