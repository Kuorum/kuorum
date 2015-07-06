%{--<g:form url="[mapping:'projectVoteNoTotalUser', params:project.encodeAsLinkProperties()]" method="post" name="basicUserDataForm" role="form" autocomplete="off" >--}%
    <ul class="">
        <li>
            <g:link mapping="projectVoteNoTotalUser" params="${project.encodeAsLinkProperties()}" role="button" data-voteType="${kuorum.core.model.VoteType.POSITIVE}">
                <span class="icon-smiley ${cssIconSize}"></span> <span class="sr-only"><g:message code="project.vote.yes"/></span>
            </g:link>
        </li>
        <li>
            <g:link mapping="projectVoteNoTotalUser" params="${project.encodeAsLinkProperties()}" role="button" data-voteType="${kuorum.core.model.VoteType.POSITIVE}">
                <span class="icon-sad ${cssIconSize}"></span> <span class="sr-only"><g:message code="project.vote.no"/></span>
            </g:link>
        </li>
        <li>
            <g:link mapping="projectVoteNoTotalUser" params="${project.encodeAsLinkProperties()}" role="button">
                <span class="icon-neutral ${cssIconSize}"></span> <span class="sr-only"><g:message code="project.vote.abs" data-voteType="${kuorum.core.model.VoteType.POSITIVE}"/></span>
            </g:link>
        </li>
        <li>
        <g:link mapping="projectVoteNoTotalUser" params="${project.encodeAsLinkProperties()}" role="button" class="propose">
            <span class="fa fa-lightbulb-o ${cssIconSize}"></span> <span class="sr-only"><g:message code="project.vote.newPost"/></span>
        </g:link>
        </li>
    </ul>
%{--</g:form>--}%