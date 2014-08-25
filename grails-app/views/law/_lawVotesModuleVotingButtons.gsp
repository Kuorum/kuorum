<%@ page import="kuorum.core.model.VoteType; kuorum.core.model.LawStatusType" %>
<div class="voting">
    <g:if test="${law.status == kuorum.core.model.LawStatusType.OPEN}">
        <sec:ifLoggedIn>
            <ul style="${userVote?'display: none;':''}">
                <!-- LOGADO NO VOTADO -->
                <li>
                    <g:link mapping="lawVote" params="${law.encodeAsLinkProperties()+[voteType:VoteType.POSITIVE]}" class="btn btn-blue yes" data-lawId="${law.id}">
                        <span class="icon-smiley fa-2x"></span>
                        <g:message code="law.vote.yes"/>
                    </g:link>
                </li>
                <li>
                    <g:link mapping="lawVote" params="${law.encodeAsLinkProperties()+[voteType:VoteType.NEGATIVE]}" class="btn btn-blue no" data-lawId="${law.id}">
                        <span class="icon-sad fa-2x"></span>
                        <g:message code="law.vote.no"/>
                    </g:link>

                </li>
                <li>
                    <g:link mapping="lawVote" params="${law.encodeAsLinkProperties()+[voteType:VoteType.ABSTENTION]}" class="btn btn-blue neutral" data-lawId="${law.id}">
                        <span class="icon-neutral fa-2x"></span>
                        <g:message code="law.vote.abs"/>
                    </g:link>
                </li>
            </ul>

            <!-- LOGADO VOTADO -->
            <a href="#" class="changeOpinion" style="${userVote?'display: block;':''}"><g:message code="law.vote.changeVote"/></a>
        </sec:ifLoggedIn>
        <sec:ifNotLoggedIn>
            <g:link mapping="lawShowSec" params="${law.encodeAsLinkProperties()}" class="btn btn-blue btn-block vote">
                <g:message code="law.vote.voteButton" encodeAs="raw"/>
            </g:link><!-- al hacer click lo deshabilito y cambio el texto -->
        </sec:ifNotLoggedIn>
    </g:if> %{--FIN DE LA LEY ABIERTA--}%
    <g:else> %{-- LEY CERRADA--}%
        <g:set var="statusLaw" value="${message(code:"${LawStatusType.name}.${law.status}")}"/>
        <a href="#" class="btn btn-blue btn-block vote disabled"><g:message code="law.vote.voteClosed" args="[statusLaw.toLowerCase()]" encodeAs="raw"/></a><!-- al hacer click lo deshabilito y cambio el texto -->
    </g:else>
    <g:if test="${law.availableStats}">
        <g:link mapping="lawStats" params="${law.encodeAsLinkProperties()}" class="hidden-xs">
            <g:message code="law.vote.stats"/>
        </g:link>
    </g:if>
</div>