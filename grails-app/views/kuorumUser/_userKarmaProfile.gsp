<section class="boxes profile-user clearfix" itemtype="http://schema.org/Person" itemscope>
    <img itemprop="image" class="user-img big" alt="${user.name}" src="${image.userImgSrc(user: user)}">
    <h1><a href="#" class="user-name" itemprop="name">${user.name}</a></h1>
    <h2 class="user-type"><userUtil:roleName user="${user}"/></h2>
    <div class="actions">
        <userUtil:followButton user="${user}"/>
        %{--<a href="#" class="btn btn-blue" role="button"><span class="fa fa-envelope-o fa-lg"></span> Mensaje</a>--}%
    </div>
    <p>${user.bio}</p>
    <!-- verificada y te sigue, cuando proceda -->
    <small>verificada <span class="fa fa-check"></span></small>
    <div class="pull-right">
        <span class="fa fa-check-circle-o"></span> te sigue
    </div>
</section>


%{--<section class="boxes userkarma">--}%
    %{--<h1><span class="fa fa-user"></span>--}%
        %{--<g:message code="kuorumUser.show.userkarma.title"/> </h1>--}%
    %{--<p><g:message code="kuorumUser.show.userkarma.description"/></p>--}%
    %{--<ul class="activity">--}%
        %{--<li><span class="counter">${user.gamification.numEggs}</span> <span class="icon-Flaticon_17919"></span> <br>votos</li>--}%
        %{--<li><span class="counter">${user.gamification.numCorns}</span> <span class="icon-Flaticon_20188"></span> <br>impulsos</li>--}%
        %{--<li><span class="counter">${user.gamification.numPlumes}</span> <span class="icon-Flaticon_24178"></span> <br>propuestas</li>--}%
    %{--</ul>--}%
    %{--<button id="follow" class="btn btn-blue btn-lg allow enabled" type="button">Seguir</button>--}%
    %{--<userUtil:followButton user="${user}" cssSize="btn-lg"/>--}%
%{--</section>--}%