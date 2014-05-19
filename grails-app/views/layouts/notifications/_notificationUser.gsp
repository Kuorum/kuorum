<g:if test="${user}">
    <g:if test="${modalUser}">
        <userUtil:showUser user="${user}"/>
    </g:if>
    <g:else>
        <span itemscope itemtype="http://schema.org/Person">
            <g:link mapping="userShow" params="${user.encodeAsLinkProperties()}" itemprop="url">
                <img src="${image.userImgSrc(user:user)}" alt="${user.name}" class="user-img" itemprop="image">
                <span itemprop="name">${user.name}</span>
            </g:link>
        </span>
    </g:else>
</g:if>