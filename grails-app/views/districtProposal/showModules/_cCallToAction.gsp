<div class="comment-box call-to-action call-to-action-add-proposal hidden-sm hidden-xs">
    <g:if test="${districtProposal.published}">
        <g:render template="/districtProposal/showModules/cCallToActionStatus/cCallToAction_${districtProposal.participatoryBudget.status}" model="[districtProposal:districtProposal]"/>
    </g:if>
    <g:else>
        <g:render template="/districtProposal/showModules/cCallToActionStatus/cCallToAction_NO_PUBLISHED" model="[districtProposal:districtProposal]"/>
    </g:else>
</div>
