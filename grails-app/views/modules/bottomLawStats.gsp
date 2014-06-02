<ul>
    <li><span class="fa fa-heart fa-lg"></span> <g:message code="law.module.footerStats.publicPosts" args="[lawStats.numPublicPosts, lawStats.nomVotesToBePublic]"/></li>
    <g:if test="${lawStats.lastActivity}">
        <li><span class="icon-clock2 fa-lg"></span><g:message code="law.module.footerStats.lastActivity"/> <kuorumDate:humanDate date="${lawStats.lastActivity}"/></li>
    </g:if>
    %{--<li><span class="icon-eye fa-lg"></span> 3.228 vistas de esta ley.</li>--}%
    <li><span class="icon-users fa-lg"></span> <g:message code="law.module.footerStats.totalPosts" args="[lawStats.numUsers]" /></li>
</ul>