<%@ page import="org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO" %>

<article role="article" class="box-ppal clearfix ${highlighted?'highlighted':''}">
    <div class="link-wrapper" id="districtProposal-${districtProposal.id}" data-datepublished="${districtProposal.datePublished.time}">
        <g:link mapping="districtProposalShow" params="${districtProposal.encodeAsLinkProperties()}" class="hidden"></g:link>
            <div class="card-header-photo">
                <g:if test="${districtProposal.photoUrl}">
                    <img src="${districtProposal.photoUrl}" alt="${districtProposal.title}">
                </g:if>
                <g:elseif test="${districtProposal.videoUrl}">
                    <image:showYoutube youtube="${districtProposal.videoUrl}" campaign="${districtProposal}"/>
                </g:elseif>
                <g:else>
                    <div class="imagen-shadowed-main-color-domain">
                        <img class="empty" src="${g.resource(dir: "images", file: "emptyCampaign.png")}" alt="${districtProposal.title}"/>
                    </div>
                </g:else>
            </div>
        <div class="card-body">
            <h1>
                <g:link mapping="districtProposalShow" class="link-wrapper-clickable" params="${districtProposal.encodeAsLinkProperties()}">
                    ${districtProposal.title}
                </g:link>
            </h1>
            %{--<g:if test="${!surveyMultimedia}">--}%
                %{--<div class="card-text"><modulesUtil:shortText text="${survey.body}"/></div>--}%
            %{--</g:if>--}%
        </div>
        <g:render template="/campaigns/cards/districtProposalCardFooter" model="[districtProposal: districtProposal]"/>
    </div>
</article>
