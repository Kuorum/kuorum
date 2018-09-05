<g:if test="${survey.published}">
    <sec:ifLoggedIn><g:set var="isLogged" value="${true}"/></sec:ifLoggedIn>
    <sec:ifNotLoggedIn><g:set var="isLogged" value="${false}"/></sec:ifNotLoggedIn>
    <g:set var="activeButton" value="${survey.completed && isLogged}"/>
    <div class="comment-counter pull-right">
        <button type="button" class="${activeButton?'active':''}" data-goto="#survey-progress">
            <span class="fal fa-chart-pie" aria-hidden="true"></span>
            <span class="number">${survey.amountAnswers}</span>
        </button>
    </div>
</g:if>
