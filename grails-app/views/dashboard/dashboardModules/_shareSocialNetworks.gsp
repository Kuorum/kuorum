<section class="panel panel-default">
    <div class="panel-heading">
        <h3 class="panel-title"><g:message code="dashboard.social.title"/> </h3>
    </div>
    <div class="panel-body">

        <g:set var="sharedLink"><g:createLink
                mapping="searcherLanding"
                params="[lang:user?.language?.locale?.language]"
                absolute="true"/></g:set>

        <ul class="social">
            <li>
                <g:set var="twitterName" value="${user?.socialLinks?.twitter?.encodeAsTwitter()?:user.name}"/>
                <g:set var="twitterShareText"><g:message
                        code="dashboard.social.twitter.text"
                        args="[twitterName]"
                        encodeAs="HTML"/></g:set>
                <g:set var="twitterLink">https://twitter.com/share?url=${sharedLink}&text=${twitterShareText}&hashtags=compol</g:set>


                <a href="${twitterLink}" target="_blank" class="social-share twitter" title="${g.message(code: 'dashboard.social.twitter.label')}">
                    <span class="fa-stack fa-lg">
                        <span class="fa fa-circle fa-stack-2x"></span>
                        <span class="fa fa-twitter fa-stack-1x fa-inverse"></span>
                        <span class="sr-only"><g:message code="dashboard.social.twitter.label"/></span>
                    </span>
                </a>
            </li>
            <li>
                <g:set var="facebookLink">https://www.facebook.com/sharer/sharer.php?u=${sharedLink}</g:set>
                <a href="${facebookLink}" target="_blank" class="social-share facebook" title="${g.message(code: 'dashboard.social.facebook.label')}">
                    <span class="fa-stack fa-lg">
                        <span class="fa fa-circle fa-stack-2x"></span>
                        <span class="fa fa-facebook fa-stack-1x fa-inverse"></span>
                        <span class="sr-only"><g:message code="dashboard.social.facebook.label"/></span>
                    </span>
                </a>
            </li>
            <li>
                <g:set var="linkedinTitle" value="${g.message(code:'dashboard.social.linkedin.title')}"/>
                <g:set var="linkedinSum" value="${g.message(code:'dashboard.social.linkedin.summary')}"/>
                <a class="social-share linkedin" href="http://www.linkedin.com/shareArticle?mini=true&url=${sharedLink}&title=${linkedinTitle}&summary=${linkedinSum}&source=kuorum.org" target="_blank" title="${g.message(code:'dashboard.social.linkedin.label')}">
                    <span class="fa-stack fa-lg">
                        <span class="fa fa-circle fa-stack-2x"></span>
                        <span class="fa fa-linkedin fa-stack-1x fa-inverse"></span>
                        <span class="sr-only"><g:message code="dashboard.social.linkedin.label"/></span>
                    </span>
                </a>
            </li>
            <li>
                <a href="https://plus.google.com/share?url=${sharedLink}" class="social-share google" title="${g.message(code:'dashboard.social.googlePlus.label')}">
                    <span class="fa-stack fa-lg">
                        <span class="fa fa-circle fa-stack-2x"></span>
                        <span class="fa fa-google-plus fa-stack-1x fa-inverse"></span>
                        <span class="sr-only"><g:message code="dashboard.social.googlePlus.label"/> </span>
                    </span>
                </a>
            </li>
        </ul>
    </div>
</section>