<g:set var="surveyClosedClasses" value=""/>
<g:if test="${survey.closed}">
    <g:set var="surveyClosedClasses" value="survey-closed"/>
    <g:if test="${survey.endDate?.before(new Date()) && !survey.surveyStarted}">
        <g:set var="surveyClosedClasses" value="${surveyClosedClasses} survey-closed-hideQuestions"/>
    </g:if>
</g:if>

<div class="campaign-survey ${survey.hideResults?'campaign-hide-results':''} ${surveyClosedClasses} ">
    <g:render template="/survey/showModules/surveyData" model="[survey: survey, campaignUser: campaignUser]" />
    <g:render template="/campaigns/showModules/campingModalEditScheduled"/>

    <g:render template="/survey/showModules/surveyQuestions" model="[survey:survey,campaignUser:campaignUser]"/>
</div>