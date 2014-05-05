<g:each in="${clucks}" var="cluck">
    <g:render template="/cluck/cluck" model="[cluck:cluck]"/>
</g:each>