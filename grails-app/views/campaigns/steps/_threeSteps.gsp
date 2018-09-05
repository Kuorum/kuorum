<ul class="actionIcons newsletter-steps">
    <li class="fontIcon ${editReference == 'politicianMassMailingSettings'?'active':''}">
        <a href="#" data-redirectLink="politicianMassMailingSettings">
            <span class="fal fa-cogs"></span>
            <span class="label"><g:message code="tools.campaign.new.steps.settings"/></span>
        </a>
    </li>
    <li class="fontIcon arrow">
        <span class="fal fa-angle-right fa-3x"></span>
    </li>
    <li class="fontIcon ${editReference == 'politicianMassMailingTemplate'?'active':''} newsletter-steps threeSteps">
        <a href="#" data-redirectLink="politicianMassMailingTemplate">
            <span class="fal fa-object-ungroup"></span>
            <span class="label"><g:message code="tools.campaign.new.steps.template"/></span>
        </a>
    </li>
    <li class="fontIcon arrow">
        <span class="fal fa-angle-right fa-3x"></span>
    </li>
    <li class="fontIcon ${editReference == 'politicianMassMailingContent'?'active':''} newsletter-steps threeSteps">
        <a href="#" data-redirectLink="politicianMassMailingContent">
            <span class="fal fa-file-alt"></span>
            <span class="label"><g:message code="tools.campaign.new.steps.content"/></span>
        </a>
    </li>
</ul>