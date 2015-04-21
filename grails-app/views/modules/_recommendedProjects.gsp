<section class="boxes guay">
    <h1>Proyectos destacados</h1>
    <ul class="kakareo-list project">
        <g:each in="${projects}" var="project">
        <li>
            <g:render template="/modules/projects/projectOnList" model="[project: project]"/>
        </li>
        </g:each>
    </ul>
</section>