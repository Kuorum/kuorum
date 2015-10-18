<g:if test="${userProjects}">
    <section id="otros-proyectos" role="complementary" class="row main">
        <div class="container-fluid">
            <h2 class="underline"><g:message code="modules.lastCreatedPost.title"/></h2>
            <div class="row">
                <ul class="kakareo-list project">
                    <g:each in="${userProjects}" var="project">
                        <li class="col-md-4">
                            <g:render template="/modules/projects/projectOnList" model="[project: project]"/>
                        </li>
                    </g:each>
                </ul>
            </div>
        </div>
    </section>
</g:if>