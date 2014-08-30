
<g:each in="${users}" var="user">
    <li>
        <article itemtype="http://schema.org/Article" itemscope role="article" class="kakareo post">
            <div class="link-wrapper">
                <g:link mapping="userShow" class="hidden" params="${user.encodeAsLinkProperties()}"><g:message code="search.list.user.goTo" args="[user.name]"/> </g:link>
                <div class="popover-box">
                    <div class="user" itemscope itemtype="http://schema.org/Person">
                        <a href="#" itemprop="url">
                            <img src="${image.userImgSrc(user:user)}" alt="${user.name}" class="user-img" itemprop="image"><span itemprop="name">${user.name}</span>
                        </a>
                        <span class="user-type">
                            <small>${userUtil.roleName(user:user)}</small>
                        </span>
                    </div><!-- /user -->
                <userUtil:followButton user="${user}"/>
                <userUtil:isFollower user="${user}"/>
                <g:render template="/kuorumUser/userActivity" model="[user:user]"/>
                </div>
            </div>
        </article>
    </li>
</g:each>