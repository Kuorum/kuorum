<%@ page import="kuorum.core.model.UserType" %>
<section class="panel panel-default" id="${panelId?:''}">
    <div class="panel-heading">
        <h3 class="panel-title"><g:message code="politician.leaningIndex.title"/> </h3>
    </div>
    <div class="panel-body text-center">
        <div class="tooltip top" role="tooltip" style="position:relative;opacity:1;left:${leaningIndex.liberalIndex}%;width: 3em;margin-left: -1.5em;margin-bottom: 2px;">
            <div class="tooltip-arrow"></div>
            <div class="tooltip-inner">${leaningIndex.liberalIndex}%</div>
        </div>
        <div class="progress" >
            <div class="progress-bar progress-bar-warning" role="progressbar" aria-valuenow="${leaningIndex.liberalIndex}" aria-valuemin="0" aria-valuemax="100" style="width: ${leaningIndex.liberalIndex}%">
                <span class="sr-only">${leaningIndex.liberalIndex}% Complete</span>
            </div>
        </div>
        <div class="clearfix">
            <span class="pull-left"><g:message code="politician.leaningIndex.left"/></span>
            <span class="pull-right"><g:message code="politician.leaningIndex.right"/></span>
        </div>
        <g:if test="${user.userType == UserType.POLITICIAN}">
            <div class="text-right">
                <a href="javascript:return false" class="popover-trigger" data-trigger="manual" rel="popover" role="button" data-toggle="popover" data-original-title="" title="" id="seeMore-politicalLeaningIndex">
                    <g:message code="politician.leaningIndex.link"/>
                </a>
                <div class="popover">
                    <g:message code="politician.leaningIndex.description"/>
                </div>
            </div>
        </g:if>
        <g:else>
            <div class="info-text">
                This information won’t be showed
            </div>
        </g:else>
    </div>
</section>