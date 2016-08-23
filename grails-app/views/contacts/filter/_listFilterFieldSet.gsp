<%@ page import="org.kuorum.rest.model.contact.filter.ConditionRDTO; org.kuorum.rest.model.contact.filter.FilterRDTO" %>
<g:each in="${filters}" var="filter">
    <g:render template="/contacts/filter/filterFieldSet" model="[filter:filter]"/>
</g:each>
<g:render template="/contacts/filter/filterFieldSet" model="[filter:new org.kuorum.rest.model.contact.filter.ExtendedFilterRSDTO([id:-2, filterConditions:[new org.kuorum.rest.model.contact.filter.ConditionRDTO()]])]"/>