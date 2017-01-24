<!-- ^leader-post !-->
<div class="leader-post">
    <g:if test="${debate.photoUrl}">
        <img src="${debate.photoUrl}" >
    </g:if>
    <div class="header">
        <h1 class="title">${debate.title}</h1>
        <userUtil:showUser user="${debateUser}" showRole="true"/>
        <div class="clearfix">
            <span class="time-ago pull-left">2 months ago</span>
            <g:if test="${debateUser.id == user?.id}">
                <g:link class="edit" mapping="debateEdit" params="[debateId: debate.id]">
                    <span class="fa fa-pencil-square-o pull-right fa-2x" aria-hidden="true"></span>
                </g:link>
            </g:if>
        </div>
    </div>

    <div class="body">
        ${debate.body}
    </div>

    <div class="footer clearfix">
        <!--<ul class="labels">
            <li>
                <span class="label-leader-post">Brexit</span>
            </li>
            <li>
                <span class="label-leader-post">Nothern Powerhouse</span>
            </li>
            <li>
                <span class="label-leader-post">NHS</span>
            </li>
        </ul>-->

        <ul class="social pull-left">
            <g:if test="${debateUser.socialLinks?.twitter}">
                <li>
                    <a href="${debateUser.socialLinks.twitter}" target="_blank" title="${message(code:"kuorum.web.commands.profile.SocialNetworkCommand.twitter.label")}">
                        <span class="fa-stack fa-lg" aria-hidden="true">
                            <span class="fa fa-circle dark fa-stack-2x"></span>
                            <span class="fa fa-twitter fa-stack-1x fa-inverse"></span>
                        </span>
                    </a>
                </li>
            </g:if>
            <g:if test="${debateUser.socialLinks?.facebook}">
                <li>
                    <a href="${debateUser.socialLinks.facebook}" target="_blank" title="${message(code:"kuorum.web.commands.profile.SocialNetworkCommand.facebook.label")}">
                        <span class="fa-stack fa-lg" aria-hidden="true">
                            <span class="fa fa-circle dark fa-stack-2x"></span>
                            <span class="fa fa-facebook fa-stack-1x fa-inverse"></span>
                        </span>
                    </a>
                </li>
            </g:if>
            <g:if test="${debateUser.socialLinks?.googlePlus}">
                <li>
                    <a href="${debateUser.socialLinks?.googlePlus}" target="_blank" title="${message(code:"kuorum.web.commands.profile.SocialNetworkCommand.googlePlus.label")}">
                        <i class="fa fa-google-plus-square fa-2x"></i>
                    </a>
                </li>
            </g:if>
            <g:if test="${debateUser.socialLinks?.linkedIn}">
                <li>
                    <a href="${debateUser.socialLinks?.linkedIn}" target="_blank" title="${message(code:"kuorum.web.commands.profile.SocialNetworkCommand.linkedIn.label")}">
                        <span class="fa-stack fa-lg" aria-hidden="true">
                            <span class="fa fa-circle dark fa-stack-2x"></span>
                            <span class="fa fa-linkedin fa-stack-1x fa-inverse"></span>
                        </span>
                    </a>
                </li>
            </g:if>
            <g:if test="${debateUser.socialLinks?.googlePlus}">
                <li>
                    <a href="${debateUser.socialLinks?.googlePlus}" target="_blank" title="${message(code:"kuorum.web.commands.profile.SocialNetworkCommand.blog.label")}">
                        <span class="fa-stack fa-lg" aria-hidden="true">
                            <span class="fa fa-circle dark fa-stack-2x"></span>
                            <span class="fa fa-google-plus fa-stack-1x fa-inverse"></span>
                        </span>
                    </a>
                </li>
            </g:if>
        </ul>

        <div class="comment-counter pull-right">
            <button type="button">
                <span class="fa fa-lightbulb-o" aria-hidden="true"></span>
                <span class="number">36</span>
            </button>
        </div>
    </div>
</div> <!-- ^leader-post !-->