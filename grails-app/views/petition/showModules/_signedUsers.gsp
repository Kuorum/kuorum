<div class="comment-box call-to-action" id="module-debate-pinned-users">
    <div class="comment-header">
        <span class="call-title">
            <g:message code="modules.debate.pinned.users.title"/>
            <span class="fa-stack fa-lg" aria-hidden="true">
                <span class="fas fa-circle dark fa-stack-2x"></span>
                <span class="fas fa-flag fa-stack-1x fa-inverse"></span>
            </span>
        </span>
        <span class="call-subTitle"><g:message code="modules.debate.pinned.users.subtitle" args="[campaignUser.name]"/></span>
    </div>
    <div class="comment-proposal clearfix">
        <userUtil:showListUsers users="${signs}" visibleUsers="11" messagesPrefix="modules.debate.pinned.users.dropdown"/>
    </div>
</div>

