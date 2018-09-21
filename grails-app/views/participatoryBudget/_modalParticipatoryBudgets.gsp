<g:if test="${pbList}">
    <g:each in="${pbList}" var="pb">
        <div class="modal-card row">
            <div class="modal-thumbnail col-sm-4 hidden-xs">
                <g:render template="/campaigns/showModules/campaignDataMultimedia" model="[campaign: pb]"/>
            </div>
            <div class="modal-info col-sm-offset-1 col-sm-7 col-xs-12">
                <h4>${pb.title}</h4>
                <ul class="list">
                    <li>
                        <g:link mapping="districtProposalCreate" params="${pb.encodeAsLinkProperties()}" class="btn btn-blue">
                            <g:message code="dashboard.payment.chooseCampaign.tooltip.districtProposal.modal.button.create"/>
                        </g:link>
                    </li>
                    <li>
                        <g:link mapping="campaignShow" params="${pb.encodeAsLinkProperties()}" class="btn btn-blue inverted" data-redirectLink="">
                            <g:message code="dashboard.payment.chooseCampaign.tooltip.districtProposal.modal.button.goToParticipatoryBudget"/>
                        </g:link>
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