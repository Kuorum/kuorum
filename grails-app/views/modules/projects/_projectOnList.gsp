<article class="kakareo post ley" role="article" itemtype="http://schema.org/Article">
    <div class="link-wrapper">
        <g:link mapping="projectShow" params="${project.encodeAsLinkProperties()}" class="hidden"/>
        <div class="user author" itemprop="author" itemtype="http://schema.org/Person">
            <userUtil:showUser user="${project.owner}"/>
            <span itemprop="datePublished">
                <time><span class="hidden-xs hidden-md"><g:message code="cluck.header.action.CREATE"/> </span><kuorumDate:humanDate date="${project.dateCreated}"/></time>
            </span>
        </div>
        <g:render template="/project/projectMultimedia" model="[project:project]"/>
        <div class="more-info">
            <div class="row">
                <div class="col-xs-8 laley" itemprop="keywords">${project.hashtag}</div>
                <div class="col-xs-4 infoVotes text-right">
                    <g:if test="${project.projectBasicStats.percentagePositiveVotes > project.projectBasicStats.percentageNegativeVotes && project.projectBasicStats.percentagePositiveVotes > project.projectBasicStats.percentageAbsVotes}">
                        <span class="vote-yes">
                            <span><g:formatNumber number="${project.projectBasicStats.percentagePositiveVotes}" type="percent"/> </span>
                            <span class="sr-only"><g:message code="project.subHeader.positiveVotes"/></span>
                            <span class="icon-smiley fa-lg"></span>
                        </span>
                    </g:if>
                    <g:elseif test="${project.projectBasicStats.percentageNegativeVotes > project.projectBasicStats.percentagePositiveVotes && project.projectBasicStatspercentageNegativeVotes > project.projectBasicStats.percentageAbsVotes}">
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
        <p><projectUtil:showFirstCharsFromDescription project="${project}" numChars="200"/></p>
        <footer>
            <div class="row">
                <ul class="col-xs-5 col-sm-5 col-md-6 info-kak">
                    <li itemprop="keywords">
                        <projectUtil:showProjectRegionIcon project="${project}"/>
                    </li>
                    <li itemprop="datePublished" class="hidden-xs hidden-sm">
                        <kuorumDate:humanDate date="${project.deadline}"/>
                    </li>
                </ul>
                <div class="col-xs-7 col-sm-7 col-md-6 voting">
                    <g:render template="/project/projectVotesModuleVotingButtons" model="[project:project, iconSmall:true, header:Boolean.TRUE]"/>
                </div>
            </div>
        </footer>
    </div>
</article>