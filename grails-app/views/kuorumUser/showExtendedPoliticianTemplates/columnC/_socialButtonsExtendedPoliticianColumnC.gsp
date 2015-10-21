<r:require modules="social"/>

<g:set var="userLink"><g:createLink
        mapping="userShow"
        params="${user.encodeAsLinkProperties()}"
        absolute="true"/></g:set>

<ul class="share-buttons hidden-xs">
    <li>
        <div class="tooltip left" role="tooltip">
            <div class="tooltip-arrow"></div>
            <div class="tooltip-inner">Share</div>
        </div>
    </li>
    <li>
        <g:set var="facebookLink">https://www.facebook.com/sharer/sharer.php?u=${userLink}</g:set>
        <a href="${facebookLink}" target="_blank" title="${g.message(code:"project.social.facebook")}">
            <i class="social-share fa fa-facebook-square"></i>
        </a>
    </li>
    <li>
        <g:set var="twitterShareText"><g:message
                code="kuorumUser.social.twitter.text"
                args="[user.name]"
                encodeAs="HTML"/></g:set>
        <g:set var="twitterLink">https://twitter.com/share?url=${userLink}&text=${twitterShareText}&hashtags=${user.alias?:""}</g:set>

        <a href="${twitterLink}" target="_blank" title="${g.message(code: 'project.social.twitter')}">
            <i class="social-share fa fa-twitter-square"></i>
        </a>
    </li>
    <li>
        <a href="https://plus.google.com/share?url=${userLink}" target="_blank" title="${g.message(code:'project.social.googlePlus')}">
            <i class="social-share fa fa-google-plus-square"></i>
        </a>
    </li>
    %{--<li><a href="http://pinterest.com/pin/create/button/?url=&description=" target="_blank" title="Pin it"><i class="social-share fa fa-pinterest-square"></i></a></li>--}%
    <li>
        <a href="http://www.reddit.com/submit?url=${userLink}&title=${user.name}" target="_blank" title="${g.message(code:'project.social.reddit')}">
            <i class="social-share fa fa-reddit-square"></i>
        </a>
    </li>
    <li>
        <a href="http://www.linkedin.com/shareArticle?mini=true&url=${userLink}&title=${user.name}&summary=${user.bio}&source=kuorum.org" target="_blank" title="${g.message(code:'project.social.linkedin')}">
            <i class="social-share fa fa-linkedin-square"></i>
        </a>
    </li>
</ul>