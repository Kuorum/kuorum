<%@ page import="kuorum.web.commands.payment.contact.ContactFilterCommand" %>
<g:set var="formId" value="formFilter_${filter?.id?.toString().replace("-","_")}"/>
<g:set var="commandFilter" value="${new kuorum.web.commands.payment.contact.ContactFilterCommand(filter)}"/>
<div id="${formId}" class="disabled-filters hide">
    %{--<input type="hidden" name="filterId" value="${filter.id}"/>--}%
    <input type="hidden" name="filterEdited" value="false"/>
    <input type="hidden" name="filterName" value="${filter.name}"/>
    <fieldset class="form-group new-filter-options first">
        <label class="col-sm-2 col-md-1 control-label contact-filter-text"><g:message code="tools.contact.filter.form.matches"/></label>
        <div class="col-sm-4 col-md-3">
            <formUtil:selectEnum
                    field="operator"
                    command="${commandFilter}"
                    cssLabel="hide"/>
        </div>
        <div class="col-sm-5 contact-filter-text">
            <g:message code="tools.contact.filter.form.followingConditions"/>:
        </div>
    </fieldset>
    <formUtil:dynamicComplexInputs
            command="${commandFilter}"
            field="filterConditions"
            listClassName="kuorum.web.commands.payment.contact.ContactFilterOptionCommand"
            customRemoveButton="true"
            customAddButton="true"
            appendLast="true"
            formId="${formId}">
        <fieldset class="form-group new-filter-options">

            <div class="col-sm-2 col-sm-offset-2 col-md-2 col-md-offset-1">
                <a href="#" role="button" class="minus-condition"><span class="fal fa-minus-circle fa-lg"></span> <span class="text">Delete condition</span></a>
                <formUtil:selectEnum
                        field="field"
                        command="${listCommand}"
                        prefixFieldName="${prefixField}"
                        values="${org.kuorum.rest.model.contact.filter.condition.ConditionFieldTypeRDTO.values() - org.kuorum.rest.model.contact.filter.condition.ConditionFieldTypeRDTO.ID}"
                        cssLabel="sr-only"/>
            </div>
            <div class="filter-operator text-operator">
                <div class="col-sm-3">
                    <formUtil:selectEnum
                            field="operatorText"
                            command="${listCommand}"
                            prefixFieldName="${prefixField}"
                            enumClass="${org.kuorum.rest.model.contact.filter.condition.TextConditionOperatorTypeRDTO.class}"
                            cssLabel="sr-only"/>
                </div>
                <div class="col-sm-4">
                    <formUtil:input
                            field="value"
                            command="${listCommand}"
                            prefixFieldName="${prefixField}"
                            labelCssClass="sr-only"
                            showLabel="true"/>
                </div>
            </div>
            <div class="filter-operator status-operator hide">
                <div class="col-sm-3">
                    <formUtil:selectEnum
                        field="operatorNumber"
                        command="${listCommand}"
                        prefixFieldName="${prefixField}"
                        enumClass="${org.kuorum.rest.model.contact.filter.condition.NumberConditionOperatorTypeRDTO.class}"
                        cssLabel="sr-only"/>
                </div>
                <div class="col-sm-4">
                    <formUtil:selectEnum
                            field="value"
                            command="${listCommand}"
                            prefixFieldName="${prefixField}"
                            enumClass="${org.kuorum.rest.model.contact.ContactStatusRSDTO.class}"
                            cssLabel="sr-only"/>
                </div>
            </div>
            <div class="filter-operator boolean-operator hide">
                <div class="col-sm-3">
                    <formUtil:selectEnum
                            field="operatorBoolean"
                            command="${listCommand}"
                            prefixFieldName="${prefixField}"
                            enumClass="${org.kuorum.rest.model.contact.filter.condition.BooleanConditionOperatorTypeRDTO.class}"
                            cssLabel="sr-only"/>
                </div>
                <div class="col-sm-4">
                    <input type="text" class="hidden" value="NO_VALID" name="${prefixField}value"/>
                </div>
            </div>
            <div class="filter-operator contactType-operator hide">
                <div class="col-sm-3">
                    <formUtil:selectEnum
                            field="operatorContactType"
                            command="${listCommand}"
                            prefixFieldName="${prefixField}"
                            enumClass="${org.kuorum.rest.model.contact.filter.condition.ContactTypeConditionOperatorTypeRDTO.class}"
                            cssLabel="sr-only"/>
                </div>
                <div class="col-sm-4">
                    <input type="text" class="hidden" value="NO_VALID" name="${prefixField}value"/>
                </div>
            </div>
            <div class="filter-operator language-operator">
                <div class="col-sm-3">
                    <select class="form-control input-lg" disabled="disabled">
                        <option value="EQUALS" selected><g:message code="org.kuorum.rest.model.contact.filter.condition.TextConditionOperatorTypeRDTO.EQUALS"/></option>
                    </select>
                </div>
                <div class="col-sm-4">
                    <formUtil:selectEnum
                            field="value"
                            command="${listCommand}"
                            prefixFieldName="${prefixField}"
                            enumClass="${org.kuorum.rest.model.contact.ContactLanguageRDTO.class}"
                            cssLabel="sr-only"/>
                </div>
            </div>
            <div class="filter-operator assistant-event-operator">
                <div class="col-sm-3">
                    <formUtil:selectEnum
                            field="operatorAssistantEvent"
                            defaultEmpty="false"
                            command="${listCommand}"
                            prefixFieldName="${prefixField}"
                            enumClass="${org.kuorum.rest.model.contact.filter.condition.EventAssistantConditionOperatorTypeRDTO.class}"
                            cssLabel="sr-only"/>
                </div>
                <div class="col-sm-4">
                    <formUtil:selectEvent
                            field="value"
                            command="${listCommand}"
                            prefixFieldName="${prefixField}"
                            cssLabel="sr-only"/>
                </div>
            </div>
        </fieldset>
    </formUtil:dynamicComplexInputs>

    <fieldset class="form-group new-filter-options last">
        <div class="col-sm-2 col-sm-offset-2 col-md-2 col-md-offset-1">
            <a href="#" role="button" class="plus-condition addButton">
                <span class="fal fa-plus-circle fa-lg"></span>
                <span class="text"><g:message code="tools.contact.filter.form.add"/></span>
            </a>
        </div>
    </fieldset>
    <%-- miguel este conjunto de campos tiene un margen de 35 px que tendria que ser de 22 si es que se desea que coincida con el div box-ppal filterbox --%>
    <%-- miguel procedo a crear una clase para este fielfset en concreto  --%>
    <fieldset class="new-filter-actions">
        <g:link mapping="politicianContactsSearch" elementId="numberRecipients"><g:message code="tools.contact.filter.form.recipients" args="[filter.amountOfContacts==null?'-':filter.amountOfContacts]"/></g:link>
        <g:link mapping="politicianContactFilterRefresh"    data-callaBackFunction="campaignFilterRefresh" role="button" class="btn btn-blue inverted" elementId="refreshFilter"><span class="far fa-sync-alt"></span> <g:message code="tools.contact.filter.form.refresh"/></g:link>
        <g:link mapping="politicianContactFilterUpdate"     data-callaBackFunction="campaignFilterSave" role="button" class="btn btn-blue inverted ${filter?.id==-2?'disabled':''}" elementId="saveFilter"><g:message code="default.save"/></g:link>
        <a href="#"        role="button" class="btn btn-blue inverted" id="saveFilterAsBtnOpenModal"><g:message code="tools.contact.filter.form.saveAs"/></a>
        <div id="saveFilterAsPopUp">
            <label class="sr-only" for="newFilterName">Write a name</label>
            <input class="form-control" type="text" name="newFilterName" id="newFilterName">
            <g:link mapping="politicianContactFilterNew" data-callaBackFunction="campaignFilterSaveAs" type="submit" class="btn btn-blue inverted" elementId="saveFilterAs"><g:message code="default.save"/></g:link>
            <a href="#" id="saveFilterAsBtnCancel"><g:message code="tools.contact.filter.form.save.cancel"/></a>
        </div>
        <g:link mapping="politicianContactFilterDelete" params="[filterId:filter.id]" data-callaBackFunction="" role="button" class="btn btn-transparent ${filter?.id<=0 ?'disabled':''}" elementId="deleteFilter" title="${g.message(code:'tools.contact.filter.form.delete')}"><span class="fal fa-trash"></span></g:link>
    </fieldset>
</div>