<g:if test="${starredCampaign}">
    <div id="starredCampaign">
        <div class="starredCampaign-title col-md-10 col-xs-8">
            <campaignUtil:showIcon campaign="${starredCampaign}"/>
            <span class="starredCampaing-title-text">
                <span class="hidden-xs"><g:message code="landingPage.starredCampaign.whatsGoingOn"/>:</span>
                ${starredCampaign.title}
            </span>
        </div>
        <div class="starredCampaign-btn col-md-2 col-xs-4">
            <g:link mapping="campaignShow" params="${starredCampaign.encodeAsLinkProperties()}" class="btn btn-lg hidden-xs">Participa ahora</g:link>
            <g:link mapping="campaignShow" params="${starredCampaign.encodeAsLinkProperties()}" class="btn visible-xs">Participa</g:link>
        </div>
    </div>
</g:if>