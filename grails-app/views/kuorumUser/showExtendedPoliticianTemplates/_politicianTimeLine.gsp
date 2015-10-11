%{--<g:if test="${politician.timeLine}">--}%
<div class="panel panel-default" id="political-experience">
    <div class="panel-heading">
        <h3 class="panel-title">
            <g:message code="politician.timeLine.title"/>
        </h3>
    </div>
    <div class="panel-body text-center">
        <ul class="timeline">
            <g:each in="${politician.timeLine}" var="timeEvent" status="i">
                <g:set var="cssPos" value=""/>
                <g:set var="sizePoint" value="small"/>
                <g:if test="${timeEvent.important}">
                    <g:set var="sizePoint" value=""/>
                </g:if>
                <g:if test="${i % 2}">
                    <g:set var="cssPos" value="timeline-inverted"/>
                </g:if>

                <li class="${cssPos}">
                    <div class="timeline-badge warning ${sizePoint}"></div>
                    <div class="timeline-panel">
                        <div class="timeline-heading">
                            <h4 class="timeline-title">${timeEvent.title}</h4>
                            <!--  <p class='updates-meta'><i class="glyphicon glyphicon-time"></i> 11 hours ago via Twitter</small></p> -->
                        </div>
                        <div class="timeline-body">
                            <p>${timeEvent.text}</p>
                        </div>
                    </div>
                    <div class="date text-warning">
                        <g:formatDate date="${timeEvent.date}" format="dd-MM-yyyy"/>
                    </div>
                </li>
            </g:each>
        </ul><!--/.timeline-->
    </div><!--/.panel-body.text-center -->
</div>
%{--</g:if>--}%