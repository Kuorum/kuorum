<!-- ^leader-post !-->
<div class="leader-post">
    <g:render template="/campaigns/showModules/campaignDataMultimedia" model="[campaign: contestApplication]"/>
    <div class="header">
        <h1 class="title" itemprop="headline">${contestApplication.title}</h1>
        <userUtil:showUser user="${campaignUser}" showRole="true" itemprop="author"/>
        %{--<g:render template="/participatoryBudget/showModules/mainContent/participatoryBudgetDataMobileCallToAction"/>--}%
        <g:render template="/campaigns/showModules/campaignDataDatePublished"
                  model="[campaign: contestApplication, campaignUser: campaignUser, editMappingName: 'contestApplicationEditContent']"/>
    </div>

    <div class="body" itemprop="articleBody">
        ${raw(contestApplication.body)}
    </div>

    <div class="footer clearfix">
        <g:render template="/campaigns/showModules/campaignDataLabels" model="[causes: contestApplication.causes]"/>
        <g:render template="/campaigns/showModules/campaignDataSocial" model="[campaign: contestApplication]"/>
        %{--        <g:render template="/districtProposal/showModules/districtProposalDataIcon" model="[districtProposal:districtProposal]"/>--}%
    </div>
</div>