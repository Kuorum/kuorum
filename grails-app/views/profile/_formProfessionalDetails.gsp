<r:require modules="forms"/>
<input type="hidden" name="politician.id" value="${command.politician.id}"/>
<userUtil:isPolitician user="${command.politician}">
    <div class="box-ppal-section">
        <h4 class="box-ppal-section-title"><g:message code="kuorum.users.extendedPoliticianData.ProfessionalDetails.title"/> </h4>
        <fieldset class="row">
            <div class="form-group col-md-6">
                <formUtil:input command="${command}" field="institution" showLabel="true"/>
            </div>
        </fieldset>

        <fieldset class="row">
            <div class="form-group col-md-6">
                <formUtil:regionInput
                        command="${command}"
                        field="region"
                        showLabel="true"
                        label="${g.message(code:'kuorum.users.extendedPoliticianData.ProfessionalDetails.region.label')}"
                        helpBlock="${g.message(code:'kuorum.users.extendedPoliticianData.ProfessionalDetails.region.helpBlock')}"
                />
            </div>
            <div class="form-group col-md-6">
                <formUtil:regionInput
                        command="${command}"
                        field="constituency"
                        showLabel="true"
                        label="${g.message(code:'kuorum.users.extendedPoliticianData.ProfessionalDetails.constituency.label')}"
                        helpBlock="${g.message(code:'kuorum.users.extendedPoliticianData.ProfessionalDetails.constituency.helpBlock')}"
                />
            </div>
        </fieldset>
    </div>
</userUtil:isPolitician>
<div class="box-ppal-section">
    <h4 class="box-ppal-section-title"><g:message code="kuorum.users.extendedPoliticianData.CareerDetails.title"/></h4>
    <fieldset class="row">
        <div class="form-group col-md-6">
            <formUtil:input command="${command.careerDetails}" field="profession" showLabel="true" prefixFieldName="careerDetails."/>
        </div>
    </fieldset>
    <fieldset class="row">
        <div class="form-group col-md-6">
            <formUtil:input command="${command.careerDetails}" field="studies" showLabel="true" prefixFieldName="careerDetails."/>
        </div>
    </fieldset>
    <fieldset class="row">
        <div class="form-group col-md-6">
            <formUtil:input command="${command.careerDetails}" field="university" showLabel="true" prefixFieldName="careerDetails."/>
        </div>
    </fieldset>
    <fieldset class="row">
        <div class="form-group col-md-6">
            <formUtil:input command="${command.careerDetails}" field="school" showLabel="true" prefixFieldName="careerDetails."/>
        </div>
    </fieldset>
    <fieldset class="row">
        <div class="form-group col-md-6">
            <formUtil:editPdf command="${command.careerDetails}" field="cvLink" showLabel="true" prefixFieldName="careerDetails."/>
        </div>
        <div class="form-group col-md-6">
            <formUtil:editPdf command="${command.careerDetails}" field="declarationLink" showLabel="true" prefixFieldName="careerDetails."/>
        </div>
    </fieldset>
</div>
<div class="box-ppal-section">
    <fieldset>
        <div class="form-group text-center">
            <button type="submit" class="btn btn-default btn-lg"><g:message code="admin.createUser.submit"/></button>
        </div>
    </fieldset>
</div>