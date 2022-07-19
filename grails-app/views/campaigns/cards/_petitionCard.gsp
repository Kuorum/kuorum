<article role="article" class="box-ppal clearfix ${highlighted ? 'highlighted' : ''}">
    <div class="link-wrapper" id="petition-${petition.id}" data-datepublished="${petition.datePublished.time}">
        <g:set var="campaignLink"
               value="${g.createLink(mapping: 'petitionShow', params: petition.encodeAsLinkProperties())}"/>
        <a href="${campaignLink}" class="hidden"></a>
        <g:render template="/campaigns/cards/campaignMultimediaCard" model="[campaign: petition]"/>
        <g:render template="/campaigns/cards/campaignBodyCard"
                  model="[campaign: petition, campaignLink: campaignLink]"/>
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
