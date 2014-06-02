<section class="boxes userkarma">
    <h1><span class="fa fa-user"></span>
        <g:message code="kuorumUser.show.userkarma.title"/> </h1>
    <p><g:message code="kuorumUser.show.userkarma.description"/></p>
    <ul class="activity">
        <li><span class="counter">${user.gamification.numEggs}</span> <span class="icon-Flaticon_17919"></span> <br>votos</li>
        <li><span class="counter">${user.gamification.numCorns}</span> <span class="icon-Flaticon_20188"></span> <br>impulsos</li>
        <li><span class="counter">${user.gamification.numPlumes}</span> <span class="icon-Flaticon_24178"></span> <br>propuestas</li>
    </ul>
    %{--<button id="follow" class="btn btn-blue btn-lg allow enabled" type="button">Seguir</button>--}%
    <userUtil:followButton user="${user}" cssSize="btn-lg"/>
</section>