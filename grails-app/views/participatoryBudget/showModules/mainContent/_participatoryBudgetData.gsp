<!-- ^leader-post !-->
<div class="leader-post">
    <g:render template="/campaigns/showModules/campaignDataMultimedia" model="[campaign: participatoryBudget]"/>
    <div class="header">
        <h1 class="title" itemprop="headline">${participatoryBudget.title}</h1>
        <userUtil:showUser user="${campaignUser}" showRole="true" itemprop="author"/>
        %{--<g:render template="/participatoryBudget/showModules/mainContent/participatoryBudgetDataMobileCallToAction"/>--}%
        <g:render template="/campaigns/showModules/campaignDataDatePublished" model="[campaign:participatoryBudget, campaignUser:campaignUser, editMappingName:'participatoryBudgetEditDistricts', editable:true]"/>
    </div>

    <div class="body" itemprop="articleBody">
        ${raw(participatoryBudget.body)}
    </div>

    <div class="footer clearfix">
        <g:render template="/campaigns/showModules/campaignDataLabels" model="[causes:participatoryBudget.causes]"/>
        <g:render template="/campaigns/showModules/campaignDataSocial" model="[campaign:participatoryBudget]"/>
        <g:if test="${participatoryBudget.published}">
            <g:render template="/participatoryBudget/showModules/participatoryBudgetDataIcon" model="[participatoryBudget:participatoryBudget]"/>
        </g:if>
    </div>
</div>