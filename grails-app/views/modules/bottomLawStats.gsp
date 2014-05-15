<ul>
    <li><span class="fa fa-heart fa-lg"></span> ${lawStats.numPostsWithManyVotes} propuestas han alcanzado los 1.000 apoyos.</li>
    <g:if test="${lawStats.lastActivity}">
        <li><span class="icon-clock2 fa-lg"></span> última actividad sobre la ley <kuorumDate:humanDate date="${lawStats.lastActivity}"/></li>
    </g:if>
    <li><span class="icon-eye fa-lg"></span> 3.228 vistas de esta ley.</li>
    <li><span class="icon-users fa-lg"></span> ${lawStats.numPosts} personas actuando sobre la ley.</li>
</ul>