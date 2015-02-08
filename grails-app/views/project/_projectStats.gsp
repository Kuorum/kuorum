<%@ page import="kuorum.core.model.Gender" %>
<ul class="col-xs-12 col-sm-8 col-md-8 pull-right infoVotes">
    <g:render template="/project/projectLiBasicPercentageStats" model="[project:project, projectStats:projectStats, extraCss:'']"/>
    <li class="arrow">
        <span class="sr-only"><g:message code="project.stats.moreData"/></span>
        <a aria-controls="detailsInfoVotes" aria-expanded="false" href="#detailsInfoVotes" data-toggle="collapse" class="open-infoVotes">
            <span class="fa fa-caret-down fa-lg"></span>
        </a>
    </li>
</ul>
<div class="col-xs-12 collapse" id="detailsInfoVotes">
    <div class="row">
        <div class="col-sm-8 col-sm-push-4 col-md-9 col-md-push-3 col-lg-10 col-lg-push-2">
            <table class="table table-condensed">
                <thead class="sr-only">
                <tr>
                    <th></th>
                    <th><g:message code="project.list.project.votesInFavour"/> </th>
                    <th><g:message code="project.list.project.votesAgainst"/></th>
                    <th><g:message code="project.list.project.abstentions"/></th>
                    <th><g:message code="project.list.project.proposals"/></th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <th scope="row">
                        <span class="fa fa-female hidden-md hidden-lg"></span>
                        <span class="hidden-xs hidden-sm"><g:message code="project.stats.women"/></span>
                    </th>
                    <td>${regionStats.genderVotes[Gender.FEMALE].yes}</td>
                    <td>${regionStats.genderVotes[Gender.FEMALE].no}</td>
                    <td>${regionStats.genderVotes[Gender.FEMALE].abs}</td>
                    <td>${regionStats.genderVotes[Gender.FEMALE].numPosts}</td>
                </tr>
                <tr>
                    <th scope="row">
                        <span class="fa fa-male hidden-md hidden-lg"></span>
                        <span class="hidden-xs hidden-sm"><g:message code="project.stats.men"/></span>
                    </th>
                    <td>${regionStats.genderVotes[Gender.MALE].yes}</td>
                    <td>${regionStats.genderVotes[Gender.MALE].no}</td>
                    <td>${regionStats.genderVotes[Gender.MALE].abs}</td>
                    <td>${regionStats.genderVotes[Gender.MALE].numPosts}</td>
                </tr>
                <tr>
                    <th scope="row">
                        <span class="fa fa-users hidden-md hidden-lg"></span>
                        <span class="hidden-xs hidden-sm"><g:message code="project.stats.organizations"/></span></th>
                    <td>${regionStats.genderVotes[Gender.ORGANIZATION].yes}</td>
                    <td>${regionStats.genderVotes[Gender.ORGANIZATION].no}</td>
                    <td>${regionStats.genderVotes[Gender.ORGANIZATION].abs}</td>
                    <td>${regionStats.genderVotes[Gender.ORGANIZATION].numPosts}</td>
                </tr>
                <tr>
                    <th scope="row">
                        <span class="hidden-md hidden-lg">*</span>
                        <span class="hidden-xs hidden-sm"><g:message code="project.stats.otherRegions"/></span>
                    </th>
                    <td>${regionStats.noRegionVotes.yes}</td>
                    <td>${regionStats.noRegionVotes.no}</td>
                    <td>${regionStats.noRegionVotes.abs}</td>
                    <td>${regionStats.noRegionVotes.numPosts}</td>
                </tr>
                </tbody>
            </table>
        </div>
        <g:render template="projectInfoIcons" model="[project:project]"/>
    </div>
</div>