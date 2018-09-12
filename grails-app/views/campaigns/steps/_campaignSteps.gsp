<ul class="actionIcons ">
    <g:render template="/campaigns/steps/campaignStepActionButton" model="[
            show:mappings.settings,
            currentActive:mappings.step == 'settings',
            link:mappings.settings,
            faIcon:'fa-cogs',
            label:g.message(code:'tools.campaign.new.steps.settings')
    ]"/>
    <g:render template="/campaigns/steps/campaignStepActionButton" model="[
            show:mappings.district,
            currentActive:mappings.step == 'district',
            link:mappings.district,
            faIcon:'fa-money-bill-alt',
            label:g.message(code:'tools.campaign.new.steps.district')
    ]"/>
    <g:render template="/campaigns/steps/campaignStepActionButton" model="[
            show:attachEvent,
            currentActive:mappings.step == 'event',
            link:mappings.event,
            faIcon:'fa-globe',
            label:g.message(code:'tools.campaign.new.steps.eventData')
    ]"/>
    <g:render template="/campaigns/steps/campaignStepActionButton" model="[
            show:mappings.content,
            currentActive:mappings.step == 'content',
            link:mappings.content,
            faIcon:'fa-file-alt',
            label:g.message(code:'tools.campaign.new.steps.content')
    ]"/>
    <g:render template="/campaigns/steps/campaignStepActionButton" model="[
            show:mappings.questions,
            currentActive:mappings.step == 'questions',
            link:mappings.questions,
            faIcon:'fa-question-circle',
            label:g.message(code:'tools.campaign.new.steps.questions')
    ]"/>
    <g:render template="/campaigns/steps/campaignStepActionButton" model="[
            show:mappings.districts,
            currentActive:mappings.step == 'districts',
            link:mappings.districts,
            faIcon:'fa-globe',
            label:g.message(code:'tools.campaign.new.steps.districts')
    ]"/>
</ul>