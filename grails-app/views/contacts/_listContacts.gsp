<%@ page import="org.kuorum.rest.model.contact.filter.ConditionFieldTypeRDTO" %>
<!-- LISTADO DE CONTACTOS -->
<div class="box-ppal list-contacts">
    <div id="contactsOrderOptions" class="box-order-options clearfix">
        <span><g:message code="tools.contact.list.sort.label"/> </span>
        <ul class="pag-list-sort">
            <!-- añadir la clase "active asc" o "active desc" al <a> -->
            <li><a href="#" role="button" class="sort ${searchContacts.sort.field==org.kuorum.rest.model.contact.filter.ConditionFieldTypeRDTO.NAME?"active "+searchContacts.sort.direction.toString().toLowerCase():''}"   data-sort="${org.kuorum.rest.model.contact.filter.ConditionFieldTypeRDTO.NAME}"><g:message code="org.kuorum.rest.model.contact.filter.ConditionFieldTypeRDTO.NAME"/></a></li>
            <li><a href="#" role="button" class="sort ${searchContacts.sort.field==org.kuorum.rest.model.contact.filter.ConditionFieldTypeRDTO.EMAIL?"active "+searchContacts.sort.direction.toString().toLowerCase():''}"  data-sort="${org.kuorum.rest.model.contact.filter.ConditionFieldTypeRDTO.EMAIL}"><g:message code="org.kuorum.rest.model.contact.filter.ConditionFieldTypeRDTO.EMAIL"/></a></li>
            %{--<li><a href="#" role="button" class="sort" data-sort="recip-number">Followers</a></li>--}%
            %{--<li><a href="#" role="button" class="sort" data-sort="open-number">Engagement</a></li>--}%
        </ul>
        <div class="pag-list-contacts">
            <nav:contactPagination
                    currentPage="${searchContacts.page}"
                    sizePage="${searchContacts.size}"
                    total="${contacts.total}"
                    ulClasss="paginationTop"/>
        </div>
    </div>
    <ul id="contactsList" class="list">
        <g:each in="${contacts.data}" var="contact">
            <g:render template="/contacts/liContact" model="[contact:contact]"/>
        </g:each>
    </ul>

    <div class="pag-list-contacts clearfix">
        <nav:contactPagination
                currentPage="${searchContacts.page}"
                sizePage="${searchContacts.size}"
                total="${contacts.total}"
                ulClasss="paginationBottom"/>
    </div>
</div>