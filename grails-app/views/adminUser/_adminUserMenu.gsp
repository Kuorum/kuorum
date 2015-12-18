<div class="admin-user-options">
    <label>User actions</label>
    <ul>
        <li><g:render template="/adminUser/switchUser" model="[user:user]"/></li>
        <li><g:link mapping="adminEditPoliticianExternalActivity" params="${user.encodeAsLinkProperties()}"> Edit External activity</g:link></li>
        <li>extra link</li>
        <li>extra link</li>
    </ul>
</div>