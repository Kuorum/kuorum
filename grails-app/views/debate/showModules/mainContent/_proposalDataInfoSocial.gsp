<g:if test="${!request.xhr}">
    <r:require modules="social"/>
</g:if>

<g:set var="debateLink"><g:createLink
        mapping="debateShow"
        params="${debate.encodeAsLinkProperties()}"
        fragment="proposal_${proposal.id}"
        absolute="true"/></g:set>
<g:if test="${_showSocialButtons}">
    <ul class="social pull-left">
        <li>
            <g:set var="twitterShareText">${debate.title}</g:set>
            <g:set var="twitterLink">https://twitter.com/share?url=${debateLink.encodeAsURL()}&text=${twitterShareText}</g:set>
            <a href="${twitterLink}" target="_blank" rel="noopener noreferrer"
               title="${g.message(code: 'project.social.twitter')}">
                <span class="fa-stack fa-lg" aria-hidden="true">
                    <span class="fas fa-circle dark fa-stack-2x"></span>
                    <span class="fab fa-twitter fa-stack-1x fa-inverse"></span>
                </span>
            </a>
        </li>
        <li>
            <g:set var="facebookLink">https://www.facebook.com/sharer/sharer.php?u=${debateLink}</g:set>
            <a href="${facebookLink}" target="_blank" rel="noopener noreferrer" title="${g.message(code: 'project.social.facebook')}">
                <span class="fa-stack fa-lg" aria-hidden="true">
                    <span class="fas fa-circle dark fa-stack-2x"></span>
                    <span class="fab fa-facebook-f fa-stack-1x fa-inverse"></span>
                </span>
            </a>
        </li>
        <li>
            <a href="http://www.linkedin.com/shareArticle?mini=true&url=${debateLink}&title=${debate.title}&summary=${debate.body}&source=${kuorum.core.customDomain.CustomDomainResolver.domainRSDTO.domain}"
               target="_blank" rel="noopener noreferrer" title="${g.message(code: 'project.social.linkedin')}">
                <span class="fa-stack fa-lg" aria-hidden="true">
                    <span class="fas fa-circle dark fa-stack-2x"></span>
                    <span class="fab fa-linkedin-in fa-stack-1x fa-inverse"></span>
                </span>
            </a>
        </li>
    </ul>
</g:if>