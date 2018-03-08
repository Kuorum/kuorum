
<li class="col-sm-12 col-md-6 search-article">
    <article role="article" class="box-ppal clearfix">
        <div class="link-wrapper" id="campaign-${campaign.id}" data-datepublished="${campaign.dateCreated.time}">
            <g:link mapping="campaignShow" params="${campaign.encodeAsLinkProperties()}" class="hidden"></g:link>
            <g:if test="${campaign.urlImage || campaign.urlVideo}">
                <g:set var="campaignMultimedia" value="${false}"/>
                <div class="card-header-photo">
                <g:if test="${campaign.urlImage}">
                    <g:set var="campaignMultimedia" value="${true}"/>
                    <img src="${campaign.urlImage}" alt="${campaign.name}">
                </g:if>
                <g:elseif test="${campaign.urlVideo}">
                    <g:set var="campaignMultimedia" value="${true}"/>
                    <image:showYoutube youtube="${campaign.urlVideo}"/>
                </g:elseif>
                    </div>
            </g:if>
                <div class="card-body">
                    <h1>
                        <g:link mapping="campaignShow" class="link-wrapper-clickable" params="${campaign.encodeAsLinkProperties()}">
                            ${campaign.name}
                        </g:link>
                    </h1>
                <g:if test="${!campaignMultimedia}">
                    <div class="card-text"><modulesUtil:shortText text="${campaign.text}"/></div>
                </g:if>
            </div>
            <div class="card-footer">
                <ul>
                    <g:if test="${showAuthor}">
                        <li class="owner">
                            <userUtil:showUser
                                    user="${campaign.alias}"
                                    showName="true"
                                    showActions="false"
                                    showDeleteRecommendation="false"
                                    htmlWrapper="div"
                            />
                        </li>
                    </g:if>

                    <li>attrs.user
                        <g:if test="${campaign.type== org.kuorum.rest.model.search.SearchTypeRSDTO.SURVEY}">
                            <g:link mapping="campaignShow" params="${campaign.encodeAsLinkProperties()}" fragment="survey-progress" role="button" class="${campaign.completed?'active':''}">
                                <span class="fa fa fa-pie-chart fa-lg"></span>
                                <span class="number">${campaign.amountAnswers}</span>
                            </g:link>
                        </g:if>
                        <g:elseif test="${campaign.type== org.kuorum.rest.model.search.SearchTypeRSDTO.DEBATE}">
                            <g:link mapping="campaignShow" params="${campaign.encodeAsLinkProperties()}" fragment="openProposal" role="button">
                                <span class="fa fa fa-lightbulb-o fa-lg"></span>
                                <span class="number">${campaign.numProposals}</span>
                            </g:link>
                        </g:elseif>
                        <g:elseif test="${campaign.type== org.kuorum.rest.model.search.SearchTypeRSDTO.POST}">
                            <a class="post-like ${campaign.liked?'active':''}"
                               data-postId="${campaign.id}"
                               data-userAlias="${campaign.alias}"
                               data-urlAction="${g.createLink(mapping: "postLike")}"
                               data-loggedUser="${sec.username()}">
                                <span class="fa ${campaign.liked?'fa-heart':'fa-heart-o'} fa-lg"></span>
                                <span class="number">${campaign.likes}</span>
                            </a>
                        </g:elseif>
                        <g:elseif test="${campaign.type== org.kuorum.rest.model.search.SearchTypeRSDTO.EVENT}">
                            <g:link mapping="campaignShow" params="${campaign.encodeAsLinkProperties()}" role="button" class="${campaign.registered?'active':''}">
                                <span class="fa fa fa-ticket fa-lg"></span>
                                <span class="number">${campaign.amountAssistants}</span>
                            </g:link>
                        </g:elseif>
                    </li>
                </ul>
            </div>
        </div>
    </article>
</li>