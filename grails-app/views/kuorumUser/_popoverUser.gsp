%{--<!-- POPOVER PARA IMÁGENES USUARIOS -->--}%
<div class="popover">
    %{--<button type="button" class="close" aria-hidden="true"  data-dismiss="popover"><span class="fa fa-times-circle-o fa"></span><span class="sr-only"><g:message code="kuorumUser.popover.close"/></span></button>--}%
    %{--<a href="#" class="hidden" rel="nofollow"><g:message code="kuorumUser.popover.showUser"/> </a>--}%

    <div class="popover-user">
        <div class="popover-user-header clearfix">
            <div class="user pull-left" itemscope itemtype="http://schema.org/Person">
                <g:link mapping="userShow" params="${user.encodeAsLinkProperties()}" itemprop="url">
                    <img src="${image.userImgSrc(user: user)}" alt="${user.fullName}" class="user-img" itemprop="image"><span
                        itemprop="name">${user.fullName}</span>
                </g:link>
                <span class="user-type">
                    <small><userUtil:roleName user="${user}"/></small>
                </span>
            </div><!-- /user -->
            <userUtil:followButton user="${user}" cssSize="btn-xs"/>
            %{--<userUtil:ifIsFollower user="${user}">--}%
                %{--<div class="pull-right">--}%
                    %{--<span class="fa fa-check-circle-o"></span>--}%
                    %{--<small><g:message code="kuorumUser.popover.follower"/></small>--}%
                %{--</div>--}%
            %{--</userUtil:ifIsFollower>--}%
        </div>
        <div class="popover-user-body">
            <form action="https://kuorum.org/ajax/sadiqkhan/rate" id="user-rating-form" method="post" class="rating">
                <input type="hidden" name="politicianId" value="565826e3e4b0e824f577f368">
                <fieldset class="rating">
                    <legend class="sr-only">Mi valoración</legend>

                    <input id="star5" type="radio" name="rating" value="5">
                    <label for="star5">
                        5 estrellas
                        <span class="rate-message">Me encanta</span>
                    </label>

                    <input id="star4" type="radio" name="rating" value="4">
                    <label for="star4">
                        4 estrellas
                        <span class="rate-message">Me gusta bastante</span>
                    </label>

                    <input id="star3" type="radio" name="rating" value="3" checked="checked">
                    <label for="star3">
                        3 estrellas
                        <span class="rate-message">No está mal</span>
                    </label>

                    <input id="star2" type="radio" name="rating" value="2">
                    <label for="star2">
                        2 estrellas
                        <span class="rate-message">No me gusta mucho</span>
                    </label>

                    <input id="star1" type="radio" name="rating" value="1">
                    <label for="star1">
                        1 estrellas
                        <span class="rate-message">No me gusta nada</span>
                    </label>

                </fieldset>
            </form>
        </div>
    </div><!-- /popover-user -->
</div>
