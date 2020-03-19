<div class="campaign-survey ${survey.hideResults?'campaign-hide-results':''} ${survey.closed?'survey-closed':''}">
    <g:render template="/survey/showModules/surveyData" model="[survey: survey, campaignUser: campaignUser]" />
    <g:render template="/campaigns/showModules/campingModalEditScheduled"/>

    <g:render template="/survey/showModules/surveyQuestions" model="[survey:survey,campaignUser:campaignUser]"/>
</div>