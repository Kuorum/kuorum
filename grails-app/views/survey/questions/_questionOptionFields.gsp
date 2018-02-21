<fieldset class="row question">
    <input type="hidden" name="${prefixField}options[${pos}].id" value="${option?.id}"/>
    <div class="col-md-offset-1 col-md-9">
        <input type="text" name="${prefixField}options[${pos}].text" value="${option?.text}" class="form-control input-lg">
    </div>
    <div class="form-group col-md-2">
        <button type="button" class="btn btn-lg btn-icon btn-transparent removeQuestionButton"><i class="fa fa-trash"></i></button>
    </div>
    <div class="col-md-offset-1 col-md-2">
        <button type="button" class="btn btn-lg btn-icon btn-transparent addQuestionOptionButton">
            <g:message code="survey.form.button.addOption"/>
            <i class="fa fa-plus"></i>
        </button>
    </div>
</fieldset>