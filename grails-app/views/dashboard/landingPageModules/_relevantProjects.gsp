<section role="complementary" class="homeSub">
<div class="row">
<h1><g:message code="home.projects.titel"/></h1>
<ul class="kakareo-list project">
    <g:each in="${projects}" var="project">
        <li class="col-md-4">
            <g:render template="/modules/projects/projectOnList" model="[project: project]"/>
        </li>
    </g:each>
</ul>
</div>
%{-- DESCOMENTAR CUANDO ESTÉ LISTO EL DESCUBRE--}%
%{--<div class="homeMore">--}%
    %{--<a href="#" class="btn btn-blue btn-lg btn-block">Conoce otros proyectos</a>--}%
%{--</div>--}%
</section>