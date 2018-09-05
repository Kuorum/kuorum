
<formUtil:validateForm bean="${command}" form="districtParticipatoryBudgetForm" dirtyControl="true"/>
<form action="#" class="form-horizontal campaign-form" id="districtParticipatoryBudgetForm" method="POST" data-generalErrorMessage="${g.message(code:'kuorum.web.commands.payment.massMailing.DebateCommand.form.genericError')}">
    <input type="hidden" name="redirectLink" id="redirectLink"/>
    <input type="hidden" name="sendType" value="${status== org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.SENT?'SEND':'DRAFT'}" id="sendMassMailingType"/>
    <input type="hidden" name="campaignId" value="${command.campaignId}"/>
    <fieldset class="form-group">
        <label for="deadLineProposals" class="col-sm-2 col-md-1 control-label"><g:message code="kuorum.web.commands.payment.participatoryBudget.DistrictsCommand.deadLineProposals.label"/>:</label>
        <div class="col-sm-4">
            <formUtil:date command="${command}" field="deadLineProposals"/>
        </div>
    </fieldset>
    <fieldset class="form-group">
        <label for="deadLineTechnicalReview" class="col-sm-2 col-md-1 control-label"><g:message code="kuorum.web.commands.payment.participatoryBudget.DistrictsCommand.deadLineTechnicalReview.label"/>:</label>
        <div class="col-sm-4">
            <formUtil:date command="${command}" field="deadLineTechnicalReview"/>
        </div>
    </fieldset>
    <fieldset class="form-group">
        <label for="deadLineVotes" class="col-sm-2 col-md-1 control-label"><g:message code="kuorum.web.commands.payment.participatoryBudget.DistrictsCommand.deadLineVotes.label"/>:</label>
        <div class="col-sm-4">
            <formUtil:date command="${command}" field="deadLineVotes"/>
        </div>
    </fieldset>
    <fieldset class="form-group">
        <label for="deadLineResults" class="col-sm-2 col-md-1 control-label"><g:message code="kuorum.web.commands.payment.participatoryBudget.DistrictsCommand.deadLineResults.label"/>:</label>
        <div class="col-sm-4">
            <formUtil:date command="${command}" field="deadLineResults"/>
        </div>
    </fieldset>

    <hr class="districts-form-margin"/>
    <fieldset class="form-group dynamic-input-list">
        <label for="districs" class="col-sm-2 col-md-1 control-label"><g:message code="kuorum.web.commands.payment.participatoryBudget.DistrictsCommand.districts"/>:</label>
        <div class="col-sm-8 col-md-7">
            <formUtil:dynamicComplexInputs
                    command="${command}"
                    field="districts"
                    listClassName="kuorum.web.commands.payment.participatoryBudget.DistrictCommand"
                    customRemoveButton="true"
                    customAddButton="true"
                    appendLast="true"
                    formId="districtParticipatoryBudgetForm">
                <fieldset class="row">
                    <formUtil:input cssClass="hidden" field="districtId" command="${listCommand}" prefixFieldName="${prefixField}"/>
                    <div class="form-group district-data">
                        %{--<div class="col-xs-12 col-sm-8 col-md-7">--}%
                        <div class="col-xs-12 col-sm-5">
                            <formUtil:input field="name" command="${listCommand}" prefixFieldName="${prefixField}" />
                        </div>
                        <div class="col-xs-12 col-sm-2 ">
                            <formUtil:input field="budget" command="${listCommand}" prefixFieldName="${prefixField}"/>
                        </div>
                        <div class="col-xs-12 col-sm-4 form-group-checkbox-inline no-label-lg">
                            <formUtil:checkBox field="allCity" command="${listCommand}" prefixFieldName="${prefixField}"/>
                        </div>
                        <div class="col-xs-12 col-sm-1 form-group-remove no-label-lg">
                            <button type="button" class="btn btn-transparent btn-lg btn-icon removeButton"><i class="fal fa-trash"></i></button>
                        </div>
                        %{--</div>--}%
                    </div>
                </fieldset>
            </formUtil:dynamicComplexInputs>
            <fieldset class="row">
                <div class="form-group right">
                    <div class="col-md-12">
                        <button type="button" class="btn btn-lg btn-icon btn-transparent addButton">
                            <g:message code="kuorum.web.commands.payment.participatoryBudget.DistrictsCommand.addDistrict"/>
                            <i class="far fa-plus"></i>
                        </button>
                    </div>
                </div>
            </fieldset>
        </div>
    </fieldset>

    <g:render template="/campaigns/edit/stepButtons" model="[
            saveAndSentButtons:true,
            mappings:mappings,
            status:status,
            command: command,
            numberRecipients:numberRecipients]"/>
</form>