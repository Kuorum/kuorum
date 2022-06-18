<article role="article" class="box-ppal clearfix ${highlighted ? 'highlighted' : ''}">
    <div class="link-wrapper" id="debate-${debate.id}" data-datePublished="${debate.datePublished.time}">
        <g:set var="campaignLink"
               value="${g.createLink(mapping: 'debateShow', params: debate.encodeAsLinkProperties())}"/>
        <a href="${campaignLink}" class="hidden"></a>
        <g:render template="/campaigns/cards/campaignMultimediaCard" model="[campaign: debate]"/>
        <g:render template="/campaigns/cards/campaignBodyCard" model="[campaign: debate, campaignLink: campaignLink]"/>
        <div class="card-footer">
            <ul>
                <g:if test="${showAuthor}">
                    <li class="owner">
                        <userUtil:showUser
                                user="${debate.user}"
                                showName="true"
                                showActions="false"
                                showDeleteRecommendation="false"
                                htmlWrapper="div"
                        />
                    </li>
                </g:if>
                <g:if test="${debate.event}">
                    <li>
                        <g:link mapping="debateShow" params="${debate.encodeAsLinkProperties()}" role="button" class="${debate.event.registered?'active':''}">
                            <span class="fal fa-ticket-alt fa-lg"></span>
                            <span class="number">${debate.event.amountAssistants}</span>
                        </g:link>
                    </li>
                </g:if>
                <g:else>
                    <li>
                        <g:link mapping="debateShow" params="${debate.encodeAsLinkProperties()}" fragment="openProposal" role="button">
                            <span class="fal fa-lightbulb fa-lg"></span>
                            <span class="number">${debate.numProposals}</span>
                        </g:link>
                    </li>
                </g:else>
            </ul>
        </div>
    </div>
</article>
