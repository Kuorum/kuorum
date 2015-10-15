<%@ page import="kuorum.users.extendedPoliticianData.PoliticalActivityActionType" %>
<g:if test="${politician.externalPoliticianActivities}">
    <div class="hidden-xs panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title"><g:message code="politician.latestActivity.title"/></h3>
        </div>
        <div class="panel-body">
            <table class="table panel-table">
                <thead>
                <tr>
                    <th><g:message code="politician.latestActivity.table.date"/></th>
                    <th><g:message code="politician.latestActivity.table.title"/></th>
                    <th><g:message code="politician.latestActivity.table.action"/></th>
                    <th><g:message code="politician.latestActivity.table.outcome"/></th>
                </tr>
                </thead>
                <tbody>
                <g:each in="${politician.externalPoliticianActivities}" var="externalPoliticianActivity">
                    <g:set var="cssVote" value=""/>
                    <g:if test="${externalPoliticianActivity.actionType==PoliticalActivityActionType.VOTED_FOR}">
                        <g:set var="cssVote" value="text-success"/>
                    </g:if>
                    <g:if test="${externalPoliticianActivity.actionType==PoliticalActivityActionType.VOTED_AGAINST}">
                        <g:set var="cssVote" value="text-danger"/>
                    </g:if>
                    <tr>
                        <td><g:formatDate format="dd MMM yyyy" date="${externalPoliticianActivity.date}"/></td>
                        <td class='political-activity-title'>
                            <a href="${externalPoliticianActivity.link}">${externalPoliticianActivity.title}</a>
                        </td>
                        <td class="${cssVote}"><g:message code="kuorum.users.extendedPoliticianData.PoliticalActivityActionType.${externalPoliticianActivity.actionType}"/></td>
                        <td><g:message code="kuorum.users.extendedPoliticianData.PoliticalActivityOutcomeType.${externalPoliticianActivity.outcomeType}"/></td>
                    </tr>
                </g:each>
                </tbody>
                <tfoot>
                <tr>
                    <td colspan="4" class="text-center">
                        <a href="#" class="btn-primary btn-sm" role="button">
                            <g:message code="search.list.seeMore"/>
                        </a>
                    </td>
                </tr>
                </tfoot>
            </table>
        </div><!--/.panel-body -->
    </div><!--/.panel.panel-default -->
</g:if>