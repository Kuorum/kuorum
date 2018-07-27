
<article role="article" class="box-ppal clearfix">
    <div class="link-wrapper" id="districtProposal-${districtProposal.id}" data-datepublished="${districtProposal.datePublished.time}">
        <g:link mapping="districtProposalShow" params="${districtProposal.encodeAsLinkProperties()}" class="hidden"></g:link>
        %{--<g:if test="${survey.photoUrl || survey.videoUrl}">--}%
            <div class="card-header-photo">
                <g:if test="${districtProposal.photoUrl}">
                    <img src="${districtProposal.photoUrl}" alt="${districtProposal.title}">
                </g:if>
                <g:elseif test="${districtProposal.videoUrl}">
                    <image:showYoutube youtube="${districtProposal.videoUrl}"/>
                </g:elseif>
                <g:else>
                    <div class="multimedia-campaign-default">
                        <img class="empty" src="${g.resource(dir: "images", file: "emptyCampaign.png")}" alt="${districtProposal.title}"/>
                    </div>
                </g:else>
            </div>
        %{--</g:if>--}%
        <div class="card-body">
            <h1>
                <g:link mapping="surveyShow" class="link-wrapper-clickable" params="${districtProposal.encodeAsLinkProperties()}">
                    ${districtProposal.title}
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
                                user="${districtProposal.user}"
                                showName="true"
                                showActions="false"
                                showDeleteRecommendation="false"
                                htmlWrapper="div"
                        />
                    </li>
                </g:if>

                <li>
                    <g:link mapping="districtProposalShow" params="${districtProposal.encodeAsLinkProperties()}" role="button">
                        <span class="fa fa fa-rocket fa-lg"></span>
                        <span class="number">XXXXX</span>
                    </g:link>
                </li>
            </ul>
        </div>
    </div>
</article>
