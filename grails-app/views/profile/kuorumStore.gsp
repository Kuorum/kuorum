<%@ page import="kuorum.core.model.gamification.GamificationAward" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.kuorumStore"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <h1>
        <g:message code="profile.kuorumStore.salutation"/>,
        <br>
        <span class="user-name">${user.name}</span>
    </h1>
    <p><g:message code="profile.kuorumStore.description"/></p>
    <g:render template="leftMenu" model="[user:user, activeMapping:'profileKuorumStore', menu:menu]"/>

</content>
<content tag="mainContent">

    <h1><g:message code="profile.kuorumStore.title"/></h1>
    <div class="saldo row">
        <div class="col-xs-12 col-sm-12 col-md-6">
            <h2><g:message code="profile.kuorumStore.balance.title"/> </h2>
            <p><g:message code="profile.kuorumStore.balance.description"/></p>
        </div>
        <div class="col-xs-12 col-sm-12 col-md-6">
            <ul class="activity">
                <li><span id="numEggs" class="counter">${user.gamification.numEggs}</span> <span class="icon-Flaticon_17919"></span> <br><g:message code="profile.kuorumStore.eggs.description"/> </li>
                <li><span id="numCorns" class="counter">${user.gamification.numCorns}</span> <span class="icon-Flaticon_20188"></span> <br><g:message code="profile.kuorumStore.corns.description"/></li>
                <li><span id="numPlumes" class="counter">${user.gamification.numPlumes}</span> <span class="icon-Flaticon_24178"></span> <br><g:message code="profile.kuorumStore.plumes.description"/></li>
            </ul>
        </div>
    </div>
    <div class="role">
        <h2><g:message code="profile.kuorumStore.roles.title"/></h2>
        <div class="row">
            <g:each in="${GamificationAward.values().findAll{it.toString().startsWith('ROLE_')}}" var="roleAward">
                    <section class="col-xs-12 col-sm-12 col-md-6">
                        <h1><g:message code="${GamificationAward.canonicalName}.${roleAward}.${user.personalData.gender}"/></h1>
                        <p><g:message code="${GamificationAward.canonicalName}.${roleAward}.description"/></p>
                        <div class="options">
                            <g:set var="cssInactive" value=""/>
                            <gamification:ifRoleIsBought role="${roleAward}">
                                <g:set var="cssInactive" value="inactive"/>
                            </gamification:ifRoleIsBought>
                            <ul class="activity ${cssInactive}">
                                <li><span class="counter">${roleAward.numEggs}</span> <span class="icon-Flaticon_17919"></span> <br><span class="sr-only"><g:message code="profile.kuorumStore.eggs.description"/></span></li>
                                <li><span class="counter">${roleAward.numCorns}</span> <span class="icon-Flaticon_20188"></span> <br><span class="sr-only"><g:message code="profile.kuorumStore.corns.description"/></span></li>
                                <li><span class="counter">${roleAward.numPlumes}</span> <span class="icon-Flaticon_24178"></span> <br><span class="sr-only"><g:message code="profile.kuorumStore.plumes.description"/></span></li>
                            </ul>
                            <gamification:roleButton role="${roleAward}"/>
                        </div>
                    </section>
            </g:each>
        </div>
    </div><!-- /.role -->
    <div class="skill">
        <h2>Consigue nuevas habilidades</h2>
        <div class="row">
            <section class="col-xs-12 col-sm-12 col-md-6">
                <h1>Lorem ipsum dolor sit amet, consectetur</h1>
                <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod <a href="#">tempor incididunt ut labore et dolore magna aliqua.</a></p>
                <div class="options">
                    <ul class="activity inactive">
                        <li><span class="counter">4</span> <span class="icon-Flaticon_17919"></span> <br><span class="sr-only">votos</span></li>
                        <li><span class="counter">2</span> <span class="icon-Flaticon_20188"></span> <br><span class="sr-only">impulsos</span></li>
                        <li><span class="counter">6</span> <span class="icon-Flaticon_24178"></span> <br><span class="sr-only">propuestas</span></li>
                    </ul>
                    <a href="#" class="btn active">Adquirida</a>
                </div>
            </section>
            <section class="col-xs-12 col-sm-12 col-md-6">
                <h1>Lorem ipsum dolor sit amet, consectetur</h1>
                <p>Lorem ipsum dolor sit amet, consectetur do eiusmod <a href="#">tempor incididunt ut labore et dolore magna aliqua.</a></p>
                <div class="options">
                    <ul class="activity active">
                        <li><span class="counter">4</span> <span class="icon-Flaticon_17919"></span> <br><span class="sr-only">votos</span></li>
                        <li><span class="counter">2</span> <span class="icon-Flaticon_20188"></span> <br><span class="sr-only">impulsos</span></li>
                        <li><span class="counter">6</span> <span class="icon-Flaticon_24178"></span> <br><span class="sr-only">propuestas</span></li>
                    </ul>
                    <a href="#" class="btn">Canjear</a>
                </div>
            </section>
            <section class="col-xs-12 col-sm-12 col-md-6">
                <h1>Lorem ipsum dolor sit amet, consectetur</h1>
                <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod <a href="#">tempor incididunt ut labore et dolore magna aliqua.</a></p>
                <div class="options">
                    <ul class="activity active">
                        <li><span class="counter">4</span> <span class="icon-Flaticon_17919"></span> <br><span class="sr-only">votos</span></li>
                        <li><span class="counter">2</span> <span class="icon-Flaticon_20188"></span> <br><span class="sr-only">impulsos</span></li>
                        <li><span class="counter">6</span> <span class="icon-Flaticon_24178"></span> <br><span class="sr-only">propuestas</span></li>
                    </ul>
                    <a href="#" class="btn">Canjear</a>
                </div>
            </section>
            <section class="col-xs-12 col-sm-12 col-md-6">
                <h1>Lorem ipsum dolor sit amet, consectetur</h1>
                <p>Lorem ipsum dolor sit amet, consectetur do eiusmod <a href="#">tempor incididunt ut labore et dolore magna aliqua.</a></p>
                <div class="options">
                    <ul class="activity active">
                        <li><span class="counter">4</span> <span class="icon-Flaticon_17919"></span> <br><span class="sr-only">votos</span></li>
                        <li><span class="counter">2</span> <span class="icon-Flaticon_20188"></span> <br><span class="sr-only">impulsos</span></li>
                        <li><span class="counter">6</span> <span class="icon-Flaticon_24178"></span> <br><span class="sr-only">propuestas</span></li>
                    </ul>
                    <a href="#" class="btn disabled">Canjear</a>
                </div>
            </section>
        </div>
    </div><!-- /.skill -->
    <div class="item">
        <h2>Cómo consigo más items</h2>
        <ul>
            <li>
                <h3><span class="icon-Flaticon_17919"></span> Huevos</h3>
                <p>Cada vez que votas una ley consigues un huevo. Los huevos son un símbolo de participación. Las gallinas de Rebelión en la Granja prefirieron estallar sus huevos  contra el suelo antes de regalárselos al líder opresor, el cerdo Napoleón. Así que no  los malgastes, nunca se sabe cuando podrás necesitarlos.</p>
            </li>
            <li>
                <h3><span class="icon-Flaticon_24178"></span> Plumas</h3>
                <p>Cada vez que una de tus publicaciones alcanza los 10 impulsos recibes una pluma.  Para mantener el plumaje en buenas condiciones, una gallina debe mudar sus plumas  con frecuencia. Lo mismo ocurre con tus publicaciones. Publica frecuentemente para mantener la relevancia de tu perfil.</p>
            </li>
            <li>
                <h3><span class="icon-Flaticon_20188"></span> Maíz</h3>
                <p>Cuando impulsas las publicaciones de otros recibes un grano de maíz. La gallina es el ave más numerosa del planeta con 13 000 millones de ejemplares, y se alimenta   principalmente de maíz. En Korum, a los lectores más activos nunca les faltará el maíz.</p>
                <p>Si quieres acumular más granos de maíz de golpe, también puedes promocionar una publicación que te guste especialmente. Recuerda que las publicaciones promocionadas son de pago. Al promocionar una publicación no sólo estás dándole una mayor visibilidad, además estás contribuyendo a hacer posible este proyecto.</p>
            </li>
        </ul>
    </div><!-- /.item -->

    <H1> El gallinero de ${user.name}: ROLE: <g:message code="${GamificationAward.canonicalName}.${user.gamification.activeRole}.${user.personalData.gender}"/></H1>
    <H3>Gamification</H3>
    <ul>
        <li>HUEVOS: ${user.gamification.numEggs}</li>
        <li>corn: ${user.gamification.numCorns}</li>
        <li>Plumas: ${user.gamification.numPlumes}</li>
    </ul>
    <h3>Tienda</h3>
    <ul>
        <g:each in="${GamificationAward.values().findAll{it.toString().startsWith('ROLE_')}}" var="roleAward">
            <li>
                <g:message code="${GamificationAward.canonicalName}.${award}.${user.personalData.gender}"/>:
                %{--TODO: Pasar a taglib--}%
                <g:if test="${user.gamification.boughtAwards.contains(award)}">
                    <g:link mapping="profileActivateAward" params="[award:award]">Activar</g:link>
                </g:if>
                <g:else>
                    <g:link mapping="profileBuyAward" params="[award:award]">Comprar</g:link>
                </g:else>
            </li>
        </g:each>

    </ul>
</content>
