<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="admin.createProject.title"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <h1>
        <g:message code="admin.editProject.title" args="[project.hashtag]"/>,
    </h1>
    %{--<p><g:message code="profile.changeEmail.description"/></p>--}%
    <g:render template="/admin/adminMenu" model="[activeMapping:'profileChangeEmail', menu:menu]"/>

</content>

<content tag="mainContent">
    <h1><g:message code="admin.editProject.title" args="[project.hashtag]"/></h1>
    <formUtil:validateForm bean="${command}" form="createProject"/>
    <g:form method="POST" mapping="adminEditProject" params="${project.encodeAsLinkProperties()}" name="createProject" role="form">
        <g:render template="formProject" model="[command:command]"/>
        <div class="form-group">
            <input type="submit" value="${message(code:'admin.editProject.submit')}" class="btn btn-grey btn-lg">
            <g:if test="${project.published}">
                <g:link mapping="adminUnpublishProject" params="${project.encodeAsLinkProperties()}" class="cancel">
                    <g:message code="admin.editProject.unPublish"/>
                </g:link>
            </g:if>
            <g:else>
                <g:link mapping="adminPublishProject" params="${project.encodeAsLinkProperties()}" class="cancel">
                    <g:message code="admin.editProject.publish"/>
                </g:link>
            </g:else>
        </div>
    </g:form>
</content>
