<article itemtype="http://schema.org/Person" itemscope role="article" class="box-ppal clearfix">

    <div class="photo">
        <img src="${image.userImgProfile(user:user)}" alt="${user.name}">
    </div>
    <div class="user">
        <div class="profile-pic-sm">
            <img itemprop="image" class="user-img big" alt="nombre" src="${image.userImgSrc(user:user)}">
            <g:if test="${user.verified}">
                <i class="fa fa-check"></i>
            </g:if>
        </div>
        %{--<button type="button" class="btn btn-blue btn-lg follow allow">Seguir</button>--}%
        <userUtil:followButton user="${user}" cssSize="btn-lg" cssExtra="cssExtra"/>
        <g:link mapping="userShow" params="${user.encodeAsLinkProperties()}" class="user-name" itemprop="name">
            ${user.name}
        </g:link>
        <cite>President, United States</cite>
        <p class="party"><userUtil:roleName user="${user}"/></p>
        %{--<span class="user-type"><userUtil:roleName user="${user}"/> </span>--}%
    </div>
    <p><kuorumDate:showShortedText text="${user.bio}" numChars="120"/> </p>
</article>