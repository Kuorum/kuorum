<%@ page import="kuorum.core.model.VoteType" %>
<ul>
    <li class="${VoteType.POSITIVE}">
        <g:link mapping="projectVote" params="${project.encodeAsLinkProperties()+[voteType:VoteType.POSITIVE]}" role="button" class="ajaxVote ${userVote?.voteType.equals(VoteType.POSITIVE)?"active":""}" data-projectId="${project.id}">
            <span class="icon-smiley ${cssIconSize}"></span>
            <span class="${header?'sr-only':''}"><g:message code="project.vote.yes"/></span>
        </g:link>
    </li>
    <li class="${VoteType.NEGATIVE}">
        <g:link mapping="projectVote" params="${project.encodeAsLinkProperties()+[voteType:VoteType.NEGATIVE]}" role="button" class="ajaxVote ${userVote?.voteType.equals(VoteType.NEGATIVE)?"active":""}" data-projectId="${project.id}">
            <span class="icon-sad ${cssIconSize}"></span>
            <span class="${header?'sr-only':''}"><g:message code="project.vote.no"/></span>
        </g:link>

    </li>
    <li class="${VoteType.ABSTENTION}">
        <g:link mapping="projectVote" params="${project.encodeAsLinkProperties()+[voteType:VoteType.ABSTENTION]}" role="button" class="ajaxVote ${userVote?.voteType.equals(VoteType.ABSTENTION)?"active":""}" data-projectId="${project.id}">
            <span class="icon-neutral ${cssIconSize}"></span>
            <span class="${header?'sr-only':''}"><g:message code="project.vote.abs"/></span>
        </g:link>
    </li>
    <projectUtil:ifAllowedToAddPost project="${project}">
        <li>
            <g:link mapping="postCreate" params="${project.encodeAsLinkProperties()}" role="button" class="propose" data-projectId="${project.id}">
                <span class="fa fa-lightbulb-o ${cssIconSize}"></span>
                <span class="${header?'sr-only':''}"><g:message code="project.vote.newPost"/></span>
            </g:link>
        </li>
    </projectUtil:ifAllowedToAddPost>

    <projectUtil:ifAllowedToUpdateProject project="${project}">
        <li>
            <g:link mapping="projectUpdate" params="${project.encodeAsLinkProperties()}" class="update">
                <span class="icon2-update ${cssIconSize}"></span>
                <span class="${header?'sr-only':''}"><g:message code="project.vote.updateProject"/></span>
            </g:link>
        </li>
    </projectUtil:ifAllowedToUpdateProject>
</ul>