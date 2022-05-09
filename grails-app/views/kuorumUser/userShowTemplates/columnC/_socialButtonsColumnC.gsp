<%@ page import="kuorum.core.customDomain.CustomDomainResolver" %>
<r:require modules="social"/>

<g:set var="userLink"><g:createLink
        mapping="userShow"
        params="${user.encodeAsLinkProperties()}"
        absolute="true"/></g:set>

<ul class="share-buttons hidden-xs">
    <li>
        <div class="tooltip left" role="tooltip">
            <div class="tooltip-arrow"></div>
            <div class="tooltip-inner"><g:message code="politician.sharebuttons"/></div>
        </div>
    </li>
    <li>
        <g:set var="facebookLink">https://www.facebook.com/sharer/sharer.php?u=${userLink}</g:set>
        <a class="social-link-facebook" href="${facebookLink}" target="_blank" rel="nofollow noopener noreferrer" title="${g.message(code:"project.social.facebook")}">
            <span class="social-share fab fa-facebook-f"></span>
        </a>
    </li>
    <li>
        <g:set var="twitterName" value="${user?.socialLinks?.twitter?.encodeAsTwitter()?:user.name}"/>
        <g:set var="twitterShareText"><g:message
                code="kuorumUser.social.twitter.text"
                args="[twitterName, kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.name]"
                encodeAs="HTML"/></g:set>
        <g:set var="twitterLink">https://twitter.com/share?url=${userLink}&text=${twitterShareText}&hashtags=${user.alias?:""}</g:set>

        <a href="${twitterLink}" target="_blank" rel="nofollow noopener noreferrer" title="${g.message(code: 'project.social.twitter')}">
            <span class="social-share fab fa-twitter"></span>
        </a>
    </li>
    <li>
        <a href="http://www.reddit.com/submit?url=${userLink}&title=${user.name}" target="_blank" rel="nofollow noopener noreferrer" title="${g.message(code:'project.social.reddit')}">
            <span class="social-share fab fa-reddit"></span>
        </a>
    </li>
    <li>
        <a href="http://www.linkedin.com/shareArticle?mini=true&url=${userLink}&title=${user.name}&summary=${user.bio}&source=${kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.domain}" target="_blank" rel="nofollow noopener noreferrer" title="${g.message(code:'project.social.linkedin')}">
            <span class="social-share fab fa-linkedin-in"></span>
        </a>
    </li>
</ul>