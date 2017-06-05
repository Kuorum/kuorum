<g:if test="${!request.xhr}">
    <r:require modules="social"/>
</g:if>

<g:set var="debateLink"><g:createLink
        mapping="debateShow"
        params="${debate.encodeAsLinkProperties()}"
        fragment="proposal_${proposal.id}"
        absolute="true"/></g:set>

<ul class="social pull-left">
    <li>
        <g:set var="twitterShareText">${debate.title}</g:set>
        <g:set var="twitterLink">https://twitter.com/share?url=${debateLink.encodeAsURL()}&text=${twitterShareText}</g:set>
        <a href="${twitterLink}" target="_blank" title="${g.message(code:'project.social.twitter')}">
            <span class="fa-stack fa-lg" aria-hidden="true">
                <span class="fa fa-circle dark fa-stack-2x"></span>
                <span class="fa fa-twitter fa-stack-1x fa-inverse"></span>
            </span>
        </a>
    </li>
    <li>
        <g:set var="facebookLink">https://www.facebook.com/sharer/sharer.php?u=${debateLink}</g:set>
        <a href="${facebookLink}" target="_blank" title="${g.message(code:'project.social.facebook')}">
            <span class="fa-stack fa-lg" aria-hidden="true">
                <span class="fa fa-circle dark fa-stack-2x"></span>
                <span class="fa fa-facebook fa-stack-1x fa-inverse"></span>
            </span>
        </a>
    </li>
    <li>
        <a href="http://www.linkedin.com/shareArticle?mini=true&url=${debateLink}&title=${debate.title}&summary=${debate.body}&source=kuorum.org" target="_blank" title="${g.message(code:'project.social.linkedin')}">
            <span class="fa-stack fa-lg" aria-hidden="true">
                <span class="fa fa-circle dark fa-stack-2x"></span>
                <span class="fa fa-linkedin fa-stack-1x fa-inverse"></span>
            </span>
        </a>
    </li>
    <li>
        <a href="https://plus.google.com/share?url=${debateLink}" target="_blank" title="${g.message(code:'project.social.googlePlus')}">
            <span class="fa-stack fa-lg" aria-hidden="true">
                <span class="fa fa-circle dark fa-stack-2x"></span>
                <span class="fa fa-google-plus fa-stack-1x fa-inverse"></span>
            </span>
        </a>
    </li>
</ul>