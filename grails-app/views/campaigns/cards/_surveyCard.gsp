<article role="article" class="box-ppal clearfix ${highlighted ? 'highlighted' : ''}">
    <div class="link-wrapper" id="survey-${survey.id}" data-datepublished="${survey.datePublished.time}">
        <g:set var="campaignLink"
               value="${g.createLink(mapping: 'surveyShow', params: survey.encodeAsLinkProperties())}"/>
        <a href="${campaignLink}" class="hidden"></a>
        <g:render template="/campaigns/cards/campaignMultimediaCard" model="[campaign: survey]"/>
        <g:render template="/campaigns/cards/campaignBodyCard" model="[campaign: survey, campaignLink: campaignLink]"/>
        <div class="card-footer">
            <ul>
                <g:if test="${showAuthor}">
                    <li class="owner">
                        <userUtil:showUser
                                user="${survey.user}"
                                showName="true"
                                showActions="false"
                                showDeleteRecommendation="false"
                                htmlWrapper="div"
                        />
                    </li>
                </g:if>

                <li>
                    <g:link mapping="surveyShow" params="${survey.encodeAsLinkProperties()}" fragment="survey-progress" role="button" class="${survey.completed?'active':''}">
                        <span class="fal fa-chart-pie fa-lg"></span>
                        <span class="number">${survey.amountAnswers}</span>
                    </g:link>
                </li>
            </ul>
        </div>
    </div>
</article>
