<%@ page import="kuorum.core.model.ProjectStatusType; kuorum.core.model.VoteType" %>
<section class="boxes vote signin" id="vote" data-projectId="${project.id}">
    <g:if test="${project.deadline > new Date()}">
        <g:set var="numDays" value="${kuorumDate.differenceDays(initDate:  project.deadline, endDate: new Date())}"/>
        <h1><g:message code="project.vote.headTitle" args="[numDays]"/> </h1>

        <div id="sign">
            <g:render template="/project/projectVotesModuleVotingButtons" model="[project:project, userVote: userVote, header:Boolean.FALSE, basicPersonalDataCommand:basicPersonalDataCommand]"/>
        </div>
    </g:if>
    <g:else>
        %{--PROYECTO CERRADO --}%
        <h1><g:message code="project.subHeader.closedProject"/></h1>
        %{--DESCOMENTAR Y CONFIGURAR BOTÓN CUANDO ESTÉ HECHO EL DESCUBRE--}%
        %{--<a href="#" class="btn btn-grey btn-lg"><g:message code="project.subHeader.closedProject.seeMoreProjects" encodeAs="raw"/></a>--}%
        <projectUtil:ifAllowedToUpdateProject project="${project}">
            <div class="action text-center"><a href="#" class="text-center"><span class="fa icon2-update fa-3x"></span></a></div>
        </projectUtil:ifAllowedToUpdateProject>
    </g:else>
    <g:if test="${social}">
        <g:render template="/project/projectSocialShare" model="[project:project]"/>
    </g:if>
</section>