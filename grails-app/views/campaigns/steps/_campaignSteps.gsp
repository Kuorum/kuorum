<ul class="actionIcons ">
    <li class="fontIcon ${mappings.step == 'settings' ?'active':''}">
        <a href="#" data-redirectLink="${mappings.settings}">
            <span class="fal fa-cogs"></span>
            <span class="label"><g:message code="tools.campaign.new.steps.settings"/></span>
        </a>
    </li>
    <g:if test="${mappings.district}">
        <li class="fontIcon arrow">
            <span class="fal fa-angle-right fa-3x"></span>
        </li>
        <li class="fontIcon ${mappings.step == 'district'?'active':''}">
            <a href="#" data-redirectLink="${mappings.district}">
                <span class="fal fa-money-bill-alt"></span>
                <span class="label"><g:message code="tools.campaign.new.steps.district"/></span>
            </a>
        </li>
    </g:if>
    <g:if test="${attachEvent}">
        <li class="fontIcon arrow">
            <span class="fal fa-angle-right fa-3x"></span>
        </li>
        <li class="fontIcon ${mappings.step == 'event'?'active':''} ${numberStepsClass}">
            <a href="#" data-redirectLink="${mappings.event}">
                <span class="fal fa-globe"></span>
                <span class="label"><g:message code="tools.campaign.new.steps.eventData"/></span>
            </a>
        </li>
    </g:if>
    <li class="fontIcon arrow">
        <span class="fal fa-angle-right fa-3x"></span>
    </li>
    <li class="fontIcon ${mappings.step == 'content'?'active':''} ${numberStepsClass}">
        <a href="#" data-redirectLink="${mappings.content}">
            <span class="fal fa-file-alt"></span>
            <span class="label"><g:message code="tools.campaign.new.steps.content"/></span>
        </a>
    </li>
    <g:if test="${mappings.questions}">
        <li class="fontIcon arrow">
            <span class="fal fa-angle-right fa-3x"></span>
        </li>
        <li class="fontIcon ${mappings.step == 'questions'?'active':''}">
            <a href="#" data-redirectLink="${mappings.questions}">
                <span class="fal fa-question-circle"></span>
                <span class="label"><g:message code="tools.campaign.new.steps.questions"/></span>
            </a>
        </li>
    </g:if>
    <g:if test="${mappings.districts}">
        <li class="fontIcon arrow">
            <span class="fal fa-angle-right fa-3x"></span>
        </li>
        <li class="fontIcon ${mappings.step == 'districts'?'active':''}">
            <a href="#" data-redirectLink="${mappings.districts}">
                <span class="fal fa-globe"></span>
                <span class="label"><g:message code="tools.campaign.new.steps.districts"/></span>
            </a>
        </li>
    </g:if>
</ul>