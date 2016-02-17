<g:if test="${!columnsCss}">
    <g:set var="columnsCss" value="col-sm-12 col-md-6"/>
</g:if>

<g:each in="${docs}" var="solrElement">
        <g:if test="${solrElement instanceof kuorum.core.model.solr.SolrKuorumUser}">
            <li class="${columnsCss}">
                <g:render template="/search/searchUserElement" model="[solrUser:solrElement]"/>
            </li>
        </g:if>
        <g:elseif test="${solrElement instanceof kuorum.core.model.solr.SolrPost}">
            <li>
                <g:render template="/search/searchPostElement" model="[solrPost:solrElement]"/>
            </li>
        </g:elseif>
        <g:elseif test="${solrElement instanceof kuorum.core.model.solr.SolrProject}">
            <li class="${columnsCss}">
                <g:render template="/search/searchProjectElement" model="[solrProject:solrElement]"/>
            </li>
        </g:elseif>
        <g:else>
            <sec:ifAllGranted roles="ROLE_ADMIN">
                <li style="background: red">WAAARRNNN => ${solrElement} (${solrElement.name} - ${solrElement.id})</li>
            </sec:ifAllGranted>
        </g:else>
</g:each>