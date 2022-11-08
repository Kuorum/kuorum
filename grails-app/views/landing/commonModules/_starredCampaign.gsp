<g:if test="${starredCampaign}">
    <div id="starredCampaign">
        <div class="starredCampaign-title col-md-10 col-xs-8">
            <span class="fal fa-megaphone"></span>
            <span class="starredCampaing-title-text">
                <span class="hidden-xs"><g:message code="landingPage.starredCampaign.whatsGoingOn"/>:</span>
                ${starredCampaign.title}
            </span>
        </div>
        <div class="starredCampaign-btn col-md-2 col-xs-4">
            <g:link mapping="campaignShow" params="${starredCampaign.encodeAsLinkProperties()}" class="btn btn-lg hidden-xs"><g:message code="landingPage.starredCampaign.participate.now"/></g:link>
            <g:link mapping="campaignShow" params="${starredCampaign.encodeAsLinkProperties()}" class="btn visible-xs"><g:message code="landingPage.starredCampaign.participate"/></g:link>
        </div>
    </div>
</g:if>