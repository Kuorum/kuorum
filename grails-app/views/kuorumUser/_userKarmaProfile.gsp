<section class="boxes profile-user clearfix" itemtype="http://schema.org/Person" itemscope>
    <img itemprop="image" class="user-img big" alt="${user.name}" src="${image.userImgSrc(user: user)}">
    <h1><span href="#" class="user-name" itemprop="name">${user.name}</span></h1>
    <h2 class="user-type"><userUtil:roleName user="${user}"/></h2>
    <div class="actions">
        <userUtil:followButton user="${user}"/>
        %{--<a href="#" class="btn btn-blue" role="button"><span class="fa fa-envelope-o fa-lg"></span> Mensaje</a>--}%
    </div>
    <p>${user.bio}</p>
    <!-- verificada y te sigue, cuando proceda -->
    <g:if test="${user.verified}">
        <small><g:message code="kuorumUser.verified"/> <span class="fa fa-check"></span></small>
    </g:if>
    <userUtil:ifIsFollower user="${user}">
        <div class="pull-right">
            <span class="fa fa-check-circle-o"></span> te sigue
        </div>
    </userUtil:ifIsFollower>
</section>