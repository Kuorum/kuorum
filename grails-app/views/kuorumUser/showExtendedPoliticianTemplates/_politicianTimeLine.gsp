<g:if test="${politician.timeLine}">
    <div class="panel panel-default" id="political-experience-timeline">
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
                                %{--<p class='updates-meta'><i class="glyphicon glyphicon-time"></i> 11 hours ago via Twitter</small></p>--}%
                            </div>
                            <div class="timeline-body">
                                <p>${timeEvent.text}</p>
                                %{--<hr>--}%
                                %{--<div class="btn-group">--}%
                                    %{--<button type="button" class="btn btn-primary btn-sm dropdown-toggle" data-toggle="dropdown">--}%
                                        %{--<i class="glyphicon glyphicon-cog"></i> <span class="caret"></span>--}%
                                    %{--</button>--}%
                                    %{--<ul class="dropdown-menu" role="menu">--}%
                                        %{--<li><a href="#">Action</a></li>--}%
                                        %{--<li><a href="#">Another action</a></li>--}%
                                        %{--<li><a href="#">Something else here</a></li>--}%
                                        %{--<li class="divider"></li>--}%
                                        %{--<li><a href="#">Separated link</a></li>--}%
                                    %{--</ul>--}%
                                %{--</div>--}%
                            </div>
                        </div>
                        <div class="date text-brand-dark">
                            <g:formatDate date="${timeEvent.date}" format="yyyy"/>
                        </div>
                    </li>
                </g:each>
            </ul><!--/.timeline-->
        </div><!--/.panel-body.text-center -->
    </div>
</g:if>