<r:require modules="datepicker, postForm, debateForm"/>

<div class="box-steps container-fluid campaign-steps">
    <g:render template="/campaigns/steps/campaignSteps" model="[mappings: mappings]"/>
</div>

<div class="box-ppal campaign-new">
    <h1 class="sr-only"><g:message code="admin.createDebate.title"/></h1>

    <%-- validateHidden: the logo is a hidden input filled by the uploader, jquery-validate would skip it --%>
    <formUtil:validateForm bean="${command}" form="contestApplicationEditAssociation" dirtyControl="true" validateHidden="true"/>
    <form action="#" class="form-horizontal campaign-form" id="contestApplicationEditAssociation" method="POST"
          data-generalErrorMessage="${g.message(code: 'kuorum.web.commands.payment.massMailing.DebateCommand.form.genericError')}">
        <input type="hidden" name="redirectLink" id="redirectLink"/>

        <%-- Same grid as the Contenido step: labels (right aligned, short enough for col-md-1) + controls in col-sm-8 col-md-7.
             The info icon hangs after the label text exactly as the one of the image in Contenido. --%>
        <fieldset aria-live="polite" class="form-group">
            <label for="associationName" class="col-sm-2 col-md-1 control-label"><g:message
                    code="contestApplication.association.name.label"/>:</label>

            <div class="col-sm-8 col-md-7">
                <formUtil:input command="${command}" field="associationName"
                                placeHolder="${g.message(code: 'kuorum.web.commands.payment.contest.ContestApplicationAssociationCommand.associationName.label.withCollaborator')}"/>
            </div>
        </fieldset>

        <fieldset aria-live="polite" class="form-group multimedia">
            <label for="associationImage" class="col-sm-2 col-md-1 control-label"><g:message
                    code="contestApplication.association.logo.label"/>:
                <span class="fas fa-info-circle" data-toggle="tooltip" data-placement="top"
                      title="${g.message(code: 'kuorum.web.commands.payment.contest.ContestApplicationAssociationCommand.associationImage.label.withCollaborator')}"></span></label>

            <%-- The logo is a square (1:1): a narrow column keeps the upload box from growing as tall as the form is wide --%>
            <div class="col-sm-4 col-md-3">
                <span class="span-label sr-only"><g:message code="default.upload.title"/></span>
                <formUtil:editImage command="${command}" field="associationImage"
                                    fileGroup="${kuorum.core.FileGroup.ASSOCIATION_IMAGE_PROFILE}"/>
            </div>
        </fieldset>

        <fieldset aria-live="polite" class="form-group">
            <label for="files" class="col-sm-2 col-md-1 control-label"><g:message
                    code="kuorum.web.commands.payment.massMailing.MassMailingCommand.files.label"/>:
                <span class="fas fa-info-circle" data-toggle="tooltip" data-placement="top"
                      title="${g.message(code: 'contestApplication.association.files.label')}"></span></label>

            <%-- Same place as the documents of the registration (files of the admin contact): private, they never show in the Content step --%>
            <div class="textareaContainer col-sm-8 col-md-7">
                <formUtil:uploadContactFiles contact="${contact}" adminContact="true"/>
            </div>
        </fieldset>

        <g:render template="/campaigns/edit/stepButtons"
                  model="[campaign: campaign, mappings: mappings, status: status, command: command, numberRecipients: numberRecipients]"/>
    </form>
</div>



