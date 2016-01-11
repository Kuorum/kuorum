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
                    <g:if test="${externalPoliticianActivity.actionType=='Voted for'}">
                        <g:set var="cssVote" value="text-success"/>
                    </g:if>
                    <g:if test="${externalPoliticianActivity.actionType=='Voted against'}">
                        <g:set var="cssVote" value="text-danger"/>
                    </g:if>
                    <tr>
                        <td><g:formatDate format="dd MMM yyyy" date="${externalPoliticianActivity.date}"/></td>
                        <td class='political-activity-title'>
                            <g:if test="${externalPoliticianActivity.link}">
                                <a href="${externalPoliticianActivity.link}" target="_blank">
                                    ${externalPoliticianActivity.title}
                                </a>
                            </g:if>
                            <g:else>
                                ${externalPoliticianActivity.title}
                            </g:else>
                        </td>
                        <td class="${cssVote}">${externalPoliticianActivity.actionType}</td>
                        <td>${externalPoliticianActivity.outcomeType}</td>
                    </tr>
                </g:each>
                </tbody>
                <tfoot>
                <tr>
                    <td colspan="4" class="text-center">
                        <a href="${politician?.socialLinks?.institutionalWebSite}" class="btn btn-xs btn-blue" role="button" target="_blank">
                            <g:message code="search.list.seeMore"/>
                        </a>
                    </td>
                </tr>
                </tfoot>
            </table>
        </div><!--/.panel-body -->
    </div><!--/.panel.panel-default -->
</g:if>