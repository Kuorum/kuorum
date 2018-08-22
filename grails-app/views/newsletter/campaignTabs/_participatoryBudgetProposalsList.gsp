
Lista de propuestas de ${participatoryBudget}

<div id="toolbar"></div>
<table id="table"
       data-toggle="table"
       data-url="${g.createLink(mapping:'participatoryBudgetDistrictProposalsPaginationTechincalReview', params:participatoryBudget.encodeAsLinkProperties())}"
       data-side-pagination="server"
       data-toolbar="#toolbar"
       data-id-field="id"
       data-pagination="true"
       data-search="true"
       data-filter-control="true"
       data-detail-view="true"
       data-show-columns="true"
       data-row-style="rowStyle"
       data-detail-formatter="detailFormatter">
    <thead>
    <tr>
        <th data-field="id" data-sortable="true" data-filter-control="input">ID</th>
        <th data-field="name" data-sortable="true" data-filter-control="input">Item Name</th>
        <th data-field="price" data-sortable="true" data-filter-control="input" data-formatter="formatEditableField" data-events="inputEventsPrice">Item Price</th>
        <th data-field="valid" data-sortable="false" data-visible="true" data-filter-control="select" data-formatter="formatBoolean">Valid</th>
    </tr>
    </thead>
</table>

