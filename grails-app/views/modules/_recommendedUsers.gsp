<section class="boxes follow">
    <h1><g:message code="modules.recommendedUsers.title"/></h1>
    <div class="kakareo follow">
        <userUtil:showListUsers
                users="${recommendedUsers}"
                visibleUsers="${recommendedUsers.size()}"
                total="${recommendedUsers.size()}"
                messagesPrefix="modules.recommendedUsers.userList"
        />
    </div>
</section>