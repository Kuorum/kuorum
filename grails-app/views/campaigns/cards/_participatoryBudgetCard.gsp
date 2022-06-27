<article role="article" class="box-ppal clearfix ${highlighted ? 'highlighted' : ''}">
    <div class="link-wrapper" id="participatoryBudget-${participatoryBudget.id}"
         data-datepublished="${participatoryBudget.datePublished.time}">
        <g:set var="campaignLink"
               value="${g.createLink(mapping: 'participatoryBudgetShow', params: participatoryBudget.encodeAsLinkProperties())}"/>
        <a href="${campaignLink}" class="hidden"></a>
        <g:render template="/campaigns/cards/campaignMultimediaCard" model="[campaign: participatoryBudget]"/>
        <g:render template="/campaigns/cards/campaignBodyCard"
                  model="[campaign: participatoryBudget, campaignLink: campaignLink]"/>
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
