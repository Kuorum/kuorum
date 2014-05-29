<%@ page import="kuorum.core.model.LawStatusType; kuorum.core.model.VoteType" %>
<section class="boxes vote" id="vote" data-lawId="${law.id}">
    <g:render template="/law/lawVotesModuleIntro" model="[law:law, userVote: userVote, title:title,necessaryVotesForKuorum:necessaryVotesForKuorum ]"/>
    <g:render template="/law/lawVotesModuleVotingButtons" model="[law:law, userVote: userVote]"/>
    <g:if test="${social}">
        <g:render template="/law/lawSocialShare" model="[law:law]"/>
    </g:if>
</section>