<div id="contest-applications-list" contest-applications-list="${contest.status}">
    <ul class="nav nav-pills nav-pills-lvl2 nav-underline ${contest.status != org.kuorum.rest.model.communication.contest.ContestStatusDTO.VOTING ? 'hide' : ''}">
        <li class="${contest.status == org.kuorum.rest.model.communication.contest.ContestStatusDTO.VOTING ? "active active-no-click" : ""}">
            <a href="#" data-listSelector="random">
                <g:message code="participatoryBudget.show.listProposals.sort.random"/>
            </a>
        </li>
        <li class="${contest.status != org.kuorum.rest.model.communication.contest.ContestStatusDTO.VOTING ? "active active-no-click" : ""}">
            <a href="#" data-listSelector="vote" data-direction="DESC">
                <g:message code="contest.show.applications.list.sort.byVotes"/><span class="fal "></span>
            </a>
        </li>
    </ul>
    %{--    </g:if>--}%
    <ul class="search-list clearfix random"
        data-page="0"
        data-randomSeed=""
        data-loadProposals="${g.createLink(mapping: 'contestListApplications', params: contest.encodeAsLinkProperties())}"></ul>
    <ul class="search-list clearfix vote"
        data-page="0"
        data-direction="DESC"
        data-loadProposals="${g.createLink(mapping: 'contestListApplications', params: contest.encodeAsLinkProperties())}"></ul>
</div>