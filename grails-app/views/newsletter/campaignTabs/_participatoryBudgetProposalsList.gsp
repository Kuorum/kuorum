<script>
    var filterDataValid={'true':'Valida','key2':'No valida'}
    var filterDataImplemented={'true':'Seleccionada','key2':'No seleccionada'}
    var filterDataDistrict={ ${raw(participatoryBudget.districts.collect{"'${it.id}':'${it.name}'"}.join(","))} };
    var filterDataCause={ ${raw(participatoryBudget.causes.collect{"'${it}':'${it}'"}.join(","))} };
    var filterDataTechnicalReview={ ${raw(org.kuorum.rest.model.communication.participatoryBudget.TechnicalReviewStatusRDTO.values().collect{"'${it}':'${g.message(code:"org.kuorum.rest.model.communication.participatoryBudget.TechnicalReviewStatusRDTO."+it)}'"}.join(','))} };

    function formatTechnicalReview(value){
        <g:each in="${org.kuorum.rest.model.communication.participatoryBudget.TechnicalReviewStatusRDTO.values()}" var="techincalStatus">
            if (value=='${techincalStatus}'){
                return '${g.message(code:"org.kuorum.rest.model.communication.participatoryBudget.TechnicalReviewStatusRDTO.${techincalStatus}")}'
            }
        </g:each>
        return "UNDEFINED"
    }
</script>
<div id="toolbar"></div>
<table id="table"
       data-toggle="table"
       data-url="${g.createLink(mapping:'participatoryBudgetDistrictProposalsPaginationTechincalReview', params:participatoryBudget.encodeAsLinkProperties())}"
       data-side-pagination="server"
       data-toolbar="#toolbar"
       data-id-field="id"
       data-pagination="true"
       data-search="false"
       data-filter-control="true"
       data-detail-view="true"
       data-show-columns="true"
       data-row-style="rowStyle"
       data-detail-formatter="detailFormatter">
    <thead>
    <tr>
        <th data-field="id"                         data-halign="center" data-align="center" data-sortable="true"  data-visible="true"  data-filter-control="input"  data-filter-control-placeholder="Search By ID"      > ID</th>
        <th data-field="title"                      data-halign="center" data-align="center" data-sortable="true"  data-visible="true"  data-filter-control="input"  data-filter-control-placeholder="Search By Title"   >Titulo</th>
        <th data-field="district.name"              data-halign="center" data-align="center" data-sortable="true"  data-visible="true"  data-filter-control="select" data-filter-data="var:filterDataDistrict">Distrito</th>
        <th data-field="district.budget"            data-halign="center" data-align="center" data-sortable="false" data-visible="false"                                                                                  >Distrito (Budget)</th>
        <th data-field="causes[0]"                  data-halign="center" data-align="center" data-sortable="false" data-visible="true"  data-filter-control="select" data-filter-data="var:filterDataCause"           > Causa</th>
        <th data-field="user.name"                  data-halign="center" data-align="center" data-sortable="true"  data-visible="false"  data-filter-control="input"  data-filter-control-placeholder="Search By author"   >Autor</th>
        <th data-field="numSupports"                data-halign="center" data-align="center" data-sortable="true"  data-visible="true"  >Apoyos</th>
        <th data-field="numVotes"                   data-halign="center" data-align="center" data-sortable="true"  data-visible="true"  >Votos</th>
        <th data-field="approved"                   data-halign="center" data-align="center" data-sortable="false" data-visible="true"  data-filter-control="select" data-formatter="formatCheckValidation" data-events="inputEventsCheckValidation" data-filter-data="var:filterDataValid">Valid</th>
        <th data-field="price"                      data-halign="center" data-align="center" data-sortable="true"  data-visible="true"                               data-formatter="formatPrice" data-events="inputEventsPrice">Item Price</th>
        <th data-field="rejectComment"              data-halign="center" data-align="center" data-sortable="false" data-visible="true"  data-filter-control="input"  data-filter-control-placeholder="Search By comment" data-formatter="formatRejectText" data-events="inputEventsPrice">Reject comment</th>
        <th data-field="technicalReviewStatus.name" data-halign="center" data-align="center" data-sortable="false" data-visible="true"  data-filter-control="select"  data-filter-data="var:filterDataTechnicalReview" data-formatter="formatTechnicalReview" >Technical review status</th>
        <th data-field="implemented"                data-halign="center" data-align="center" data-sortable="false" data-visible="true"  data-filter-control="select"  data-filter-data="var:filterDataImplemented" >Seleccionada</th>
    </tr>
    </thead>
</table>

