<formUtil:validateForm bean="${command}" form="districtParticipatoryBudgetForm" dirtyControl="true"/>
<form action="#" class="form-horizontal campaign-form" id="districtParticipatoryBudgetForm" method="POST"
      data-generalErrorMessage="${g.message(code: 'kuorum.web.commands.payment.massMailing.DebateCommand.form.genericError')}">
    <input type="hidden" name="redirectLink" id="redirectLink"/>
    <input type="hidden" name="sendType"
           value="${status == org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.SENT ? 'SEND' : 'DRAFT'}"
           id="sendMassMailingType"/>
    <input type="hidden" name="campaignId" value="${command.campaignId}"/>


    <fieldset aria-live="polite" class="form-group">
        <div class="col-xs-3 col-sm-3 col-sm-offset-1">
            <formUtil:selectEnum command="${command}" field="participatoryBudgetType" extraClass="" showLabel="true"/>
        </div>

        <div class="col-xs-3 col-sm-3 ">
            <formUtil:input command="${command}" field="maxDistrictProposalsPerUser" type="number" showLabel="true"/>
        </div>

    </fieldset>
    <fieldset aria-live="polite" class="form-group fieldset-check-box">
        <label for="activeSupport" class="col-xs-12 col-sm-1 col-md-1 control-label">
            <span class="fas fa-info-circle" data-toggle="tooltip" data-placement="top"
                  title="${g.message(code: 'kuorum.web.commands.payment.participatoryBudget.DistrictsCommand.activeSupport.label.info')}"></span>
        </label>

        <div class="col-xs-12 col-sm-6 ">
            <formUtil:checkBox command="${command}" field="activeSupport" extraClass=""/>
        </div>
    </fieldset>
    <fieldset aria-live="polite" class="form-group fieldset-check-box">
        <label for="activeSupport" class="col-xs-12 col-sm-1 col-md-1 control-label">
            <span class="fas fa-info-circle" data-toggle="tooltip" data-placement="top"
                  title="${g.message(code: 'kuorum.web.commands.payment.participatoryBudget.DistrictsCommand.addProposalsWithValidation.label.info')}"></span>
        </label>

        <div class="col-xs-12 col-sm-6 ">
            <formUtil:checkBox command="${command}" field="addProposalsWithValidation" extraClass=""/>
        </div>
    </fieldset>
    <hr/>

    <div class="form-group hidden-xs">
        <div class="col-sm-offset-2 col-md-offset-1 col-sm-10">
            <div class="col-sm-offset-3 col-sm-2 participatory-budget-voting-container ${command.participatoryBudgetType}">
                <span class="participatory-budget-type-BUDGET"><g:message
                        code="kuorum.web.commands.payment.participatoryBudget.DistrictCommand.budget.label"/></span>
                <span class="participatory-budget-type-SIMPLE_VOTE"><g:message
                        code="kuorum.web.commands.payment.participatoryBudget.DistrictCommand.singleVote.label"/></span>
            </div>

            <div class="col-sm-2 participatory-budget-voting-container ${command.participatoryBudgetType}">
                <span class=""><g:message
                        code="kuorum.web.commands.payment.participatoryBudget.DistrictCommand.minVotesImplementProposals.label"/></span>
                <span class="info-disabled">
                    <span role="button" rel="popover" class="fas fa-info-circle" data-toggle="tooltip"
                          data-placement="top" title=""
                          data-original-title="${g.message(code: 'kuorum.web.commands.payment.participatoryBudget.DistrictCommand.minVotesImplementProposals.headTableInfo')}"></span>
                </span>
            </div>

            <div class="col-sm-2 participatory-budget-voting-container ${command.participatoryBudgetType}">
                <span class=""><g:message
                        code="kuorum.web.commands.payment.participatoryBudget.DistrictCommand.maxEligibleProposals.label"/></span>
                <span class="info-disabled">
                    <span role="button" rel="popover" class="fas fa-info-circle" data-toggle="tooltip"
                          data-placement="top" title=""
                          data-original-title="${g.message(code: 'kuorum.web.commands.payment.participatoryBudget.DistrictCommand.maxEligibleProposals.headTableInfo')}"></span>
                </span>
            </div>

            <div class="col-sm-2 participatory-budget-voting-container ${command.participatoryBudgetType}">
                <span class=""><g:message
                        code="kuorum.web.commands.payment.participatoryBudget.DistrictCommand.allCity.label"/></span>
                <span class="info-disabled">
                    <span role="button" rel="popover" class="fas fa-info-circle" data-toggle="tooltip"
                          data-placement="top" title=""
                          data-original-title="${g.message(code: 'kuorum.web.commands.payment.participatoryBudget.DistrictCommand.allCity.tooltip')}"></span>
                </span>
            </div>
        </div>
    </div>
    <fieldset aria-live="polite" class="dynamic-input-list">
        <label for="districs" class="col-sm-2 col-md-1 control-label"><g:message
                code="kuorum.web.commands.payment.participatoryBudget.DistrictsCommand.districts"/>:</label>

        <div class="col-sm-10">
            <formUtil:dynamicComplexInputs
                    command="${command}"
                    field="districts"
                    listClassName="kuorum.web.commands.payment.participatoryBudget.DistrictCommand"
                    customRemoveButton="true"
                    customAddButton="true"
                    appendLast="true"
                    formId="districtParticipatoryBudgetForm">
                <fieldset aria-live="polite" class="row">
                    <formUtil:input cssClass="hidden" field="districtId" command="${listCommand}"
                                    prefixFieldName="${prefixField}"/>

                    <div class="form-group district-data">
                        %{--<div class="col-xs-12 col-sm-8 col-md-7">--}%
                        <div class="col-xs-12 col-sm-3">
                            <formUtil:input field="name" command="${listCommand}" prefixFieldName="${prefixField}"
                                            showLabel="true" labelCssClass="visible-xs"/>
                        </div>

                        <div class="col-xs-12 col-sm-2 ">
                            <label for="districts[0].budget"
                                   class="visible-xs participatory-budget-voting-container ${command.participatoryBudgetType}">
                                <span class="participatory-budget-type-BUDGET"><g:message
                                        code="kuorum.web.commands.payment.participatoryBudget.DistrictCommand.budget.label"/></span>
                                <span class="participatory-budget-type-SIMPLE_VOTE"><g:message
                                        code="kuorum.web.commands.payment.participatoryBudget.DistrictCommand.singleVote.label"/></span>
                            </label>
                            <formUtil:input field="budget" command="${listCommand}" prefixFieldName="${prefixField}"
                                            showLabel="false"/>
                        </div>

                        <div class="col-xs-12 col-sm-2 ">
                            <label for="districts[0].minVotesImplementProposals"
                                   class="visible-xs participatory-budget-voting-container ${command.participatoryBudgetType}">
                                <span class=""><g:message
                                        code="kuorum.web.commands.payment.participatoryBudget.DistrictCommand.minVotesImplementProposals.label"/></span>
                            </label>
                            <formUtil:input field="minVotesImplementProposals" command="${listCommand}"
                                            prefixFieldName="${prefixField}"
                                            showLabel="false"/>
                        </div>

                        <div class="col-xs-12 col-sm-2 ">
                            <label for="districts[0].maxEligibleProposals"
                                   class="visible-xs participatory-budget-voting-container ${command.participatoryBudgetType}">
                                <span class=""><g:message
                                        code="kuorum.web.commands.payment.participatoryBudget.DistrictCommand.maxEligibleProposals.label"/></span>
                            </label>
                            <formUtil:input field="maxEligibleProposals" command="${listCommand}"
                                            prefixFieldName="${prefixField}"
                                            showLabel="false"/>
                        </div>

                        <div class="col-xs-12 col-sm-2 form-group-checkbox-inline no-label-lg">
                            <formUtil:checkBox field="allCity" command="${listCommand}"
                                               prefixFieldName="${prefixField}"
                                               labelCssClass="visible-xs"/>
                        </div>

                        <div class="col-xs-12 col-sm-1 form-group-remove no-label-lg">
                            <button type="button" class="btn btn-transparent btn-lg btn-icon removeButton"><i
                                    class="fal fa-trash"></i></button>
                        </div>
                        %{--</div>--}%
                    </div>
                </fieldset>
            </formUtil:dynamicComplexInputs>
            <fieldset aria-live="polite" class="row">
                <div class="form-group right">
                    <div class="col-md-12">
                        <button type="button" class="btn btn-lg btn-icon btn-transparent addButton">
                            <g:message
                                    code="kuorum.web.commands.payment.participatoryBudget.DistrictsCommand.addDistrict"/>
                            <i class="far fa-plus"></i>
                        </button>
                    </div>
                </div>
            </fieldset>
        </div>
    </fieldset>

    <g:render template="/campaigns/edit/stepButtons" model="[
            saveAndSentButtons: true,
            mappings          : mappings,
            status            : status,
            command           : command,
            numberRecipients  : numberRecipients]"/>
</form>