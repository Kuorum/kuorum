<r:require modules="datepicker" />
<h1 class="sr-only">Newsletter</h1>
<formUtil:validateForm bean="${command}" form="politicianMassMailingForm" dirtyControl="true"/>
<form action="#" class="form-horizontal" id="politicianMassMailingForm" method="POST" data-generalErrorMessage="${g.message(code:'kuorum.web.commands.payment.massMailing.MassMailingCommand.form.genericError')}">
    <input type="hidden" name="sendType" value="DRAFT" id="sendMassMailingType"/>
    <input type="hidden" name="campaignId" value="${campaignId?:''}"/>
    <fieldset class="form-group" id="toFilters">
        <label for="to" class="col-sm-2 col-md-1 control-label"><g:message code="tools.massMailing.fields.filter.to"/> :</label>
        <div class="col-sm-4 col-md-3">
            <select name="filterId" class="form-control input-lg" id="recipients">
                %{--<g:if test="${totalContacts}">--}%
                    <option value="0" data-amountContacts="${totalContacts?:0}"><g:message code="tools.massMailing.fields.filter.to.all"/></option>
                %{--</g:if>--}%
                <g:each in="${filters}" var="filter">
                    <option value="${filter.id}" ${command.filterId == filter.id?'selected':''} data-amountContacts="${filter.amountOfContacts}">${filter.name}</option>
                </g:each>
                <g:if test="${anonymousFilter}">
                    <option value="${anonymousFilter.id}" ${command.filterId == anonymousFilter.id?'selected':''} data-amountContacts="${anonymousFilter.amountOfContacts}" data-anononymus="true">${anonymousFilter.name}</option>
                </g:if>
                %{--<g:if test="${totalContacts}">--}%
                    <option value="-2" data-amountContacts="-"><g:message code="tools.massMailing.fields.filter.to.createNew"/></option>
                %{--</g:if>--}%
            </select>
        </div>
        <div class="col-sm-4">
            <g:link mapping="politicianContactFilterData" role="button" elementId="filterContacts" title="${g.message(code:'tools.contact.filter.conditions.open')}">
                <span class="fa fa-filter fa-lg"></span>
                <span class="sr-only"><g:message code="tools.massMailing.fields.filter.button"/></span>
            </g:link>
            <g:link mapping="politicianContactsSearch" elementId="infoToContacts">
                <span class="amountRecipients"></span>
                <g:message code="tools.massMailing.fields.filter.recipients"/>
                %{--<span class="fa fa-filter fa-lg"></span>--}%
            </g:link>
            <g:link mapping="politicianMassMailingSendTest" absolute="true" class="btn ${hightLigthTestButtons?'btn-blue':'btn-grey'} pull-right" elementId="sendTest" title="${g.message(code:'tools.massMailing.sendTest')}">
                <span class="fa fa-envelope"></span>
            </g:link>
        </div>
    </fieldset>
    <div id="newFilterContainer">
        <g:render template="/contacts/filter/listFilterFieldSet" model="[filters:filters,anonymousFilter:anonymousFilter]"/>
    </div>
    <fieldset class="form-group">
        <label for="subject" class="col-sm-2 col-md-1 control-label"><g:message code="kuorum.web.commands.payment.massMailing.MassMailingCommand.subject.label"/>:</label>
        <div class="col-sm-8 col-md-7">
            <formUtil:input
                    command="${command}"
                    field="subject"/>
        </div>

        %{--<label for="subject" class="col-sm-2 col-md-1 control-label">Subject:</label>--}%
        %{--<div class="col-sm-8 col-md-7">--}%
            %{--<input type="text" class="form-control input-lg" id="subject" placeholder="It’s time to build a better country for everybody" equired aria-required="true">--}%
        %{--</div>--}%
    </fieldset>
    <fieldset class="form-group image header-campaign" data-multimedia-switch="on" data-multimedia-type="IMAGE">
        <label for="header" class="col-sm-2 col-md-1 control-label"><g:message code="kuorum.web.commands.payment.massMailing.MassMailingCommand.headerPictureId.label"/>:</label>
        <formUtil:editImage
                command="${command}"
                field="headerPictureId"
                fileGroup="${ kuorum.core.FileGroup.MASS_MAIL_IMAGE}"
                cssClass="col-sm-8 col-md-7"
                labelCssClass="sr-only"/>
        <!-- AQUÍ LA BARRA DE PROGRESO Y LA MODAL DE RECORTAR COMO EN EDICIÓN PROYECTO -->
    </fieldset>
    <fieldset class="form-group">
        <label for="text" class="col-sm-2 col-md-1 control-label"><g:message code="kuorum.web.commands.payment.massMailing.MassMailingCommand.text.label"/>:</label>
        <div class="textareaContainer col-sm-8 col-md-7">
            %{--<textarea name="text" class="form-control texteditor" rows="8" placeholder="${message(code:'kuorum.web.commands.payment.massMailing.MassMailingCommand.text.placeholder')}" id="textProject" required aria-required="true"></textarea>--}%
            <formUtil:textArea command="${command}" field="text" rows="8" texteditor="texteditor"/>
        </div>
        <div class="col-sm-8 col-sm-offset-2 col-md-4 col-md-offset-0">
            <ul class="form-final-options">
                <li>
                    <a href="#" id="save-draft-campaign">
                        <g:message code="tools.massMailing.saveDraft"/>
                    </a>
                </li>
                <li>
                    <a href="#" class="btn btn-blue inverted" role="button" id="openCalendar">
                        <span class="fa fa-clock-o"></span>
                        <g:message code="tools.massMailing.schedule"/>
                    </a>
                    <div id="selectDate">
                        %{--<form>--}%
                            <label class="sr-only"><g:message code="tools.massMailing.schedule.label"/></label>
                            <formUtil:date field="scheduled" command="${command}" cssClass="form-control" time="true"/>
                            <a href="#" class="btn btn-blue inverted" id="sendLater">
                                <g:message code="tools.massMailing.schedule.sendLater"/>
                            </a>
                        %{--</form>--}%
                    </div>
                </li>
                <li><a href="#" class="btn btn-blue inverted" id="send"><g:message code="tools.massMailing.send"/></a></li>
            </ul>
        </div>
    </fieldset>
