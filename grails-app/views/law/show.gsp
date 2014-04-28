<%@ page import="kuorum.core.model.PostType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title>${law.shortName}</title>
    <meta name="layout" content="columnCLayout">
    <g:render template="lawMetaTags" model="[law:law]"/>
</head>


<content tag="mainContent">
    <article class="kakareo post ley" role="article" itemscope itemtype="http://schema.org/Article">
        <div class="callMobile"><a href="#vote" class="smooth"><g:message code="law.vote.mobileButton"/> </a></div>
        <g:if test="${law.image}">
            <g:render template="lawPhoto" model="[law:law]"/>
        </g:if>
        <div itemprop="keywords" class="laley">${law.hashtag}</div>
        <h1>${law.shortName}</h1>

        <div class="row options">
            <div class="col-xs-12 col-sm-6 col-md-6 editPost">
                <!-- dejar la estructura aunque a veces esté vacío  -->
            </div>
            <div class="col-xs-12 col-sm-6 col-md-6 leerLey">
                <a href="${law.urlPdf}" target="_blank">
                    <g:message code="law.showCompleteLaw"/>
                </a>
            </div>
        </div>

        <p class="cl-ntral-dark">${law.realName}</p>
        <p>
            ${raw(law.introduction.replaceAll('\n','</p><p>'))}
        </p>
        <div class="readMore">
            <a data-toggle="collapse" data-parent="#accordion" href="#collapse" class="collapsed">
                <g:message code="law.text.seeMore"/>
                <span class="fa fa-chevron-circle-down fa-lg"></span>
            </a>
        </div>
        <div id="collapse" class="panel-collapse collapse">
            <p>
                ${raw(law.description.replaceAll('\n','</p><p>'))}
            </p>
        </div>
    </article><!-- /article -->

    <aside class="participate">
        <h1><g:message code="law.createPost.title"/> </h1>
        <p><g:message code="law.createPost.description"/> </p>
        <div class="row">
            <div class="col-xs-12 col-sm-4 col-md-4">
                <g:link mapping="postCreate" params="${law.encodeAsLinkProperties()+[postType:PostType.HISTORY]}">
                    <span class="icon-notebook26 fa-2x"></span><br>
                    <g:message code="${kuorum.core.model.PostType.class.name}.${kuorum.core.model.PostType.HISTORY}"/>
                </g:link>
                <p><g:message code="law.createPost.${kuorum.core.model.PostType.HISTORY}.description"/> </P>
            </div>
            <div class="col-xs-12 col-sm-4 col-md-4">
                <g:link mapping="postCreate" params="${law.encodeAsLinkProperties()+[postType:PostType.QUESTION]}">
                    <span class="fa fa-question-circle fa-2x"></span><br>
                    <g:message code="${kuorum.core.model.PostType.class.name}.${kuorum.core.model.PostType.QUESTION}"/>
                </g:link>
                <p><g:message code="law.createPost.${kuorum.core.model.PostType.QUESTION}.description"/> </P>
            </div>
            <div class="col-xs-12 col-sm-4 col-md-4">
                <g:link mapping="postCreate" params="${law.encodeAsLinkProperties()+[postType:PostType.PURPOSE]}">
                    <span class="fa fa-lightbulb-o fa-2x"></span><br>
                    <g:message code="${kuorum.core.model.PostType.class.name}.${kuorum.core.model.PostType.PURPOSE}"/>
                </g:link>
                <p><g:message code="law.createPost.${kuorum.core.model.PostType.PURPOSE}.description"/> </P>
            </div>
        </div>
    </aside>

    <g:if test="${victories}">
        <g:render template="lawVictories" model="[law:law, victories:victories]"/>
    </g:if>
    <aside class="participAll">
        <h1><g:message code="law.clucks.title"/> </h1>
        <p><g:message code="law.clucks.description"/> </p>
        <!-- COMIENZA LISTA DE KAKAREOS -->
        <ul class="kakareo-list" role="log" aria-live="assertive" aria-relevant="additions">
            <g:each in="${clucks}" var="cluck">
                <g:render template="/cluck/cluck" model="[cluck:cluck]"/>
            </g:each>
        </ul>
    </aside>
</content>

<content tag="cColumn">
<section class="boxes vote" id="vote">
    <h1><g:message code="law.vote.title"/></h1>
    <p><g:message code="law.vote.description"/></p>
    <ul class="activity">
        <li class="favor"><span>${law.peopleVotes.yes}</span> <g:message code="law.vote.yes"/></li>
        <li class="contra"><span>${law.peopleVotes.no}</span> <g:message code="law.vote.no"/></li>
        <li class="abstencion"><span>${law.peopleVotes.abs}</span> <g:message code="law.vote.abs"/></li>
    </ul>
    <div class="kuorum">
        <g:message code="law.vote.kuorum.title" args="[30]" encodeAs="raw"/>
        <span class="popover-trigger fa fa-info-circle" data-toggle="popover" rel="popover" role="button"></span>
        <!-- POPOVER KUORUM -->
        <div class="popover">
            <a href="#" class="hidden" rel="nofollow">Mostrar lista de usuarios</a>
            <div class="popover-kuorum">

                <p class="text-center"><g:message code="law.vote.kuorum.info.title" encodeAs="raw"/></p>
                <p><g:message code="law.vote.kuorum.info.description"/></p>
            </div><!-- /popover-more-kuorum -->
        </div>
        <!-- FIN POPOVER KUORUM -->
    </div>
    <div class="voting">
        <!-- LOGADO NO VOTADO -->
        <ul>
            <li><a href="#" class="btn btn-blue yes"><span class="icon-smiley fa-2x"></span> A favor</a></li>
            <li><a href="#" class="btn btn-blue no"><span class="icon-sad fa-2x"></span> En contra</a></li>
            <li><a href="#" class="btn btn-blue neutral"><span class="icon-neutral fa-2x"></span> Abstención</a></li>
        </ul>

        <!-- LOGADO VOTADO -->
        <a href="#" class="changeOpinion">¿Has cambiado de opinión?</a>

        <!-- NO LOGADO NO VOTADO -->
        <!-- <a href="#" class="btn btn-blue btn-block vote">Vota <br> <small>Es tu momento de hablar</small></a> --> <!-- al hacer click lo deshabilito y cambio el texto -->

        <a href="#">Ficha técnica</a>
    </div>

    <g:render template="lawSocialShare" model="[law:law]"/>
</section>



<g:include controller="modules" action="activePeopleOnLaw" params="[hashtag:law.hashtag]"/>
<g:include controller="modules" action="recommendedLawPosts" params="[hashtag:law.hashtag]"/>
</content>

<content tag="footerStats">
    <g:include controller="modules" action="bottomLawStats" params="[law:law]"/>
    <a href="#main" class="smooth top">
        <span class="fa fa-caret-up fa-lg"></span>
        <g:message code="law.up"/>
    </a>
</content>
