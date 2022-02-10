
<r:require modules="datepicker, campaignForm" />


<div class="box-steps container-fluid campaign-steps">
    <g:render template="/campaigns/steps/campaignSteps" model="[mappings: mappings]"/>
</div>

<div class="box-ppal campaign-new">
    <h1 class="sr-only"><g:message code="admin.createDebate.title"/></h1>

    <formUtil:validateForm bean="${command}" form="districtProposalChooseDistrict"  dirtyControl="true"/>
    <form action="#" class="form-horizontal campaign-form" id="districtProposalChooseDistrict" method="POST" data-generalErrorMessage="${g.message(code:'kuorum.web.commands.payment.massMailing.DebateCommand.form.genericError')}">
        <input type="hidden" name="sendType" value="DRAFT" id="sendMassMailingType"/>
        <input type="hidden" name="redirectLink" id="redirectLink"/>

        <g:if test="${!campaign}">
            <fieldset class="form-group">
                <label for="name" class="col-sm-2 col-md-1 control-label"><g:message code="kuorum.web.commands.payment.massMailing.DebateCommand.title.label"/>:</label>
                <div class="col-sm-8 col-md-7">
                    <formUtil:input command="${command}" field="name"/>
                </div>
            </fieldset>
        </g:if>
        <g:else>
            <input type="hidden" name="name" value="${campaign.title}" />
        </g:else>
        <fieldset class="form-group">
            <label for="districtId" class="col-sm-2 col-md-1 control-label"><g:message code="kuorum.web.commands.payment.participatoryBudget.DistrictProposalChooseDistrictCommand.district.label"/>:</label>
            <div class="col-sm-8 col-md-7">
                <select class="form-control input-lg" name="districtId" data-original-value="${command.districtId}">
                    <option value="">----</option>
                    <g:each in="${participatoryBudget.districts}" var="district">
                        <option value="${district.id}" ${district.id==command.districtId?'selected':''}>${district.name}</option>
                    </g:each>
                </select>
            </div>
        </fieldset>
        <fieldset class="form-group">
            <label for="cause" class="col-sm-2 col-md-1 control-label"><g:message code="kuorum.web.commands.payment.participatoryBudget.DistrictProposalChooseDistrictCommand.cause.label"/>:</label>
            <div class="col-sm-8 col-md-7">
                <select class="form-control input-lg" name="cause">
                    <option value="">---</option>
                    <g:each in="${participatoryBudget.causes}" var="cause">
                        <option value="${cause}" ${cause==command.cause?'selected':''}>${cause}</option>
                    </g:each>
                </select>
            </div>
        </fieldset>
        <fieldset class="form-group">
            <label for="type" class="col-sm-2 col-md-1 control-label"><g:message code="kuorum.web.commands.payment.participatoryBudget.DistrictProposalChooseDistrictCommand.type.label"/>:</label>
            <div class="col-sm-8 col-md-7">
               <select class="form-control input-lg" name="type" >
                   <option value="">---</option>
                    <g:each in="${org.kuorum.rest.model.communication.participatoryBudget.ProposalEntityType}" var="type">
                        <option value="${type}" ${type==command.type?'selected':''}>${type}</option>
                    </g:each>
                </select>
            </div>
        </fieldset>


    <g:render template="/campaigns/edit/stepButtons" model="[mappings:mappings, status:status, command: command, numberRecipients:numberRecipients]"/>
    </form>

    <div class="modal fade in" id="changeDistrictWarn" tabindex="-1" role="dialog" aria-labelledby="changeDistrictWarnTitle" aria-hidden="true">
        <div class="modal-dialog ">
            <div class="modal-title"><g:message code="kuorum.web.commands.payment.participatoryBudget.DistrictProposalChooseDistrictCommand.districtId.modal.title"/></div>
            <div class="modal-content">
                <div class="modal-header"><h4><g:message code="kuorum.web.commands.payment.participatoryBudget.DistrictProposalChooseDistrictCommand.districtId.modal.title"/></h4></div>
                <div class="modal-body">
                    <p>
                        <g:message code="kuorum.web.commands.payment.participatoryBudget.DistrictProposalChooseDistrictCommand.districtId.modal.text"/>
                    </p>
                </div>
                <div class="modal-footer">
                    <a href="#" class="btn btn-grey" data-dismiss="modal" aria-label="Close" id="changeDistrictWarnRevertStatus"><g:message code="kuorum.web.commands.payment.participatoryBudget.DistrictProposalChooseDistrictCommand.districtId.modal.button.cancel"/></a>
                    <a href="#" class="btn btn-blue" data-dismiss="modal" aria-label="Close"><g:message code="kuorum.web.commands.payment.participatoryBudget.DistrictProposalChooseDistrictCommand.districtId.modal.button.ok"/></a>
                </div>
            </div>
        </div>
    </div>

</div>