<div class="box-ppal" id="createNewCampaign">
    <h2><g:message code="dashboard.payment.newCampaign.title"/></h2>
    <g:if test="${contacts.total<=0}">
        <p>
            <g:message code="dashboard.payment.noContacts.sendTestCampaign" args="[g.createLink(mapping: 'politicianMassMailingNew', params: [testFilter:true])]"/>:
            <br/>
            <g:link mapping="politicianContactImport" class="btn btn-lg inverted">
                <g:message code="dashboard.payment.importContact"/>
            </g:link>
        </p>
    </g:if>
    <g:else>
        <p>
            <g:message code="dashboard.payment.newCampaign.lastCampaignSent" args="[durationDays, '#']" encodeAs="raw"/>
            <br/>
            <g:link mapping="politicianMassMailing" class="btn btn-lg inverted" role="button">
                <g:message code="dashboard.payment.newCampaign.sentNew"/>
            </g:link>
        </p>
    </g:else>
</div>