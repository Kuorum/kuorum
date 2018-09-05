<fieldset class="row question">
    <input type="hidden" name="${prefixField}options[${pos}].id" value="${option?.id}"/>
    <div class="col-xs-offset-1 col-xs-10">
        <input type="text" name="${prefixField}options[${pos}].text" value="${option?.text}" class="form-control input-lg">
    </div>
    <div class="form-group col-xs-1">
        <button type="button" class="btn btn-lg btn-icon btn-transparent removeQuestionButton"><i class="fal fa-trash"></i></button>
    </div>
    <div class="col-xs-offset-1 col-xs-2">
        <button type="button" class="btn btn-lg btn-icon btn-transparent addQuestionOptionButton">
            <g:message code="survey.form.button.addOption"/>
            <i class="far fa-plus"></i>
        </button>
    </div>
</fieldset>