<div class="comment-header">
    <span class="call-title">
        <g:message code="modules.petitions.signs.signedUsers.title" args="[g.formatNumber(number:petition.signs, type:'number')]"/>
    </span>
    <span class="call-subTitle"><g:message code="modules.petitions.signs.signedUsers.subtitle"/></span>
</div>
<div class="comment-proposal clearfix">
    <userUtil:showListUsers users="${signs}" visibleUsers="12" messagesPrefix="modules.debate.pinned.users.dropdown"/>
</div>