<section class="boxes edit-user">
    <h1 class="sr-only"><g:message code="dashboard.userProfile.user"/> </h1>
    %{--<a href="" class="text-right edit">Editar</a>--}%
    <div class="user" itemscope itemtype="http://schema.org/Person">
        <g:link mapping="userShow" params="${user.encodeAsLinkProperties()}" itemprop="url" class="clearfix">
            <img src="${image.userImgSrc(user:user)}" alt="${user.name}" class="user-img big" itemprop="image">
            <div class="personal">
                <span itemprop="name" class="name">${user.name}</span>
                <span class="user-type"><small><userUtil:roleName user="${user}"/></small></span>
            </div>
        </g:link>
        <ul class="activity">
            <li class="followers">
                <userUtil:counterFollowers user="${user}"/>
            </li>

            <li class="following">
                <userUtil:counterFollowing user="${user}"/>
            </li>
            <li class="posts"><span>${numCauses}</span> <g:message code="dashboard.userProfile.causes"/></li>
        </ul>
    </div>
</section>