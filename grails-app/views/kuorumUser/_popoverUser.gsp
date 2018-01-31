%{--<!-- POPOVER PARA IMÁGENES USUARIOS -->--}%
<div class="popover">
    %{--<button type="button" class="close" aria-hidden="true"  data-dismiss="popover"><span class="fa fa-times-circle-o fa"></span><span class="sr-only"><g:message code="kuorumUser.popover.close"/></span></button>--}%
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
                    %{--<span class="fa fa-check-circle-o"></span>--}%
                    %{--<small><g:message code="kuorumUser.popover.follower"/></small>--}%
                %{--</div>--}%
            %{--</userUtil:ifIsFollower>--}%
        </div>
        <div class="popover-user-body load-rating" data-rating-link="${g.createLink(mapping:'userLoadRate', params:user.encodeAsLinkProperties())}">
            <div class="loading md"></div>
        </div>
    </div>
</div>
