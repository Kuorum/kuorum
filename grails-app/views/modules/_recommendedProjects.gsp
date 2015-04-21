<section class="boxes guay">
    <h1><g:message code="modules.recommendedProjects.title"/> </h1>
    <ul class="kakareo-list project">
        <g:each in="${projects}" var="project">
        <li>
            <g:render template="/modules/projects/projectOnList" model="[project: project]"/>
        </li>
        </g:each>
        <g:if test="${projects}">
            <li class="text-center">
                <small>
                    <g:link mapping="discoverProjects">
                        <g:message code="modules.recommendedProjects.seeMore"/>
                    </g:link>
                </small>
            </li>
        </g:if>
    </ul>
</section>