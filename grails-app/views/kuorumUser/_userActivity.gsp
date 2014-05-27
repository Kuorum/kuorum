<ul class="infoActivity clearfix">
    <li>
        <span class="fa fa-question-circle"></span>
        <span class="counter">${user.activity.numQuestions}</span>
        <span class="sr-only"><g:message code="kuorumUser.popover.questions"/></span>
    </li>
    <li>
        <span class="fa fa-comment"></span>
        <span class="counter">${user.activity.numHistories}</span>
        <span class="sr-only"><g:message code="kuorumUser.popover.histories"/></span>
    </li>
    <li>
        <span class="fa fa-lightbulb-o"></span>
        <span class="counter">${user.activity.numPurposes}</span>
        <span class="sr-only"><g:message code="kuorumUser.popover.purposes"/></span>
    </li>
    <li>
        <span class="fa fa-user"></span>
        <small><span class="fa fa-forward"></span></small>
        <span class="counter">${user.following.size()}</span>
        <span class="sr-only"><g:message code="kuorumUser.popover.following"/></span>
    </li>
    <li>
        <span class="fa fa-user"></span>
        <small><span class="fa fa-backward"></span></small>
        <span class="counter">${user.numFollowers}</span>
        <span class="sr-only"><g:message code="kuorumUser.popover.followers"/></span>
    </li>
    <g:if test="${user.verified}">
        <li class="pull-right">
            <span class="sr-only"><g:message code="kuorumUser.verified"/></span>
            <span class="fa fa-check"></span>
        </li>
    </g:if>
    <g:else>
        <li></li>
    </g:else>
</ul>