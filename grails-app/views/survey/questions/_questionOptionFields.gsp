<fieldset class="row question">
    <input type="hidden" name="${prefixField}options[${pos}].id" value="${option?.id}"/>
    <div class="col-xs-offset-1 col-xs-7">
        <input type="text" name="${prefixField}options[${pos}].text" value="${option?.text}" class="form-control input-lg">
    </div>
    <div class="col-xs-3">
        <formUtil:selectEnum field="questionOptionType" command="${option}" prefixFieldName="${prefixField}options[${pos}]." showLabel="false"/>
    </div>
    <div class="col-xs-1">
        <button type="button" class="btn btn-lg btn-icon btn-transparent removeQuestionButton"><i class="fal fa-trash"></i></button>
    </div>
</fieldset>