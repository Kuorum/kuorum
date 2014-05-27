<%@ page import="kuorum.core.model.LawStatusType; kuorum.core.model.VoteType" %>
<section class="boxes vote" id="vote" data-lawId="${law.id}">
    <g:if test="${title}">
        <h1><g:message code="law.vote.title"/></h1>
        <p><g:message code="law.vote.description"/></p>
    </g:if>
    <ul class="activity">
        <li class="POSITIVE ${userVote?.voteType==VoteType.POSITIVE?'active':''}"><span>${law.peopleVotes.yes}</span> <g:message code="law.vote.yes"/></li>
        <li class="NEGATIVE ${userVote?.voteType==VoteType.NEGATIVE?'active':''}"><span>${law.peopleVotes.no}</span> <g:message code="law.vote.no"/></li>
        <li class="ABSTENTION ${userVote?.voteType==VoteType.ABSTENTION?'active':''}"><span>${law.peopleVotes.abs}</span> <g:message code="law.vote.abs"/></li>
    </ul>
    <div class="kuorum">
        <g:message code="law.vote.kuorum.title" args="[necessaryVotesForKuorum]" encodeAs="raw"/>
        <span class="popover-trigger fa fa-info-circle" data-toggle="popover" rel="popover" role="button"></span>
        <!-- POPOVER KUORUM -->
        <div class="popover">
            <div class="popover-kuorum">

                <p class="text-center"><g:message code="law.vote.kuorum.info.title" encodeAs="raw"/></p>
                <p><g:message code="law.vote.kuorum.info.description"/></p>
            </div><!-- /popover-more-kuorum -->
        </div>
        <!-- FIN POPOVER KUORUM -->
    </div>
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
        <a href="#">Ficha técnica</a>
    </div>
    <g:if test="${social}">
        <g:render template="/law/lawSocialShare" model="[law:law]"/>
    </g:if>
</section>