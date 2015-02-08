<g:each in="${posts}" var="post">
    <g:render template="/cluck/cluck" model="[post:post]"/>
</g:each>