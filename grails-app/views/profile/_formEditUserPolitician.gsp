<fieldset class="row">
    <div class="form-group col-md-6">
        <formUtil:input command="${command}" field="position" showLabel="true"/>
    </div>
    <div class="form-group col-md-6">
        <formUtil:radioEnum command="${command}" field="gender"  deleteOptions="${[kuorum.core.model.Gender.ORGANIZATION]}"/>
    </div>
</fieldset>
<fieldset class="row">
    <div class="form-group col-md-6">
        <formUtil:input command="${command}" field="politicalParty" showLabel="true"/>
    </div>
    <div class="form-group col-md-6">
        <formUtil:input command="${command}" field="politicalLeaningIndex" showLabel="true"/>
    </div>
</fieldset>
