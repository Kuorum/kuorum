<!-- Módulo Gráficos evaluación político -->
<r:require modules="statCharts"/>
<section class="panel panel-default" id="right-panel-politicalValuationChart">
    <div class="panel-heading">
        <h3 class="panel-title"><g:message code="politician.valuation.chart.module.title"/> </h3>
    </div>
    <div class="panel-body text-center">
        <div class="polValChart" data-urljs="${createLink(mapping: 'userHistoricRate', params:user.encodeAsLinkProperties(), absolute: true)}"></div>
    </div>
</section>