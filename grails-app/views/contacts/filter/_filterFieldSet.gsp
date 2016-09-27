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
                <a href="#" role="button" class="minus-condition"><span class="fa fa-minus-circle fa-lg"></span> <span class="text">Delete condition</span></a>
                <formUtil:selectEnum
                        field="field"
                        command="${listCommand}"
                        prefixFieldName="${prefixField}"
                        cssLabel="sr-only"/>
            </div>
            <div class="col-sm-3">
                %{--<label for="condition1How" class="sr-only">Choose...</label>--}%
                <formUtil:selectEnum
                        field="operator"
                        command="${listCommand}"
                        prefixFieldName="${prefixField}"
                        cssLabel="sr-only"/>
                %{--<select name="operator" class="form-control input-lg" id="condition1How">--}%
                    %{--<option value="">Contains</option>--}%
                    %{--<option value="">Opción 2</option>--}%
                %{--</select>--}%
            </div>
            <div class="col-sm-4">
                %{--<input class="form-control" type="text" id="condition1Text" name="value">--}%
                <formUtil:input
                        field="value"
                        command="${listCommand}"
                        prefixFieldName="${prefixField}"
                        labelCssClass="sr-only"
                        showLabel="true"/>
            </div>
        </fieldset>
    </formUtil:dynamicComplexInputs>

    <fieldset class="form-group new-filter-options last">
        <div class="col-sm-2 col-sm-offset-2 col-md-2 col-md-offset-1">
            <a href="#" role="button" class="plus-condition addButton">
                <span class="fa fa-plus-circle fa-lg"></span>
                <span class="text"><g:message code="tools.contact.filter.form.add"/></span>
            </a>
        </div>
    </fieldset>
    <fieldset class="new-filter-actions">
        <g:link mapping="politicianContactsSearch" elementId="numberRecipients"><g:message code="tools.contact.filter.form.recipients" args="[filter.amountOfContacts==null?'-':filter.amountOfContacts]"/></g:link>
        <g:link mapping="politicianContactFilterRefresh"    data-callaBackFunction="campaignFilterRefresh" role="button" class="btn btn-blue inverted" elementId="refreshFilter"><span class="fa fa-refresh"></span> <g:message code="tools.contact.filter.form.refresh"/></g:link>
        <g:link mapping="politicianContactFilterUpdate"     data-callaBackFunction="campaignFilterSave" role="button" class="btn btn-blue inverted ${filter?.id==-2?'disabled':''}" elementId="saveFilter"><g:message code="tools.contact.filter.form.save"/></g:link>
        <a href="#"        role="button" class="btn btn-blue inverted" id="saveFilterAsBtnOpenModal"><g:message code="tools.contact.filter.form.saveAs"/></a>
        <div id="saveFilterAsPopUp">
            <label class="sr-only" for="newFilterName">Write a name</label>
            <input class="form-control" type="text" name="newFilterName" id="newFilterName">
            <a href="#" id="saveFilterAsBtnCancel">Cancel</a>
            <g:link mapping="politicianContactFilterNew" data-callaBackFunction="campaignFilterSaveAs" type="submit" class="btn btn-blue inverted" elementId="saveFilterAs">Save</g:link>
        </div>
    </fieldset>
</div>