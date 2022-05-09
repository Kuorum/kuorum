<g:if test="${provinceName || user.socialLinks?.twitter || user.socialLinks?.facebook || user.socialLinks?.googlePlus || user.socialLinks?.blog}">
    <ul class="box-ppal socialContact">
        <g:if test="${provinceName}">
            <li><span class="fas fa-map-marker-alt fa-lg"></span> ${provinceName}</li>
        </g:if>
        <g:if test="${user.socialLinks?.twitter}">
            <li><span class="fab fa-twitter fa-lg"></span> <a href="${user.socialLinks.twitter}" target="_blank" rel="nofollow noopener noreferrer"><g:message code="kuorum.web.commands.profile.SocialNetworkCommand.twitter.label"/></a></li>
        </g:if>
        <g:if test="${user.socialLinks?.facebook}">
            <li><span class="fab fa-facebook fa-lg"></span> <a href="${user.socialLinks.facebook}" target="_blank" rel="nofollow noopener noreferrer"><g:message code="kuorum.web.commands.profile.SocialNetworkCommand.facebook.label"/></a></li>
        </g:if>
        <g:if test="${user.socialLinks?.blog}">
            <li><span class="fal fa-rss-square fa-lg"></span> <a href="${user.socialLinks?.blog}" target="_blank" rel="nofollow noopener noreferrer"><g:message code="kuorum.web.commands.profile.SocialNetworkCommand.blog.label"/></a></li>
        </g:if>
    </ul>
</g:if>