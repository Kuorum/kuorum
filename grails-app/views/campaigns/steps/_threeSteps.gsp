<ul id="mails" class="campaigns threeSteps">
    <li class="fontIcon ${editReference == 'politicianMassMailingSettings'?'active':''}">
        <a href="#" data-redirectLink="politicianMassMailingEdit1">
            <span class="fa fa-gears"></span>
            <span class="label"><g:message code="tools.campaign.new.steps.settings"/></span>
        </a>
    </li>
    <li class="fontIcon arrow">
        <span class="fa fa-angle-right fa-3x"></span>
    </li>
    <li class="fontIcon ${editReference == 'politicianMassMailingTemplate'?'active':''}">
        <a href="#" data-redirectLink="politicianMassMailingEdit2">
            <span class="fa fa-object-ungroup"></span>
            <span class="label"><g:message code="tools.campaign.new.steps.template"/></span>
        </a>
    </li>
    <li class="fontIcon arrow">
        <span class="fa fa-angle-right fa-3x"></span>
    </li>
    <li class="fontIcon ${editReference == 'politicianMassMailingContent'?'active':''}">
        <a href="#" data-redirectLink="politicianMassMailingEdit3">
            <span class="fa fa-file-text-o"></span>
            <span class="label"><g:message code="tools.campaign.new.steps.content"/></span>
        </a>
    </li>
</ul>