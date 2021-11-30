<fieldset class="row question">
    <input type="hidden" name="${prefixField}options[${pos}].id" value="${option?.id}"/>
    <div class="col-xs-offset-1 col-xs-11 col-sm-offset-1 col-sm-4">
        <formUtil:input field="text" command="${option}" prefixFieldName="${prefixField}options[${pos}]."  showLabel="true" labelCssClass="visible-xs"/>
    </div>
    <div class="col-xs-offset-1 col-xs-11 col-sm-offset-0 col-sm-1 question-option-image-popup">
        <formUtil:imageCropperJs field="urlImage" command="${option}" prefixFieldName="${prefixField}options[${pos}]." fileGroup="${kuorum.core.FileGroup.USER_AVATAR}" showLabel="true" labelCssClass="visible-xs" label="${g.message(code:'kuorum.core.FileType.IMAGE')}"/>
    </div>
    <div class="col-xs-offset-1 col-xs-11 col-sm-offset-0 col-sm-2">
        <formUtil:selectEnum field="questionOptionType" command="${option}" prefixFieldName="${prefixField}options[${pos}]." showLabel="true" labelCssClass="visible-xs"/>
    </div>
    <div class="col-xs-offset-1 col-xs-11 col-sm-offset-0 col-sm-3 question-logic">
        <formUtil:selectQuestion field="nextQuestionId" command="${option}" prefixFieldName="${prefixField}options[${pos}]." showLabel="true" labelCssClass="visible-xs" survey="${survey}"/>
    </div>
    <div class="col-xs-12 col-sm-offset-0 col-sm-1 center">
        <button type="button" class="btn btn-lg btn-icon btn-transparent removeQuestionButton"><span class="visible-xs"><g:message code="dynamicInput.modal.remove.row.delete"/></span><i class="far fa-trash"></i></button>
    </div>
</fieldset>