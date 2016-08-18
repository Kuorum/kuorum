<r:require modules="datepicker" />
<h1 class="sr-only">Newsletter</h1>
<formUtil:validateForm bean="${command}" form="politicianMassMailingForm"/>
<form action="#" class="form-horizontal" name="politicianMassMailingForm" method="POST">
    <input type="hidden" name="sendType" value="DRAFT" id="sendMassMailingType"/>
    <fieldset class="form-group" id="toFilters">
        <label for="to" class="col-sm-2 col-md-1 control-label"><g:message code="tools.massMailing.fields.filter.to"/> :</label>
        <div class="col-sm-4 col-md-3">
            <select name="filterId" class="form-control input-lg" id="recipients">
                <option value="0" id="all" data-amountContacts="${totalContacts}"><g:message code="tools.massMailing.fields.filter.to.all"/></option>
                <g:each in="${filters}" var="filter">
                    <option value="${filter.id}" ${command.filterId == filter.id?'selected':''} data-amountContacts="${filter.amountOfContacts}">${filter.name}</option>
                </g:each>
                <option value="new" id="newFilter" data-amountContacts="-"><g:message code="tools.massMailing.fields.filter.to.createNew"/></option>
            </select>
        </div>
        <div class="col-sm-5">
            <a href="#" role="buttom" id="filterContacts">
                <span class="fa fa-filter fa-lg"></span>
                <span class="sr-only"><g:message code="tools.massMailing.fields.filter.button"/></span>
            </a>
            <span id="infoToContacts">
                <span class="amountRecipients"></span> recipients <span class="fa fa-filter fa-lg"></span>
            </span>
        </div>
    </fieldset>
    <div id="newFilterContainer">
        <g:render template="/contacts/filter/filterFieldSet"/>
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
                            <label class="sr-only">Select your date</label>
                            <formUtil:date field="scheduled" command="${command}" cssClass="form-control"/>
                            <button type="submit" class="btn btn-blue inverted" id="sendLater">
                                <g:message code="tools.massMailing.schedule.sendLater"/>
                            </button>
                        %{--</form>--}%
                    </div>
                </li>
                <li><a href="#" class="btn btn-blue inverted" id="send">
                    <g:message code="tools.massMailing.send"/>
                </a></li>
            </ul>
        </div>
    </fieldset>
<form>

<!-- MODAL CONFIRM -->
<div class="modal fade in" id="campaignConfirm" tabindex="-1" role="dialog" aria-labelledby="campaignConfirmTitle" aria-hidden="true">
    <div class="modal-dialog ">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
                    <span aria-hidden="true" class="fa fa-times-circle-o fa"></span><span class="sr-only">Cerrar</span>
                </button>
                <h4 id="campaignConfirmTitle">Send this to 3,567 recipients?</h4>
            </div>
            <div class="modal-body">
                <a href="#" class="btn btn-blue inverted btn-lg" id="send">Oh, yeah!</a>
            </div>
        </div>
    </div>
</div>