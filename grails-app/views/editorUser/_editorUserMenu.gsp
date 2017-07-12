%{--<div class="admin-user-options">--}%
    %{--<label>User actions</label>--}%

<h1>
    <g:link mapping="userShow" params="${user.encodeAsLinkProperties()}">
        %{--<g:message code="admin.editUser.title" args="[user.name]"/>--}%
        ${user.name}
    </g:link>
</h1>

<ul>
    <sec:ifAnyGranted roles="ROLE_ADMIN">
        <g:if test="${user.enabled}">
            <li><g:render template="/editorUser/switchUser" model="[user:user]"/></li>
        </g:if>
        <li class="${nav.activeMenuCss(mappingName: 'editorAdminUserRights', urlParams:user.encodeAsLinkProperties())}"><g:link mapping="editorAdminUserRights" params="${user.encodeAsLinkProperties()}"><g:message code="admin.menu.user.editRights" args="[user.name]"/></g:link></li>
        <li class="${nav.activeMenuCss(mappingName: 'editorAdminEmailSender', urlParams:user.encodeAsLinkProperties())}"><g:link mapping="editorAdminEmailSender" params="${user.encodeAsLinkProperties()}"><g:message code="admin.menu.user.editEmailSender" args="[user.name]"/></g:link></li>
    </sec:ifAnyGranted>
    <sec:ifAnyGranted roles="ROLE_EDITOR">
        <g:if test="${user.userType == kuorum.core.model.UserType.POLITICIAN}">
            <li class="${nav.activeMenuCss(mappingName: 'editorKuorumAccountEdit', urlParams:user.encodeAsLinkProperties())}">                  <g:link mapping="editorKuorumAccountEdit" params="${user.encodeAsLinkProperties()}"><g:message code="admin.menu.user.editAccount" args="[user.name]"/></g:link></li>
            <li class="${nav.activeMenuCss(mappingName: 'editorEditPoliticianCauses', urlParams:user.encodeAsLinkProperties())}">               <g:link mapping="editorEditPoliticianCauses" params="${user.encodeAsLinkProperties()}"> <g:message code="admin.menu.user.editCauses" args="[user.name]"/></g:link></li>
            <li class="${nav.activeMenuCss(mappingName: 'editorEditUserProfile', urlParams:user.encodeAsLinkProperties())}">                    <g:link mapping="editorEditUserProfile" params="${user.encodeAsLinkProperties()}"> <g:message code="admin.menu.user.editProfile" args="[user.name]"/></g:link></li>
            <li class="${nav.activeMenuCss(mappingName: 'editorEditNews', urlParams:user.encodeAsLinkProperties())}">                           <g:link mapping="editorEditNews" params="${user.encodeAsLinkProperties()}"><g:message code="admin.menu.user.editNews" args="[user.name]"/> </g:link></li>
            <li class="${nav.activeMenuCss(mappingName: 'editorEditSocialNetwork', urlParams:user.encodeAsLinkProperties())}">                  <g:link mapping="editorEditSocialNetwork" params="${user.encodeAsLinkProperties()}"> <g:message code="admin.menu.user.editSocialNetwork" args="[user.name]"/></g:link></li>
            <li class="${nav.activeMenuCss(mappingName: 'editorEditPoliticianProfessionalDetails', urlParams:user.encodeAsLinkProperties())}">  <g:link mapping="editorEditPoliticianProfessionalDetails" params="${user.encodeAsLinkProperties()}"><g:message code="admin.menu.user.editProfessionalDetails" args="[user.name]"/> </g:link></li>
            <li class="${nav.activeMenuCss(mappingName: 'editorEditPoliticianQuickNotes', urlParams:user.encodeAsLinkProperties())}">           <g:link mapping="editorEditPoliticianQuickNotes" params="${user.encodeAsLinkProperties()}"> <g:message code="admin.menu.user.editPersonalNotes" args="[user.name]"/></g:link></li>
        </g:if>
        <g:else>
            <li class="${nav.activeMenuCss(mappingName: 'editorKuorumAccountEdit', urlParams:user.encodeAsLinkProperties())}">                  <g:link mapping="editorKuorumAccountEdit" params="${user.encodeAsLinkProperties()}"><g:message code="admin.menu.user.editAccount" args="[user.name]"/></g:link></li>
            <li class="${nav.activeMenuCss(mappingName: 'editorEditUserProfile', urlParams:user.encodeAsLinkProperties())}">                    <g:link mapping="editorEditUserProfile" params="${user.encodeAsLinkProperties()}"> <g:message code="admin.menu.user.editProfile" args="[user.name]"/></g:link></li>
            <li class="${nav.activeMenuCss(mappingName: 'editorEditSocialNetwork', urlParams:user.encodeAsLinkProperties())}">                  <g:link mapping="editorEditSocialNetwork" params="${user.encodeAsLinkProperties()}"> <g:message code="admin.menu.user.editSocialNetwork" args="[user.name]"/></g:link></li>
        </g:else>
    </sec:ifAnyGranted>
</ul>
%{--</div>--}%