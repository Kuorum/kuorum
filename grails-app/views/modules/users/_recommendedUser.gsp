<article itemtype="http://schema.org/Person" itemscope role="article" class="box-ppal">

    <div class="photo">
        <img src="${image.userImgProfile(user:user)}" alt="${user.name}">
    </div>
    <div class="user">
        <img itemprop="image" class="user-img big" alt="nombre" src="${image.userImgSrc(user:user)}">
        %{--<button type="button" class="btn btn-blue btn-lg follow allow">Seguir</button>--}%
        <userUtil:followButton user="${user}" showNoLoggedButton="${true}" cssSize="btn-lg follow"/>
        <a href="#" class="user-name" itemprop="name">${user.name}</a>
        <span class="user-type"><userUtil:roleName user="${user}"/> </span>
    </div>
    <p>${user.bio}</p>
    <g:if test="${user.verified}">
        <small><g:message code="kuorumUser.verified"/><span class="fa fa-check"></span></small>
    </g:if>
</article>