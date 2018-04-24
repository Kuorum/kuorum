<li class="${!notification.checked?'new':''} user">
    <g:set var="actor" value="${kuorum.users.KuorumUser.findByAliasAndDomain(notification.actorAlias, kuorum.core.customDomain.CustomDomainResolver.domain)}"/>
    <span itemscope="" itemtype="http://schema.org/Person">
        <g:link mapping="userShow" params="${actor.encodeAsLinkProperties()}" itemprop="url">
            <img src="${image.userImgSrc(user:actor)}" alt="${actor.name}" class="user-img" itemprop="image">
        </g:link>
        <span class="notification-text">
            ${raw(text)}
        </span>
    </span>
</li>