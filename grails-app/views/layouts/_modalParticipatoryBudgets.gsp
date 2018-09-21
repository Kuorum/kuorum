<g:if test="${pbList}">
    <g:each in="${pbList}" var="pb">
        <div class="modal-card row">
            <div class="modal-thumbnail col-sm-4">
                <g:render template="/campaigns/showModules/campaignDataMultimedia" model="[campaign: pb]"/>
            </div>
            <div class="modal-info col-sm-8">
                <h4>${pb.title}</h4>
                <ul class="list">
                    <li>
                        <a href="#" class="btn btn-blue" data-redirectLink="politicianCampaigns">
                            <g:message code="dashboard.payment.chooseCampaign.tooltip.districtProposal.modal.button.create"/>
                        </a>
                    </li>
                    <li>
                        <a href="#" class="btn btn-blue inverted" data-redirectLink="">
                            <g:message code="dashboard.payment.chooseCampaign.tooltip.districtProposal.modal.button.goToParticipatoryBudget"/>
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </g:each>
</g:if>
<g:else>
    <h5>
        <g:message code="dashboard.payment.chooseCampaign.tooltip.districtProposal.modal.noActiveParticipatoryBudgets"/>
    </h5>
</g:else>