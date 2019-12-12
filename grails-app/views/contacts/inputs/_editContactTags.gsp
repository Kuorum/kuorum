<%@ page import="org.kuorum.rest.model.contact.filter.condition.TextConditionOperatorTypeRDTO; org.kuorum.rest.model.contact.filter.condition.ConditionFieldTypeRDTO; org.kuorum.rest.model.contact.filter.condition.ConditionTextRDTO; org.kuorum.rest.model.contact.filter.condition.ConditionRDTO" %>
<form action="${g.createLink(mapping: 'politicianContactAddTagsAjax',params: [contactId:contact.id])}" class="addTag off">
    <a href="#" role="button" class="tag label label-info addTagBtn"><span class="fal fa-tag"></span><g:message code="tools.contact.list.contact.editTags"/></a>
    <ul data-genericTagLink="${g.createLink(mapping: "politicianContacts", params: ['filterConditions[0].field':'TAG','filterConditions[0].operatorText':'EQUALS', 'filterConditions[0].value':'REPLACED_TAG'])}">
        <g:each in="${contact.tags}" var="tag">
            <li><a href="${g.createLink(mapping: "politicianContacts", params: ['filterConditions[0].field':'TAG','filterConditions[0].operatorText':'EQUALS', 'filterConditions[0].value':URLEncoder.encode(tag, "UTF-8")])}" class="tag label label-info">${tag}</a></li>
        </g:each>
    </ul>
    <label for="tagsField_${contact.id}" class="sr-only"><g:message code="tools.contact.list.contact.saveTags"/> </label>
    <input id="tagsField_${contact.id}" name="tags" class="tagsField" type="text" data-urlTags="${g.createLink(mapping:'politicianContactTagsAjax')}" value="${contact.tags.collect{URLEncoder.encode(it, "UTF-8")}.join(",")}">
    <input type="submit" value="Save tags" class="btn btn-blue inverted" id="inputAddTags">
</form>