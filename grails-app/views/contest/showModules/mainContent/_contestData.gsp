<!-- ^leader-post !-->
<div class="leader-post ${contest.published? 'campaign-published': '' }">
    <g:render template="/campaigns/showModules/campaignDataMultimedia" model="[campaign: contest]"/>
    <div class="header">
        <h1 class="title" itemprop="headline">${contest.title}</h1>
        <userUtil:showUser user="${campaignUser}" showRole="true" itemprop="author"/>
        <g:render template="/contest/showModules/mainContent/contestDataMobileCallToAction"/>
        <g:render template="/campaigns/showModules/campaignDataDatePublished"
                  model="[campaign: contest, campaignUser: campaignUser, editMappingName: 'contestEditContent']"/>
    </div>

    <div class="body" itemprop="articleBody">
        ${raw(contest.body)}
    </div>

    <div class="footer clearfix">
        <g:render template="/campaigns/showModules/campaignDataLabels" model="[causes: contest.causes]"/>
        <g:render template="/campaigns/showModules/campaignDataSocial" model="[campaign: contest]"/>
        <g:render template="/contest/showModules/mainContent/contestDataIcon" model="[contest: contest]"/>
    </div>
</div>