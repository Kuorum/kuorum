<li class="${!notification.checked?'new':''} user">
    <span itemscope="" itemtype="http://schema.org/Person">
        <g:link mapping="userShow" params="${notification.actor.encodeAsLinkProperties()}" itemprop="url">
            <img src="${image.userImgSrc(user:notification.actor)}" alt="${notification.actor.name}" class="user-img" itemprop="image">
        </g:link>
        <span class="notification-text">
            ${raw(text)}
        </span>
    </span>
</li>