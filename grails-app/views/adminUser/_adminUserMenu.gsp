<div class="admin-user-options">
    <label>User actions</label>
    <ul>
        <li><g:render template="/adminUser/switchUser" model="[user:user]"/></li>
        <li><g:link mapping="adminKuorumAccountEdit" params="${user.encodeAsLinkProperties()}"> Kuorum Account </g:link></li>
        <li><g:link mapping="adminEditPoliticianCauses" params="${user.encodeAsLinkProperties()}"> Edit causes</g:link></li>
        <li><g:link mapping="adminEditUser" params="${user.encodeAsLinkProperties()}"> Edit Perfil</g:link></li>
        <li><g:link mapping="adminEditPoliticianExternalActivity" params="${user.encodeAsLinkProperties()}"> Edit Latest activity</g:link></li>
        <li><g:link mapping="adminEditPoliticianRelevantEvents" params="${user.encodeAsLinkProperties()}"> Edit Known for</g:link></li>
        <li><g:link mapping="adminEditPoliticianProfessionalDetails" params="${user.encodeAsLinkProperties()}"> Edit Professional details</g:link></li>
        <li><g:link mapping="adminEditPoliticianQuickNotes" params="${user.encodeAsLinkProperties()}"> Edit Personal notes</g:link></li>
        <li><g:link mapping="adminEditPoliticianExperience" params="${user.encodeAsLinkProperties()}"> <g:message code="admin.menu.adminEditPoliticianExperience.link"/></g:link></li>
    </ul>
</div>