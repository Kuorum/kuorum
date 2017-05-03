<formUtil:validateForm bean="${command}" form="politicianMassMailingForm" dirtyControl="true"/>
<form action="${g.createLink(mapping: 'politicianMassMailingContentTemplate', params: [campaignId: campaignId])}" class="form-horizontal" id="politicianMassMailingForm" method="POST" data-generalErrorMessage="${g.message(code:'kuorum.web.commands.payment.massMailing.MassMailingCommand.form.genericError')}">
    <input type="hidden" name="sendType" value="DRAFT" id="sendMassMailingType"/>
    <input type="hidden" name="redirectLink" id="redirectLink"/>

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
    </fieldset>
    <fieldset class="form-group ">
        <label for="text" class="col-sm-2 col-md-1 control-label"><g:message code="kuorum.web.commands.payment.massMailing.MassMailingCommand.text.label"/>:</label>
        <div class="textareaContainer col-sm-8 col-md-7">
            %{--<textarea name="text" class="form-control texteditor" rows="8" placeholder="${message(code:'kuorum.web.commands.payment.massMailing.MassMailingStep3Command.text.placeHolder')}" id="textProject" required aria-required="true"></textarea>--}%
            <formUtil:textArea command="${command}" field="text" rows="8" texteditor="texteditor" placeholder="${message(code:'kuorum.web.commands.payment.massMailing.MassMailingCommand.text.placeHolder')}"/>
        </div>
    </fieldset>

    <fieldset class="form-group">
        <div class="col-sm-8 col-sm-offset-2 col-md-7 col-md-offset-5 form-control-campaign">
            <ul class="form-final-options">
                <li>
                    <g:link mapping="politicianMassMailingSendTest" params="[campaignId:campaignId]" elementId="sendTest" title="${g.message(code:'tools.massMailing.sendTest')}">${g.message(code:'tools.massMailing.sendTest')}</g:link>
                </li>
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