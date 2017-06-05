<div class="box-ppal" id="followOthers">
    <h2><g:message code="dashboard.payment.followPoliticians.title"/></h2>
    <ul class="user-list-followers hide3">
        <g:each in="${recommendedUsers}" var="user">
            <userUtil:showUser
                    user="${user}"
                    showName="true"
                    showRole="true"
                    showActions="true"
                    showDeleteRecommendation="true"
                    htmlWrapper="li"
            />
        </g:each>
    </ul>
</div>