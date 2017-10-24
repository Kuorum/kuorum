<section class="box-ppal">
    <div class="box-ppal-title">
        <h3>${boxTitle}</h3>
    </div>
    <div class="box-ppal-section">
        <ul class="user-list-followers">
            <g:each in="${recommendedUsers}" var="user">
                <userUtil:showUser
                        user="${user}"
                        showName="true"
                        showRole="true"
                        showActions="true"
                        showDeleteRecommendation="false"
                        htmlWrapper="li"
                />
            </g:each>
        </ul>
    </div>
</section>

