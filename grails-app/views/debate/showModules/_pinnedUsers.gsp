<div class="comment-box call-to-action" id="module-debate-pinned-users">
    <div class="comment-header">
        <span class="call-title">
            <g:message code="modules.debate.pinned.users.title"/>
            <span class="fa-stack fa-lg" aria-hidden="true">
                <span class="fa fa-circle dark fa-stack-2x"></span>
                <span class="fa fa-flag-o fa-stack-1x fa-inverse"></span>
            </span>
        </span>
        <span class="call-subTitle"><g:message code="modules.debate.pinned.users.subtitle" args="[debateUser.name]"/></span>
    </div>
    <div class="comment-proposal clearfix">
        <ul class="user-list-images">
            <g:each in="${pinnedUsers}" var="user">
                <userUtil:showUser
                        user="${user}"
                        showName="false"
                        showActions="false"
                        showDeleteRecommendation="false"
                        htmlWrapper="li"
                />
            </g:each>
        </ul>
    </div>
</div>

