<ul class="user-list-followers">
    <g:each in="${users}" var="user">
        <userUtil:showUser
                user="${user}"
                showName="true"
                showActions="true"
                showDeleteRecommendation="${showDeleteRecommendation}"
                htmlWrapper="li"
        />
    </g:each>
</ul>