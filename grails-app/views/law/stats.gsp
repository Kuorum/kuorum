<%@ page import="kuorum.core.model.Gender; kuorum.core.model.VoteType; kuorum.core.model.PostType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="law.stats.head.title" args="[law.shortName, law.hashtag]"/></title>
    <meta name="layout" content="columnCLayout">
    <g:render template="lawMetaTags" model="[law:law]"/>
    <r:require module="lawStats"/>
    <script>
        var urlDataMap = '${createLink(mapping: 'lawStatsDataMap', params: law.encodeAsLinkProperties())}'
        var urlPieChart = '${createLink(mapping: 'lawStatsPieChart', params: law.encodeAsLinkProperties())}'
    </script>
</head>


<content tag="mainContent">
    <article class="kakareo post ley ficha" role="article" itemscope itemtype="http://schema.org/Article">
        <g:link mapping="lawShow" itemprop="keywords" class="laley" params="${law.encodeAsLinkProperties()}">${law.hashtag}</g:link>
        %{--<a  href="#" itemprop="keywords" class="laley">${law.hashtag}</a>--}%
        <h1>${law.shortName}</h1>
        <p class="cl-ntral-dark">${law.realName}</p>
        <h2 class="pull-left"><g:message code="law.stats.introRegionName"/> <span>${region.name}</span></h2>
        <a id="allMap" class="pull-right"><g:message code="law.stats.restartParentRegion"/> </a>
        <div id="map"></div>
        <dl class="moreInfo row">
            <div class="col-xs-12 col-sm-6 col-md-6">
                <dt><g:message code="law.stats.total.numVotes.total"/></dt>
                <dd>${law.peopleVotes.total}</dd>
            </div>
            <div class="col-xs-12 col-sm-6 col-md-6">
                <dt><g:message code="law.stats.total.numVotes.abs"/></dt>
                <dd>${law.peopleVotes.abs}</dd>
            </div>
            <div class="col-xs-12 col-sm-6 col-md-6">
                <dt><g:message code="law.stats.total.numVotes.yes"/></dt>
                <dd>${law.peopleVotes.yes}</dd>
            </div>
            <div class="col-xs-12 col-sm-6 col-md-6">
                <dt><g:message code="law.stats.total.numVotes.no"/></dt>
                <dd>${law.peopleVotes.no}</dd>
            </div>
         </dl>
        <small><g:message code="law.stats.total.description"/> </small>
    </article><!-- /article -->
</content>

<content tag="cColumn">
    <div class="back">
        <g:link mapping="lawShow" class="btn btn-blue" params="${law.encodeAsLinkProperties()}">
            <g:message code="law.stats.backToLaw"/>
        </g:link>
    </div>
    <section class="boxes vote votation" id="votation">
        <h1><g:message code="law.stats.columnC.title"/> </h1>
        <div class="graphContainer">
            <h2><g:message code="law.stats.columnC.pieChart.title"/></h2>
            <p>
                <span class="numberVotes">${law.peopleVotes.total}</span>
                <span><g:message code="law.stats.columnC.pieChart.votes"/></span>
                <small><g:message code="law.stats.columnC.pieChart.remainingKuorumVotes" args="[necessaryVotesForKuorum]"/></small>
            </p>
            <div id="placeholder">
                <canvas id="votesChart" width="255" height="255"></canvas>
            </div>
        </div>
        <script>
            var votes = {
                        yes: ${law.peopleVotes.yes},
                        no: ${law.peopleVotes.no},
                        abs:${law.peopleVotes.abs},
                        total:${law.peopleVotes.total}
                };
        </script>
        <ul class="activity">
            <li class="favor"><span>${stats.totalVotes.yes}</span> <g:message code="law.stats.columnC.vote.yes"/></li>
            <li class="contra"><span>${stats.totalVotes.no}</span> <g:message code="law.stats.columnC.vote.no"/></li>
            <li class="abstencion"><span>${stats.totalVotes.abs}</span> <g:message code="law.stats.columnC.vote.abs"/></li>
        </ul>
        <table class="segmentation">
            <caption class="sr-only"><g:message code="law.stats.columnC.byGender.title"/> </caption>
            <thead>
            <tr>
                <th class="hombre"><span><span class="sr-only"><g:message code="kuorum.core.model.Gender.MALE"/> </span></span></th>
                <th class="mujer"><span><span class="sr-only"><g:message code="kuorum.core.model.Gender.FEMALE"/></span></span></th>
                <th class="todos"><span><span class="sr-only"><g:message code="kuorum.core.model.Gender.ORGANIZATION"/></span></span></th>
            </tr>
            </thead>
            <tbody>
            <tr class="favor">
                <td class="hombre"><span>${stats.genderVotes[Gender.MALE].yes}</span> <small><g:message code="law.stats.columnC.vote.yes"/></small></td>
                <td class="mujer"><span>${stats.genderVotes[Gender.FEMALE].yes}</span> <small><g:message code="law.stats.columnC.vote.yes"/></small></td>
                <td class="todos"><span>${stats.genderVotes[Gender.ORGANIZATION].yes}</span> <small><g:message code="law.stats.columnC.vote.yes"/></small></td>
            </tr>
            <tr class="contra">
                <td class="hombre"><span>${stats.genderVotes[Gender.MALE].no}</span> <small><g:message code="law.stats.columnC.vote.no"/></small></td>
                <td class="mujer"><span>${stats.genderVotes[Gender.FEMALE].no}</span> <small><g:message code="law.stats.columnC.vote.no"/></small></td>
                <td class="todos"><span>${stats.genderVotes[Gender.ORGANIZATION].no}</span> <small><g:message code="law.stats.columnC.vote.no"/></small></td>
            </tr>
            <tr class="abstencion">
                <td class="hombre"><span>${stats.genderVotes[Gender.MALE].abs}</span> <small><g:message code="law.stats.columnC.vote.abs"/></small></td>
                <td class="mujer"><span>${stats.genderVotes[Gender.FEMALE].abs}</span> <small><g:message code="law.stats.columnC.vote.abs"/></small></td>
                <td class="todos"><span>${stats.genderVotes[Gender.ORGANIZATION].abs}</span> <small><g:message code="law.stats.columnC.vote.abs"/></small></td>
            </tr>
            </tbody>
        </table>
        <dl class="infoVotation">
            <dt><g:message code="law.stats.columnC.infoLaw.status"/></dt><dd><g:message code="${kuorum.core.model.LawStatusType.name}.${law.status}"/> </dd>
            %{--<dt>Tipo de ley</dt><dd>Proyecto de ley</dd>--}%
            %{--<dt><g:message code="law.stats.columnC.infoLaw.proposalBy"/></dt><dd>${law.politicalParty.name}</dd>--}%
            <dt><g:message code="law.stats.columnC.infoLaw.commissions"/></dt>
            <dd>
                <g:set var="comma" value=""/>
                <g:each in="${law.commissions}" var="commission">
                    <g:message code="${kuorum.core.model.CommissionType.name}.${commission}"/>${comma}
                    <g:set var="comma" value=", "/>
                </g:each>
            </dd>
            %{--<dt>Tipo de tramitación</dt> <dd>Por determinar</dd>--}%
            <g:if test="${law.urlPdf}">
                <dt><g:message code="law.stats.columnC.infoLaw.pdf"/></dt>
                <dd><a href="${law.urlPdf}" target="_blank"><g:message code="law.stats.columnC.infoLaw.pdf.download"/></a></dd>
            </g:if>
        </dl>
    </section>
</content>
