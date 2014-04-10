<section class="boxes guay pending">
    <h1>
        <span class="badgeContainer">
            <span class="fa fa-bookmark"></span>
            <span class="badge" role="log" aria-labelledby="alerts" aria-live="assertive" aria-relevant="additions">8</span>
        </span>
        <span class="bigger">Post pendientes de leer</span>
    </h1>
    <ul class="kakareo-list">
        <g:each in="${favorites}" var="post">
            <g:render template="/modules/columnCPost/columnCPost" model="[post:post]"/>
        </g:each>
    </ul>
</section>