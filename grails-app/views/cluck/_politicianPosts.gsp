<div class="user actor politic" itemprop="contributor" itemscope itemtype="http://schema.org/Person">
    <g:if test="${recluck}">
        <span class="from"><span class="inside"></span></span>
    </g:if>
    <span class="type pull-left"><strong>${onomatopoeia}</strong></span>

    <g:if test="${defender}">
        <span class="state hidden-xs">${usersDesc}</span>
        <userUtil:showUser user="${defender}"/>
    </g:if>
    <g:elseif test="${debateUsers}">
        <userUtil:showListUsers messagesPrefix="post.show.important.usersList" users="${debateUsers}" visibleUsers="2"/>
    </g:elseif>
</div><!-- /autor -->