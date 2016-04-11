<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="admin.menu.adminEditorsMonitoring"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <h1>
        <g:message code="admin.menu.adminEditorsMonitoring"/>,
    </h1>
    %{--<p><g:message code="profile.changeEmail.description"/></p>--}%
    <g:render template="/admin/adminMenu" model="[activeMapping:'adminPrincipal', menu:menu]"/>

</content>

<content tag="mainContent">
    <h1><g:message code="admin.menu.adminEditorsMonitoring"/></h1>

    <g:each in="${audits}" var="audit">

        <div class="panel-group" style="margin-bottom: 5px;">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h4 class="panel-title" style="font-size: 14px;">
                        <g:link mapping="userShow" params="${audit.editor.encodeAsLinkProperties()}">${audit.editor.name}</g:link>
                        <small> has edited </small>
                        <g:link mapping="userShow" params="${audit.user.encodeAsLinkProperties()}">${audit.user.name}</g:link>
                        <a data-toggle="collapse" style="float:right" href="#collapse_${audit.id}">Open changes</a>
                    </h4>
                </div>
                <div id="collapse_${audit.id}" class="panel-collapse collapse" style="padding:1em">
                    <table width="100%;">
                        <thead style="background-color: #CCCCCC">
                            <tr>
                                <td style="width: 50%">Label</td>
                                <td style="width: 50%">new value</td>
                            </tr>
                        </thead>
                        <tbody>
                            <g:each in="${audit.updates}" var="update">
                                <tr>
                                    <td style="width: 50%">${update.key}</td>
                                    <td style="width: 50%">${update.value}</td>
                                </tr>
                            </g:each>
                        </tbody>
                    </table>
                    <div class="panel-footer">Audit date: <g:formatDate date="${audit.dateCreated}" format="dd/MM/yyyy HH:mm:ss"/></div>
                </div>
            </div>
        </div>

    </g:each>
</content>
