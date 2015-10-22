
<g:set var="max" value="${6}"/>
<g:set var="cssHide" value="${favorites ? '' : 'hide'}"/>
<g:set var="cssSeeMoreHide" value="${favorites.size() == max ? '' : 'hide'}"/>


<section class="boxes guay pending ${cssHide}">
    <h1>
        <span class="badgeContainer">
            <span class="fa fa-bookmark"></span>
            <span class="badge" role="log" aria-labelledby="alerts" aria-live="assertive" aria-relevant="additions">${favorites.size()}</span>
        </span>
        <span class="bigger"><g:message code="modules.favorites.title"/></span>
    </h1>
    <ul class="kakareo-list">
        <g:each in="${favorites}" var="post">
                <g:render template="/cluck/liCluck" model="[post:post, displayingColumnC:true]"/>
        </g:each>
        <li class="text-center ${cssSeeMoreHide}">
            <small>
                <g:link mapping="toolsFavorites">
                    <g:message code="modules.favorites.seeMore"/>
                </g:link>
            </small>
        </li>
    </ul>
</section>


