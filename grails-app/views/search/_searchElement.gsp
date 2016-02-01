<g:each in="${docs}" var="solrElement">
        <g:if test="${solrElement instanceof kuorum.core.model.solr.SolrKuorumUser}">
            <li class="col-xs-12 col-sm-6 col-md-4">
                <g:render template="searchUserElement" model="[solrUser:solrElement]"/>
            </li>
        </g:if>
        <g:elseif test="${solrElement instanceof kuorum.core.model.solr.SolrPost}">
            <li>
                <g:render template="searchPostElement" model="[solrPost:solrElement]"/>
            </li>
        </g:elseif>
        <g:elseif test="${solrElement instanceof kuorum.core.model.solr.SolrProject}">
            <li class="col-xs-12 col-sm-6 col-md-4">
                <g:render template="searchProjectElement" model="[solrProject:solrElement]"/>
            </li>
        </g:elseif>
        <g:else>
            <sec:ifAllGranted roles="ROLE_ADMIN">
                <li style="background: red">WAAARRNNN => ${solrElement} (${solrElement.name} - ${solrElement.id})</li>
            </sec:ifAllGranted>
        </g:else>
</g:each>