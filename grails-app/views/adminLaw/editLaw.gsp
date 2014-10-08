<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="admin.createLaw.title"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <h1>
        <g:message code="admin.editLaw.title" args="[law.hashtag]"/>,
    </h1>
    %{--<p><g:message code="profile.changeEmail.description"/></p>--}%
    <g:render template="/admin/adminMenu" model="[activeMapping:'profileChangeEmail', menu:menu]"/>

</content>

<content tag="mainContent">
    <h1><g:message code="admin.editLaw.title" args="[law.hashtag]"/></h1>
    <formUtil:validateForm bean="${command}" form="createLaw"/>
    <g:form method="POST" mapping="adminEditLaw" params="${law.encodeAsLinkProperties()}" name="createLaw" role="form">
        <g:render template="formLaw" model="[command:command, regions:regions, institutions:institutions]"/>
        <div class="form-group">
            <input type="submit" value="${message(code:'admin.editLaw.submit')}" class="btn btn-grey btn-lg">
            <g:if test="${law.published}">
                <g:link mapping="adminUnpublishLaw" params="${law.encodeAsLinkProperties()}" class="cancel">
                    <g:message code="admin.editLaw.unPublish"/>
                </g:link>
            </g:if>
            <g:else>
                <g:link mapping="adminPublishLaw" params="${law.encodeAsLinkProperties()}" class="cancel">
                    <g:message code="admin.editLaw.publish"/>
                </g:link>
            </g:else>
        </div>
    </g:form>
</content>
