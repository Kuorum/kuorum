<sec:ifLoggedIn><g:set var="isLogged" value="${true}"/></sec:ifLoggedIn>
<sec:ifNotLoggedIn><g:set var="isLogged" value="${false}"/></sec:ifNotLoggedIn>
<g:set var="activeButton" value="${survey.completed && isLogged}"/>
<div class="comment-counter pull-right">
    <button type="button" class="post-like ${activeButton?'active':''}">
        <span class="fa fa-pie-chart" aria-hidden="true"></span>
        <span class="number">${post.likes}</span>
    </button>
</div>