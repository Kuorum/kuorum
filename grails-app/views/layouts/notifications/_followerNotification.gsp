<li>
    <span itemscope itemtype="http://schema.org/Person">
        <img src="${image.userImgSrc(user:notification.follower)}" alt="${notification.follower.name}" class="user" itemprop="image">
        <g:link mapping="userShow" params="${notification.follower.encodeAsLinkProperties()}" itemprop="url">
            <span itemprop="name">${notification.follower.name}</span>
        </g:link>
    </span>
    <span class="text-notification">
        <g:message code="notifications.followerNotification.text"/>
    </span>
</li>