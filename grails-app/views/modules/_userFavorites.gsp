<section class="boxes guay pending">
    <h1>
        <span class="badgeContainer">
            <span class="fa fa-bookmark"></span>
            <span class="badge" role="log" aria-labelledby="alerts" aria-live="assertive" aria-relevant="additions">${favorites.size()}</span>
        </span>
        <span class="bigger"><g:message code="modules.favorites.title"/></span>
    </h1>
    <ul class="kakareo-list">
        <g:each in="${favorites}" var="post">
            <g:render template="/cluck/liCluck" model="[post:post]"/>
        </g:each>
    </ul>
</section>