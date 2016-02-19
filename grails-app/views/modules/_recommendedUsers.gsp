<div class="extendedPolitician">
    <section class="boxes follow">
        <h1>${boxTitle}</h1>
        <ul class="user-list-followers hide3">
            <g:each in="${recommendedUsers}" var="user">
                <li itemtype="http://schema.org/Person" itemscope class="user">
                    <userUtil:showUser user="${user}" showName="true" showRole="true" showActions="true" showDeleteRecommendation="true"/>
                </li>
            </g:each>
        </ul>
    </section>
</div>