<g:each in="${posts}" var="post">
    <g:render template="/cluck/liCluck" model="[post:post, displayingColumnC:false]"/>
</g:each>