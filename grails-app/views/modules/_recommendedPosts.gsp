<section class="${specialCssClass} boxes guay">
    <h1>${title}</h1>
    <ul class="kakareo-list">
        <g:each in="${recommendedPost}" var="post">
            <g:render template="/cluck/liCluck" model="[post:post]"/>
        </g:each>
    </ul>
</section>