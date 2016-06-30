<div class="extendedPolitician">
    <section class="boxes follow" id='${id}'>
        <h3>${boxTitle}</h3>
        <ul class="user-list-followers hide3">
            <g:each in="${recommendedUsers}" var="user">
                <li itemprop="colleague" id="user-list-followers-${user.id}">
                    <userUtil:showUser user="${user}" showName="true" showRole="true" showActions="true" showDeleteRecommendation="true"/>
                </li>
            </g:each>
        </ul>
    </section>
</div>