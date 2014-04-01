<li>
    <span itemscope itemtype="http://schema.org/Person">
        <img src="${image.userImgSrc(user:notification.clucker)}" alt="${notification.clucker.name}" class="user" itemprop="image">
        <g:link mapping="userShow" params="${notification.clucker.encodeAsLinkProperties()}" itemprop="url">
            <span itemprop="name">${notification.clucker.name}</span>
        </g:link>
    </span>
    <g:set var="postType">
        <g:link mapping="postShow" params="${notification.post.encodeAsLinkProperties()}">
            <g:message code="kuorum.core.model.PostType.${notification.post.postType}"/>
        </g:link>
    </g:set>
    <span class="text-notification"><g:message code="notifications.cluckNotification.text" args="[postType]" encodeAs="raw"/></span>
</li>