</form>

<!-- MODAL CONFIRM -->
<div class="modal fade in" id="campaignConfirm" tabindex="-1" role="dialog" aria-labelledby="campaignConfirmTitle" aria-hidden="true">
    <div class="modal-dialog ">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
                    <span aria-hidden="true" class="fa fa-times-circle-o fa"></span><span class="sr-only"><g:message code="modalDefend.close"/></span>
                </button>
                <h4 id="campaignConfirmTitle">
                    <g:message code="tools.massMailing.confirmationModal.title"/>
                </h4>
            </div>
            <div class="modal-body">
                <a href="#" class="btn btn-blue inverted btn-lg" id="saveCampaignBtn">
                    <g:message code="tools.massMailing.confirmationModal.button"/>
                </a>
            </div>
        </div>
    </div>
</div>

<!-- MODAL TEST ADVISE -->
<div class="modal fade in" id="sendTestModal" tabindex="-1" role="dialog" aria-labelledby="sendTestModalTitle" aria-hidden="true">
    <div class="modal-dialog ">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
                    <span aria-hidden="true" class="fa fa-times-circle-o fa"></span><span class="sr-only"><g:message code="modalDefend.close"/></span>
                </button>
                <h4 id="sendTestModalTitle">
                    <g:message code="tools.massMailing.sendTestModal.title"/>
                </h4>
            </div>
            <div class="modal-body">
                <p><g:message code="tools.massMailing.sendTestModal.text"/></p>
                <a href="#" class="btn btn-blue inverted btn-lg" id="sendTestModalButonOk">
                    <g:message code="tools.massMailing.sendTestModal.button"/>
                </a>
            </div>
        </div>
    </div>
</div>

<!-- WARN USING ANONYMOUS FILTER -->
<div class="modal fade in" id="campaignWarnFilterEdited" tabindex="-1" role="dialog" aria-labelledby="campaignWarnFilterEditedTitle" aria-hidden="true">
    <div class="modal-dialog ">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
                    <span aria-hidden="true" class="fa fa-times-circle-o fa"></span><span class="sr-only"><g:message code="modalDefend.close"/></span>
                </button>
                <h4>
                    <g:message code="tools.massMailing.warnFilterEdited.title"/>
                </h4>
            </div>
            <div class="modal-body">
                <p><g:message code="tools.massMailing.warnFilterEdited.text"/> </p>
                <a href="#" class="btn btn-blue inverted btn-lg" id="campaignWarnFilterEditedButtonOk">
                    <g:message code="tools.massMailing.warnFilterEdited.button"/>
                </a>
            </div>
        </div>
    </div>
</div>