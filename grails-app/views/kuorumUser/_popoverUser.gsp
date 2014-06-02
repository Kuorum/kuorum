<!-- POPOVER PARA IMÁGENES USUARIOS -->
<div class="popover">
    <button type="button" class="close" aria-hidden="true"  data-dismiss="popover"><span class="fa fa-times-circle-o fa"></span><span class="sr-only"><g:message code="kuorumUser.popover.close"/></span></button>
    %{--<a href="#" class="hidden" rel="nofollow"><g:message code="kuorumUser.popover.showUser"/> </a>--}%

    <div class="popover-user">
        <div class="user" itemscope itemtype="http://schema.org/Person">
            <g:link mapping="userShow" params="${user.encodeAsLinkProperties()}" itemprop="url">
                <img src="${image.userImgSrc(user: user)}" alt="${user.name}" class="user-img" itemprop="image"><span
                    itemprop="name">${user.name}</span>
            </g:link>
            <span class="user-type">
                <small><userUtil:roleName user="${user}"/></small>
            </span>
        </div><!-- /user -->
        <userUtil:followButton user="${user}"/>
        <userUtil:ifIsFollower user="${user}">
            <div class="pull-right">
                <span class="fa fa-check-circle-o"></span>
                <small><g:message code="kuorumUser.popover.follower"/></small>
            </div>
        </userUtil:ifIsFollower>
        <g:render template="/kuorumUser/userActivity" model="[user:user]"/>
    </div><!-- /popover-user -->
</div>
<!-- FIN POPOVER PARA IMÁGENES USUARIOS -->
