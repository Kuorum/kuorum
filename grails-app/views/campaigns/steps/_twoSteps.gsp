<g:set var="numberStepsClass" value="twoSteps"/>
<g:if test="${attachEvent || mappings.questions}">
    <g:set var="numberStepsClass" value="threeSteps"/>
</g:if>
<ul class="actionIcons campaign-steps ${numberStepsClass}">
    <li class="fontIcon ${mappings.step == 'settings' ?'active':''}">
        <a href="#" data-redirectLink="${mappings.settings}">
            <span class="fa fa-gears"></span>
            <span class="label"><g:message code="tools.campaign.new.steps.settings"/></span>
        </a>
    </li>
    <li class="fontIcon arrow">
        <span class="fa fa-angle-right fa-3x"></span>
    </li>
    <g:if test="${attachEvent}">
        <li class="fontIcon ${mappings.step == 'event'?'active':''} campaign-steps ${numberStepsClass}">
            <a href="#" data-redirectLink="${mappings.event}">
                <span class="fa fa-globe"></span>
                <span class="label"><g:message code="tools.campaign.new.steps.eventData"/></span>
            </a>
        </li>
        <li class="fontIcon arrow">
            <span class="fa fa-angle-right fa-3x"></span>
        </li>
    </g:if>
    <li class="fontIcon ${mappings.step == 'content'?'active':''} campaign-steps ${numberStepsClass}">
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
</ul>