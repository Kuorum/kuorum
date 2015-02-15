<g:if test="${project.updates}">
    <h2 class="underline">Actualizaciones de proyectos</h2>
    <ul class="list-updates clearfix">
        <g:each in="${project.updates}" var="projectUpdate">
            <li>
                <g:render template="projectUpdate" model="[project:project, projectUpdate:projectUpdate]"/>
            </li>
        </g:each>
    </ul>
</g:if>