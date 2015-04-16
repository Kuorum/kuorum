<g:each in="${clucks}" var="cluck">
    <g:render template="/cluck/liCluck" model="[cluck:cluck, displayingColumnC:false]"/>
</g:each>