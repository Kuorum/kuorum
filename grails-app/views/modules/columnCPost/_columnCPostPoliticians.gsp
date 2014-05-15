<postUtil:ifIsImportant post="${post}">
    <g:if test="${post.defender}">
        <div class="user actor politic" itemprop="contributor" itemscope itemtype="http://schema.org/Person">
            <userUtil:showUser user="${post.defender}" />
        </div>
    </g:if>
    <g:elseif test="${post.debates}">
        <userUtil:showListUsers users="${post.debates.findAll{it.kuorumUser != post.owner}.kuorumUser}" visibleUsers="2" messagesPrefix="post.debate.politicians.userList"/>
    </g:elseif>
</postUtil:ifIsImportant>