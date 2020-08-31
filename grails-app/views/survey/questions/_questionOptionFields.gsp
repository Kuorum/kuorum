<fieldset class="row question">
    <input type="hidden" name="${prefixField}options[${pos}].id" value="${option?.id}"/>
    <div class="col-xs-offset-1 col-xs-11 col-sm-offset-1 col-sm-4">
        <formUtil:input field="text" command="${option}" prefixFieldName="${prefixField}options[${pos}]."  showLabel="true" labelCssClass="visible-xs"/>
    </div>
    <div class="col-xs-offset-1 col-xs-11 col-sm-offset-0 col-sm-3">
        <formUtil:selectEnum field="questionOptionType" command="${option}" prefixFieldName="${prefixField}options[${pos}]." showLabel="true" labelCssClass="visible-xs"/>
    </div>
    <div class="col-xs-offset-1 col-xs-11 col-sm-offset-0 col-sm-3 question-logic">
        <formUtil:selectQuestion field="nextQuestionId" command="${option}" prefixFieldName="${prefixField}options[${pos}]." showLabel="true" labelCssClass="visible-xs" survey="${survey}"/>
    </div>
    <div class="col-xs-12 col-sm-offset-0 col-sm-1 center">
        <button type="button" class="btn btn-lg btn-icon btn-transparent removeQuestionButton"><span class="visible-xs"><g:message code="dynamicInput.modal.remove.row.delete"/></span><i class="fal fa-trash"></i></button>
    </div>
</fieldset>