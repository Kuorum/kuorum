<%@ page import="kuorum.core.model.gamification.GamificationAward" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.kuorumStore"/> </title>
    <meta name="layout" content="normalLayout">
</head>


<content tag="mainContent">
    <H1> El gallinero de ${user.name}: ROLE: <g:message code="${GamificationAward.canonicalName}.${user.gamification.activeRole}.${user.personalData.gender}"/></H1>
    <H3>Gamification</H3>
    <ul>
        <li>HUEVOS: ${user.gamification.numEggs}</li>
        <li>corn: ${user.gamification.numCorns}</li>
        <li>Plumas: ${user.gamification.numPlumes}</li>
    </ul>
    <h3>Tienda</h3>
    <ul>
        <g:each in="${GamificationAward.values()}" var="award">
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
