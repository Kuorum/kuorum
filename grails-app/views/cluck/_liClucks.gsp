<g:each in="${clucks}" var="cluck">
    <g:render template="/cluck/liCluck" model="[post:cluck.post]"/>
</g:each>