<input type="hidden" name="politician.id" value="${command.politician.id}"/>
<div class="box-ppal-section">
    <h4 class="box-ppal-section-title"><g:message code="politician.quickNotes.data.background.title"/> </h4>
    <fieldset class="row">
        <div class="form-group col-md-6">
            <formUtil:input command="${command.politicianExtraInfo}" field="completeName" showLabel="true" prefixFieldName="politicianExtraInfo."/>
        </div>
    </fieldset>
    <fieldset class="row">
        <div class="form-group col-md-6">
            <formUtil:input command="${command.politicianExtraInfo}" field="birthPlace" showLabel="true" prefixFieldName="politicianExtraInfo."/>
        </div>
        <div class="form-group col-md-6">
            <formUtil:date command="${command.politicianExtraInfo}" field="birthDate" showLabel="true" prefixFieldName="politicianExtraInfo."/>
        </div>
        <div class="form-group col-md-12">
            <formUtil:input command="${command.politicianExtraInfo}" field="family" showLabel="true" prefixFieldName="politicianExtraInfo."/>
        </div>
    </fieldset>
</div>
<div class="box-ppal-section">
    <h4 class="box-ppal-section-title"><g:message code="politician.institutionalOffice.title"/></h4>
    <fieldset class="row">
        <div class="form-group col-md-6">
            <formUtil:input command="${command.institutionalOffice}" field="assistants" showLabel="true" prefixFieldName="institutionalOffice."/>
        </div>
    </fieldset>
    <fieldset class="row">
        <div class="form-group col-md-6">
            <formUtil:input command="${command.institutionalOffice}" field="address" showLabel="true" prefixFieldName="institutionalOffice."/>
        </div>
        <div class="form-group col-md-6">
            <formUtil:input command="${command.institutionalOffice}" field="mobile" showLabel="true" prefixFieldName="institutionalOffice."/>
        </div>
        <div class="form-group col-md-6">
            <formUtil:input command="${command.institutionalOffice}" field="telephone" showLabel="true" prefixFieldName="institutionalOffice."/>
        </div>
        <div class="form-group col-md-6">
            <formUtil:input command="${command.institutionalOffice}" field="fax" showLabel="true" prefixFieldName="institutionalOffice."/>
        </div>
    </fieldset>
</div>
<div class="box-ppal-section">
    <h4 class="box-ppal-section-title"><g:message code="politician.politicalOffice.title"/></h4>
    <fieldset class="row">
        <div class="form-group col-md-6">
            <formUtil:input command="${command.politicalOffice}" field="assistants" showLabel="true" prefixFieldName="politicalOffice."/>
        </div>
    </fieldset>
    <fieldset class="row">
        <div class="form-group col-md-6">
            <formUtil:input command="${command.politicalOffice}" field="address" showLabel="true" prefixFieldName="politicalOffice."/>
        </div>
        <div class="form-group col-md-6">
            <formUtil:input command="${command.politicalOffice}" field="mobile" showLabel="true" prefixFieldName="politicalOffice."/>
        </div>
        <div class="form-group col-md-6">
            <formUtil:input command="${command.politicalOffice}" field="telephone" showLabel="true" prefixFieldName="politicalOffice."/>
        </div>
        <div class="form-group col-md-6">
            <formUtil:input command="${command.politicalOffice}" field="fax" showLabel="true" prefixFieldName="politicalOffice."/>
        </div>
    </fieldset>
</div>
<fieldset>
    <div class="form-group text-center">
        <button type="submit" class="btn btn-default"><g:message code="admin.createUser.submit"/></button>
    </div>
</fieldset>
