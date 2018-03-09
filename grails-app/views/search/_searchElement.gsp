<g:each in="${docs}" var="solrElement">
        <g:if test="${solrElement instanceof org.kuorum.rest.model.search.kuorumElement.SearchKuorumUserRSDTO}">
            <li class="col-sm-12 col-md-6 search-kuorum-user">
                <g:render template="/search/searchUserElement" model="[solrUser:solrElement]"/>
            </li>
        </g:if>
        <g:elseif test="${solrElement instanceof org.kuorum.rest.model.search.SearchKuorumElementRSDTO}">
            <r:require modules="post"/>
            <g:render template="/campaigns/cards/searchCampaignList" model="[campaign:solrElement, showAuthor: true]" />
        </g:elseif>
        <g:else>
            <sec:ifAllGranted roles="ROLE_ADMIN">
                <li style="background: red">WAAARRNNN => ${solrElement} (${solrElement.name} - ${solrElement.id})</li>
            </sec:ifAllGranted>
        </g:else>
</g:each>