<ul class="actionIcons ">
    <li class="fontIcon ${mappings.step == 'settings' ?'active':''}">
        <a href="#" data-redirectLink="${mappings.settings}">
            <span class="fa fa-gears"></span>
            <span class="label"><g:message code="tools.campaign.new.steps.settings"/></span>
        </a>
    </li>
    <g:if test="${mappings.district}">
        <li class="fontIcon arrow">
            <span class="fa fa-angle-right fa-3x"></span>
        </li>
        <li class="fontIcon ${mappings.step == 'district'?'active':''}">
            <a href="#" data-redirectLink="${mappings.district}">
                <span class="fa fa-money"></span>
                <span class="label"><g:message code="tools.campaign.new.steps.district"/></span>
            </a>
        </li>
    </g:if>
    <g:if test="${attachEvent}">
        <li class="fontIcon arrow">
            <span class="fa fa-angle-right fa-3x"></span>
        </li>
        <li class="fontIcon ${mappings.step == 'event'?'active':''} ${numberStepsClass}">
            <a href="#" data-redirectLink="${mappings.event}">
                <span class="fa fa-globe"></span>
                <span class="label"><g:message code="tools.campaign.new.steps.eventData"/></span>
            </a>
        </li>
        <li class="fontIcon arrow">
            <span class="fa fa-angle-right fa-3x"></span>
        </li>
    </g:if>
    <li class="fontIcon arrow">
        <span class="fa fa-angle-right fa-3x"></span>
    </li>
    <li class="fontIcon ${mappings.step == 'content'?'active':''} ${numberStepsClass}">
        <a href="#" data-redirectLink="${mappings.content}">
            <span class="fa fa-file-text-o"></span>
            <span class="label"><g:message code="tools.campaign.new.steps.content"/></span>
        </a>
    </li>
    <g:if test="${mappings.questions}">
        <li class="fontIcon arrow">
            <span class="fa fa-angle-right fa-3x"></span>
        </li>
        <li class="fontIcon ${mappings.step == 'questions'?'active':''}">
            <a href="#" data-redirectLink="${mappings.questions}">
                <span class="fa fa-question-circle-o"></span>
                <span class="label"><g:message code="tools.campaign.new.steps.questions"/></span>
            </a>
        </li>
    </g:if>
    <g:if test="${mappings.districts}">
        <li class="fontIcon arrow">
            <span class="fa fa-angle-right fa-3x"></span>
        </li>
        <li class="fontIcon ${mappings.step == 'districts'?'active':''}">
            <a href="#" data-redirectLink="${mappings.districts}">
                <span class="fa fa-globe"></span>
                <span class="label"><g:message code="tools.campaign.new.steps.districts"/></span>
            </a>
        </li>
    </g:if>
</ul>