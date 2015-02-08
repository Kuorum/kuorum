
<li class="vote-yes">
    <span><g:formatNumber number="${projectStats.percentagePositiveVotes}" type="percent"/> </span>
    <span class="sr-only"><g:message code="project.subHeader.positiveVotes"/></span>
    <span class="icon-smiley fa-lg"></span>
</li>
<li class="vote-no">
    <span><g:formatNumber number="${projectStats.percentageNegativeVotes}" type="percent"/> </span>
    <span class="sr-only"><g:message code="project.subHeader.negativeVotes"/></span>
    <span class="icon-sad fa-lg"></span>
</li>
<li class="vote-neutral">
    <span><g:formatNumber number="${projectStats.percentageAbsVotes}" type="percent"/> </span>
    <span class="sr-only"><g:message code="project.subHeader.absVotes"/></span>
    <span class="icon-neutral fa-lg"></span>
</li>
<li>
    <span>${projectStats.numPublicPosts}</span>
    <span class="sr-only"><g:message code="project.subHeader.numPosts"/></span>
    <span class="fa fa-lightbulb-o fa-lg"></span>
</li>