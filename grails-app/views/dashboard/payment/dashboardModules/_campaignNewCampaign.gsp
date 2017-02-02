<div class="box-ppal" id="createNewCampaign">
    <h2><g:message code="dashboard.payment.newCampaign.title"/></h2>
    <g:if test="${!numberCampaigns}">
        <p>
            <g:message code="dashboard.payment.newCampaign.neverSent" args="[g.createLink(mapping: 'blog')]" encodeAs="raw"/>
            <br/>
            <g:link mapping="politicianCampaignsNew" class="btn btn-lg inverted" role="button">
                <g:message code="dashboard.payment.newCampaign.sentNew"/>
            </g:link>
        </p>
    </g:if>
    <g:else>
        <p>
            <g:message code="dashboard.payment.newCampaign.lastCampaignSent" args="[durationDays, g.createLink(mapping: 'blog')]" encodeAs="raw"/>
            <br/>
            <g:link mapping="politicianCampaignsNew" class="btn btn-lg inverted" role="button">
                <g:message code="dashboard.payment.newCampaign.sentNew"/>
            </g:link>
        </p>
    </g:else>
</div>