<script>
    var filterDataImplemented={'true':'${g.message(code:"tools.massMailing.view.participatoryBudget.proposalList.table.implemented.filter.true")}','false':'${g.message(code:"tools.massMailing.view.participatoryBudget.proposalList.table.implemented.filter.false")}'}
    var filterDataApproved={'true':'${g.message(code:"tools.massMailing.view.participatoryBudget.proposalList.table.approved.filter.true")}','false':'${g.message(code:"tools.massMailing.view.participatoryBudget.proposalList.table.approved.filter.false")}'}
    var filterDataDistrict={ ${raw(participatoryBudget.districts.collect{"'${it.id}':'${it.name}'"}.join(","))} };
    var filterDataCause={ ${raw(participatoryBudget.causes.collect{"'${it}':'${it}'"}.join(","))} };
    var filterDataTechnicalReview={ ${raw(org.kuorum.rest.model.communication.participatoryBudget.TechnicalReviewStatusRDTO.values().collect{"'${it}':'${g.message(code:"org.kuorum.rest.model.communication.participatoryBudget.TechnicalReviewStatusRDTO."+it)}'"}.join(','))} };

</script>

<g:set var="visibleSupportColumn" value="${[org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.ADDING_PROPOSALS, org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.TECHNICAL_REVIEW].contains(participatoryBudget.status)?'true':'false'}"/>
<g:set var="visibleVotesColumn" value="${![org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.ADDING_PROPOSALS, org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.TECHNICAL_REVIEW].contains(participatoryBudget.status)?'true':'false'}"/>
<g:set var="visibleImplemented" value="${[org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.RESULTS, org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.CLOSED].contains(participatoryBudget.status)?'true':'false'}"/>
<g:set var="visibleTechnicalReviewStatus" value="${![org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.ADDING_PROPOSALS].contains(participatoryBudget.status)?'true':'false'}"/>
<g:set var="currentLang" value="${org.springframework.web.servlet.support.RequestContextUtils.getLocale(request)}" />

<div class="actions" id="toolbar-participatoryBudgetProposalReviewTable">
    <g:link mapping="politicianMassMailingParticipatoryBudgetReport" params="[campaignId:campaign.id]" class="btn btn-blue inverted export-modal-button" data-modalId="export-proposalsList-modal">
        <span class="fal fa-file-excel"></span>
        <g:message code="tools.massMailing.view.participatoryBudget.report"/>
    </g:link>

    <a href="#" class="btn btn-blue inverted" id="changeParticipatoryBudgetBtn">
        <span class="fal fa-edit"></span>
        <g:message code="org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.label"/>:
        <span id="changeParticipatoryBudgetBtnStatusText">
            <g:message code="org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.${participatoryBudget.status}"/>
        </span>
    </a>
