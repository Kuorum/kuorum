<g:if test="${displayingHorizontalModule}">
    <g:set var="cssClassUsers" value="col-sm-6 col-md-12 col-lg-6"/>
</g:if>
<g:else>
    <g:set var="cssClassUsers" value="col-sm-6"/>
</g:else>
<div class="main-kakareo row">
    <div class="${cssClassUsers} user author" itemprop="author" itemscope itemtype="http://schema.org/Person">
        <userUtil:showUser user="${solrPost?:post.owner}" showRole="true"/>
    </div>
    <div class="${cssClassUsers} text-right sponsor">
        <g:if test="${post.defender}">
            <userUtil:showListUsers users="${[post.defender]}" visibleUsers="1" messagesPrefix="cluck.defendUsers.victory.${post.victory}"/>
        </g:if>
        <g:elseif test="${post.debates}">
            <userUtil:showDebateUsers post="${post}" visibleUsers="1"/>
        </g:elseif>
    </div>
</div>
