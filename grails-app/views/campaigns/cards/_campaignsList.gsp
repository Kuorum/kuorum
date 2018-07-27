<g:each in="${campaigns}" var="campaign">
    <g:set var="columsSize" value="col-sm-12 col-md-6"/>
    <g:if test="${numColumns && numColumns==3}">
        <g:set var="columsSize" value="col-xs-12 col-sm-4"/>
    </g:if>

    <li class="${columsSize} search-article">
        <g:if test="${campaign instanceof org.kuorum.rest.model.communication.post.PostRSDTO}">
            <g:render template="/campaigns/cards/postList" model="[post:campaign, showAuthor: showAuthor, referred:'dashboard']"/>
        </g:if>
        <g:if test="${campaign instanceof org.kuorum.rest.model.communication.debate.DebateRSDTO}">
            <g:render template="/campaigns/cards/debateList" model="[debate:campaign, showAuthor: showAuthor, referred:'dashboard']" />
        </g:if>
        <g:if test="${campaign instanceof org.kuorum.rest.model.communication.survey.SurveyRSDTO}">
            <g:render template="/campaigns/cards/surveyList" model="[survey:campaign, showAuthor: showAuthor, referred:'dashboard']" />
        </g:if>
        <g:if test="${campaign instanceof org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetRSDTO}">
            <g:render template="/campaigns/cards/districtProposal" model="[participatoryBudget:campaign, showAuthor: showAuthor, referred:'dashboard']" />
        </g:if>
        <g:if test="${campaign instanceof org.kuorum.rest.model.communication.participatoryBudget.DistrictProposalRSDTO}">
            <g:render template="/campaigns/cards/districtProposal" model="[districtProposal:campaign, showAuthor: showAuthor, referred:'dashboard']" />
        </g:if>
        <g:if test="${campaign instanceof org.kuorum.rest.model.search.SearchKuorumElementRSDTO}">
            <g:render template="/campaigns/cards/searchCampaignList" model="[campaign:campaign, showAuthor: showAuthor, referred:'dashboard']" />
        </g:if>
    </li>
</g:each>