
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

        <fieldset class="form-group">
            <label for="title" class="col-sm-2 col-md-1 control-label"><g:message code="kuorum.web.commands.payment.massMailing.DebateCommand.title.label"/>:</label>
            <div class="col-sm-8 col-md-7">
                <select class="form-control input-lg" name="districtId">
                    <g:each in="${participatoryBudget.districts}" var="district">
                        <option value="${district.id}" ${district.id==command.districtId?'selected':''}>${district.name}</option>
                    </g:each>
                </select>
            </div>
        </fieldset>
        <fieldset class="form-group">
            <label for="title" class="col-sm-2 col-md-1 control-label"><g:message code="kuorum.web.commands.payment.massMailing.DebateCommand.title.label"/>:</label>
            <div class="col-sm-8 col-md-7">
                <select class="form-control input-lg" name="cause">
                    <g:each in="${participatoryBudget.causes}" var="cause">
                        <option value="${cause}" ${cause==command.cause?'selected':''}>${cause}</option>
                    </g:each>
                </select>
            </div>
        </fieldset>


    <g:render template="/campaigns/edit/stepButtons" model="[mappings:mappings, status:status, command: command, numberRecipients:numberRecipients]"/>
    </form>

</div>