<div class="admin-user-options">
    <label>User actions</label>
    <ul>
        <li><g:render template="/adminUser/switchUser" model="[user:user]"/></li>
        <li><g:link mapping="adminEditPoliticianExternalActivity" params="${user.encodeAsLinkProperties()}"> Edit External activity</g:link></li>
        <li><g:link mapping="adminEditPoliticianRelevantEvents" params="${user.encodeAsLinkProperties()}"> Edit Relevant events</g:link></li>
        <li><g:link mapping="adminEditPoliticianProfessionalDetails" params="${user.encodeAsLinkProperties()}"> Edit Professional details</g:link></li>
    </ul>
</div>