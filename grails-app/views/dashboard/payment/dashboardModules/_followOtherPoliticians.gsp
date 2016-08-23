<div class="box-ppal" id="followOthers">
    <h2><g:message code="dashboard.payment.followPoliticians.title"/></h2>
    <ul class="user-list-followers hide4">
        <g:each in="${recommendedUsers}" var="user">
            <li itemtype="http://schema.org/Person" itemscope="" class="user">
                <userUtil:showUser user="${user}" showName="true" showRole="true" showActions="true" showDeleteRecommendation="true"/>
            </li>
        </g:each>
    </ul>
</div>