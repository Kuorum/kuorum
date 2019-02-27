<div class="section-header">
    <h1><g:message code="landingServices.howItWorks.title"/></h1>
    <h3 class="hidden-xs"><g:message code="landingServices.howItWorks.subtitle"/></h3>
</div>
<div class="row section-body">
    <ul class="globus">
        <g:render template="/landing/servicesModules/serviceActiveCampaign"
                  model="[
                          landingVisibleRoles:landingVisibleRoles,
                          role:org.kuorum.rest.model.kuorumUser.UserRoleRSDTO.ROLE_CAMPAIGN_POST,
                          icon:'fa-newspaper',
                          searchMapping:'searcherSearchPOST',
                          title:g.message(code:'landingServices.howItWorks.post.title'),
                          text:g.message(code:'landingServices.howItWorks.post.text')
                  ]"/>
        <g:render template="/landing/servicesModules/serviceActiveCampaign"
                  model="[
                          landingVisibleRoles:landingVisibleRoles,
                          role:org.kuorum.rest.model.kuorumUser.UserRoleRSDTO.ROLE_CAMPAIGN_DEBATE,
                          icon:'fa-comments',
                          searchMapping:'searcherSearchDEBATE',
                          title:g.message(code:'landingServices.howItWorks.debates.title'),
                          text:g.message(code:'landingServices.howItWorks.debates.text')
                  ]"/>
        <g:render template="/landing/servicesModules/serviceActiveCampaign"
                  model="[
                          landingVisibleRoles:landingVisibleRoles,
                          role:org.kuorum.rest.model.kuorumUser.UserRoleRSDTO.ROLE_CAMPAIGN_SURVEY,
                          icon:'fa-chart-bar',
                          searchMapping:'searcherSearchSURVEY',
                          title:g.message(code:'landingServices.howItWorks.surveys.title'),
                          text:g.message(code:'landingServices.howItWorks.surveys.text')
                  ]"/>
        <g:render template="/landing/servicesModules/serviceActiveCampaign"
                  model="[
                          landingVisibleRoles:landingVisibleRoles,
                          role:org.kuorum.rest.model.kuorumUser.UserRoleRSDTO.ROLE_CAMPAIGN_EVENT,
                          icon:'fa-calendar-check',
                          searchMapping:'searcherSearchEVENT',
                          title:g.message(code:'landingServices.howItWorks.events.title'),
                          text:g.message(code:'landingServices.howItWorks.events.text')
                  ]"/>
        <g:render template="/landing/servicesModules/serviceActiveCampaign"
                  model="[
                          landingVisibleRoles:landingVisibleRoles,
                          role:org.kuorum.rest.model.kuorumUser.UserRoleRSDTO.ROLE_CAMPAIGN_PETITION,
                          icon:'fa-microphone',
                          searchMapping:'searcherSearchPETITION',
                          title:g.message(code:'landingServices.howItWorks.petition.title'),
                          text:g.message(code:'landingServices.howItWorks.petition.text')
                  ]"/>
        <g:render template="/landing/servicesModules/serviceActiveCampaign"
                  model="[
                          landingVisibleRoles:landingVisibleRoles,
                          role:org.kuorum.rest.model.kuorumUser.UserRoleRSDTO.ROLE_CAMPAIGN_PARTICIPATORY_BUDGET,
                          icon:'fa-money-bill-alt',
                          searchMapping:'searcherSearchPARTICIPATORY_BUDGET',
                          title:g.message(code:'landingServices.howItWorks.participatoryBudget.title'),
                          text:g.message(code:'landingServices.howItWorks.participatoryBudget.text')
                  ]"/>
        <g:render template="/landing/servicesModules/serviceActiveCampaign"
                  model="[
                          landingVisibleRoles:landingVisibleRoles,
                          role:org.kuorum.rest.model.kuorumUser.UserRoleRSDTO.ROLE_CAMPAIGN_NEWSLETTER,
                          icon:'fa-envelope',
                          searchMapping:'searcherSearch',
                          title:g.message(code:'landingServices.howItWorks.newsletters.title'),
                          text:g.message(code:'landingServices.howItWorks.newsletters.text')
                  ]"/>

    </ul>
</div>