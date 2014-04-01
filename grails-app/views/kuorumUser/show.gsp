<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="kuorum.name"/> </title>
    <meta name="layout" content="normalLayout">
</head>


<content tag="mainContent">
    <H1> Perfil de ${user.name}</H1>
    <H3>Gamification</H3>
     <ul>
         <li>HUEVOS: ${user.gamification.numEggs}</li>
         <li>corn: ${user.gamification.numCorns}</li>
         <li>Plumas: ${user.gamification.numPlumes}</li>
     </ul>

</content>

<content tag="cColumn">
    Columna C de dashboard
</content>
