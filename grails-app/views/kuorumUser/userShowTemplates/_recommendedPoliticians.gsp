<g:if test="${recommendPoliticians}">
    <section role="complementary" class="row main" id="suggested-politicians">
        <div class="container-fluid">
            <h2 class="underline"><g:message code="home.politicians.title"/></h2>
            <div class="row limit-height" data-collapsedHeight="430">
                <ul class="search-list">
                    <g:each in="${recommendPoliticians}" var="politician">
                        <li class="col-xs-12 col-sm-4 col-md-4 search-kuorum-user" itemprop="colleague">
                            <g:render template="/modules/users/recommendedUser" model="[user:politician]"/>
                        </li>
                    </g:each>
                </ul>
            </div>
            %{--<div class="text-center">--}%
                %{--<a href="#" class="btn btn-sm btn-blue"><g:message code="politician.quickNotes.readMore"/></a>--}%
            %{--</div>--}%
        </div>
    </section>
</g:if>