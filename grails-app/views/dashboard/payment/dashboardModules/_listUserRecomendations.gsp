<g:each in="${recommendedUsers}" var="user">
    <userUtil:showUser
            user="${user}"
            showName="true"
            showActions="true"
            showDeleteRecommendation="true"
            htmlWrapper="li"
    />
</g:each>