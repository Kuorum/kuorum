<g:if test="${starredCampaign}">
    <div id="starredCampaign">
        <div class="starredCampaign-title col-xs-10">
            <campaignUtil:showIcon campaign="${starredCampaign}"/>
            <span class="starredCampaing-title-text">
                <span class="hidden-xs"><g:message code="landingPage.starredCampaign.whatsGoingOn"/>:</span>
                ${starredCampaign.title}
            </span>
        </div>
        <div class="starredCampaign-btn col-xs-2">
            <g:link mapping="campaignShow" params="${starredCampaign.encodeAsLinkProperties()}" class="btn btn-lg">Participa</g:link>
        </div>
    </div>
</g:if>