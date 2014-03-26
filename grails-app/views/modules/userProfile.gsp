

<H1><g:link mapping="userShow" params="${user.encodeAsLinkProperties()}">${user.name}</g:link></H1>
<div>${numPost} || ${numFollowers} || ${numFollowing}</div>
<div>ROLE: <g:message code="kuorum.core.model.GamificationAward.${user.gamification.activeRole}.${user.personalData.gender}"/> </div>