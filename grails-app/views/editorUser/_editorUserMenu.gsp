%{--<div class="admin-user-options">--}%
    %{--<label>User actions</label>--}%

<h1>
    <g:link mapping="userShow" params="${user.encodeAsLinkProperties()}">
        %{--<g:message code="admin.editUser.title" args="[user.name]"/>--}%
        ${user.name}
    </g:link>
</h1>

<ul>
    <sec:ifAnyGranted roles="ROLE_SUPER_ADMIN">
        %{--<g:if test="${user.active}">--}%
            <li><g:render template="/editorUser/switchUser" model="[user:user]"/></li>
        %{--</g:if>--}%
        <li class="${nav.activeMenuCss(mappingName: 'editorKuorumAccountEdit', urlParams:user.encodeAsLinkProperties())}"><g:link mapping="editorKuorumAccountEdit" params="${user.encodeAsLinkProperties()}"><g:message code="admin.menu.user.editAccount" args="[user.name]"/></g:link></li>
        <li class="${nav.activeMenuCss(mappingName: 'editorAdminUserRights', urlParams:user.encodeAsLinkProperties())}"><g:link mapping="editorAdminUserRights" params="${user.encodeAsLinkProperties()}"><g:message code="admin.menu.user.editRights" args="[user.name]"/></g:link></li>
    </sec:ifAnyGranted>
</ul>
%{--</div>--}%