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
            show         :mappings.template,
            currentActive:mappings.step == 'template',
            link         :mappings.template,
            faIcon       :'fa-object-ungroup',
            label        : g.message(code: 'tools.campaign.new.steps.template')
    ]"/>
    <g:render template="/campaigns/steps/campaignStepActionButton" model="[
            show         : attachEvent,
            currentActive: mappings.step == 'event',
            link         : mappings.event,
            faIcon       : 'fa-globe',
            label        : g.message(code: 'tools.campaign.new.steps.eventData')
    ]"/>
    <g:render template="/campaigns/steps/campaignStepActionButton" model="[
            show         : mappings.environment,
            currentActive: mappings.step == 'environment',
            link         : mappings.environment,
            faIcon       : 'fa-globe-europe',
            label        : g.message(code: 'tools.campaign.new.steps.environment')
    ]"/>
    <g:render template="/campaigns/steps/campaignStepActionButton" model="[
            show         : mappings.content,
            currentActive: mappings.step == 'content',
            link         : mappings.content,
            faIcon       : 'fa-file-alt',
            label        : g.message(code: 'tools.campaign.new.steps.content')
    ]"/>
    <g:render template="/campaigns/steps/campaignStepActionButton" model="[
            show         : mappings.deadlines,
            currentActive: mappings.step == 'deadlines',
            link         : mappings.deadlines,
            faIcon       : 'fa-calendar-exclamation',
            label        : g.message(code: 'tools.campaign.new.steps.deadlines')
    ]"/>
    <g:render template="/campaigns/steps/campaignStepActionButton" model="[
            show         : mappings.questions,
            currentActive: mappings.step == 'questions',
            link         : mappings.questions,
            faIcon       : 'fa-question-circle',
            label        : g.message(code: 'tools.campaign.new.steps.questions')
    ]"/>
    <g:render template="/campaigns/steps/campaignStepActionButton" model="[
            show         : mappings.districts,
            currentActive: mappings.step == 'districts',
            link         : mappings.districts,
            faIcon       : 'fa-globe',
            label        : g.message(code: 'tools.campaign.new.steps.districts')
    ]"/>
    <g:render template="/campaigns/steps/campaignStepActionButton" model="[
            show         : mappings.configContest,
            currentActive: mappings.step == 'configContest',
            link         : mappings.configContest,
            faIcon       : 'fa-tools',
            label        : g.message(code: 'tools.campaign.new.steps.configContest')
    ]"/>
    <g:render template="/campaigns/steps/campaignStepActionButton" model="[
            show         : mappings.authorizations,
            currentActive: mappings.step == 'authorizations',
            link         : mappings.authorizations,
            faIcon       : 'fa-hands-helping',
            label        : g.message(code: 'tools.campaign.new.steps.authorizations')
    ]"/>
</ul>