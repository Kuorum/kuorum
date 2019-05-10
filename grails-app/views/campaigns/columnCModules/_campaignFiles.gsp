<g:if test="${campaignFiles}">
    <div class="comment-box call-to-action">
        <div class="comment-header">
            %{--<span class="call-title">Ficheros asociados</span>--}%
            <span class="call-subTitle"><g:message code="campaign.show.files.title"/></span>
        </div>
        <div class="comment-proposal clearfix">
            <ul class="list-campaign-files">
                <g:each in="${campaignFiles}" var="file">
                    <li><span class="${file.icon}"></span><a href="${file.url}">${file.name}</a></li>
                </g:each>
            </ul>
        </div>
    </div>
</g:if>