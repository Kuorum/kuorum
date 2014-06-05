<ul class="socialContact">
    <g:if test="${provinceName}">
        <li><span class="fa fa-map-marker fa-lg"></span> ${provinceName}</li>
    </g:if>
    <g:if test="${user.socialLinks?.twitter}">
        <li><span class="fa fa-twitter fa-lg"></span> <a href="https://twitter.com/${user.socialLinks.twitter - '@'}" target="_blank" rel="nofollow">${user.socialLinks.twitter}</a></li>
    </g:if>
    <g:if test="${user.socialLinks?.facebook}">
        <li><span class="fa fa-facebook fa-lg"></span> <a href="${user.socialLinks.facebook}" target="_blank" rel="nofollow">${user.socialLinks.facebook}</a></li>
    </g:if>
    <g:if test="${user.socialLinks?.googlePlus}">
        <li><span class="fa fa-google-plus fa-lg"></span> <a href="${user.socialLinks?.googlePlus}" target="_blank" rel="nofollow">${user.socialLinks?.googlePlus}</a></li>
    </g:if>
    <g:if test="${user.socialLinks?.blog}">
        <li><span class="fa fa-rss-square fa-lg"></span> <a href="${user.socialLinks?.blog}" target="_blank" rel="nofollow">${user.socialLinks?.googlePlus}</a></li>
    </g:if>
</ul>