</div>
<table id="participatoryBudgetProposalReviewTable"
       data-toggle="table"
       data-url="${g.createLink(mapping:'participatoryBudgetDistrictProposalsPagination', params:participatoryBudget.encodeAsLinkProperties())}"
       data-side-pagination="server"
       data-toolbar="#toolbar-participatoryBudgetProposalReviewTable"
       data-show-export="true"
       data-id-field="id"
       data-pagination="true"
       data-search="false"
       data-filter-control="true"
       data-locale="${currentLang}"
       data-detail-view="true"
       data-show-columns="true"
       data-row-style="districtProposalTableRowStyle"
       data-detail-formatter="detailFormatter"
       data-update-technicalReview-url="${g.createLink(mapping:'participatoryBudgetDistrictProposalsTechnicalReview', params:participatoryBudget.encodeAsLinkProperties())}"
    >
    <thead>
    <tr>
        <th data-field="id"                         data-halign="center" data-align="center" data-sortable="true"  data-visible="true"  data-filter-control="input"  data-formatter="formatTableParticipatoryBudgetDistrictProposalId" data-filter-control-placeholder="${g.message(code:'tools.massMailing.view.participatoryBudget.proposalList.table.id.searchPlaceholder')}"     ><g:message code="tools.massMailing.view.participatoryBudget.proposalList.table.id"/></th>
        <th data-field="title"                      data-halign="center" data-align="center" data-sortable="true"  data-visible="true"  data-filter-control="input"                                                                    data-filter-control-placeholder="${g.message(code:'tools.massMailing.view.participatoryBudget.proposalList.table.title.searchPlaceholder')}"  ><g:message code="tools.massMailing.view.participatoryBudget.proposalList.table.title"/></th>
        <th data-field="district.name"              data-halign="center" data-align="center" data-sortable="true"  data-visible="true"  data-filter-control="select" data-filter-data="var:filterDataDistrict"          ><g:message code="tools.massMailing.view.participatoryBudget.proposalList.table.districtName"/></th>
        <th data-field="district.budget"            data-halign="center" data-align="center" data-sortable="false" data-visible="false"                                                                                 ><g:message code="tools.massMailing.view.participatoryBudget.proposalList.table.districtBudget"/></th>
        <th data-field="cause"                      data-halign="center" data-align="center" data-sortable="false" data-visible="false" data-filter-control="select" data-filter-data="var:filterDataCause"             ><g:message code="tools.massMailing.view.participatoryBudget.proposalList.table.cause"/></th>
        <th data-field="user.name"                  data-halign="center" data-align="center" data-sortable="true"  data-visible="false" data-filter-control="input"  data-formatter="formatTableParticipatoryBudgetDistrictProposalAuthor" data-filter-control-placeholder="${g.message(code:'tools.massMailing.view.participatoryBudget.proposalList.table.author.searchPlaceholder')}" ><g:message code="tools.massMailing.view.participatoryBudget.proposalList.table.author"/></th>
        <th data-field="numSupports"                data-halign="center" data-align="center" data-sortable="true"  data-visible="${visibleSupportColumn}"                                                               ><g:message code="tools.massMailing.view.participatoryBudget.proposalList.table.supports"/></th>
        <th data-field="numVotes"                   data-halign="center" data-align="center" data-sortable="true"  data-visible="${visibleVotesColumn}"                                                                 ><g:message code="tools.massMailing.view.participatoryBudget.proposalList.table.votes"/></th>
        <th data-field="approved"                   data-halign="center" data-align="center" data-sortable="false" data-visible="${visibleTechnicalReviewStatus}"   data-filter-control="select" data-formatter="formatCheckValidation" data-events="inputEventsCheckValidation" data-filter-data="var:filterDataApproved"             ><g:message code="tools.massMailing.view.participatoryBudget.proposalList.table.approved"/></th>
        <th data-field="price"                      data-halign="center" data-align="center" data-sortable="true"  data-visible="${visibleTechnicalReviewStatus}"                                data-formatter="formatPrice"           data-events="inputEventsPrice"                                                              ><g:message code="tools.massMailing.view.participatoryBudget.proposalList.table.price"/></th>
        <th data-field="rejectComment"              data-halign="center" data-align="center" data-sortable="false" data-visible="${visibleTechnicalReviewStatus}"   data-filter-control="input"  data-formatter="formatRejectText"      data-events="inputEventsRejectComment"  data-filter-control-placeholder="${g.message(code:'tools.massMailing.view.participatoryBudget.proposalList.table.rejectComment.searchPlaceholder')}" ><g:message code="tools.massMailing.view.participatoryBudget.proposalList.table.rejectComment"/></th>
        <th data-field="technicalReviewStatus.i18n" data-halign="center" data-align="center" data-sortable="false" data-visible="${visibleTechnicalReviewStatus}"   data-filter-control="select"                                                                    data-filter-data="var:filterDataTechnicalReview"    ><g:message code="tools.massMailing.view.participatoryBudget.proposalList.table.technicalReviewStatus"/></th>
        <th data-field="implemented"                data-halign="center" data-align="center" data-sortable="false" data-visible="${visibleImplemented}"             data-filter-control="select" data-formatter="formatBoolean"                                                 data-filter-data="var:filterDataImplemented"        ><g:message code="tools.massMailing.view.participatoryBudget.proposalList.table.implemented"/></th>
    </tr>
    </thead>
</table>

<div id="export-proposalsList-modal" class="modal fade in" tabindex="-1" role="dialog" aria-labelledby="exportDebateStatsTitle" aria-hidden="true">
    <div class="modal-dialog ">
        <div class="modal-content">
            <div class="modal-header"><h4><g:message code="modal.exportedParticipatoryBudgetProposals.title"/></h4></div>
            <div class="modal-body">
                <p>
                    <g:message code="modal.exportedParticipatoryBudgetProposals.explanation" args="[participatoryBudget.title]"/>
                </p>
            </div>
            <div class="modal-footer">
                <a href="#" class="btn" data-dismiss="modal" aria-label="Close"><g:message code="modal.exportedDebateStats.close"/></a>
            </div>
        </div>
    </div>
</div>

<div id="changeParticipatoryBudgetStatusModal" class="modal fade in" tabindex="-1" role="dialog" aria-labelledby="changeParticipatoryBudgetStatusTitle" aria-hidden="true">
    <div class="modal-dialog ">
        <div class="modal-content">
            <div class="modal-header"><h4><g:message code="modal.changeParticipatoryBudgetStatusModal.title"/></h4></div>
            <div class="modal-body">
                <p>
                    <g:message code="modal.changeParticipatoryBudgetStatusModal.text" args="[participatoryBudget.title]"/>
                </p>
                <select class="form-control input-lg" id="changeParticipatoryBudgetStatusModalSelect">
                    <g:each in="${org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO.values()}" var="status">
                        <option value="${status}" ${participatoryBudget.status==status?'selected':''}>${g.message(code:"org.kuorum.rest.model.communication.participatoryBudget.ParticipatoryBudgetStatusDTO."+status)}</option>
                    </g:each>
                </select>

            </div>
            <div class="modal-footer">
                <a href="#" class="btn btn-grey-light" data-dismiss="modal" aria-label="Close"><g:message code="modal.changeParticipatoryBudgetStatusModal.close"/></a>
                <g:link mapping="participatoryBudgetEditStatus" params="${participatoryBudget.encodeAsLinkProperties()}" class="btn btn-blue" aria-label="Ok" elementId="changeParticipatoryBudgetStatusSubmit"><g:message code="modal.changeParticipatoryBudgetStatusModal.submit"/></g:link>
            </div>
        </div>
    </div>
</div>