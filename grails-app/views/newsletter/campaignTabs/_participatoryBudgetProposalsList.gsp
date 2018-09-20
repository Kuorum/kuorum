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

<div id="toolbar"></div>
<table id="participatoryBudgetProposalReviewTable"
       data-toggle="table"
       data-url="${g.createLink(mapping:'participatoryBudgetDistrictProposalsPagination', params:participatoryBudget.encodeAsLinkProperties())}"
       data-side-pagination="server"
       data-toolbar="#toolbar"
       data-show-export="true"
       data-id-field="id"
       data-pagination="true"
       data-search="false"
       data-filter-control="true"
       data-detail-view="true"
       data-show-columns="true"
       data-row-style="districtProposalTableRowStyle"
       data-detail-formatter="detailFormatter"
       data-update-technicalReview-url="${g.createLink(mapping:'participatoryBudgetDistrictProposalsTechnicalReview', params:participatoryBudget.encodeAsLinkProperties())}"
    >
    <thead>
    <tr>
        <th data-field="id"                         data-halign="center" data-align="center" data-sortable="true"  data-visible="true"  data-filter-control="input"  data-formatter="formatTableParticipatoryBudgetDistrictProposalId" data-filter-control-placeholder="Search By ID"     ><g:message code="tools.massMailing.view.participatoryBudget.proposalList.table.id"/></th>
        <th data-field="title"                      data-halign="center" data-align="center" data-sortable="true"  data-visible="true"  data-filter-control="input"  data-filter-control-placeholder="Search By Title"  ><g:message code="tools.massMailing.view.participatoryBudget.proposalList.table.title"/></th>
        <th data-field="district.name"              data-halign="center" data-align="center" data-sortable="true"  data-visible="true"  data-filter-control="select" data-filter-data="var:filterDataDistrict"          ><g:message code="tools.massMailing.view.participatoryBudget.proposalList.table.districtName"/></th>
        <th data-field="district.budget"            data-halign="center" data-align="center" data-sortable="false" data-visible="false"                                                                                 ><g:message code="tools.massMailing.view.participatoryBudget.proposalList.table.districtBudget"/></th>
        <th data-field="cause"                      data-halign="center" data-align="center" data-sortable="false" data-visible="false" data-filter-control="select" data-filter-data="var:filterDataCause"             ><g:message code="tools.massMailing.view.participatoryBudget.proposalList.table.cause"/></th>
        <th data-field="user.name"                  data-halign="center" data-align="center" data-sortable="true"  data-visible="false" data-filter-control="input"  data-formatter="formatTableParticipatoryBudgetDistrictProposalAuthor" data-filter-control-placeholder="Search By author" ><g:message code="tools.massMailing.view.participatoryBudget.proposalList.table.author"/></th>
        <th data-field="numSupports"                data-halign="center" data-align="center" data-sortable="true"  data-visible="${visibleSupportColumn}"                                                               ><g:message code="tools.massMailing.view.participatoryBudget.proposalList.table.supports"/></th>
        <th data-field="numVotes"                   data-halign="center" data-align="center" data-sortable="true"  data-visible="${visibleVotesColumn}"                                                                 ><g:message code="tools.massMailing.view.participatoryBudget.proposalList.table.votes"/></th>
        <th data-field="approved"                   data-halign="center" data-align="center" data-sortable="false" data-visible="${visibleTechnicalReviewStatus}"   data-filter-control="select" data-formatter="formatCheckValidation" data-events="inputEventsCheckValidation" data-filter-data="var:filterDataApproved"             ><g:message code="tools.massMailing.view.participatoryBudget.proposalList.table.approved"/></th>
        <th data-field="price"                      data-halign="center" data-align="center" data-sortable="true"  data-visible="${visibleTechnicalReviewStatus}"                                data-formatter="formatPrice"           data-events="inputEventsPrice"                                                              ><g:message code="tools.massMailing.view.participatoryBudget.proposalList.table.price"/></th>
        <th data-field="rejectComment"              data-halign="center" data-align="center" data-sortable="false" data-visible="${visibleTechnicalReviewStatus}"   data-filter-control="input"  data-formatter="formatRejectText"      data-events="inputEventsRejectComment"  data-filter-control-placeholder="Search By comment" ><g:message code="tools.massMailing.view.participatoryBudget.proposalList.table.rejectComment"/></th>
        <th data-field="technicalReviewStatus.i18n" data-halign="center" data-align="center" data-sortable="false" data-visible="${visibleTechnicalReviewStatus}"   data-filter-control="select"                                                                    data-filter-data="var:filterDataTechnicalReview"    ><g:message code="tools.massMailing.view.participatoryBudget.proposalList.table.technicalReviewStatus"/></th>
        <th data-field="implemented"                data-halign="center" data-align="center" data-sortable="false" data-visible="${visibleImplemented}"             data-filter-control="select" data-formatter="formatBoolean"                                                 data-filter-data="var:filterDataImplemented"        ><g:message code="tools.massMailing.view.participatoryBudget.proposalList.table.implemented"/></th>
    </tr>
    </thead>
</table>

