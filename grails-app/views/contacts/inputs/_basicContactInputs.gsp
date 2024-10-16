<div class="row">
    <div class="form-group col-md-4">
        <formUtil:input command="${command}" field="name" showLabel="true"/>
    </div>
    <div class="form-group col-md-4">
        <formUtil:input command="${command}" field="surname" showLabel="true"/>
    </div>
    <div class="form-group col-md-4">
        <formUtil:input command="${command}" field="externalId" showLabel="true"
                        label="${kuorum.core.customDomain.CustomDomainResolver.getDomainRSDTO().externalIdName}"/>
    </div>
</div>
<div class="row">
    <div class="form-group col-md-4">
        <formUtil:input command="${command}" field="email" showLabel="true" disabled="${!contact.visibleEmail}"
                        readonly="${contact.isFollower}"/>
    </div>
    <div class="form-group form-group-phone col-md-4">
        <formUtil:selectPhonePrefix command="${command}" field="phonePrefix" showLabel="true" label="${g.message(code:'kuorum.web.commands.payment.contact.ContactCommand.phone.label')}"/>
        <formUtil:input command="${command}" field="phone" showLabel="true" type="number"/>
    </div>
    <div class="form-group form-group-inputButton col-md-4">
        <g:render template="/contacts/inputs/basicContactInput_personalCode" model="[contact:contact]"/>
    </div>
</div>
<div class="row">
    <div class="form-group col-md-4">
        <formUtil:date command="${command}" field="birthDate" showLabel="true"/>
    </div>
    <div class="form-group col-md-4">
        <formUtil:selectEnum command="${command}" field="gender" showLabel="true"/>
    </div>
    <div class="form-group col-md-4">
        <formUtil:selectEnum
                field="language"
                label="${g.message(code:'tools.contact.import.table.columnOption.language')}"
                command="${command}"
                enumClass="${org.kuorum.rest.model.contact.ContactLanguageRDTO.class}"/>
    </div>
</div>
<div class="row">
    <div class="form-group col-md-4">
        <formUtil:input command="${command}" field="surveyVoteWeight" showLabel="true" type="number"/>
    </div>
    <div class="col-md-offset-4 col-md-4">
        <input type="submit" value="${g.message(code:'default.save')}" class="btn btn-blue inverted">
    </div>
</div>