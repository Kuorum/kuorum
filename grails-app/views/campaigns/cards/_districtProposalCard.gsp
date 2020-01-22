<%@ page import="org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO" %>

<article role="article" class="box-ppal clearfix">
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
        <div class="card-footer">
            <ul>
                <g:if test="${[
                        org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.BALLOT,
                        org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.CLOSED,
                        org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.RESULTS,
                ].contains(districtProposal.participatoryBudget.status) && districtProposal.price}">
                    <li class="districtProposalPrice"><g:message code="kuorum.multidomain.money" args="[districtProposal.price]"/></li>
                </g:if>
                <g:elseif test="${showAuthor}">
                    <li class="owner">
                        <userUtil:showUser
                                user="${districtProposal.user}"
                                showName="true"
                                showActions="false"
                                showDeleteRecommendation="false"
                                htmlWrapper="div"
                        />
                    </li>
                </g:elseif>

                <li class="${districtProposal.technicalReviewStatus == org.kuorum.rest.model.communication.participatoryBudget.TechnicalReviewStatusRDTO.VALID && districtProposal.participatoryBudget.status==org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.BALLOT?'comment-counter-as-button':''}">
                    <g:render template="/districtProposal/showModules/districtProposalDataIcon" model="[districtProposal:districtProposal]"/>
                </li>
            </ul>
        </div>
    </div>
</article>
