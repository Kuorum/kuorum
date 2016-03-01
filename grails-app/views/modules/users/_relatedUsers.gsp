<section class="panel panel-default follow">
    <div class="panel-heading">
        <h3 class="panel-title"><g:message code="modules.relatedUsers.title"/> </h3>
    </div>
    <g:render template="/modules/users/recommendedUsersAsList" model="[users:relatedUsers, showDeleteRecommendation:'false']"/>
</section>