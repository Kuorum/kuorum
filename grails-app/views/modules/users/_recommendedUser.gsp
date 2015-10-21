<article itemtype="http://schema.org/Person" itemscope role="article" class="box-ppal clearfix">
    <div class="link-wrapper">
        <g:link mapping="userShow" params="${user.encodeAsLinkProperties()}" class="hidden"></g:link>
        <div class="card-header-photo">
            <img src="${image.userImgProfile(user:user)}" alt="${user.name}">
        </div>
        <div class="user">
            <div class='profile-pic-div'>
                <img itemprop="image" class="user-img big" alt="nombre" src="${image.userImgSrc(user:user)}">
                <g:if test="${user.verified}">
                    <i class="fa fa-check"></i>
                </g:if>
            </div>
            %{--<button type="button" class="btn btn-blue btn-lg follow allow">Seguir</button>--}%
            <userUtil:followButton user="${user}" cssSize="btn-lg">
                <i class="fa fa-plus"></i>
            </userUtil:followButton>
            <g:link mapping="userShow" params="${user.encodeAsLinkProperties()}" class="user-name" itemprop="name">
                ${user.name}
            </g:link>
            <cite><userUtil:politicianPosition user="${user}"/></cite>
            <p class="party"><userUtil:roleName user="${user}"/></p>
            %{--<span class="user-type"><userUtil:roleName user="${user}"/> </span>--}%
        </div>
        <p><kuorumDate:showShortedText text="${user.bio}" numChars="165"/> </p>
        <div class='card-footer'>
            <userUtil:ifIsFollower user="${user}">
                <span class="fa fa-check-circle-o"></span>
                <g:message code="kuorumUser.popover.follower"/>
            </userUtil:ifIsFollower>
        </div>
    </div>
</article>