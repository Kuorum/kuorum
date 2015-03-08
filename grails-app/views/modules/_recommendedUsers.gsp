<section class="boxes follow">
    <h1><g:message code="modules.recommendedUsers.title"/></h1>
    <ul class="user-list-followers hide4">
        %{--<userUtil:showListUsers--}%
                %{--users="${recommendedUsers}"--}%
                %{--visibleUsers="${recommendedUsers.size()}"--}%
                %{--total="${recommendedUsers.size()}"--}%
                %{--messagesPrefix="modules.recommendedUsers.userList"--}%
        %{--/>--}%
        <g:each in="${recommendedUsers}" var="user">
            <li itemtype="http://schema.org/Person" itemscope class="user">
                <userUtil:showUser user="${user}" showName="true" showRole="true" showActions="true" showDeleteRecommendation="true"/>
            </li>
        </g:each>
    </ul>
</section>