<g:if test="${[org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.SENT, org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.REVIEW].contains(status)}">
    <!-- Hidden inputs -->
    <input type="hidden" name="publishOn"
           value="${g.formatDate(date: command.publishOn, format: kuorum.web.constants.WebConstants.WEB_FORMAT_DATE)}"/>
    <li><button class="btn btn-blue inverted" id="update-campaign" data-redirectLink="${mappings.showResult}"
                value="${g.message(code: "tools.massMailing.save")}">${g.message(code: "tools.massMailing.save")}</button>
    </li>
</g:if>
<g:elseif test="${status == org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.PAUSE}">
    <input type="hidden" name="publishOn" value="${g.formatDate(date: command.publishOn, format: kuorum.web.constants.WebConstants.WEB_FORMAT_DATE)}"/>
    <li><button class="btn btn-grey inverted" id="update-campaign-reactivate" data-redirectLink="${mappings.showResult}" value="${g.message(code: "tools.massMailing.save")}" >${g.message(code: "tools.massMailing.saveAndActivate")}</button></li>
    <li><button class="btn btn-blue inverted" id="update-campaign" data-redirectLink="${mappings.showResult}" value="${g.message(code: "tools.massMailing.save")}" >${g.message(code: "tools.massMailing.save")}</button></li>
</g:elseif>
<g:else>

    <g:if test="${campaign && campaign.campaignType == org.kuorum.rest.model.communication.CampaignTypeRSDTO.BULLETIN}">
        <li>
            <g:link mapping="politicianMassMailingSendTest" params="[campaignId: campaign.id]" elementId="sendTest"
                    title="${g.message(code: 'tools.massMailing.sendTest')}"
                    class="btn btn-grey-light">${g.message(code: 'tools.massMailing.sendTest')}</g:link>
        </li>
    </g:if>

    <li>
        <a href="#" id="save-draft-debate" data-redirectLink="politicianCampaigns" class="btn btn-grey-light">
            <g:message code="tools.massMailing.saveDraft"/>
        </a>
    </li>
    <g:if test="${!mappings.hideScheduler}">
        <li>
            <a href="#" class="btn btn-blue inverted" role="button" id="openCalendar">
                <span class="fal fa-clock"></span>
                <g:message code="tools.massMailing.schedule"/>
            </a>

            <div id="selectDate">
                <label class="sr-only"><g:message code="tools.massMailing.schedule.label"/></label>
                <formUtil:date command="${command}" field="publishOn" cssClass="form-control" time="true"/>
                <a href="#" class="btn btn-blue inverted" id="send-campaign-later">
                    <g:message
                            code="${numberRecipients > 0 ? 'tools.massMailing.schedule.sendLater' : 'tools.massMailing.schedule.publishLater'}"/>
                </a>
            </div>
        </li>
    </g:if>
    <li>
        <a href="#" class="btn btn-blue inverted" id="send-draft">
            <g:message code="${numberRecipients > 0 ? 'tools.massMailing.send' : 'tools.massMailing.publish'}"/>
        </a>
    </li>
    <g:if test="${campaign && campaign.campaignType == org.kuorum.rest.model.communication.CampaignTypeRSDTO.SURVEY}">
        <a href="#" class="btn btn-blue inverted" id="publish-with-summoning">
            <g:message code="tools.summoning.campaign.publish"/>
        </a>
        <input type="hidden" name="create-summoning"/>
    </g:if>
</g:else>


<!-- MODAL CONFIRM -->
<div class="modal fade in" id="campaignConfirm" tabindex="-1" role="dialog" aria-labelledby="campaignConfirmTitle" aria-hidden="true">
    <div class="modal-dialog ">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
                    <span aria-hidden="true" class="fal fa-times-circle fa"></span><span class="sr-only"><g:message code="modalDefend.close"/></span>
                </button>
                <h4 id="campaignConfirmTitle">
                    <g:message code="${numberRecipients>0?'tools.massMailing.confirmationModal.title':'tools.massMailing.confirmationModal.noContacts.title'}" args="[numberRecipients]"/>
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
                    <span aria-hidden="true" class="fal fa-times-circle fa"></span><span class="sr-only"><g:message code="modalDefend.close"/></span>
                </button>
                <h4 id="sendTestModalTitle">
                    <g:message code="tools.massMailing.sendTestModal.title"/>
                </h4>
            </div>
            <div class="modal-body">
                <p class="text-left"><g:message code="tools.massMailing.sendTestModal.text"/></p>
                <a href="#" class="btn btn-blue inverted btn-lg" id="sendTestModalButonOk" data-dismiss="modal">
                    <g:message code="tools.massMailing.sendTestModal.button"/>
                </a>
            </div>
        </div>
    </div>
</div>
