<section role="complementary" class="homeSub">
    <div class="row">
        <h1>Sigue a tus representantes</h1>
        <ul class="politician-list">
            <g:each in="${politicians}" var="politician">
                <li class="col-md-4">
                    <g:render template="/modules/users/recommendedUser" model="[user:politician]"/>
                </li>
            </g:each>
        </ul>
    </div>
    <div class="homeMore">
        <g:link mapping="funnelSuccessfulStories" class="btn btn-blue btn-lg btn-block"><g:message code="landingPage.recommendedPoliticians.button"/></g:link>
    </div>
</section>