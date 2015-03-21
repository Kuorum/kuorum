<%@ page import="kuorum.core.model.gamification.GamificationAward" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.kuorumStore"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <g:render template="leftMenu" model="[user:user, activeMapping:'toolsKuorumStore', menu:menu]"/>
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
                            <gamification:ifAwardIsBought role="${roleAward}">
                                <g:set var="cssInactive" value="inactive"/>
                            </gamification:ifAwardIsBought>
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
        <h2><g:message code="profile.kuorumStore.skills.title"/></h2>
        <div class="row">
            <g:each in="${GamificationAward.values().findAll{!it.toString().startsWith('ROLE_')}}" var="skillAward">
                <section class="col-xs-12 col-sm-12 col-md-6">
                    <h1><g:message code="${GamificationAward.canonicalName}.${skillAward}.${user.personalData.gender}"/></h1>
                    <p><g:message code="${GamificationAward.canonicalName}.${skillAward}.description"/></p>
                    <div class="options">
                        <g:set var="cssInactive" value=""/>
                        <gamification:ifAwardIsBought role="${skillAward}">
                            <g:set var="cssInactive" value="inactive"/>
                        </gamification:ifAwardIsBought>
                        <ul class="activity ${cssInactive}">
                            <li><span class="counter">${skillAward.numEggs}</span> <span class="icon-Flaticon_17919"></span> <br><span class="sr-only"><g:message code="profile.kuorumStore.eggs.description"/></span></li>
                            <li><span class="counter">${skillAward.numCorns}</span> <span class="icon-Flaticon_20188"></span> <br><span class="sr-only"><g:message code="profile.kuorumStore.corns.description"/></span></li>
                            <li><span class="counter">${skillAward.numPlumes}</span> <span class="icon-Flaticon_24178"></span> <br><span class="sr-only"><g:message code="profile.kuorumStore.plumes.description"/></span></li>
                        </ul>
                        %{--<a href="#" class="btn active">Adquirida</a>--}%
                        <gamification:skillButton skill="${skillAward}"/>
                    </div>
                </section>
            </g:each>
        </div>
    </div><!-- /.skill -->
    <div class="item">
        <h2><g:message code="profile.kuorumStore.awards.explanation.title"/></h2>
        <ul>
            <li>
                <h3><span class="icon-Flaticon_17919"></span> <g:message code="profile.kuorumStore.awards.explanation.eggs"/></h3>
                <p><g:message code="profile.kuorumStore.awards.explanation.eggs.description"/></p>
            </li>
            <li>
                <h3><span class="icon-Flaticon_24178"></span> <g:message code="profile.kuorumStore.awards.explanation.plumes"/></h3>
                <p><g:message code="profile.kuorumStore.awards.explanation.plumes.description"/></p>
            </li>
            <li>
                <h3><span class="icon-Flaticon_20188"></span> <g:message code="profile.kuorumStore.awards.explanation.corn"/></h3>
                <p><g:message code="profile.kuorumStore.awards.explanation.corn.description1"/></p>
                %{--<p><g:message code="profile.kuorumStore.awards.explanation.corn.description2"/></p>--}%
            </li>
        </ul>
    </div><!-- /.item -->
</content>
