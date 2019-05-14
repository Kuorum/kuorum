<g:if test="${campaignFiles}">
    <div class="comment-box call-to-action">
        <div class="comment-header">
            <span class="call-title"><g:message code="campaign.show.files.title"/></span>
            <span class="call-subTitle"><g:message code="campaign.show.files.subtitle"/></span>
        </div>
        <div class="comment-proposal clearfix">
            <ul class="list-campaign-files">
                <g:each in="${campaignFiles}" var="file">
                    <li><a href="${file.url}" target="_blank"><span class="${file.icon}"></span>${file.name}</a></li>
                </g:each>
            </ul>
        </div>
    </div>
</g:if>