<input type="hidden" name="politician.id" value="${command.politician.id}"/>
<fieldset class="row">
    <div class="form-group col-md-6">
        <formUtil:input command="${command.professionalDetails}" field="position" showLabel="true" prefixFieldName="professionalDetails."/>
    </div>
    <div class="form-group col-md-6">
        <formUtil:input command="${command.professionalDetails}" field="institution" showLabel="true" prefixFieldName="professionalDetails."/>
    </div>
    <div class="form-group col-md-6">
        <formUtil:input command="${command.professionalDetails}" field="politicalParty" showLabel="true" prefixFieldName="professionalDetails."/>
    </div>
</fieldset>

<fieldset class="row">
    <div class="form-group col-md-6">
        <formUtil:regionInput
                command="${command}"
                field="region"
                showLabel="true"
                label="${g.message(code:'kuorum.users.extendedPoliticianData.ProfessionalDetails.region.label')}"
        />
    </div>
    <div class="form-group col-md-6">
        <formUtil:regionInput
                command="${command}"
                field="constituency"
                showLabel="true"
                label="${g.message(code:'kuorum.users.extendedPoliticianData.ProfessionalDetails.constituency.label')}"
        />
    </div>
</fieldset>

<fieldset>
    <div class="form-group">
        <div class="col-xs-5 col-xs-offset-1">
            <button type="submit" class="btn btn-default"><g:message code="admin.createUser.submit"/></button>
        </div>
    </div>
</fieldset>
