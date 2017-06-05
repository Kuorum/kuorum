<%@ page import="org.kuorum.rest.model.contact.filter.condition.ConditionStatusRDTO; org.kuorum.rest.model.contact.filter.condition.ConditionTextRDTO" %>
<ul class="engagement">
    <g:each in="${org.kuorum.rest.model.contact.ContactStatusRSDTO.values()}" var="status">
        <li class="${contact.status == status?'active':''}">
            <a href="${g.createLink(mapping: "politicianContacts", params: ['filterConditions[0].field':'STATUS','filterConditions[0].operatorNumber':'EQUALS', 'filterConditions[0].value':status])}">
                <g:message code="org.kuorum.rest.model.contact.ContactStatusRSDTO.${status}"/>
            </a>
        </li>
    </g:each>
</ul>