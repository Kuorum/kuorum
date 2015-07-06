<%@ page import="kuorum.core.model.VoteType" %>
<ul class="noMailConfirmedVoteDiv">
    <li class="${VoteType.POSITIVE}">
        <a role="button" data-projectId="${project.id}">
            <span class="icon-smiley ${cssIconSize}"></span>
            <span class="${header?'sr-only':''}"><g:message code="project.vote.yes"/></span>
        </a>
    </li>
    <li class="${VoteType.NEGATIVE}">
        <a role="button" data-projectId="${project.id}">
            <span class="icon-sad ${cssIconSize}"></span>
            <span class="${header?'sr-only':''}"><g:message code="project.vote.no"/></span>
        </a>

    </li>
    <li class="${VoteType.ABSTENTION}">
        <a role="button" data-projectId="${project.id}">
            <span class="icon-neutral ${cssIconSize}"></span>
            <span class="${header?'sr-only':''}"><g:message code="project.vote.abs"/></span>
        </a>
    </li>
    <li>
        <a role="button" data-projectId="${project.id}" class="propose">
            <span class="fa fa-lightbulb-o ${cssIconSize}"></span>
            <span class="${header?'sr-only':''}"><g:message code="project.vote.newPost"/></span>
        </a>
    </li>
</ul>
<script>
    $(function(){
        i18n.showMailConfirm = "${userUtil.showMailConfirm()}"
    })
</script>