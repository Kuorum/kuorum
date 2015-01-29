<ul>
    <li><span class="fa fa-heart fa-lg"></span> <g:message code="project.module.footerStats.publicPosts" args="[projectStats.numPublicPosts, projectStats.nomVotesToBePublic]"/></li>
    <g:if test="${projectStats.lastActivity}">
        <li><span class="icon-clock2 fa-lg"></span><g:message code="project.module.footerStats.lastActivity"/> <kuorumDate:humanDate date="${projectStats.lastActivity}"/></li>
    </g:if>
    %{--<li><span class="icon-eye fa-lg"></span> 3.228 vistas de esta ley.</li>--}%
    <li><span class="icon-users fa-lg"></span> <g:message code="project.module.footerStats.totalPosts" args="[projectStats.numUsers]" /></li>
</ul>