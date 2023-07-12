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
        <li><g:render template="/editorUser/switchUser" model="[user: user]"/></li>
    </sec:ifAnyGranted>
    <sec:ifAnyGranted roles="ROLE_SUPER_ADMIN, ROLE_ADMIN">
        <li class="${nav.activeMenuCss(mappingName: 'editorKuorumAccountEdit', urlParams: user.encodeAsLinkProperties())}"><g:link
                mapping="editorKuorumAccountEdit" params="${user.encodeAsLinkProperties()}"><g:message
                    code="admin.menu.user.editAccount" args="[user.name]"/></g:link></li>
        <li class="${nav.activeMenuCss(mappingName: 'editorKuorumAccountPicturesEdit', urlParams: user.encodeAsLinkProperties())}"><g:link
                mapping="editorKuorumAccountPicturesEdit" params="${user.encodeAsLinkProperties()}"><g:message
                    code="customRegister.fillProfile.images" args="[user.name]"/></g:link></li>
    </sec:ifAnyGranted>
    <sec:ifAnyGranted roles="ROLE_SUPER_ADMIN">
        <li class="${nav.activeMenuCss(mappingName: 'editorAdminUserRights', urlParams: user.encodeAsLinkProperties())}"><g:link
                mapping="editorAdminUserRights" params="${user.encodeAsLinkProperties()}"><g:message
                    code="admin.menu.user.editRights" args="[user.name]"/></g:link></li>
    </sec:ifAnyGranted>
</ul>
%{--</div>--}%