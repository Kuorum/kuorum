<div class="comment-box call-to-action call-to-action-add-proposal hidden-sm hidden-xs">
    <div class="comment-header">
        <span class="call-title">${message(code: "debate.proposals.callToAction.title")}</span>
        <span class="call-subTitle">${message(code: "debate.proposals.callToAction.subTitle", args: [debateUser.name])}</span>
    </div>
    <div class="comment-proposal clearfix">
        <div class="comment editable col-md-11 col-xs-12" data-placeholder="${message(code: "debate.proposal.placeholder")}" style="min-height: 100px; padding-top: 20px"></div>
    </div>

    <span for="editable" class="error" style="display: none;"><span class="tooltip-arrow"></span>${message(code: "debate.proposals.error")}</span>

    <div class="actions clearfix">
        <button
                type="button"
                class="btn btn-blue btn-lg publish publish-proposal"
                data-userLoggedAlias="${userUtil.loggedUserId()}"
                data-postUrl="${g.createLink(mapping: 'debateProposalNew')}"
                data-debateId="${debate.id}"
                data-debateAlias="${debateUser.alias}"
        >
            ${message(code: "debate.proposals.callToAction.button")}
        </button>
    </div>
</div>