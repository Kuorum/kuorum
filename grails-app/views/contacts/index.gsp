<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.contacts"/></title>
    <meta name="layout" content="basicPlainLayout">

    <meta itemprop="name" content="${_domainName}">
    <r:require modules="contacts"/>
</head>

<content tag="mainContent">
    <ol class="breadcrumb">
        <li class="active"><g:message code="tools.contact.title"/></li>
    </ol>

    <div id="searchContacts">

        <!-- FILTRO Y BUSCADOR DE CONTACTOS -->
        <div class="box-ppal filterbox">
            <formUtil:validateForm command="kuorum.web.commands.payment.contact.ContactFilterCommand" form="contactFilterForm"/>
            <form class="form-horizontal" id="contactFilterForm">
                <input type="hidden" name="sort.field" value="${searchContacts.sort.field}"/>
                <input type="hidden" name="sort.direction" value="${searchContacts.sort.direction}"/>
                <input type="hidden" name="page" value="${searchContacts.page}"/>
                <input type="hidden" name="size" value="${searchContacts.size}"/>
        <fieldset aria-live="polite" class="form-group" id="boxfiltersContacts">
            <div class="col-sm-3">
                <label for="recipients" class="sr-only">Filter Contacts:</label>
                <select name="filterId" class="form-control input-lg" id="recipients">
                    <option value="0" data-amountContacts="${totalContacts}"><g:message code="tools.massMailing.fields.filter.to.all"/></option>
                            <g:each in="${filters}" var="filter">
                                <option value="${filter.id}" ${filterId == filter.id?'selected':''} >${filter.name}</option>
                            </g:each>
                            <g:if test="${anonymousFilter}">
                                <option value="${anonymousFilter.id}" data-amountContacts="${anonymousFilter.amountOfContacts}" data-orginal-filter-id="-2" ${filterId == anonymousFilter.id?'selected':''}>${anonymousFilter.name}</option>
                            </g:if>
                            <option value="-2" data-amountContacts="-"><g:message code="tools.massMailing.fields.filter.to.createNew"/></option>
                        </select>
                    </div>
                    <div class="col-sm-3">
                        <!-- para activarlo añadir clase "on" -->
                        <g:link mapping="politicianContactFilterData" role="button" elementId="filterContacts" title="${g.message(code:'tools.contact.filter.conditions.open')}">
                            <span class="fas fa-filter fa-lg"></span>
                            <span class="sr-only">Filter contacts</span>
                        </g:link>
                        <g:link mapping="politicianContactsSearch" elementId="infoToContacts">
                            <span class="amountRecipients">${anonymousFilter?anonymousFilter.amountOfContacts:contacts.total}</span>
                            <g:message code="tools.massMailing.fields.filter.recipients"/>
                            %{--<span class="fas fa-filter fa-lg"></span>--}%
                            %{--<span class="fal fa-search fa-lg"></span>--}%
                        </g:link>
                    </div>
                    <div class="col-sm-3 col-md-2 col-lg-3">
                        <div class="searchContainer">
                            <input type="text"
                                   class="form-control searchContacts"
                                   name="quickSearchByName"
                                   id="quickSearchByName"
                                   placeholder="${g.message(code:'tools.contact.list.quickSearchByName.placeHolder')}">
                        </div>
                    </div>
                    <div class="col-sm-3 col-md-4 col-lg-3">
                        <ul>
                            <li>
                                <g:link mapping="politicianContactImport" class="btn btn-blue inverted">
                                    <span class="far fa-plus"></span>
                                    <g:message code="tools.contact.list.import"/>
                                </g:link>
                            </li>
                            <li>
                                <a href="#" role="button" id="openContactsOptions" class="btn btn-blue inverted dropdown-toggle" data-toggle="dropdown"><span class="fas fa-caret-down fa-lg"></span></a>
                                <ul id="contactsOptions" class="dropdown-menu dropdown-menu-right" aria-labelledby="openContactsOptions" role="menu">
                                    <li>
                                        <g:link mapping="politicianContactExport" elementId="exportContacts">
                                            <span class="fal fa-file-excel"></span>
                                            <g:message code="tools.contact.list.export.csv"/>
                                        </g:link>
                                    </li>
                                    <li>
                                        <g:link mapping="politicianContactNew" class="">
                                            <span class="fal fa-user-plus"></span>
                                            <g:message code="tools.contact.list.newContact"/>
                                        </g:link>
                                    </li>
                                </ul>
                            </li>
                        </ul>
                    </div>
                </fieldset>
                <div id="newFilterContainer">
                    <g:render template="/contacts/filter/listFilterFieldSet" model="[filters:filters, anonymousFilter:anonymousFilter]"/>
                </div>
            </form>
        </div>

        <div id="listContacts" data-ajaxUrlContacts="${g.createLink(mapping: 'politicianContactsSearch', absolute:true)}">
            %{--<g:render template="/contacts/listContacts" model="[contacts:contacts,searchContacts:searchContacts]"/>--}%
        </div>
    </div>

    <!-- Bulk actions modals -->
    <div id="bulk-action-delete-all-modal" class="modal fade in" tabindex="-1" role="dialog" aria-labelledby="contactDeleteTitle" aria-hidden="true">
        <div class="modal-dialog ">
            <div class="modal-content">
                <g:form method="POST" mapping="bulkActionRemoveContactsAjax" role="form" class="submitOrangeButton"
                        autocomplete="off" data-type="1">
                    <div class="modal-header"><h4><g:message code="tools.contact.bulkActions.deleteAll"/></h4></div>

                    <div class="modal-body">
                        <p>
                            <g:if test="${deleteAttachedUsers}">
                                <g:message code="modal.bulkAction.deleteAllUsers.explain" args="[contacts.total]"/>
                            </g:if>
                            <g:else>
                                <g:message code="modal.bulkAction.deleteAll.explain" args="[contacts.total]"/>
                            </g:else>
                        </p>
                    </div>

                    <div class="modal-footer">
                        <button class="btn" type="submit"><g:message
                                code="modal.bulkAction.deleteAll.deleteContacts"/></button>
                    </div>
                </g:form>
            </div>
        </div>
    </div>
    <div id="bulk-action-add-tags-modal" class="modal fade in" tabindex="-1" role="dialog" aria-labelledby="addTagsTitle" aria-hidden="true">
        <div class="modal-dialog ">
            <div class="modal-content">
                <g:form method="POST" mapping="bulkActionAddTagsContactsAjax" role="form" class="submitOrangeButton" autocomplete="off" data-type="2">
                    <div class="modal-header"><h4><g:message code="tools.contact.bulkActions.addTags"/></h4></div>
                    <div class="modal-body">
                        <p>
                            <g:message code="modal.bulkAction.addTags.explain" args="[contacts.total]"/>
                        </p>
                        <fieldset aria-live="polite" class="row">
                            <div class="form-group col-md-12">
                                <label for="addTagsField" class="sr-only"><g:message code="tools.contact.bulkActions.addTags"/> </label>
                                <input id="addTagsField" name="tags" class="tagsField" type="text" data-urlTags="${g.createLink(mapping:'politicianContactTagsAjax')}" />
                            </div>
                        </fieldset>
                    </div>
                    <div class="modal-footer">
                        <button class="btn" type="submit"><g:message code="modal.bulkAction.addTags.addToContacts"/></button>
                    </div>
                </g:form>
            </div>
        </div>
    </div>
    <div id="bulk-action-remove-tags-modal" class="modal fade in" tabindex="-1" role="dialog" aria-labelledby="removeTagsTitle" aria-hidden="true">
        <div class="modal-dialog ">
            <div class="modal-content">
                <g:form method="POST" mapping="bulkActionRemoveTagsContactsAjax" role="form" class="submitOrangeButton" autocomplete="off" data-type="3">
                    <div class="modal-header"><h4><g:message code="tools.contact.bulkActions.addTags"/></h4></div>
                    <div class="modal-body">
                        <p>
                            <g:message code="modal.bulkAction.removeTags.explain" args="[contacts.total]"/>
                        </p>
                        <fieldset aria-live="polite" class="row">
                            <div class="form-group col-md-12">
                                <label for="removeTagsField" class="sr-only"><g:message code="tools.contact.bulkActions.removeTags"/> </label>
                                <input id="removeTagsField" name="tags" class="tagsField" type="text" data-urlTags="${g.createLink(mapping:'politicianContactTagsAjax')}" />
                            </div>
                        </fieldset>
                    </div>
                    <div class="modal-footer">
                        <button class="btn" type="submit"><g:message code="modal.bulkAction.removeTags.removeFromContacts"/></button>
                    </div>
                </g:form>
            </div>
        </div>
    </div>

    <div id="bulk-action-generate-personal-code" class="modal fade in" tabindex="-1" role="dialog" aria-labelledby="generatePersonalCodeTitle" aria-hidden="true">
        <div class="modal-dialog ">
            <div class="modal-content">
                <g:form method="POST" mapping="bulkActionGeneratePersonalCodeAjax" role="form" class="submitOrangeButton" autocomplete="off" data-type="3">
                    <div class="modal-header"><h4><g:message code="tools.contact.bulkActions.generatePersonalCode"/></h4></div>
                    <div class="modal-body">
                        <p>
                            <g:message code="modal.bulkAction.generatePersonalCode.explain" args="[contacts.total]"/>
                        </p>
                    </div>
                    <div class="modal-footer">
                        <button class="btn" type="submit"><g:message code="modal.bulkAction.generatePersonalCode.submit"/></button>
                    </div>
                </g:form>
            </div>
        </div>
    </div>

    <div id="bulk-action-remove-personal-code" class="modal fade in" tabindex="-1" role="dialog" aria-labelledby="removePersonalCodeTitle" aria-hidden="true">
        <div class="modal-dialog ">
            <div class="modal-content">
                <g:form method="POST" mapping="bulkActionRemovePersonalCodeAjax" role="form" class="submitOrangeButton" autocomplete="off" data-type="3">
                    <div class="modal-header"><h4><g:message code="tools.contact.bulkActions.removePersonalCode"/></h4></div>
                    <div class="modal-body">
                        <p>
                            <g:message code="modal.bulkAction.removePersonalCode.explain" args="[contacts.total]"/>
                        </p>
                    </div>
                    <div class="modal-footer">
                        <button class="btn" type="submit"><g:message code="modal.bulkAction.removePersonalCode.submit"/></button>
                    </div>
                </g:form>
            </div>
        </div>
    </div>

    <div id="export-contacts-modal" class="modal fade in" tabindex="-1" role="dialog" aria-labelledby="exportTagsTitle" aria-hidden="true">
        <div class="modal-dialog ">
            <div class="modal-content">
                <div class="modal-header"><h4><g:message code="modal.exportedContacts.title"/></h4></div>
                <div class="modal-body">
                    <p>
                        <g:message code="modal.exportedContacts.explanation"/>
                        <g:message code="modal.exported.explanation"/>
                    </p>
                </div>
                <div class="modal-footer">
                    <a href="#" class="btn" data-dismiss="modal" aria-label="Close"><g:message code="modal.exportedContacts.close"/></a>
                </div>
            </div>
        </div>
    </div>

</content>
