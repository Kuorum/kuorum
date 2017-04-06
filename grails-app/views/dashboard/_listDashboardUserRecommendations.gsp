<g:each in="${politicians}" var="politician">
    <li class="col-sm-6">
        <g:render template="/modules/users/recommendedUser" model="[user:politician]"/>
    </li>
</g:each>