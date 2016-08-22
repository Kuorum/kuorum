<g:each in="${filters}" var="filter">
    <g:render template="/contacts/filter/filterFieldSet" model="[filter:filter]"/>
</g:each>