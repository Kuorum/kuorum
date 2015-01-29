<g:each in="${docs}" var="solrElement">
    <li>
        <g:if test="${solrElement instanceof kuorum.core.model.solr.SolrKuorumUser}">
            <g:render template="searchUserElement" model="[solrUser:solrElement]"/>
        </g:if>
        <g:elseif test="${solrElement instanceof kuorum.core.model.solr.SolrPost}">
            <g:render template="searchPostElement" model="[solrPost:solrElement]"/>
        </g:elseif>
        <g:elseif test="${solrElement instanceof kuorum.core.model.solr.SolrProject}">
            <g:render template="searchProjectElement" model="[solrProject:solrElement]"/>
        </g:elseif>
        <g:else>
            <sec:ifAllGranted roles="ROLE_ADMIN">
                <li style="background: red">WAAARRNNN => ${solrElement} (${solrElement.name} - ${solrElement.id})</li>
            </sec:ifAllGranted>
        </g:else>
    </li>
</g:each>