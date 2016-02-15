%{--<div class="admin-user-options">--}%
    %{--<label>User actions</label>--}%
<ul>
    <sec:ifAnyGranted roles="ROLE_ADMIN">
        <g:if test="${user}">
                <g:if test="${user.enabled}">
                    <li><g:render template="/editorUser/switchUser" model="[user:user]"/></li>
                </g:if>
                <li><g:link mapping="editorAdminUserRights" params="${user.encodeAsLinkProperties()}"><g:message code="admin.menu.user.editRights" args="[user.name]"/></g:link></li>
            </g:if>
        </sec:ifAnyGranted>
        <sec:ifAnyGranted roles="ROLE_EDITOR">
            <g:if test="${user}">
                <li><g:link mapping="editorKuorumAccountEdit" params="${user.encodeAsLinkProperties()}"><g:message code="admin.menu.user.editAccount" args="[user.name]"/></g:link></li>
                <li><g:link mapping="editorEditPoliticianCauses" params="${user.encodeAsLinkProperties()}"> <g:message code="admin.menu.user.editCauses" args="[user.name]"/></g:link></li>
                <li><g:link mapping="editorEditUserProfile" params="${user.encodeAsLinkProperties()}"> <g:message code="admin.menu.user.editProfile" args="[user.name]"/></g:link></li>
                <li><g:link mapping="editorEditPoliticianRelevantEvents" params="${user.encodeAsLinkProperties()}"><g:message code="admin.menu.user.editKnownFor" args="[user.name]"/> </g:link></li>
                <li><g:link mapping="editorEditSocialNetwork" params="${user.encodeAsLinkProperties()}"> <g:message code="admin.menu.user.editSocialNetwork" args="[user.name]"/></g:link></li>
                <li><g:link mapping="editorEditPoliticianProfessionalDetails" params="${user.encodeAsLinkProperties()}"><g:message code="admin.menu.user.editProfessionalDetails" args="[user.name]"/> </g:link></li>
                <li><g:link mapping="editorEditPoliticianQuickNotes" params="${user.encodeAsLinkProperties()}"> <g:message code="admin.menu.user.editPersonalNotes" args="[user.name]"/></g:link></li>
                <li><g:link mapping="editorEditPoliticianExternalActivity" params="${user.encodeAsLinkProperties()}">  <g:message code="admin.menu.user.editLastActivity" args="[user.name]"/></g:link></li>
                <li><g:link mapping="editorEditPoliticianExperience" params="${user.encodeAsLinkProperties()}"> <g:message code="admin.menu.user.editPoliticianExperience" args="[user.name]"/></g:link></li>
            </g:if>
        </sec:ifAnyGranted>
    </ul>
%{--</div>--}%