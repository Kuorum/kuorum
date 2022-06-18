<article role="article" class="box-ppal clearfix ${highlighted ? 'highlighted' : ''}">
    <div class="link-wrapper" id="petition-${petition.id}" data-datepublished="${petition.datePublished.time}">
        <g:set var="campaignLink"
               value="${g.createLink(mapping: 'petitionShow', params: debate.encodeAsLinkProperties())}"/>
        <a href="${campaignLink}" class="hidden"></a>
        <g:render template="/campaigns/cards/campaignMultimediaCard" model="[campaign: debate]"/>
        <g:render template="/campaigns/cards/campaignBodyCard" model="[campaign: debate, campaignLink: campaignLink]"/>
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
