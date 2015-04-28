<div class="main-kakareo row">
    <div class="col-sm-6 user author" itemprop="author" itemscope itemtype="http://schema.org/Person">
        <userUtil:showUser user="${solrPost?:post.owner}" showRole="true"/>
    </div>
    <div class="col-sm-6 text-right sponsor">
        <g:if test="${post.defender}">
            <userUtil:showListUsers users="${[post.defender]}" visibleUsers="1" messagesPrefix="cluck.defendUsers.victory.${post.victory}"/>
        </g:if>
        <g:elseif test="${post.debates}">
            <userUtil:showDebateUsers post="${post}" visibleUsers="1"/>
        </g:elseif>
    </div>
</div>
