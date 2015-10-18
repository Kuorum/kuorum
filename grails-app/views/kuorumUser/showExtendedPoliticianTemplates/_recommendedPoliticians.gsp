<g:if test="${recommendPoliticians}">
    <section role="complementary" class="row main" id="suggested-politicians">
        <div class="container-fluid">
            <h2 class="underline"><g:message code="home.politicians.title"/></h2>
            <ul class="politician-list">
                <g:each in="${recommendPoliticians}" var="politician">
                    <li class="col-xs-12 col-sm-4 col-md-4">
                        <g:render template="/modules/users/recommendedUser" model="[user:politician]"/>
                    </li>
                </g:each>
            </ul>
        </div>
    </section>
</g:if>