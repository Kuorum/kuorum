<%@ page import="kuorum.web.commands.payment.contact.ContactFilterCommand" %>
<r:require modules="forms"/>
<g:set var="formId" value="formFilter_${filter?.id?.toString().replace("-","_")}"/>
<g:set var="commandFilter" value="${new kuorum.web.commands.payment.contact.ContactFilterCommand(filter)}"/>
<div id="${formId}" class="disabled-filters hide">
    <input type="hidden" name="filterId" value="${filter.id}"/>
    <input type="hidden" name="filterName" value="${filter.name}"/>
    <fieldset class="form-group new-filter-options first">
        <label for="matches" class="col-sm-2 col-md-1 control-label"><g:message code="tools.contact.filter.form.matches"/></label>
        <div class="col-sm-4 col-md-3">
            <formUtil:selectEnum
                    field="operator"
                    command="${commandFilter}"
                    cssLabel="hide"/>
        </div>
        <div class="col-sm-5">
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
        <a href="#" id="numberRecipients"><g:message code="tools.contact.filter.form.recipients" args="[filter.amountOfContacts?:'-']"/></a>
        <a href="#" role="button" class="btn btn-blue inverted" id="refreshFilter"><g:message code="tools.contact.filter.form.refresh"/></a>
        <g:link mapping="politicianContactFilterUpdate" role="button" class="btn btn-blue inverted" elementId="saveFilter"><g:message code="tools.contact.filter.form.save"/></g:link>
        <a href="#" role="button" class="btn btn-blue inverted" id="saveFilterAs"><g:message code="tools.contact.filter.form.saveAs"/> </a>
    </fieldset>
</div>