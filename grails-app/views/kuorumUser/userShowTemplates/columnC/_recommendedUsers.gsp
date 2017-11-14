<section class="panel panel-default">
    <div class="panel-heading">
        <h3 class="panel-title">${boxTitle}</h3>
    </div>
    <div class="panel-body">
        <ul class="user-list-followers hide3">
            <g:each in="${recommendedUsers}" var="user">
                <userUtil:showUser
                        user="${user}"
                        showName="true"
                        showActions="true"
                        showDeleteRecommendation="true"
                        htmlWrapper="li"
                />
            </g:each>
        </ul>
    </div>
</section>

