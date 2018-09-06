<g:set var="callTitleMsg" value="${g.message(code:'districtProposal.callToAction.draft.title')}"/>
<g:set var="callSubtitleMsg" value="${g.message(code:'districtProposal.callToAction.draft.subtitle')}"/>

<g:if test="${![org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.TECHNICAL_REVIEW, org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.ADDING_PROPOSALS].contains(districtProposal.participatoryBudget.status) && districtProposal.technicalReviewStatus == org.kuorum.rest.model.communication.participatoryBudget.TechnicalReviewStatusRDTO.NO_VALID}">
%{--PROPOSAL NO VALID--}%
    <g:set var="callTitleMsg" value="${g.message(code:"districtProposal.callToAction.TECHNICAL_REVIEW.NO_VALID.title", args: [campaignUser.name])}"/>
    <g:set var="dateEndTime" value="${}"/>
    <g:set var="callSubtitleMsg" value="${g.message(code:"districtProposal.callToAction.TECHNICAL_REVIEW.NO_VALID.subtitle", args: [campaignUser.name])}"/>
</g:if>

<div class="comment-box call-to-action call-to-action-add-proposal hidden-sm hidden-xs">
    <g:render template="/districtProposal/showModules/cCallToActionStatus/cCallToAction_${districtProposal.participatoryBudget.status}" model="[districtProposal:districtProposal]"/>
</div>

%{--<g:set var="callTitleMsg" value="${g.message(code:'districtProposal.callToAction.draft.title')}"/>--}%
%{--<g:set var="callSubtitleMsg" value="${g.message(code:'districtProposal.callToAction.draft.subtitle')}"/>--}%
%{--<g:set var="callButtonMsgIcon" value=""/>--}%
%{--<g:set var="callButtonShow" value="${districtProposal.published}"/>--}%
%{--<g:set var="callButtonAction" value=""/>--}%
%{--<g:set var="callButtonActionClass" value=""/>--}%
%{--<g:set var="callButtonActive" value="${false}"/>--}%

%{--<g:set var="btnStatusTextOn"><g:message code="districtProposal.callToAction.${districtProposal.participatoryBudget.status}.button.on"/></g:set>--}%
%{--<g:set var="btnStatusTextOnHover"><g:message code="districtProposal.callToAction.${districtProposal.participatoryBudget.status}.button.on.hover"/></g:set>--}%
%{--<g:set var="btnStatusTextOff"><g:message code="districtProposal.callToAction.${districtProposal.participatoryBudget.status}.button.off"/></g:set>--}%
%{--<g:set var="btnStatusTextOffHover"><g:message code="districtProposal.callToAction.${districtProposal.participatoryBudget.status}.button.off.hover"/></g:set>--}%

%{--<g:set var="callButtonMsgOff"><span class="fal fa-rocket"></span> <g:message code="districtProposal.callToAction.${districtProposal.participatoryBudget.status}.button.off"/></g:set>--}%
%{--<g:set var="callButtonMsgOffHover"><span class="fal fa-rocket"></span> <g:message code="districtProposal.callToAction.${districtProposal.participatoryBudget.status}.button.off.hover"/></g:set>--}%
%{--<g:set var="callProgressBassShow" value="${true}"/>--}%

%{--<g:if test="${districtProposal.participatoryBudget.status == org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.ADDING_PROPOSALS}">--}%
    %{--<g:set var="callTitleMsg" value="${g.message(code:"districtProposal.callToAction.${districtProposal.participatoryBudget.status}.title", args: [campaignUser.name])}"/>--}%
    %{--<g:set var="callSubtitleMsg" value="${g.message(code:"districtProposal.callToAction.${districtProposal.participatoryBudget.status}.subtitle", args: [campaignUser.name])}"/>--}%
    %{--<g:set var="callButtonActive" value="${districtProposal.isSupported}"/>--}%
    %{--<g:set var="callButtonMsgIcon"><span class="fal fa-rocket"></span></g:set>--}%
    %{--<g:set var="callButtonShow" value="${districtProposal.published}"/>--}%
    %{--<g:set var="callButtonAction" value="${g.createLink(mapping:'participatoryBudgetDistrictProposalSupport', params:districtProposal.encodeAsLinkProperties())}"/>--}%
    %{--<g:set var="callButtonActionClass" value="districtProposal-support"/>--}%
