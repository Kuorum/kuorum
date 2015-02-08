<g:each in="${clucks}" var="cluck">
    <g:render template="/cluck/cluck" model="[post:cluck.post]"/>
</g:each>