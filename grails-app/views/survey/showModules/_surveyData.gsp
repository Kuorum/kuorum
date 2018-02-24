<!-- ^leader-post !-->
<div class="leader-post">
    <g:render template="/campaigns/showModules/campaignDataMultimedia" model="[campaign: survey]"/>
    <div class="header">
        <h1 class="title" itemprop="headline">${survey.title}</h1>
        <userUtil:showUser user="${campaignUser}" showRole="true" itemprop="author"/>
        <g:render template="/campaigns/showModules/campaignDataDatePublished" model="[campaign:survey, campaignUser:campaignUser, editMappingName:'surveyEditQuestions']"/>
    </div>

    <div class="body" itemprop="articleBody">
        ${raw(survey.body)}
    </div>

    <div class="footer clearfix">
        <g:render template="/campaigns/showModules/campaignDataLabels" model="[causes:survey.causes]"/>
        <g:render template="/campaigns/showModules/campaignDataSocial" model="[campaign:survey]"/>
        <g:render template="/survey/showModules/surveyDataIcon" model="[survey:survey]"/>
    </div>
</div>