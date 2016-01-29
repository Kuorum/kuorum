%{--<div class="admin-user-options">--}%
    %{--<label>User actions</label>--}%
    <ul>
        <sec:ifAnyGranted roles="ROLE_ADMIN">
            <li><g:render template="/adminUser/switchUser" model="[user:user]"/></li>
        </sec:ifAnyGranted>
        <li><g:link mapping="adminKuorumAccountEdit" params="${user.encodeAsLinkProperties()}"><g:message code="admin.menu.user.editAccount" args="[user.name]"/></g:link></li>
        <li><g:link mapping="adminEditPoliticianCauses" params="${user.encodeAsLinkProperties()}"> <g:message code="admin.menu.user.editCauses" args="[user.name]"/></g:link></li>
        <li><g:link mapping="adminEditUser" params="${user.encodeAsLinkProperties()}"> <g:message code="admin.menu.user.editProfile" args="[user.name]"/></g:link></li>
        <li><g:link mapping="adminEditPoliticianExternalActivity" params="${user.encodeAsLinkProperties()}">  <g:message code="admin.menu.user.editLastActivity" args="[user.name]"/></g:link></li>
        <li><g:link mapping="adminEditPoliticianRelevantEvents" params="${user.encodeAsLinkProperties()}"><g:message code="admin.menu.user.editKnownFor" args="[user.name]"/> </g:link></li>
        <li><g:link mapping="adminEditPoliticianProfessionalDetails" params="${user.encodeAsLinkProperties()}"><g:message code="admin.menu.user.editProfessionalDetails" args="[user.name]"/> </g:link></li>
        <li><g:link mapping="adminEditPoliticianQuickNotes" params="${user.encodeAsLinkProperties()}"> <g:message code="admin.menu.user.editPersonalNotes" args="[user.name]"/></g:link></li>
        <li><g:link mapping="adminEditPoliticianExperience" params="${user.encodeAsLinkProperties()}"> <g:message code="admin.menu.user.editPoliticianExperience" args="[user.name]"/></g:link></li>
    </ul>
%{--</div>--}%