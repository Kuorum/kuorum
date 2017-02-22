<li class="${!notification.checked?'new':''} user">
    <g:set var="actor" value="${kuorum.users.KuorumUser.findByAlias(notification.actorAlias)}"/>
    <span itemscope="" itemtype="http://schema.org/Person">
        <g:link mapping="userShow" params="${actor.encodeAsLinkProperties()}" itemprop="url">
            <img src="${image.userImgSrc(user:actor)}" alt="${actor.name}" class="user-img" itemprop="image">
        </g:link>
        <span class="notification-text">
            <g:link mapping="userShow" params="${actor.encodeAsLinkProperties()}" itemprop="url">
                ${actor.name}
            </g:link>
            ${raw(text)}
        </span>
    </span>
</li>