<%@ page import="org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO" %>

<li class="${campaign.status}" id="campaignPos_${idx}">
    <span class="id sr-only">${campaign.id}</span>
    <span class="state">${campaign.status}</span>
    <h3>
        <g:link mapping="politicianMassMailingShow" params="[campaignId:campaign.id]" class="title">
            ${campaign.name}<span></span>
        </g:link>
    </h3>
    <p class="name">
        <g:message code="${org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.class.name}.${campaign.status}"/>
        <input class="timestamp" type="hidden" val="${campaign?.sentOn?.time?:'LAST'}" />  %{-- Letters are after than numbers--}%
        <g:if test="${campaign.sentOn}">
            <span class="date">
                (<g:formatDate date="${campaign.sentOn}" type="datetime" style="LONG" timeStyle="SHORT" timeZone="${user.timeZone}"/>)
            </span>
            - ${campaign.filter?.name?:g.message(code:'tools.massMailing.fields.filter.to.all')}
        </g:if>
    </p>
    <ul>
        <li class="recipients">
            <span class="recip-number"><campaignUtil:campaignsSent campaign="${campaign}"/></span>
            <g:message code="tools.massMailing.list.recipients"/>

        </li>
        <li class="open">
            <span class='open-number'><campaignUtil:openRate campaign="${campaign}"/></span>
            <g:message code="tools.massMailing.list.opens"/>
        </li>
        <li class="click">
            <span class='click-number'><campaignUtil:clickRate campaign="${campaign}"/></span>
            <g:message code="tools.massMailing.list.click"/>
        </li>
    </ul>
    <g:if test="${campaign.status==org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.SENT}">
        <g:link mapping="politicianMassMailingShow" params="[campaignId:campaign.id]" class="campaignStats"><span class="fa fa-line-chart"></span> <span class="sr-only">Stats</span></g:link>
    </g:if>
    <g:else>
        <g:link mapping="politicianMassMailingShow" params="[campaignId:campaign.id]" class="campaignEdit"><span class="fa fa-edit"></span> <span class="sr-only">Edit</span></g:link>
    </g:else>
    <a href="#" class="campaignDelete deleteCampaignModal" id="deleteCampaignId_${campaign.id}">
        <span class="fa fa-trash"></span> <span class="sr-only">Delete</span>
    </a>
</li>



<!-- MODAL CONFIRM -->
<div class="modal fade in" id="campaignDeleteConfirm" tabindex="-1" role="dialog" aria-labelledby="campaignDeleteTitle" aria-hidden="true">
    <div class="modal-dialog ">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
                    <span aria-hidden="true" class="fa fa-times-circle-o fa"></span><span class="sr-only"><g:message code="modalDefend.close"/></span>
                </button>
                <h4 id="campaignDeleteTitle">
                    <g:message code="tools.massMailing.deleteCampaignModal.title"/>
                </h4>
            </div>
            <div class="modal-body">
                <g:link mapping="politicianMassMailingRemove" params="[campaignId:campaign.id]"  role="button" class="btn btn-blue inverted btn-lg deleteCampaignBtn">
                    <g:message code="tools.massMailing.deleteCampaignModal.button"/>
                </g:link>
            </div>
        </div>
    </div>
</div>