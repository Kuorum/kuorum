
<g:each in="${projects}" var="project">
    <li>
        <g:render template="/modules/projects/projectOnList" model="[project: project]"/>
    </li>
</g:each>