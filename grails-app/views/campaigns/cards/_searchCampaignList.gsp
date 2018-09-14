<%@ page import="org.kuorum.rest.model.search.kuorumElement.SearchDistrictProposalRSDTO" %>

<article role="article" class="box-ppal clearfix">
    <div class="link-wrapper" id="campaign-${campaign.id}" data-datepublished="${campaign.dateCreated.time}">
        <g:link mapping="campaignShow" params="${campaign.encodeAsLinkProperties()}" class="hidden"></g:link>
        %{--<g:if test="${campaign.urlImage || campaign.urlVideo}">--}%
            <div class="card-header-photo">
                <g:if test="${campaign.urlImage}">
                    <img src="${campaign.urlImage}" alt="${campaign.name}">
                </g:if>
                <g:elseif test="${campaign.urlVideo}">
                    <image:showYoutube youtube="${campaign.urlVideo}"/>
                </g:elseif>
                <g:else>
                    <div class="multimedia-campaign-default">
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
                ].contains(campaign.participatoryBudget.status) && campaign.price}">
                    <li class="districtProposalPrice"><g:message code="kuorum.multidomain.money" args="[campaign.price]"/></li>
                    <g:render template="/districtProposal/showModules/mainContent/districtProposalModalErrors" model="[district:campaign.district]"/>
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

                <li>
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
                           data-campaignValidationActive="${campaign.checkValidation}"
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
                        <g:link mapping="${org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.ADDING_PROPOSALS.equals(campaign.status)?'districtProposalCreate':'campaignShow'}" params="${campaign.encodeAsLinkProperties()}" role="button">
                            <span class="fal fa-money-bill-alt fa-lg"></span>
                            <span class="number">${campaign.basicStats.numProposals}</span>
                        </g:link>
                    </g:elseif>
                    <g:elseif test="${campaign.type== org.kuorum.rest.model.search.SearchTypeRSDTO.DISTRICT_PROPOSAL}">
                        <g:render template="/districtProposal/showModules/districtProposalDataIcon/districtProposalDataIcon_${campaign.participatoryBudget.status}" model="[districtProposal:campaign]"/>
                    </g:elseif>
                </li>
            </ul>
        </div>
    </div>
</article>