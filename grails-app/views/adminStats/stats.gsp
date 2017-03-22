<%@ page import="kuorum.core.model.Gender; kuorum.core.model.VoteType; kuorum.core.model.PostType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="admin.stats.title"/> </title>
    <meta name="layout" content="columnCLayout">
    <r:require module="projectStats"/>
    <script>
        var urlDataMap = '${createLink(mapping: 'adminStatsMap')}'
        var urlPieChart = '${createLink(mapping: 'adminStatsPieChart')}'
    </script>
</head>


<content tag="mainContent">
    <article class="kakareo post ley ficha" role="article" itemscope itemtype="http://schema.org/Article">
        <a  href="#" itemprop="keywords" class="laley">Volver</a>
        <h1><g:message code="admin.stats.title"/></h1>
        <p class="cl-ntral-dark"><g:message code="admin.stats.subTitle"/></p>
        <a id="allMap" class="pull-right"><g:message code="project.stats.restartParentRegion"/> </a>
        <div id="map"></div>
        <dl class="moreInfo row">
            <div class="col-xs-12 col-sm-6 col-md-6">
                <dt><g:message code="admin.stats.totalStats.total"/></dt>
                <dd>${totalStats.totalUsers}</dd>
            </div>
            <div class="col-xs-12 col-sm-6 col-md-6">
                <dt><g:message code="admin.stats.totalStats.activeUsers"/> </dt>
                <dd>${totalStats.activeUsers}</dd>
            </div>
            <div class="col-xs-12 col-sm-6 col-md-6">
                <dt><g:message code="admin.stats.totalStats.notActiveUsers"/></dt>
                <dd>${totalStats.notConfirmedUsers}</dd>
            </div>
            <div class="col-xs-12 col-sm-6 col-md-6">
                <dt><g:message code="admin.stats.totalStats.deletedUsers"/></dt>
                <dd>${totalStats.deleteUsers}</dd>
            </div>
            <div class="col-xs-12 col-sm-6 col-md-6">
                <dt><g:message code="admin.stats.totalStats.activePoliticians"/></dt>
                <dd>${totalStats.activePoliticians}</dd>
            </div>
            <div class="col-xs-12 col-sm-6 col-md-6">
                <dt><g:message code="admin.stats.totalStats.inactivePoliticians"/></dt>
                <dd>${totalStats.inactivePoliticians}</dd>
            </div>

        </dl>
        <small><g:message code="project.stats.total.description"/> </small>
    </article><!-- /article -->
</content>

<content tag="cColumn">
    <div class="back">
        <g:link mapping="adminPrincipal" class="btn btn-blue">
            <g:message code="project.stats.backToProject"/>
        </g:link>
    </div>
    <section class="boxes vote votation" id="votation">
        <h1><g:message code="project.stats.columnC.title"/> </h1>
        <div class="graphContainer">
            <h2><g:message code="admin.stats.totalStats.columnC.pieChart.title"/></h2>
            <p>
                <span class="numberVotes">${stats.totalVotes.total}</span>
            </p>
            <div id="placeholder">
                <canvas id="votesChart" width="255" height="255"></canvas>
            </div>
        </div>
        <script>
            var votes = {
                yes: ${stats.totalVotes.yes},
                no: ${stats.totalVotes.no},
                abs:${stats.totalVotes.abs},
                total:${stats.totalVotes.total}
            };
        </script>
        <ul class="activity">
            <li class="favor"><span>${stats.totalVotes.yes}</span> <g:message code="admin.stats.totalStats.columnC.activeUsers"/> </li>
            <li class="contra"><span>${stats.totalVotes.no}</span> <g:message code="admin.stats.totalStats.columnC.deletedUsers"/></li>
            <li class="abstencion"><span>${stats.totalVotes.abs}</span> <g:message code="admin.stats.totalStats.columnC.inactiveUsers"/></li>
        </ul>
        <table class="segmentation">
            <caption class="sr-only"><g:message code="project.stats.columnC.byGender.title"/> </caption>
            <thead>
            <tr>
                <th class="hombre"><span><span class="sr-only"><g:message code="kuorum.core.model.Gender.MALE"/> </span></span></th>
                <th class="mujer"><span><span class="sr-only"><g:message code="kuorum.core.model.Gender.FEMALE"/></span></span></th>
                <th class="todos"><span><span class="sr-only"><g:message code="kuorum.core.model.Gender.ORGANIZATION"/></span></span></th>
            </tr>
            </thead>
            <tbody>
            <tr class="favor">
                <td class="hombre"><span>${stats.genderVotes[Gender.MALE].yes}</span> <small><g:message code="admin.stats.totalStats.columnC.activeUsers"/></small></td>
                <td class="mujer"><span>${stats.genderVotes[Gender.FEMALE].yes}</span> <small><g:message code="admin.stats.totalStats.columnC.activeUsers"/></small></td>
                <td class="todos"><span>${stats.genderVotes[Gender.ORGANIZATION].yes}</span> <small><g:message code="admin.stats.totalStats.columnC.activeUsers"/></small></td>
            </tr>
            <tr class="contra">
                <td class="hombre"><span>${stats.genderVotes[Gender.MALE].no}</span> <small><g:message code="admin.stats.totalStats.columnC.deletedUsers"/></small></td>
                <td class="mujer"><span>${stats.genderVotes[Gender.FEMALE].no}</span> <small><g:message code="admin.stats.totalStats.columnC.deletedUsers"/></small></td>
                <td class="todos"><span>${stats.genderVotes[Gender.ORGANIZATION].no}</span> <small><g:message code="admin.stats.totalStats.columnC.deletedUsers"/></small></td>
            </tr>
            <tr class="abstencion">
                <td class="hombre"><span>${stats.genderVotes[Gender.MALE].abs}</span> <small><g:message code="admin.stats.totalStats.columnC.inactiveUsers"/></small></td>
                <td class="mujer"><span>${stats.genderVotes[Gender.FEMALE].abs}</span> <small><g:message code="admin.stats.totalStats.columnC.inactiveUsers"/></small></td>
                <td class="todos"><span>${stats.genderVotes[Gender.ORGANIZATION].abs}</span> <small><g:message code="admin.stats.totalStats.columnC.inactiveUsers"/></small></td>
            </tr>
            </tbody>
        </table>
        <dl class="infoVotation">
            <dt><g:message code="admin.stats.totalStats.columnC.totalProjects"/></dt><dd>${totalStats.totalProjects} </dd>
            <dt><g:message code="admin.stats.totalStats.columnC.openProjects"/></dt><dd>${totalStats.openProjects} </dd>
            <dt><g:message code="admin.stats.totalStats.columnC.closeProjects"/></dt><dd>${totalStats.closeProjects} </dd>
            <dt><g:message code="admin.stats.totalStats.columnC.numPosts"/></dt><dd>${totalStats.numPosts} </dd>
            <dt><g:message code="admin.stats.totalStats.columnC.numVictories"/></dt><dd>${totalStats.numVictories} </dd>
        </dl>
    </section>
</content>
