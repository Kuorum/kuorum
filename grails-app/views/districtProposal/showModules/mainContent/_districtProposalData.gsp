<!-- ^leader-post !-->
<div class="leader-post">
    <g:render template="/campaigns/showModules/campaignDataMultimedia" model="[campaign: districtProposal]"/>
    <div class="header">
        <h1 class="title" itemprop="headline">${districtProposal.title}</h1>
        <userUtil:showUser user="${campaignUser}" showRole="true" itemprop="author"/>
        %{--<g:render template="/participatoryBudget/showModules/mainContent/participatoryBudgetDataMobileCallToAction"/>--}%
        <g:render template="/campaigns/showModules/campaignDataDatePublished" model="[campaign:districtProposal, campaignUser:campaignUser, editMappingName:'districtProposalEditContent', editable:districtProposal.participatoryBudget.status==org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.ADDING_PROPOSALS]"/>
    </div>

    <div class="body" itemprop="articleBody">
        ${raw(districtProposal.body)}
    </div>

    <div class="footer clearfix">
        <g:render template="/campaigns/showModules/campaignDataLabels" model="[causes:districtProposal.causes]"/>
        <g:render template="/campaigns/showModules/campaignDataSocial" model="[campaign:districtProposal]"/>
        <g:render template="/districtProposal/showModules/districtProposalDataIcon" model="[districtProposal:districtProposal]"/>
    </div>
</div>