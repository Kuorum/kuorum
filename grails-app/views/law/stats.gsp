<%@ page import="kuorum.core.model.VoteType; kuorum.core.model.PostType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title>${law.shortName}</title>
    <meta name="layout" content="columnCLayout">
    <g:render template="lawMetaTags" model="[law:law]"/>
    <r:require module="lawStats"/>
    <script>
        var urlDataMap = '${createLink(mapping: 'lawStatsDataMap', params: law.encodeAsLinkProperties())}'
    </script>
</head>


<content tag="mainContent">
    <article class="kakareo post ley ficha" role="article" itemscope itemtype="http://schema.org/Article">
        <a  href="#" itemprop="keywords" class="laley">#laleyquesea</a>
        <h1>Ut enim ad minim veniam, quis nostrud.</h1>
        <p class="cl-ntral-dark">Ley Orgánica de protección de la vida del concebido y derechos de la mujer embarazada.</p>
        <h2>Estás viendo los datos de: <span>España</span></h2>
        <a id="allMap" class="pull-right">Ver datos de todo el territorio</a>
        <div id="map"></div>
        <dl class="moreInfo row">
            <div class="col-xs-12 col-sm-6 col-md-6">
                <dt>Número de votos emitidos:</dt>
                <dd>${law.peopleVotes.total}</dd>
            </div>
            <div class="col-xs-12 col-sm-6 col-md-6">
                <dt>Número de votos en contra:</dt>
                <dd>${law.peopleVotes.no}</dd>
            </div>
            <div class="col-xs-12 col-sm-6 col-md-6">
                <dt>Número de votos a favor:</dt>
                <dd>${law.peopleVotes.yes}</dd>
            </div>
            <div class="col-xs-12 col-sm-6 col-md-6">
                <dt>Abstenciones:</dt>
                <dd>${law.peopleVotes.abs}</dd>
            </div>
        </dl>
        <small>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam et porttitor massa. Sed non lectus non nisl ultrices pharetra at ac felis. Sed mi felis, tempus in aliquam a, dapibus at metus. Aliquam erat volutpat. Nam sagittis urna facilisis arcu congue euismod. Vivamus mollis venenatis felis, vel tincidunt velit egestas eu. Sed lobortis convallis risus, eget vehicula dolor luctus feugiat.Aenean venenatis, arcu nec viverra pharetra, mauris libero mollis lacus, nec posuere urna arcu quis diam. Nullam at aliquet eros. Donec luctus, nibh sit amet pulvinar laoreet, enim felis luctus augue.</small>
    </article><!-- /article -->
</content>

<content tag="cColumn">
    <div class="back"><a href="#" class="btn btn-blue">Volver a la ley</a></div>
    <section class="boxes vote votation" id="votation">
        <h1>Datos de la votación</h1>
        <div class="graphContainer">
            <h2>Kuorum</h2>
            <p>
                <span class="numberVotes">6423</span>
                <span>Votos</span>
                <small>(faltan <span class="pendingVotes">35</span> votos)</small>
            </p>
            <div id="placeholder">
                <canvas id="votesChart" width="255" height="255"></canvas>
            </div>
        </div>
        <ul class="activity">
            <li class="favor"><span>1232</span> A favor</li>
            <li class="contra"><span>2422</span> En contra</li>
            <li class="abstencion"><span>1001</span> Abstenciones</li>
        </ul>
        <table class="segmentation">
            <caption class="sr-only">Votos segmentados</caption>
            <thead>
            <tr>
                <th class="hombre"><span><span class="sr-only">Hombre</span></span></th>
                <th class="mujer"><span><span class="sr-only">Mujer</span></span></th>
                <th class="todos"><span><span class="sr-only">Todos</span></span></th>
            </tr>
            </thead>
            <tbody>
            <tr class="favor">
                <td class="hombre"><span>126</span> <small>A favor</small></td>
                <td class="mujer"><span>1246</span> <small>A favor</small></td>
                <td class="todos"><span>126</span> <small>A favor</small></td>
            </tr>
            <tr class="contra">
                <td class="hombre"><span>826</span> <small>En contra</span></td>
                <td class="mujer"><span>826</span> <small>En contra</span></td>
                <td class="todos"><span>26</span> <small>En contra</span></td>
            </tr>
            <tr class="abstencion">
                <td class="hombre"><span>567</span> <small>Abstenciones</span></td>
                <td class="mujer"><span>432</span> <small>Abstenciones</span></td>
                <td class="todos"><span>124</span> <small>Abstenciones</span></td>
            </tr>
            </tbody>
        </table>
        <dl class="infoVotation">
            <dt>Estado de la ley</dt>
            <dd>Abierta para votación</dd>
            <dt>Tipo de ley</dt>
            <dd>Proyecto de ley</dd>
            <dt>Propuesto por</dt>
            <dd>Grupo Popular</dd>
            <dt>Metria</dt>
            <dd>Justicia</dd>
            <dt>Tipo de tramitación</dt>
            <dd>Por determinar</dd>
            <dt>Texto de la ley</dt>
            <dd><a href="#">Descargar</a></dd>

        </dl>
    </section>
</content>
