<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title>Admin - Solr Index</title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <h1>
        <g:message code="admin.solrIndex.title"/>
    </h1>
    <g:render template="/admin/adminMenu" model="[activeMapping:'adminRecerateAllCss', menu:menu]"/>
</content>

<content tag="mainContent">
    <h1>Recreate all css asynchronous </h1>
    <g:form mapping="adminRecerateAllCss" method="POST">
        <fieldset aria-live="polite">
            <div class="form-group text-center">
                <button type="submit" class="btn btn-default btn-lg">Recreate</button>
            </div>
        </fieldset>
    </g:form>
</content>
