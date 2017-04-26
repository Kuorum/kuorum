<%@ page import="org.kuorum.rest.model.notification.campaign.stats.TrackingMailStatusRSDTO" %>
<r:require modules="datepicker, newsletter" />
<g:set var="contentType" value="${contentType}" />
<div class="box-steps container-fluid choose-campaign">
    <g:render template="/campaigns/creationSteps/threeSteps" model="[editReference: 'politicianMassMailingEdit3']"/>
</div>

<div class="box-ppal campaign-new">
    <h1 class="sr-only">Newsletter</h1>

        <g:if test="${contentType=='HTML'}">
            <g:render template="types/contentType/customHTML" model="[command: command, filters: filters, totalContacts: totalContacts, campaignId: campaignId, anonymousFilter: anonymousFilter]"></g:render>
        </g:if>
        <g:else>
            <g:render template="types/contentType/simpleTemplate" model="[command: command, filters: filters, totalContacts: totalContacts, campaignId: campaignId, anonymousFilter: anonymousFilter]"></g:render>
        </g:else>
</div>

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
                <a href="#" class="btn btn-blue inverted btn-lg" id="saveCampaignBtn" data-redirectLink="politicianCampaigns">
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