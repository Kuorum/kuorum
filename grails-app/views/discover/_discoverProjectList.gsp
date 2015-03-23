
<g:each in="${projects}" var="solrProject">
    <li>
        <projectUtil:projectFromSolr solrProject="${solrProject}" var="project"/>
        <g:render template="/modules/projects/projectOnList" model="[project: project]"/>
    </li>
</g:each>