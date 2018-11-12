<g:if test="${!request.xhr}">
    <r:require modules="social"/>
</g:if>

<g:set var="debateLink"><g:createLink
        mapping="debateShow"
        params="${debate.encodeAsLinkProperties()}"
        fragment="comment_${comment.id}"
        absolute="true"/></g:set>

<ul class="social pull-left">
    <li>
        <g:set var="twitterShareText">${debate.title}</g:set>
        <g:set var="twitterLink">https://twitter.com/share?url=${debateLink.encodeAsURL()}&text=${twitterShareText}</g:set>
        <a href="${twitterLink}" target="_blank" title="${g.message(code:'project.social.twitter')}">
            <span class="fa-stack fa-lg" aria-hidden="true">
                <span class="fab fa-twitter fa-stack-1x"></span>
            </span>
        </a>
    </li>
    <li>
        <g:set var="facebookLink">https://www.facebook.com/sharer/sharer.php?u=${debateLink}</g:set>
        <a href="${facebookLink}" target="_blank" title="${g.message(code:'project.social.facebook')}">
            <span class="fa-stack fa-lg" aria-hidden="true">
                <span class="fab fa-facebook-f fa-stack-1x"></span>
            </span>
        </a>
    </li>
    <li>
        <a href="http://www.linkedin.com/shareArticle?mini=true&url=${debateLink}&title=${debate.title}&summary=${debate.body}&source=${kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.domain}" target="_blank" title="${g.message(code:'project.social.linkedin')}">
            <span class="fa-stack fa-lg" aria-hidden="true">
                <span class="fab fa-linkedin-in fa-stack-1x"></span>
            </span>
        </a>
    </li>
</ul>