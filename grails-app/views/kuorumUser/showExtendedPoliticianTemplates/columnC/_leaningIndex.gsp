<g:set var="leaningIndex" value="${politician?.politicianLeaning?.liberalIndex?:50}"/>
<section class="panel panel-default">
    <div class="panel-heading">
        <h3 class="panel-title"><g:message code="politician.leaningIndex.title"/> </h3>
    </div>
    <div class="panel-body text-center">
        <!-- <span class="badge badge-default">60%</span> -->
        <div class="tooltip top" role="tooltip" style="position:relative;opacity:1;left:${leaningIndex}%;width: 3em;margin-left: -1.5em;margin-bottom: 2px;">
            <div class="tooltip-arrow"></div>
            <div class="tooltip-inner">${leaningIndex}%</div>
        </div>
        <div class="progress" >
            <div class="progress-bar progress-bar-warning" role="progressbar" aria-valuenow="${leaningIndex}" aria-valuemin="0" aria-valuemax="100" style="width: ${leaningIndex}%">
                <span class="sr-only">${leaningIndex}% Complete</span>
            </div>
        </div>
        <div class="clearfix">
            <span class="pull-left"><g:message code="politician.leaningIndex.left"/></span>
            <span class="pull-right"><g:message code="politician.leaningIndex.right"/></span>
        </div>
        <div class="text-right">
            <a href="#">
                <g:message code="politician.leaningIndex.link"/>
            </a>
        </div>
    </div>
</section>