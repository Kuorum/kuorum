<g:each in="${contestApplications}" var="campaign" status="pos">
    <li id="campaignRanking_${campaign.id}">
        <div class="id sr-only">${campaign.id}</div>

        <div class="ranking-pos">
            ${pos + 1}
        </div>

        <div class="ranking-campaign-info">
            <h3 class="ranking-campaign-title">
                <g:link mapping="campaignShow" params="${campaign.encodeAsLinkProperties()}"
                        class="title">${campaign.title}<span></span></g:link>
                <g:link mapping="campaignShow" params="${campaign.encodeAsLinkProperties()}" target="_blank" rel="noopener noreferrer">

                    <span class="fal fa-external-link fa-sm"></span><span class="sr-only"><g:message code="tools.contact.list.contact.edit"/></span>
                </g:link>
            </h3>
            <h4 class="ranking-campaign-author">
                <g:link mapping="userShow" params="${campaign.user.encodeAsLinkProperties()}"
                        class="title">${campaign.user.name}<span></span></g:link>
                <g:link mapping="userShow" params="${campaign.user.encodeAsLinkProperties()}" target="_blank" rel="noopener noreferrer">

                    <span class="fal fa-external-link fa-sm"></span><span class="sr-only"><g:message code="tools.contact.list.contact.edit"/></span>
                </g:link>
            </h4>
        </div>
        <ul class="list-campaign-extra-data">
            <li class="ranking-cause">
                ${campaign.causes[0]}
            </li>
            <li class="ranking-contest-focusType">
                <g:message
                        code="org.kuorum.rest.model.communication.contest.ContestApplicationFocusTypeDTO.${campaign.focusType}"/>
            </li>
            <li class="ranking-contest-activityType">
                <g:message
                        code="org.kuorum.rest.model.communication.contest.ContestApplicationActivityTypeDTO.${campaign.activityType}"/>
            </li>
            <li class="ranking-numVotes">
                ${campaign.votes}
            </li>
        </ul>
    </li>
</g:each>
