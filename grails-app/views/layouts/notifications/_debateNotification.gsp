<%@ page import="kuorum.notifications.DebateAlertNotification" %>


<li>
    <span itemscope itemtype="http://schema.org/Person">
        <img src="${image.userImgSrc(user:notification.debateWriter)}" alt="${notification.debateWriter.name}" class="user" itemprop="image">
        <g:link mapping="userShow" params="${notification.debateWriter.encodeAsLinkProperties()}" itemprop="url">
            <span itemprop="name">${notification.debateWriter.name}</span>
        </g:link>
    </span>
    <g:set var="postType">
        <g:link mapping="postShow" params="${notification.post.encodeAsLinkProperties()}">
            <g:message code="kuorum.core.model.PostType.${notification.post.postType}"/>
        </g:link>
    </g:set>
    <g:if test="${notification.instanceOf(DebateAlertNotification)}">
        <span class="text-notification"><g:message code="notifications.debateAlertNotification.text" args="[postType]" encodeAs="raw"/></span>
        <span class="actions"><span class="pull-right"><small><a href="#">Más tarde</a></small> <button type="button" class="btn bg-ntral-dark btn-sm">Responder</button></span></span>
    </g:if>
    <g:elseif test="${notification.user == notification.post.owner}">
        <span class="text-notification"><g:message code="notifications.debateNotification.ownerText" args="[postType]" encodeAs="raw"/></span>
        <span class="actions"><span class="pull-right"><small><a href="#">Más tarde</a></small> <button type="button" class="btn bg-ntral-dark btn-sm">Responder</button></span></span>
    </g:elseif>
    <g:else>
        <g:set var="postOwner">
            <g:link mapping="postShow" params="${notification.post.owner.encodeAsLinkProperties()}">
                ${notification.post.owner.name.encodeAsHTML()}"/>
            </g:link>
        </g:set>
        <span class="text-notification"><g:message code="notifications.debateNotification.text" args="[postOwner,postType]" encodeAs="raw"/></span>
    </g:else>
</li>
