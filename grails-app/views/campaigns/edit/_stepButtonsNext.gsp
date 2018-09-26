<g:set var="draftText"><g:message code="tools.massMailing.saveDraft"/></g:set>
<g:if test="${status == org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.SENT}">
    <g:set var="draftText"><g:message code="tools.massMailing.save"/></g:set>
</g:if>
<g:if test="${!mappings.hideDraftButton}">
    <li>
        <a href="#" id="save-draft-debate" data-redirectLink="politicianCampaigns" class="btn btn-grey-light">
            ${draftText}
        </a>
    </li>
</g:if>
<li>
    <a href="#" class="btn btn-blue inverted" id="next" data-redirectLink="${mappings.next}">
        <g:message code="tools.massMailing.next"/>
    </a>
</li>
