
<article role="article" class="box-ppal clearfix ${highlighted?'highlighted':''}">
    <div class="link-wrapper" id="participatoryBudget-${participatoryBudget.id}" data-datepublished="${participatoryBudget.datePublished.time}">
        <g:link mapping="campaignShow" params="${participatoryBudget.encodeAsLinkProperties()}" class="hidden"></g:link>
        %{--<g:if test="${survey.photoUrl || survey.videoUrl}">--}%
            <div class="card-header-photo">
                <g:if test="${participatoryBudget.photoUrl}">
                    <img src="${participatoryBudget.photoUrl}" alt="${participatoryBudget.title}">
                </g:if>
                <g:elseif test="${participatoryBudget.videoUrl}">
                    <image:showYoutube youtube="${participatoryBudget.videoUrl}" campaign="${participatoryBudget}"/>
                </g:elseif>
                <g:else>
                    <div class="imagen-shadowed-main-color-domain">
                        <img class="empty" src="${g.resource(dir: "images", file: "emptyCampaign.png")}" alt="${participatoryBudget.title}"/>
                    </div>
                </g:else>
            </div>
        %{--</g:if>--}%
        <div class="card-body">
            <h1>
                <g:link mapping="surveyShow" class="link-wrapper-clickable" params="${participatoryBudget.encodeAsLinkProperties()}">
                    ${participatoryBudget.title}
                </g:link>
            </h1>
            %{--<g:if test="${!surveyMultimedia}">--}%
                %{--<div class="card-text"><modulesUtil:shortText text="${survey.body}"/></div>--}%
            %{--</g:if>--}%
        </div>
        <div class="card-footer">
            <ul>
                <g:if test="${showAuthor}">
                    <li class="owner">
                        <userUtil:showUser
                                user="${participatoryBudget.user}"
                                showName="true"
                                showActions="false"
                                showDeleteRecommendation="false"
                                htmlWrapper="div"
                        />
                    </li>
                </g:if>

                <li>
                    <g:link mapping="campaignShow" params="${participatoryBudget.encodeAsLinkProperties()}" fragment="participatory-budget-district-proposals-list-tab" role="button">
                        <span class="fal fa-money-bill-alt" aria-hidden="true"></span>
                        <span class="number">${participatoryBudget.basicStats.numProposals}</span>
                    </g:link>
                </li>
            </ul>
        </div>
    </div>
</article>
