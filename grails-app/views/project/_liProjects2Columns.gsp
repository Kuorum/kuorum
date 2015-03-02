<g:each in="${projects}" var="project">
    <li class="col-md-6">
        <g:render template="/modules/projects/projectOnList" model="[project:project]"/>
    </li>
</g:each>