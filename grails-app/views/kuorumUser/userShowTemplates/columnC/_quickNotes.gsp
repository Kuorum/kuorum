<g:if test="${
    ['officialWebSite'].find{politician?.socialLinks?."${it}"} ||
    ['twitter', 'facebook', 'linkedIn', 'blog', 'instagram', 'youtube'].find{politician?.socialLinks?."${it}"}}">

    <section class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title">
                <g:message code="default.moreInfo"/>
            </h3>
        </div>
        <div class="panel-body text-center">
            <g:if test="${['officialWebSite'].find{politician?.socialLinks?."${it}"}}">
                <div class="table table-condensed limit-height" data-collapsedHeight="60">
                    <g:render template="/kuorumUser/userShowTemplates/columnC/rowPoliticianColumnC" model="[
                            message:g.message(code:'politician.quickNotes.data.moreInfo.webSite'),
                            link:politician?.socialLinks?.officialWebSite?:''
                    ]"/>
                </div>
            </g:if>
            <g:if test="${['twitter', 'facebook', 'linkedIn', 'blog', 'instagram', 'youtube'].find{politician?.socialLinks?."${it}"}}">
                <div class="table table-condensed no-margins">
                    <ul class="panel-share-buttons">

                        <g:if test="${politician.socialLinks?.twitter}">
                            <li><a href="${politician.socialLinks.twitter}" target="_blank" rel="nofollow noopener noreferrer" title="${message(code:"kuorum.web.commands.profile.SocialNetworkCommand.twitter.label")}"><i class="fab fa-twitter-square fa-2x"></i></a></li>
                        </g:if>
                        <g:if test="${politician.socialLinks?.facebook}">
                            <li><a href="${politician.socialLinks.facebook}" target="_blank" rel="nofollow noopener noreferrer" title="${message(code:"kuorum.web.commands.profile.SocialNetworkCommand.facebook.label")}"><i class="fab fa-facebook fa-2x"></i></a></li>
                        </g:if>
                        <g:if test="${politician.socialLinks?.linkedIn}">
                            <li><a href="${politician.socialLinks?.linkedIn}" target="_blank" rel="nofollow noopener noreferrer" title="${message(code:"kuorum.web.commands.profile.SocialNetworkCommand.linkedIn.label")}"><i class="fab fa-linkedin fa-2x"></i></a></li>
                        </g:if>
                        <g:if test="${politician.socialLinks?.blog}">
                            <li><a href="${politician.socialLinks?.blog}" target="_blank" rel="nofollow noopener noreferrer" title="${message(code:"kuorum.web.commands.profile.SocialNetworkCommand.blog.label")}"><i class="fal fa-rss-square fa-2x"></i></a></li>
                        </g:if>
                        <g:if test="${politician.socialLinks?.instagram}">
                            <li><a href="${politician.socialLinks?.instagram}" target="_blank" rel="nofollow noopener noreferrer" title="${message(code:"kuorum.web.commands.profile.SocialNetworkCommand.instagram.label")}"><i class="fab fa-instagram fa-2x"></i></a></li>
                        </g:if>
                        <g:if test="${politician.socialLinks?.youtube}">
                            <li><a href="${politician.socialLinks?.youtube}" target="_blank" rel="nofollow noopener noreferrer" title="${message(code:"kuorum.web.commands.profile.SocialNetworkCommand.youtube.label")}"><i class="fab fa-youtube-square fa-2x"></i></a></li>
                        </g:if>
                        %{--<li><a href="http://www.reddit.com/submit?url=&title=" target="_blank" title="Submit to Reddit"><i class="fab fa-youtube-square fa-2x"></i></a></li>--}%
                    </ul>
                </div>
            </g:if>
        </div>
    </section>
</g:if>