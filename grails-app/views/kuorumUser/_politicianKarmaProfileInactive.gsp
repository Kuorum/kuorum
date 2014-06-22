<section class="boxes userkarma">
    <h1><g:message code="kuorumUser.show.politicianInactive.karma.title"/> </h1>
    <p><g:message code="kuorumUser.show.politicianInactive.karma.description"/></p>
    <ul class="activity">
        <li>
            <span class="counter">0</span>
            <br><g:message code="kuorumUser.show.politiciankarma.postDefended"/>
        </li>
        <li class="victories">
            <span class="counter">0</span>
            <br><g:message code="kuorumUser.show.politiciankarma.vicotries"/>
        </li>
        <li>
            <span class="counter">0</span>
            <br><g:message code="kuorumUser.show.politiciankarma.debates"/>
        </li>
    </ul>
    %{--<button class="btn btn-blue btn-lg btn-block followInactivePolitician enabled" type="button">Quiero que se una a kuorum</button>--}%
    <userUtil:followButton
            user="${user}"
            prefixMessages="kuorumUser.follow.inactivePolitician.columnC"
            cssSize="btn-lg"
            showNoLoggedButton="${java.lang.Boolean.TRUE}"
    />
</section>