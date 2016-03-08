<section class="boxes profile-user clearfix" itemtype="http://schema.org/Person" itemscope>
    <div class="profile-pic text-center">
        <img itemprop="image" class="user-img big" alt="${user.name}" src="${image.userImgSrc(user: user)}">
        <g:if test="${user.authorities*.authority.contains('ROLE_EDITOR')}">
            <abbr title="${message(code:'editor.image.icon.text')}"><i class="fa fa-binoculars"></i></abbr>
        </g:if>
    </div>
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