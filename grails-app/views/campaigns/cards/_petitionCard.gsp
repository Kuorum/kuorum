
<article role="article" class="box-ppal clearfix">
    <div class="link-wrapper" id="petition-${petition.id}" data-datepublished="${petition.datePublished.time}">
        <g:link mapping="campaignShow" params="${petition.encodeAsLinkProperties()}" class="hidden"></g:link>
        %{--<g:if test="${survey.photoUrl || survey.videoUrl}">--}%
            <div class="card-header-photo">
                <g:if test="${petition.photoUrl}">
                    <img src="${petition.photoUrl}" alt="${petition.title}">
                </g:if>
                <g:elseif test="${petition.videoUrl}">
                    <image:showYoutube youtube="${petition.videoUrl}"/>
                </g:elseif>
                <g:else>
                    <div class="multimedia-campaign-default">
                        <img class="empty" src="${g.resource(dir: "images", file: "emptyCampaign.png")}" alt="${petition.title}"/>
                    </div>
                </g:else>
            </div>
        %{--</g:if>--}%
        <div class="card-body">
            <h1>
                <g:link mapping="surveyShow" class="link-wrapper-clickable" params="${petition.encodeAsLinkProperties()}">
                    ${petition.title}
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
                                user="${petition.user}"
                                showName="true"
                                showActions="false"
                                showDeleteRecommendation="false"
                                htmlWrapper="div"
                        />
                    </li>
                </g:if>

                <li>
                    <g:render template="/petition/showModules/mainContent/petitionDataIcon" model="[petition:petition]"/>
                </li>
            </ul>
        </div>
    </div>
</article>
