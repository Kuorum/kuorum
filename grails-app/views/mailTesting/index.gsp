<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="kuorum.name"/> </title>
    <meta name="layout" content="normalLayout">
</head>


<content tag="mainContent">
    <H1> Test de envio de emails </H1>
    <h3>email: ${email}</h3>

    <g:link action="testDebateNotificationPolitician"               params="[email:email, lang:lang]">Test envio mail de debate a un político</g:link><br/>
    <g:link action="testDebateNotificationInterestedUsers"          params="[email:email, lang:lang]">Test envio mail de debate a los usuarios interesados</g:link><br/>
    <g:link action="testDebateNotificationAuthor"                   params="[email:email, lang:lang]">Test envio mail de debate al autor del debate</g:link><br/>
    <g:link action="testPostDefendNotificationAuthor"               params="[email:email, lang:lang]">Test envio mail al autor tras un apadrinamiento</g:link><br/>
    <g:link action="testPostDefendedNotificationPeopleInterested"   params="[email:email, lang:lang]">Test envio mail a la gente que ha votado tras un apadrinamiento</g:link><br/>
    <g:link action="testPostDefendedNotificationPoliticians"        params="[email:email, lang:lang]">Test envio mail a los politicos tras un apadrinamiento</g:link><br/>
    <g:link action="testPublicMilestone"                            params="[email:email, lang:lang]">Test envio mail un post se hace publico</g:link><br/>
    <g:link action="testCluck"                                      params="[email:email, lang:lang]">Test envio mail un cluck</g:link><br/>
    <g:link action="testNewFollower"                                params="[email:email, lang:lang]">Test envio mail nuevo follower</g:link><br/>
    <g:link action="testVictoryUsers"                               params="[email:email, lang:lang]">Test envio mail victoria a usuarios</g:link><br/>
    <g:link action="testVictoryDefender"                            params="[email:email, lang:lang]">Test envio mail victoria al politico que la ha defendido</g:link><br/>
    <g:link action="testRegister"                                   params="[email:email, lang:lang]">Test envio mail registro</g:link><br/>
    <g:link action="testRegisterRRSS"                               params="[email:email, lang:lang]">Test envio mail registro via RRSS</g:link><br/>
    <g:link action="testAccountConfirmed"                           params="[email:email, lang:lang]">Test envio mail cuenta confirmada</g:link><br/>
</content>

<content tag="cColumn">
    ${flash.message}
</content>
