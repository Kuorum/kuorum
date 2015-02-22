<g:each in="${posts}" var="post">
    <g:render template="/cluck/liCluck" model="[post:post]"/>
</g:each>