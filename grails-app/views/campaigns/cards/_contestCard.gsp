<article role="article" class="box-ppal clearfix ${highlighted ? 'highlighted' : ''}">
    <div class="link-wrapper" id="contest-${contest.id}" data-datepublished="${contest.datePublished.time}">
        <g:set var="campaignLink" value="${g.link(mapping: 'contestShow', params: contest.encodeAsLinkProperties())}"/>
        <a href="${campaignLink}" class="hidden"></a>
        <g:render template="/campaigns/cards/campaignMultimediaCard" model="[campaign: contest]"/>
        <g:render template="/campaigns/cards/campaignBodyCard" model="[campaign: contest, campaignLink: campaignLink]"/>
        <div class="card-footer">
            <ul>
                <g:if test="${showAuthor}">
                    <li class="owner">
                        <userUtil:showUser
                                user="${contest.user}"
                                showName="true"
                                showActions="false"
                                showDeleteRecommendation="false"
                                htmlWrapper="div"/>
                    </li>
                </g:if>

                <li>
                    <g:link mapping="campaignShow" params="${contest.encodeAsLinkProperties()}"
                            fragment="participatory-budget-district-proposals-list-tab" role="button">
                        <span class="fal fa-trophy" aria-hidden="true"></span>
                        <span class="number">${contest.numApplications}</span>
                    </g:link>
                </li>
            </ul>
        </div>
    </div>
</article>
