<input type="hidden" name="politician.id" value="${command.politician.id}"/>
<h4><g:message code="politician.quickNotes.data.background.title"/> </h4>
<fieldset class="row">
    <div class="form-group col-md-6">
        <formUtil:input command="${command.politicianExtraInfo}" field="completeName" showLabel="true" prefixFieldName="politicianExtraInfo."/>
    </div>
    <div class="form-group col-md-6">
        <formUtil:date command="${command.politicianExtraInfo}" field="birthDate" showLabel="true" prefixFieldName="politicianExtraInfo."/>
    </div>
    <div class="form-group col-md-6">
        <formUtil:input command="${command.politicianExtraInfo}" field="birthPlace" showLabel="true" prefixFieldName="politicianExtraInfo."/>
    </div>
    <div class="form-group col-md-6">
        <formUtil:url command="${command.politicianExtraInfo}" field="webSite" showLabel="true" prefixFieldName="politicianExtraInfo."/>
    </div>
    <div class="form-group col-md-6">
        <formUtil:input command="${command.politicianExtraInfo}" field="family" showLabel="true" prefixFieldName="politicianExtraInfo."/>
    </div>
</fieldset>
<h4><g:message code="politician.institutionalOffice.title"/></h4>
<fieldset class="row">
    <div class="form-group col-md-6">
        <formUtil:input command="${command.institutionalOffice}" field="address" showLabel="true" prefixFieldName="institutionalOffice."/>
    </div>
    <div class="form-group col-md-6">
        <formUtil:input command="${command.institutionalOffice}" field="telephone" showLabel="true" prefixFieldName="institutionalOffice."/>
    </div>
    <div class="form-group col-md-6">
        <formUtil:input command="${command.institutionalOffice}" field="mobile" showLabel="true" prefixFieldName="institutionalOffice."/>
    </div>
    <div class="form-group col-md-6">
        <formUtil:input command="${command.institutionalOffice}" field="fax" showLabel="true" prefixFieldName="institutionalOffice."/>
    </div>
    <div class="form-group col-md-6">
        <formUtil:input command="${command.institutionalOffice}" field="assistants" showLabel="true" prefixFieldName="institutionalOffice."/>
    </div>
</fieldset>
<h4><g:message code="politician.politicalOffice.title"/></h4>
<fieldset class="row">
    <div class="form-group col-md-6">
        <formUtil:input command="${command.politicalOffice}" field="address" showLabel="true" prefixFieldName="politicalOffice."/>
    </div>
    <div class="form-group col-md-6">
        <formUtil:input command="${command.politicalOffice}" field="telephone" showLabel="true" prefixFieldName="politicalOffice."/>
    </div>
    <div class="form-group col-md-6">
        <formUtil:input command="${command.politicalOffice}" field="mobile" showLabel="true" prefixFieldName="politicalOffice."/>
    </div>
    <div class="form-group col-md-6">
        <formUtil:input command="${command.politicalOffice}" field="fax" showLabel="true" prefixFieldName="politicalOffice."/>
    </div>
    <div class="form-group col-md-6">
        <formUtil:input command="${command.politicalOffice}" field="assistants" showLabel="true" prefixFieldName="politicalOffice."/>
    </div>
</fieldset>
<fieldset>
    <div class="form-group">
        <div class="col-xs-5 col-xs-offset-1">
            <button type="submit" class="btn btn-default"><g:message code="admin.createUser.submit"/></button>
        </div>
    </div>
</fieldset>
