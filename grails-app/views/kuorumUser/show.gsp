<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="kuorum.name"/> </title>
    <meta name="layout" content="columnCLayout">
    <parameter name="specialContainerCssClass" value="userprofile" />
</head>


<content tag="mainContent">
    <article itemtype="http://schema.org/Person" itemscope role="article" class="kakareo post ley">
        <div class="photo">
            <img src="${image.userImgProfile(user:user)}" alt="${user.name}">
        </div>
        <div class="user">
            <img itemprop="image" class="user-img big" alt="${user.name}" src="${image.userImgSrc(user:user)}">
            <span class="user-name" itemprop="name">
                ${user.name}
                <g:if test="${user.verified}">
                    <small><g:message code="kuorumUser.verified"/> <span class="fa fa-check"></span></small>
                </g:if>
            </span>
            <span class="user-type"><userUtil:roleName user="${user}"/></span>
            <userUtil:ifIsFollower user="${user}">
                <span class="mark"><span class="fa fa-check-circle-o"></span> te sigue</span>
            </userUtil:ifIsFollower>
            <ul class="infoActivity">
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
            </ul>
        </div>
        <p>Lorem ipsum dolor sit amet, te fabellas euripidis expetendis vim, mei ut odio mucius scripserit. Timeam laoreet patrioque his ei, vel ea congue fastidii. Sit eleifend reformidans ei. Omnium euismod in pri, eam ei oblique numquam dignissim, vel et erant nostro suscipiantur.</p>
        <p>Te fabellas euripidis expetendis vim, mei ut odio mucius scripserit. Timeam laoreet patrioque his ei, vel ea congue fastidii. Sit eleifend reformidans ei. Omnium euismod in pri, eam ei oblique numquam dignissim, vel et erant nostro suscipiantur.</p>
        <ul class="socialContact">
            <li><span class="fa fa-map-marker fa-lg"></span> ${user.personalData.province.name}</li>
            <li><span class="fa fa-twitter fa-lg"></span> <a href="#" target="_blank" rel="nofollow">@twitter</a></li>
            <li><span class="fa fa-facebook fa-lg"></span> <a href="#" target="_blank" rel="nofollow">facebook.com/menganito</a></li>
            <li><span class="fa fa-linkedin fa-lg"></span> <a href="#" target="_blank" rel="nofollow">linkedin.com/menganito</a></li>
        </ul>
    </article>

    <g:set var="urlLoadMore" value="${createLink(mapping: 'dashboardSeeMore')}"/>
    <g:render template="/cluck/listClucks" model="[clucks:clucks, urlLoadMore:urlLoadMore]"/>

</content>

<content tag="cColumn">
    <section class="boxes userkarma">
        <h1><span class="fa fa-user"></span><g:message code="kuorumUser.show.userkarma.title"/> </h1>
        <p><g:message code="kuorumUser.show.userkarma.description"/></p>
        <ul class="activity">
            <li><span class="counter">${user.gamification.numEggs}</span> <span class="icon-Flaticon_17919"></span> <br>votos</li>
            <li><span class="counter">${user.gamification.numCorns}</span> <span class="icon-Flaticon_20188"></span> <br>impulsos</li>
            <li><span class="counter">${user.gamification.numPlumes}</span> <span class="icon-Flaticon_24178"></span> <br>propuestas</li>
        </ul>
        %{--<button id="follow" class="btn btn-blue btn-lg allow enabled" type="button">Seguir</button>--}%
        <userUtil:followButton user="${user}" cssSize="btn-lg"/>
    </section>

    <section class="boxes follow">
        <h1><span class="fa fa-user"></span> <small><span class="fa fa-forward"></span></small> Siguiendo</h1>
        <div class="kakareo follow">
            <userUtil:listFollowers user="${user}"/>
        </div>
    </section>

    <section class="boxes follow">
        <h1><span class="fa fa-user"></span> <small><span class="fa fa-backward"></span></small> Seguidores</h1>
        <div class="kakareo follow">
            <userUtil:listFollowing user="${user}"/>
        </div>
    </section>

    <g:if test="${activeLaws}">
        <section class="boxes laws">
            <h1><span class="fa fa-briefcase"></span> Leyes en las que participa</h1>
            <ul>
                <g:each in="${activeLaws}" var="activity">
                    <li>
                    %{--<a href="#">${activity.law.hashtag}</a>--}%
                        <g:link mapping="lawShow" params="${activity.law.encodeAsLinkProperties()}">
                            ${activity.law.hashtag}
                        </g:link>
                        <span class="counter">${activity.numTimes}</span>
                    </li>
                </g:each>
            </ul>
            <div class="text-center" id="load-more"><a href="#">Ver todas</a></div>
        </section>
    </g:if>
</content>
