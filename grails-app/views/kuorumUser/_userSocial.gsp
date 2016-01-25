<g:if test="${provinceName || user.socialLinks?.twitter || user.socialLinks?.facebook || user.socialLinks?.googlePlus || user.socialLinks?.blog}">
    <ul class="box-ppal socialContact">
        <g:if test="${provinceName}">
            <li><span class="fa fa-map-marker fa-lg"></span> ${provinceName}</li>
        </g:if>
        <g:if test="${user.socialLinks?.twitter}">
            <li><span class="fa fa-twitter fa-lg"></span> <a href="${user.socialLinks.twitter}" target="_blank" rel="nofollow"><g:message code="kuorum.web.commands.profile.SocialNetworkCommand.twitter.label"/></a></li>
        </g:if>
        <g:if test="${user.socialLinks?.facebook}">
            <li><span class="fa fa-facebook fa-lg"></span> <a href="${user.socialLinks.facebook}" target="_blank" rel="nofollow"><g:message code="kuorum.web.commands.profile.SocialNetworkCommand.facebook.label"/></a></li>
        </g:if>
        <g:if test="${user.socialLinks?.googlePlus}">
            <li><span class="fa fa-google-plus fa-lg"></span> <a href="${user.socialLinks?.googlePlus}" target="_blank" rel="nofollow"><g:message code="kuorum.web.commands.profile.SocialNetworkCommand.googlePlus.label"/></a></li>
        </g:if>
        <g:if test="${user.socialLinks?.blog}">
            <li><span class="fa fa-rss-square fa-lg"></span> <a href="${user.socialLinks?.blog}" target="_blank" rel="nofollow"><g:message code="kuorum.web.commands.profile.SocialNetworkCommand.blog.label"/></a></li>
        </g:if>
    </ul>
</g:if>