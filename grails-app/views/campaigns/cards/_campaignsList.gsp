<g:each in="${campaigns}" var="campaign">
    <g:if test="${campaign instanceof org.kuorum.rest.model.communication.post.PostRSDTO}">
        <g:render template="/campaigns/cards/postList" model="[post:campaign, showAuthor: showAuthor, referred:'dashboard']"/>
    </g:if>
    <g:if test="${campaign instanceof org.kuorum.rest.model.communication.debate.DebateRSDTO}">
        <g:render template="/campaigns/cards/debateList" model="[debate:campaign, showAuthor: showAuthor, referred:'dashboard']" />
    </g:if>
    <g:if test="${campaign instanceof org.kuorum.rest.model.communication.survey.SurveyRSDTO}">
        <g:render template="/campaigns/cards/surveyList" model="[survey:campaign, showAuthor: showAuthor, referred:'dashboard']" />
    </g:if>
</g:each>