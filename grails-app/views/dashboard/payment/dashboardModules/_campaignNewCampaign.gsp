<div class="box-ppal" id="createNewCampaign">
    %{--<h2><g:message code="dashboard.payment.newCampaign.title"/></h2>--}%
    <g:if test="${!numberCampaigns}">
        <p>
            <g:message code="dashboard.payment.newCampaign.neverSent" args="[g.createLink(mapping: 'blog')]" encodeAs="raw"/>
        </p>
        <g:render template="/newsletter/chooseCampaign"/>
    </g:if>
    <g:else>
        <p>
            <g:message code="dashboard.payment.newCampaign.lastCampaignSent" args="[durationDays, g.createLink(mapping: 'blog')]" encodeAs="raw"/>
        </p>
        <g:render template="/newsletter/chooseCampaign"/>
    </g:else>
</div>