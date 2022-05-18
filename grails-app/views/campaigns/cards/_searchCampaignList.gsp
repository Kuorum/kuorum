<%@ page import="org.kuorum.rest.model.search.kuorumElement.SearchDistrictProposalRSDTO" %>

<article role="article" class="box-ppal ${highlighted?'highlighted':''}">
    <div class="link-wrapper" id="campaign-${campaign.id}" data-datepublished="${campaign.dateCreated.time}">
        <g:link mapping="campaignShow" params="${campaign.encodeAsLinkProperties()}" class="hidden"></g:link>
        %{--<g:if test="${campaign.urlImage || campaign.urlVideo}">--}%
            <div class="card-header-photo">
                <g:if test="${campaign.urlImage}">
                    <img src="${campaign.urlImage}" alt="${campaign.name}">
                </g:if>
                <g:elseif test="${campaign.urlVideo}">
                    <image:showYoutube youtube="${campaign.urlVideo}" campaign="${campaign}"/>
                </g:elseif>
                <g:else>
                    <div class="imagen-shadowed-main-color-domain">
                        <img class="empty" src="${g.resource(dir: "images", file: "emptyCampaign.png")}" alt="${campaign.name}"/>
                    </div>
                </g:else>
            </div>
        %{--</g:if>--}%
            <div class="card-body">
                <h1>
                    <g:link mapping="campaignShow" class="link-wrapper-clickable" params="${campaign.encodeAsLinkProperties()}">
                        <searchUtil:highlightedField searchElement="${campaign}" field="name"/>
                    </g:link>
                </h1>
        </div>
        <div class="card-footer">
            <ul>
                <g:if test="${
                    (campaign instanceof org.kuorum.rest.model.search.kuorumElement.SearchDistrictProposalRSDTO) &&
                            [
                            org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.BALLOT,
                            org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.CLOSED,
                            org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.RESULTS,
                ].contains(campaign.participatoryBudget.status) &&
                            campaign.technicalReviewStatus == org.kuorum.rest.model.communication.participatoryBudget.TechnicalReviewStatusRDTO.VALID &&
                            campaign.participatoryBudget.participatoryBudgetType == org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetTypeDTO.BUDGET}">
                    <li class="districtProposalPrice"><g:message code="kuorum.multidomain.money" args="[campaign.price]"/></li>
                </g:if>
                <g:elseif test="${showAuthor}">
                    <li class="owner">
                        <userUtil:showUser
                                user="${campaign}"
                                showName="true"
                                showActions="false"
                                showDeleteRecommendation="false"
                                htmlWrapper="div"
                        />
                    </li>
                </g:elseif>

                <li class="${campaign.type== org.kuorum.rest.model.search.SearchTypeRSDTO.DISTRICT_PROPOSAL && campaign.technicalReviewStatus == org.kuorum.rest.model.communication.participatoryBudget.TechnicalReviewStatusRDTO.VALID && campaign.participatoryBudget.status==org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.BALLOT?'comment-counter-as-button':''}">
                    <g:if test="${campaign.type== org.kuorum.rest.model.search.SearchTypeRSDTO.SURVEY}">
                        <g:link mapping="campaignShow" params="${campaign.encodeAsLinkProperties()}" fragment="survey-progress" role="button" class="${campaign.completed?'active':''}">
                            <span class="fal fa-chart-pie fa-lg"></span>
                            <span class="number">${campaign.amountAnswers}</span>
                        </g:link>
                    </g:if>
                    <g:elseif test="${campaign.type== org.kuorum.rest.model.search.SearchTypeRSDTO.DEBATE}">
                        <g:link mapping="campaignShow" params="${campaign.encodeAsLinkProperties()}" fragment="openProposal" role="button">
                            <span class="fal fa-lightbulb fa-lg"></span>
                            <span class="number">${campaign.numProposals}</span>
                        </g:link>
                    </g:elseif>
                    <g:elseif test="${campaign.type== org.kuorum.rest.model.search.SearchTypeRSDTO.POST}">
                        <a class="post-like ${campaign.liked?'active':''}"
                           data-postId="${campaign.id}"
                           data-postUserId="${campaign.ownerId}"
                           data-urlAction="${g.createLink(mapping: "postLike")}"
                           data-campaignValidationActive="${campaign.checkValidationActive}"
                           data-campaignGroupValidationActive="${campaign.groupValidation?g.createLink(mapping: "campaignCheckGroupValidation", params: campaign.encodeAsLinkProperties()):''}"
                           data-campaignId="${campaign.id}"
                           data-loggedUser="${sec.username()}">
                            <span class="${campaign.liked?'fas':'fal'} fa-heart fa-lg"></span>
                            <span class="number">${campaign.likes}</span>
                        </a>
                    </g:elseif>
                    <g:elseif test="${campaign.type== org.kuorum.rest.model.search.SearchTypeRSDTO.EVENT}">
                        <g:link mapping="campaignShow" params="${campaign.encodeAsLinkProperties()}" role="button" class="${campaign.registered?'active':''}">
                            <span class="fal fa-ticket-alt fa-lg"></span>
                            <span class="number">${campaign.amountAssistants}</span>
                        </g:link>
                    </g:elseif>
                    <g:elseif test="${campaign.type== org.kuorum.rest.model.search.SearchTypeRSDTO.PARTICIPATORY_BUDGET}">
                        <g:render template="/participatoryBudget/showModules/participatoryBudgetDataIcon" model="[participatoryBudget:campaign]"/>
                    </g:elseif>
                    <g:elseif test="${campaign.type== org.kuorum.rest.model.search.SearchTypeRSDTO.DISTRICT_PROPOSAL}">
                        <g:render template="/districtProposal/showModules/districtProposalDataIcon/districtProposalDataIcon_${campaign.participatoryBudget.status}" model="[districtProposal:campaign]"/>
                    </g:elseif>
                    <g:elseif test="${campaign.type== org.kuorum.rest.model.search.SearchTypeRSDTO.PETITION}">
                        <g:render template="/petition/showModules/mainContent/petitionDataIcon" model="[petition:campaign]"/>
                    </g:elseif>
                </li>
            </ul>
        </div>
    </div>
    <g:if test="${campaign instanceof org.kuorum.rest.model.search.kuorumElement.SearchDistrictProposalRSDTO}">
        <g:render template="/districtProposal/showModules/mainContent/districtProposalModalErrors" model="[district:campaign.district]"/>
    </g:if>
</article>