%{--<!-- POPOVER PARA IMÁGENES USUARIOS -->--}%
<div class="popover">
    %{--<button type="button" class="close" aria-hidden="true"  data-dismiss="popover"><span class="fal fa-times-circle fa"></span><span class="sr-only"><g:message code="kuorumUser.popover.close"/></span></button>--}%
    %{--<a href="#" class="hidden" rel="nofollow"><g:message code="kuorumUser.popover.showUser"/> </a>--}%

    <div class="popover-user">
        <div class="popover-user-header clearfix">
            <div class="user pull-left" itemscope itemtype="http://schema.org/Person">
                <g:link mapping="userShow" params="${user.encodeAsLinkProperties()}" itemprop="url" target="${targetBlank}">
                    <img src="${image.userImgSrc(user: user)}" alt="${user.fullName}" class="user-img" itemprop="image"><span
                        itemprop="name">${user.fullName}</span>
                </g:link>
                %{--<span class="user-type">--}%
                    %{--<small><userUtil:roleName user="${user}"/></small>--}%
                %{--</span>--}%
            </div><!-- /user -->
            <userUtil:followButton user="${user}" cssSize="btn-xs"/>
            %{--<userUtil:ifIsFollower user="${user}">--}%
                %{--<div class="pull-right">--}%
                    %{--<span class="fal fa-check-circle"></span>--}%
                    %{--<small><g:message code="kuorumUser.popover.follower"/></small>--}%
                %{--</div>--}%
            %{--</userUtil:ifIsFollower>--}%
        </div>
    </div>
</div>
