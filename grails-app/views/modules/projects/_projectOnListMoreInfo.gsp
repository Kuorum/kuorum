
%{--Hashtag can be set outside from SolrProject allowing highlighting--}%
<g:set var="hashtag" value="${hashtag?:project.hashtag}"/>
<div class="more-info">
    <div class="row">
        <div class="col-xs-8 laley" itemprop="keywords">${raw(hashtag)}</div>
        <div class="col-xs-4 infoVotes text-right">
            <g:if test="${project.projectBasicStats.percentagePositiveVotes > project.projectBasicStats.percentageNegativeVotes && project.projectBasicStats.percentagePositiveVotes > project.projectBasicStats.percentageAbsVotes}">
                <span class="vote-yes">
                    <span><g:formatNumber number="${project.projectBasicStats.percentagePositiveVotes}" type="percent"/> </span>
                    <span class="sr-only"><g:message code="project.subHeader.positiveVotes"/></span>
                    <span class="icon-smiley fa-lg"></span>
                </span>
            </g:if>
            <g:elseif test="${project.projectBasicStats.percentageNegativeVotes > project.projectBasicStats.percentagePositiveVotes && project.projectBasicStats.percentageNegativeVotes > project.projectBasicStats.percentageAbsVotes}">
                <span class="vote-no">
                    <span><g:formatNumber number="${project.projectBasicStats.percentageNegativeVotes}" type="percent"/> </span>
                    <span class="sr-only"><g:message code="project.subHeader.negativeVotes"/></span>
                    <span class="icon-sad fa-lg"></span>
                </span>
            </g:elseif>
            <g:else>
                <span class="vote-neutral">
                    <span><g:formatNumber number="${project.projectBasicStats.percentageAbsVotes}" type="percent"/> </span>
                    <span class="sr-only"><g:message code="project.subHeader.absVotes"/></span>
                    <span class="icon-neutral fa-lg"></span>
                </span>
            </g:else>
        </div>
    </div>
</div>