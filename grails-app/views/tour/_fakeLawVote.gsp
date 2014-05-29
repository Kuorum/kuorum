<section class="boxes vote" id="vote">
    <g:render template="/law/lawVotesModuleIntro" model="[law:law, userVote: null, title:true,necessaryVotesForKuorum:necessaryVotesForKuorum ]"/>
    <div class="voting">
        <!-- LOGADO NO VOTADO -->
        <ul>
            <li><a href="#" class="btn btn-blue yes"><span class="icon-smiley fa-2x"></span><g:message code="law.vote.yes"/></a></li>
            <li><a href="#" class="btn btn-blue no"><span class="icon-sad fa-2x"></span><g:message code="law.vote.no"/></a></li>
            <li><a href="#" class="btn btn-blue neutral"><span class="icon-neutral fa-2x"></span> <g:message code="law.vote.abs"/></a></li>
        </ul>

        <!-- LOGADO VOTADO -->
        <a href="#" class="changeOpinion"><g:message code="law.vote.changeVote"/></a>

        <!-- NO LOGADO NO VOTADO -->
        <!-- <a href="#" class="btn btn-blue btn-block vote">Vota <br> <small>Es tu momento de hablar</small></a> --> <!-- al hacer click lo deshabilito y cambio el texto -->

        %{--<a href="#">Ficha técnica</a>--}%
    </div>

    <g:render template="/law/lawSocialShare" model="[law:law]"/>
</section>