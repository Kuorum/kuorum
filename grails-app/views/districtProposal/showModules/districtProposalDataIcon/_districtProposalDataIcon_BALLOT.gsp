<g:if test="${districtProposal.technicalReviewStatus == org.kuorum.rest.model.communication.participatoryBudget.TechnicalReviewStatusRDTO.VALID}">
        <g:render
                template="/districtProposal/showModules/districtProposalDataIcon/districtProposalDataIconGeneric"
                model="[
                        districtProposal: districtProposal,
                        iconClass:'fa-shopping-cart',
                        iconNumber:districtProposal.numVotes,
                        isActive:districtProposal.isVoted,
                        callButtonActionClass:'districtProposal-vote',
                        callButtonAction:g.createLink(mapping:'participatoryBudgetDistrictProposalVote', params:districtProposal.encodeAsLinkProperties())
                ]" />
</g:if>
<g:else>
        <g:render
                template="/districtProposal/showModules/districtProposalDataIcon/districtProposalDataIconGeneric"
                model="[
                        districtProposal: districtProposal,
                        iconClass:'fa-rocket',
                        iconNumber:districtProposal.numSupports,
                        isActive:districtProposal.isSupported,
                        callButtonActionClass:'disabled',
                        callButtonAction:g.createLink(mapping:'districtProposalShow', params:districtProposal.encodeAsLinkProperties())
                ]" />
</g:else>
