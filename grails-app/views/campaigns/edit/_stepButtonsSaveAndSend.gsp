<g:if test="${status != org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.SENT}">
    <li>
        <a href="#" id="save-draft-debate" data-redirectLink="politicianCampaigns">
            <g:message code="tools.massMailing.saveDraft"/>
        </a>
    </li>
    <li>
        <a href="#" class="btn btn-blue inverted" role="button" id="openCalendar">
            <span class="fa fa-clock-o"></span>
            <g:message code="tools.massMailing.schedule"/>
        </a>
        <div id="selectDate">
            <label class="sr-only"><g:message code="tools.massMailing.schedule.label"/></label>
            <formUtil:date command="${command}" field="publishOn" cssClass="form-control" time="true"/>
            <a href="#" class="btn btn-blue inverted" id="send-campaign-later">
                <g:message code="tools.massMailing.schedule.sendLater"/>
            </a>
        </div>
    </li>
    <li><a href="#" class="btn btn-blue inverted" id="send-draft"><g:message code="tools.massMailing.send"/></a></li>
</g:if>
<g:else>
    <!-- Hidden inputs -->
    <input type="hidden" name="publishOn" value="${g.formatDate(date: command.publishOn, format: kuorum.web.constants.WebConstants.WEB_FORMAT_DATE)}"/>
    <li><button class="btn btn-blue inverted" id="save-draft" data-redirectLink="${mappings.showResult}" value="${g.message(code: "tools.massMailing.save")}" >${g.message(code: "tools.massMailing.save")}</button></li>
</g:else>



<!-- MODAL CONFIRM -->
<div class="modal fade in" id="campaignConfirm" tabindex="-1" role="dialog" aria-labelledby="campaignConfirmTitle" aria-hidden="true">
    <div class="modal-dialog ">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
                    <span aria-hidden="true" class="fa fa-times-circle-o fa"></span><span class="sr-only"><g:message code="modalDefend.close"/></span>
                </button>
                <h4 id="campaignConfirmTitle">
                    <g:message code="tools.massMailing.confirmationModal.title" args="[numberRecipients]"/>
                </h4>
            </div>
            <div class="modal-body">
                <a href="#" class="btn btn-blue inverted btn-lg" id="saveCampaignBtn" data-redirectLink="${mappings.showResult}" data-callback="">
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
