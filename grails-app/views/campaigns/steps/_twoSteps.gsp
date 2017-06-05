<ul id="mails" class="campaigns twoSteps">
    <li class="fontIcon ${mappings.step == 'settings' ?'active':''}">
        <a href="#" data-redirectLink="${mappings.settings}">
            <span class="fa fa-gears"></span>
            <span class="label"><g:message code="tools.campaign.new.steps.settings"/></span>
        </a>
    </li>
    <li class="fontIcon arrow">
        <span class="fa fa-angle-right fa-3x"></span>
    </li>
    <li class="fontIcon ${mappings.step == 'content'?'active':''}">
        <a href="#" data-redirectLink="${mappings.content}">
            <span class="fa fa-file-text-o"></span>
            <span class="label"><g:message code="tools.campaign.new.steps.content"/></span>
        </a>
    </li>
</ul>