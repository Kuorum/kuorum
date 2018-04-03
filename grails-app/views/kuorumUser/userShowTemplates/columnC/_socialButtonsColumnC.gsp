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
        <a class="social-link-facebook" href="${facebookLink}" target="_blank" title="${g.message(code:"project.social.facebook")}">
            <span class="social-share fa fa-facebook"></span>
        </a>
    </li>
    <li>
        <g:set var="twitterName" value="${user?.socialLinks?.twitter?.encodeAsTwitter()?:user.name}"/>
        <g:set var="twitterShareText"><g:message
                code="kuorumUser.social.twitter.text"
                args="[twitterName]"
                encodeAs="HTML"/></g:set>
        <g:set var="twitterLink">https://twitter.com/share?url=${userLink}&text=${twitterShareText}&hashtags=${user.alias?:""}</g:set>

        <a href="${twitterLink}" target="_blank" title="${g.message(code: 'project.social.twitter')}">
            <span class="social-share fa fa-twitter"></span>
        </a>
    </li>
    <li>
        <a class="social-link-google-plus" href="https://plus.google.com/share?url=${userLink}" target="_blank" title="${g.message(code:'project.social.googlePlus')}">
            <span class="social-share fa fa-google-plus"></span>
        </a>
    </li>
    %{--<li><a href="http://pinterest.com/pin/create/button/?url=&description=" target="_blank" title="Pin it"><i class="social-share fa fa-pinterest-square"></i></a></li>--}%
    <li>
        <a href="http://www.reddit.com/submit?url=${userLink}&title=${user.name}" target="_blank" title="${g.message(code:'project.social.reddit')}">
            <span class="social-share fa fa-reddit"></span>
        </a>
    </li>
    <li>
        <a href="http://www.linkedin.com/shareArticle?mini=true&url=${userLink}&title=${user.name}&summary=${user.bio}&source=kuorum.org" target="_blank" title="${g.message(code:'project.social.linkedin')}">
            <span class="social-share fa fa-linkedin"></span>
        </a>
    </li>
</ul>