%{--</g:if>--}%
%{--<g:elseif test="${districtProposal.participatoryBudget.status == org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.TECHNICAL_REVIEW}">--}%
    %{--<g:set var="callTitleMsg" value="${g.message(code:"districtProposal.callToAction.${districtProposal.participatoryBudget.status}.title", args: [campaignUser.name])}"/>--}%
    %{--<g:set var="callSubtitleMsg" value="${g.message(code:"districtProposal.callToAction.${districtProposal.participatoryBudget.status}.subtitle", args: [g.formatDate(date: districtProposal.participatoryBudget.deadLineTechnicalReview, style: 'SHORT', type: 'date')])}"/>--}%
    %{--<g:set var="callButtonShow" value="${false}"/>--}%
    %{--<g:set var="callButtonActive" value="${false}"/>--}%
    %{--<g:set var="callButtonActionClass" value=""/>--}%
%{--</g:elseif>--}%
%{--<g:elseif test="${districtProposal.participatoryBudget.status == org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.BALLOT}">--}%
    %{--<g:set var="callTitleMsg" value="${g.message(code:"districtProposal.callToAction.${districtProposal.participatoryBudget.status}.title", args: [campaignUser.name])}"/>--}%
    %{--<g:set var="callSubtitleMsg" value="${g.message(code:"districtProposal.callToAction.${districtProposal.participatoryBudget.status}.subtitle", args: [campaignUser.name])}"/>--}%
    %{--<g:set var="callButtonActive" value="${districtProposal.isVoted}"/>--}%
    %{--<g:set var="callButtonMsgIcon"><span class="fal fa-shopping-cart"></span></g:set>--}%
    %{--<g:set var="callButtonShow" value="${districtProposal.published}"/>--}%
    %{--<g:set var="callButtonAction" value="${g.createLink(mapping:'participatoryBudgetDistrictProposalVote', params:districtProposal.encodeAsLinkProperties())}"/>--}%
    %{--<g:set var="callButtonActionClass" value="districtProposal-vote"/>--}%
%{--</g:elseif>--}%
%{--<g:elseif test="${districtProposal.participatoryBudget.status == org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.CLOSED}">--}%
    %{--<g:set var="callTitleMsg" value="${g.message(code:"districtProposal.callToAction.${districtProposal.participatoryBudget.status}.title", args: [campaignUser.name])}"/>--}%
    %{--<g:set var="callSubtitleMsg" value="${g.message(code:"districtProposal.callToAction.${districtProposal.participatoryBudget.status}.subtitle", args: [campaignUser.name])}"/>--}%
    %{--<g:set var="callButtonActive" value="${false}"/>--}%
    %{--<g:set var="callButtonShow" value="${false}"/>--}%
    %{--<g:set var="callButtonActionClass" value=""/>--}%
%{--</g:elseif>--}%
%{--<g:elseif test="${districtProposal.participatoryBudget.status == org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.RESULTS}">--}%
    %{--<g:set var="callTitleMsg" value="${g.message(code:"districtProposal.callToAction.${districtProposal.participatoryBudget.status}.title", args: [campaignUser.name])}"/>--}%
    %{--<g:set var="callSubtitleMsg" value="${g.message(code:"districtProposal.callToAction.${districtProposal.participatoryBudget.status}.subtitle", args: [campaignUser.name])}"/>--}%
    %{--<g:set var="callButtonActive" value="${false}"/>--}%
    %{--<g:set var="callButtonShow" value="${false}"/>--}%
    %{--<g:set var="callButtonActionClass" value=""/>--}%
%{--</g:elseif>--}%

%{--<g:if test="${![org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.TECHNICAL_REVIEW, org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.ADDING_PROPOSALS].contains(districtProposal.participatoryBudget.status) && districtProposal.technicalReviewStatus == org.kuorum.rest.model.communication.participatoryBudget.TechnicalReviewStatusRDTO.NO_VALID}">--}%
    %{--PROPOSAL NO VALID--}%
    %{--<g:set var="callTitleMsg" value="${g.message(code:"districtProposal.callToAction.TECHNICAL_REVIEW.NO_VALID.title", args: [campaignUser.name])}"/>--}%
    %{--<g:set var="callSubtitleMsg" value="${g.message(code:"districtProposal.callToAction.TECHNICAL_REVIEW.NO_VALID.subtitle", args: [campaignUser.name])}"/>--}%
    %{--<g:set var="callButtonActive" value="${false}"/>--}%
    %{--<g:set var="callButtonShow" value="${false}"/>--}%
    %{--<g:set var="callProgressBassShow" value="${false}"/>--}%

