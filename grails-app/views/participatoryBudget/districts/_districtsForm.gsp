
<formUtil:validateForm bean="${command}" form="districtParticipatoryBudgetForm" dirtyControl="true"/>
<form action="#" class="form-horizontal campaign-form" id="districtParticipatoryBudgetForm" method="POST" data-generalErrorMessage="${g.message(code:'kuorum.web.commands.payment.massMailing.DebateCommand.form.genericError')}">
    <input type="hidden" name="redirectLink" id="redirectLink"/>
    <input type="hidden" name="sendType" value="${status== org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.SENT?'SEND':'DRAFT'}" id="sendMassMailingType"/>
    <input type="hidden" name="campaignId" value="${command.campaignId}"/>


    <div class="form-group hidden-xs">
        <div class="col-sm-offset-2 col-sm-8 col-md-offset-1 col-md-7">
            <div class="col-sm-offset-5 col-sm-5">
                <g:message code="kuorum.web.commands.payment.participatoryBudget.DistrictCommand.budget.label"/>
            </div>
        </div>
    </div>
    <fieldset class="dynamic-input-list">
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
                            <formUtil:input field="name" command="${listCommand}" prefixFieldName="${prefixField}" showLabel="true" labelCssClass="visible-xs"/>
                        </div>
                        <div class="col-xs-12 col-sm-2 ">
                            <formUtil:input field="budget" command="${listCommand}" prefixFieldName="${prefixField}" showLabel="true" labelCssClass="visible-xs"/>
                        </div>
                        <div class="col-xs-12 col-sm-4 form-group-checkbox-inline no-label-lg">
                            <formUtil:checkBox field="allCity" command="${listCommand}" prefixFieldName="${prefixField}"/>
                            <span class="fal fa-info-circle" data-toggle="tooltip" data-placement="top" title="${g.message(code:'kuorum.web.commands.payment.participatoryBudget.DistrictCommand.allCity.tooltip')}"></span>
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