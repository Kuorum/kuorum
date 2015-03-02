<%@ page import="kuorum.core.model.UserType" %>

<g:if test="${UserType.POLITICIAN == user.userType}">
    <li><span class="counter">${user.politicianActivity.numVictories}</span> <br><g:message code="kuorumUser.show.records.victoryPosts.title"/> </li>
    <li><span class="counter">${user.politicianActivity.numDefends}</span> <br><g:message code="kuorumUser.show.records.defendedPosts.title"/></li>
    <li><span class="counter">${user.politicianActivity.numDebates}</span> <br><g:message code="kuorumUser.show.records.debatePost.title"/></li>
</g:if>
<g:else>
    <li><span class="counter">${user.gamification.numEggs}</span> <span class="icon-Flaticon_17919"></span> <br> <g:message code="profile.kuorumStore.eggs.description"/> </li>
    <li><span class="counter">${user.gamification.numCorns}</span> <span class="icon-Flaticon_20188"></span> <br> <g:message code="profile.kuorumStore.corns.description"/> </li>
    <li><span class="counter">${user.gamification.numPlumes}</span> <span class="icon-Flaticon_24178"></span> <br> <g:message code="profile.kuorumStore.plumes.description"/> </li>
</g:else>