%{--</g:if>--}%

%{--<div class="comment-box call-to-action call-to-action-add-proposal hidden-sm hidden-xs">--}%
    %{--<div class="comment-header">--}%
        %{--<span class="call-title">${callTitleMsg}</span>--}%
        %{--<span class="call-subTitle">${callSubtitleMsg}</span>--}%
    %{--</div>--}%
    %{--<div class="actions clearfix">--}%
        %{--<g:if test="${callButtonShow}">--}%
            %{--<a--}%
                    %{--href="${callButtonAction}"--}%
                    %{--type="button"--}%
                    %{--class="btn btn-blue-status btn-lg ${callButtonActive?'on':''} ${callButtonActionClass}"--}%
                    %{--data-campaignValidationActive="${districtProposal.checkValidationActive}"--}%
                    %{--data-districtId="${districtProposal.district.id}"--}%
                    %{--data-participatoryBudgetId="${districtProposal.participatoryBudget.id}"--}%
                    %{--data-proposalId="${districtProposal.id}"--}%
                    %{--data-loggedUser="${sec.username()}"--}%
                    %{--data-txt-on="${btnStatusTextOn}"--}%
                    %{--data-txt-on-hover="${btnStatusTextOnHover}"--}%
                    %{--data-txt-off="${btnStatusTextOff}"--}%
                    %{--data-txt-off-hover="${btnStatusTextOffHover}"--}%
            %{-->--}%
                %{--${raw(callButtonMsgIcon)}--}%
            %{--</a>--}%
        %{--</g:if>--}%
        %{--<g:else>--}%
            %{--<hr/>--}%
        %{--</g:else>--}%
    %{--</div>--}%
    %{--<g:if test="${callProgressBassShow}">--}%
        %{--<div class="call-district-proposal-info-budget">--}%
            %{--<g:if test="${districtProposal.price}">--}%
                %{--<span class="call-title-price"><g:message code="kuorum.multidomain.money" args="[districtProposal.price.encodeAsReducedPrice()]"/></span>--}%
            %{--</g:if>--}%
            %{--<span class="call-subTitle"><g:message code="districtProposal.callToAction.district.info.budget"/> <g:link mapping="participatoryBudgetShow" params="${districtProposal.participatoryBudget.encodeAsLinkProperties()}" fragment="${districtProposal.district.name}">${districtProposal.district.name}</g:link></span>--}%
            %{--<div class="budget">--}%
                %{--<div class="campaign-progress-bar-wrapper">--}%
                    %{--<h4><g:message code="districtProposal.callToAction.district.info.progressBar"/>: <span class="budget-price">${districtProposal.district.budget.encodeAsReducedPrice()} €</span></h4>--}%
                    %{--<div class="campaign-progress-bar" data-width="${Math.round(districtProposal.district.amountUserInvested / districtProposal.district.budget * 100)}">--}%
                        %{--<div class="pop-up">--}%
                            %{--<span class="amount-user-invested">${districtProposal.district.amountUserInvested}</span> gastado de ${districtProposal.district.budget.encodeAsReducedPrice()} €--}%
                            %{--<div class="arrow"></div>--}%
                        %{--</div>--}%
                        %{--<div class="progress-bar-custom">--}%
                            %{--<div class="progress-bar-custom-done"></div>--}%
                        %{--</div>--}%
                    %{--</div>--}%
                    %{--<div class="campaign-progress-bar-footer">--}%
                    %{--<g:link mapping="participatoryBudgetShow" params="${districtProposal.participatoryBudget.encodeAsLinkProperties()}" fragment="${districtProposal.district.name}">--}%
                    %{--see to the full budget--}%
                    %{--</g:link>--}%
                    %{--</div>--}%
                %{--</div>--}%
            %{--</div>--}%
        %{--</div>--}%
    %{--</g:if>--}%
    %{--<g:else>--}%
        %{--<div class="call-district-proposal-info-budget rejected">--}%
            %{--<span class="call-title">Motivo</span>--}%
            %{--<span class="call-subTitle">"${districtProposal.rejectComment}"</span>--}%
        %{--</div>--}%
    %{--</g:else>--}%
%{--</div>--}%