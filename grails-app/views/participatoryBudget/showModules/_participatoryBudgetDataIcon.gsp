<g:if test="${participatoryBudget.published}">
    <sec:ifLoggedIn><g:set var="isLogged" value="${true}"/></sec:ifLoggedIn>
    <sec:ifNotLoggedIn><g:set var="isLogged" value="${false}"/></sec:ifNotLoggedIn>
    <div class="comment-counter pull-right">
        <button type="button" data-goto="#survey-progress">
            <span class="fa fa-money" aria-hidden="true"></span>
            <span class="number">XXXXXXXXx</span>
        </button>
    </div>
</g:if>
