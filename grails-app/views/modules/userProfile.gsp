<section class="boxes edit-user">
    <h1 class="sr-only"><g:message code="dashboard.userProfile.user"/> </h1>
    <a href="" class="text-right edit">Editar</a>
    <div class="user" itemscope itemtype="http://schema.org/Person">
        <a href="#" itemprop="url">
            <img src="${image.userImgSrc(user:user)}" alt="${user.name}" class="user-img big" itemprop="image">
            <div class="personal">
                <span itemprop="name" class="name">${user.name}</span>
                <span class="user-type"><small><user:roleName user="${user}"/></small></span>
            </div>
        </a>
        <ul class="activity">
            <li class="followers"><g:link mapping="userFollowers" params="${user.encodeAsLinkProperties()}">
                <span>${numFollowers}</span></g:link> <g:message code="dashboard.userProfile.followers"/>
            </li>
            <li class="following"><g:link mapping="userFollowing" params="${user.encodeAsLinkProperties()}">
                <span>${numFollowing}</span></g:link> <g:message code="dashboard.userProfile.following"/>
            </li>
            <li class="posts">
                <span>${numPosts}</span> <g:message code="dashboard.userProfile.posts"/>
            </li>
        </ul>
    </div>
</section>