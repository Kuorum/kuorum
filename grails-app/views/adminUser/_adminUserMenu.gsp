<div class="admin-user-options">
    <label>User actions</label>
    <ul>
        <li><g:render template="/adminUser/switchUser" model="[user:user]"/></li>
        <li><g:link mapping="adminEditPoliticianExternalActivity" params="${user.encodeAsLinkProperties()}"> Edit External activity</g:link></li>
        <li><g:link mapping="adminEditPoliticianRelevantEvents" params="${user.encodeAsLinkProperties()}"> Edit Relevant events</g:link></li>
        <li><g:link mapping="adminEditPoliticianProfessionalDetails" params="${user.encodeAsLinkProperties()}"> Edit Professional details</g:link></li>
        <li><g:link mapping="adminEditPoliticianQuickNotes" params="${user.encodeAsLinkProperties()}"> Edit Quick notes</g:link></li>
        <li><g:link mapping="adminEditPoliticianCauses" params="${user.encodeAsLinkProperties()}"> Edit causes</g:link></li>
        <li><g:link mapping="adminEditPoliticianExperience" params="${user.encodeAsLinkProperties()}"> <g:message code="admin.menu.adminEditPoliticianExperience.link"/></g:link></li>
    </ul>
</div>