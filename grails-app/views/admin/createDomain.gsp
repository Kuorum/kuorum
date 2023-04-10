<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title>Admin - Solr Index</title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config"/>
</head>

<content tag="leftMenu">
    <h1>
        <g:message code="admin.solrIndex.title"/>
    </h1>
    <g:render template="/admin/adminMenu" model="[activeMapping: 'adminDomainCreate', menu: menu]"/>
</content>

<content tag="mainContent">
    <h1>Create new domain</h1>
    <g:form mapping="adminDomainCreate" method="POST">
        <fieldset aria-live="polite" class="row">
            <div class="form-group col-md-6">
                <formUtil:input field="name" command="${command}"/>
            </div>
        </fieldset>
        <fieldset aria-live="polite" class="row">
            <div class="form-group text-center">
                <button type="submit" class="btn btn-default btn-lg">Create new domain</button>
            </div>
        </fieldset>
    </g:form>
    <g:if test="${newDomainDataRSDTO}">
        <h1>Domain Created</h1>

        <p><a href="https://${newDomainDataRSDTO.domain}/sec/admin/domain/config/registering?provider=api&token=${newDomainDataRSDTO.userLoginToken}">${newDomainDataRSDTO.domain}</a>
        </p>
    </g:if>
</content>
