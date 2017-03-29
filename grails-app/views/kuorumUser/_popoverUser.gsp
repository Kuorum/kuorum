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
        <div class="popover-user-body">
            <g:form mapping="userRate" params="[userAlias: user.alias]" method="POST" class="user-rating-form rating">
                <input type="hidden" name="politicianId" value="${user.id}">
                <fieldset class="rating">
                    <legend class="sr-only"><g:message code="politician.valuation.rate"/></legend>
                    <g:each in="${(1..5).reverse()}" var="i">
                        <input id="star${i}_${user.id}" type="radio" name="rating" value="${i}" ${Math.round(userReputation.userReputation)==i?'checked':''}>
                        <label for="star${i}_${user.id}">
                            <g:message code="politician.valuation.rate.value.starts" args="[i]"/>
                            %{--<span class="rate-message"><g:message code="politician.valuation.rate.value.text.${i}"/></span>--}%
                        </label>
                    </g:each>
                </fieldset>
            </g:form>
        </div>
    </div>
</div>
