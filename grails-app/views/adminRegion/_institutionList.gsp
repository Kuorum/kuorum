
<h2><g:message code="admin.createProject.title"/></h2>
<ul>
    <g:each in="${institutions}" var="institution">
        <li><g:link mapping="adminEditInstitution" params="[id:institution.id]">${institution.name}</g:link> </li>
    </g:each>
</ul>
<g:link  mapping="adminCreateInstitution" params="[iso3166_2:region.iso3166_2]">Crear</g:link>