<!-- POPOVER PARA IMÁGENES USUARIOS -->
<div class="popover">
    <button type="button" class="close" aria-hidden="true"><span class="fa fa-times-circle-o fa"></span><span class="sr-only"><g:message code="kuorumUser.popover.close"/></span></button>
    <a href="#" class="hidden" rel="nofollow"><g:message code="kuorumUser.popover.showUser"/> </a>

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
            <div class="pull-right"><span class="fa fa-check-circle-o"></span> <small><g:message
                    code="kuorumUser.popover.follower"/></small></div>
        </userUtil:ifIsFollower>
        <ul class="infoActivity clearfix">
            <li>
                <span class="fa fa-question-circle"></span>
                <span class="counter">${user.activity.numQuestions}</span>
                <span class="sr-only"><g:message code="kuorumUser.popover.questions"/></span>
            </li>
            <li>
                <span class="fa fa-book"></span>
                <span class="counter">${user.activity.numHistories}</span>
                <span class="sr-only"><g:message code="kuorumUser.popover.histories"/></span>
            </li>
            <li>
                <span class="fa fa-lightbulb-o"></span>
                <span class="counter">${user.activity.numPurposes}</span>
                <span class="sr-only"><g:message code="kuorumUser.popover.purposes"/></span>
            </li>
            <li>
                <span class="fa fa-user"></span>
                <small><span class="fa fa-forward"></span></small>
                <span class="counter">${user.following.size()}</span>
                <span class="sr-only"><g:message code="kuorumUser.popover.following"/></span>
            </li>
            <li>
                <span class="fa fa-user"></span>
                <small><span class="fa fa-backward"></span></small>
                <span class="counter">${user.numFollowers}</span>
                <span class="sr-only"><g:message code="kuorumUser.popover.followers"/></span>
            </li>
            <g:if test="${user.verified}">
                <li class="pull-right">
                    <span class="sr-only"><g:message code="kuorumUser.verified"/></span>
                    <span class="fa fa-check"></span>
                </li>
            </g:if>
        </ul>

    </div><!-- /popover-user -->
</div>
<!-- FIN POPOVER PARA IMÁGENES USUARIOS -->
