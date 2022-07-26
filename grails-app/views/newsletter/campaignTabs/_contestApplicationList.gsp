<script>
    var filterDataCause = {${raw(contest.causes.collect{"'${it}':'${it.encodeAsHTML()}'"}.join(","))}};
    var filterDataActivityType = {${raw(org.kuorum.rest.model.communication.contest.ContestApplicationActivityTypeDTO.values().collect{"'${it}':'${g.message(code:"org.kuorum.rest.model.communication.contest.ContestApplicationActivityTypeDTO."+it)}'"}.join(','))}};
    var filterDataFocusType = {${raw(org.kuorum.rest.model.communication.contest.ContestApplicationFocusTypeDTO.values().collect{"'${it}':'${g.message(code:"org.kuorum.rest.model.communication.contest.ContestApplicationFocusTypeDTO."+it)}'"}.join(','))}};
    var filterDataCampaignType = {${raw(org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.values().collect{"'${it}':'${g.message(code:"org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO."+it)}'"}.join(','))}};

</script>

<g:set var="visibleVotesColumn"
       value="${![org.kuorum.rest.model.communication.contest.ContestStatusDTO.RESULTS, org.kuorum.rest.model.communication.contest.ContestStatusDTO.VOTING].contains(contest.status) ? 'true' : 'false'}"/>
<g:set var="visibleImplemented"
       value="${[org.kuorum.rest.model.communication.contest.ContestStatusDTO.RESULTS].contains(contest.status) ? 'true' : 'false'}"/>
<g:set var="currentLang" value="${org.springframework.web.servlet.support.RequestContextUtils.getLocale(request)}"/>

<div class="actions" id="toolbar-contestApplicationReviewTable">
    <a href="#" class="btn btn-blue inverted" id="changeParticipatoryBudgetBtn">
        <span class="fal fa-edit"></span>
        <g:message code="org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.label"/>:
        <span id="changeParticipatoryBudgetBtnStatusText">
            <g:message code="org.kuorum.rest.model.communication.contest.ContestStatusDTO.${contest.status}"/>
        </span>
    </a>
</div>
<table id="contestApplicationsReviewTable"
       class="list-working-campaign-table"
       data-toggle="table"
       data-url="${g.createLink(mapping: 'contestListApplciationsPaggination', params: contest.encodeAsLinkProperties())}"
       data-side-pagination="server"
       data-toolbar="#toolbar-contestApplicationReviewTable"
       data-show-export="true"
       data-id-field="id"
       data-pagination="true"
       data-search="false"
       data-filter-control="true"
       data-locale="${currentLang}"
       data-detail-view="true"
       data-show-columns="true"
       data-row-style="contestApplicationTableRowStyle"
       data-detail-formatter="detailFormatter"
       data-update-technicalReview-url="${g.createLink(mapping: 'contestApplicationUpdateReview', params: contest.encodeAsLinkProperties())}">
    <thead>
    <tr>
        <th data-field="id" data-halign="center" data-align="center" data-sortable="true" data-visible="true"
            data-filter-control="input" data-formatter="formatTableCampaignId"
            data-filter-control-placeholder="${g.message(code: 'tools.massMailing.view.participatoryBudget.proposalList.table.id.searchPlaceholder')}"><g:message
                code="tools.massMailing.view.participatoryBudget.proposalList.table.id"/></th>
        <th data-field="title" data-halign="center" data-align="center" data-sortable="true" data-visible="true"
            data-filter-control="input"
            data-filter-control-placeholder="${g.message(code: 'tools.massMailing.view.participatoryBudget.proposalList.table.title.searchPlaceholder')}"><g:message
                code="tools.massMailing.view.participatoryBudget.proposalList.table.title"/></th>
        <th data-field="cause" data-halign="center" data-align="center" data-sortable="false" data-visible="true"
            data-filter-control="select" data-filter-data="var:filterDataCause"><g:message
                code="tools.massMailing.view.participatoryBudget.proposalList.table.cause"/></th>
        <th data-field="user.name" data-halign="center" data-align="center" data-sortable="true" data-visible="true"
            data-filter-control="input" data-formatter="formatTableCampaignAuthor"
            data-filter-control-placeholder="${g.message(code: 'tools.massMailing.view.participatoryBudget.proposalList.table.author.searchPlaceholder')}"><g:message
                code="tools.massMailing.view.participatoryBudget.proposalList.table.author"/></th>
        <th data-field="campaignStatus.i18n" data-halign="center" data-align="center" data-sortable="false"
            data-visible="true"
            data-filter-control="select" data-filter-data="var:filterDataCampaignType"><g:message
                code="org.kuorum.rest.model.notification.campaign.CampaignStatusRSDTO.label"/></th>

        <th data-field="focusType.i18n" data-halign="center" data-align="center" data-sortable="false"
            data-visible="true"
            data-filter-control="select" data-filter-data="var:filterDataFocusType"><g:message
                code="org.kuorum.rest.model.communication.contest.ContestApplicationFocusTypeDTO.shortLabel"/></th>
        <th data-field="activityType.i18n" data-halign="center" data-align="center" data-sortable="false"
            data-visible="true"
            data-filter-control="select" data-filter-data="var:filterDataActivityType"><g:message
                code="org.kuorum.rest.model.communication.contest.ContestApplicationActivityTypeDTO.shortLabel"/></th>
        <th data-field="numVotes" data-halign="center" data-align="center" data-sortable="true"
            data-visible="${visibleVotesColumn}"><g:message
                code="tools.massMailing.view.participatoryBudget.proposalList.table.votes"/></th>
    </tr>
    </thead>
</table>

<div id="changeParticipatoryBudgetStatusModal" class="modal fade in" tabindex="-1" role="dialog"
     aria-labelledby="changeParticipatoryBudgetStatusTitle" aria-hidden="true">
    <div class="modal-dialog ">
        <div class="modal-content">
            <div class="modal-header"><h4><g:message code="modal.changeParticipatoryBudgetStatusModal.title"/></h4>
            </div>

            <div class="modal-body">
                <p>
                    <g:message code="modal.changeParticipatoryBudgetStatusModal.text" args="[contest.title]"/>
                </p>
                <select class="form-control input-lg" id="changeParticipatoryBudgetStatusModalSelect">
                    <g:each in="${org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.values()}"
                            var="status">
                        <option value="${status}" ${contest.status == status ? 'selected' : ''}>${g.message(code: "org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO." + status)}</option>
                    </g:each>
                </select>

            </div>

            <div class="modal-footer">
                <a href="#" class="btn btn-grey-light" data-dismiss="modal" aria-label="Close"><g:message
                        code="modal.changeParticipatoryBudgetStatusModal.close"/></a>
                <g:link mapping="participatoryBudgetEditStatus" params="${contest.encodeAsLinkProperties()}"
                        class="btn btn-blue" aria-label="Ok"
                        elementId="changeParticipatoryBudgetStatusSubmit"><g:message
                        code="modal.changeParticipatoryBudgetStatusModal.submit"/></g:link>
            </div>
        </div>
    </div>
</div>