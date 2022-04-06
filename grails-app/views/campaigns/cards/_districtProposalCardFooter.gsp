<div class="card-footer">
    <ul>
        <g:if test="${districtProposal.participatoryBudget.status == org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.BALLOT && districtProposal.participatoryBudget.participatoryBudgetType == org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetTypeDTO.SIMPLE_VOTE}">
            <g:render
                    template="/districtProposal/showModules/districtProposalDataIcon/districtProposalDataIconGeneric"
                    model="[
                            districtProposal     : districtProposal,
                            iconClass            : 'fa-shopping-cart',
                            iconNumber           : districtProposal.numVotes,
                            isActive             : districtProposal.isVoted,
                            callButtonActionClass: 'disabled',
                            callButtonAction     : g.createLink(mapping: 'districtProposalShow', params: districtProposal.encodeAsLinkProperties()),
                            position             : 'left'
                    ]"/>
        </g:if>
        <g:elseif test="${[
                org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.BALLOT,
                org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.CLOSED,
                org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.RESULTS,
        ].contains(districtProposal.participatoryBudget.status) && districtProposal.price && districtProposal.participatoryBudget.participatoryBudgetType == org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetTypeDTO.BUDGET}">
            <li class="districtProposalPrice"><g:message code="kuorum.multidomain.money"
                                                         args="[districtProposal.price]"/></li>
        </g:elseif>
        <g:elseif test="${showAuthor}">
            <li class="owner">
                <userUtil:showUser
                        user="${districtProposal.user}"
                        showName="true"
                        showActions="false"
                        showDeleteRecommendation="false"
                        htmlWrapper="div"/>
            </li>
        </g:elseif>

        <li class="${districtProposal.technicalReviewStatus == org.kuorum.rest.model.communication.participatoryBudget.TechnicalReviewStatusRDTO.VALID && districtProposal.participatoryBudget.status == org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.BALLOT ? 'comment-counter-as-button' : ''}">
            <g:render template="/districtProposal/showModules/districtProposalDataIcon"
                      model="[districtProposal: districtProposal, showAuthor: showAuthor]"/>
        </li>
    </ul>
</div>