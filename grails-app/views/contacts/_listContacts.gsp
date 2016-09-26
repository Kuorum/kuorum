<%@ page import="org.kuorum.rest.model.contact.filter.ConditionFieldTypeRDTO" %>
<!-- LISTADO DE CONTACTOS -->
<div class="box-ppal list-contacts">
    <div id="contactsOrderOptions" class="box-order-options clearfix">
        <span class="filter-warning">
            <span class="fa fa-filter ${(searchContacts?.filter?.filterConditions || searchContacts?.filterId)?'on':''}"></span>
            <span class="fa fa-search ${searchContacts?.quickSearch?'on':''}"></span>
        </span>
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
        <g:if test="${contacts.data}">
            <g:each in="${contacts.data}" var="contact">
                <g:render template="/contacts/liContact" model="[contact:contact]"/>
            </g:each>

            <!-- MODAL CONFIRM -->
            <div class="modal fade in" id="contactDeleteConfirm" tabindex="-1" role="dialog" aria-labelledby="contactDeleteTitle" aria-hidden="true">
                <div class="modal-dialog ">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
                                <span aria-hidden="true" class="fa fa-times-circle-o fa"></span><span class="sr-only"><g:message code="modalDefend.close"/></span>
                            </button>
                            <h4 id="contactDeleteTitle">
                                <g:message code="tools.massMailing.deleteContactModal.title"/>
                            </h4>
                        </div>
                        <div class="modal-body">
                            <a href="#UrlUpdatedByAjax" role="button" class="btn btn-blue inverted btn-lg deleteContactBtn">
                                <g:message code="tools.massMailing.deleteContactModal.button"/>
                            </a>
                        </div>
                    </div>
                </div>
            </div>

        </g:if>
        <g:else>
            <h3 class="noContacts"><g:message code="tools.contact.list.noResults"/></h3>
        </g:else>
    </ul>

    <div class="pag-list-contacts clearfix">
        <nav:contactPagination
                currentPage="${searchContacts.page}"
                sizePage="${searchContacts.size}"
                total="${contacts.total}"
                ulClasss="paginationBottom"/>
    </div>
</div>