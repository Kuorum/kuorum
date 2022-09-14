<article role="article" class="box-ppal clearfix ${highlighted ? 'highlighted' : ''}">
    <div class="link-wrapper" id="contestApplication-${contestApplication.id}"
         data-datepublished="${contestApplication.datePublished.time}">
        <g:set var="campaignLink"
               value="${g.createLink(mapping: 'contestApplicationShow', params: contestApplication.encodeAsLinkProperties())}"/>
        <a href="${campaignLink}" class="hidden"></a>
        <g:render template="/campaigns/cards/campaignMultimediaCard" model="[campaign: contestApplication]"/>
        <g:render template="/campaigns/cards/campaignBodyCard"
                  model="[campaign: contestApplication, campaignLink: campaignLink]"/>

        <div class="card-footer">
            <ul>
                <g:if test="${showAuthor}">
                    <li class="owner">
                        <userUtil:showUser
                                user="${contestApplication.user}"
                                showName="true"
                                showActions="false"
                                showDeleteRecommendation="false"
                                htmlWrapper="div"/>
                    </li>
                </g:if>

                <li>
                    <g:render template="/contestApplication/showModules/mainContent/contestApplicationDataIconButton"
                              model="[contestApplication: contestApplication]"/>
                </li>
            </ul>
        </div>
    </div>
</article>
