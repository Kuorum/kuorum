<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="admin.createProject.title"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <h1>
        <g:message code="admin.createProject.title"/>,
    </h1>

</content>

<content tag="mainContent">
    <h1><g:message code="admin.createProject.title"/></h1>
    <formUtil:validateForm bean="${command}" form="createProject"/>
    <g:form method="POST" mapping="projectCreate" name="createProject" role="form">
        <h1><g:message code='admin.createProject.region.label'/><span> ${command.region}</span> <span class="hashtag pull-right">#</span></h1>
        <g:render template="/project/formProject" model="[command:command, regions:regions, institutions:institutions]"/>
        <fieldset class="btns text-right">
            <div class="form-group">
                %{--<a href="#" class="cancel">${message(code:'admin.createProject.saveDraft')}</a>--}%
                <g:actionSubmit class="cancel" value="${message(code:'admin.createProject.saveDraft')}" action="save" />
                <input type="submit" class="btn btn-lg" value="${message(code:'admin.createProject.publish')}">
            </div>
        </fieldset>

    </g:form>
</content>

%{--<!-- ********************************************************************************************************* -->--}%
%{--<!-- ********** ASIDE: COLUMNA LATERAL CON INFORMACIÓN RELACIONADA CON LA PRINCIPAL ************************** -->--}%

%{--<aside id="aside-ppal" class="col-xs-12 col-sm-4 col-md-4" role="complementary">--}%
    %{--<h1 class="withArrow">¿Qué pasará con mi proyecto?</h1>--}%
    %{--<span class="arrow1"></span>--}%
    %{--<section class="boxes info">--}%
        %{--<!-- ATENCIÓN: este article no lleva .link-wrapper ni el <a> hidden -->--}%
        %{--<article role="article" class="kakareo post ley">--}%

            %{--<div class="user author">--}%
                %{--<span>--}%
                    %{--<img src="images/user.jpg" alt="Tu fotografía" class="user-img"><span>Tu nombre</span>--}%
                %{--</span>--}%
                %{--<span>--}%
                    %{--<time>y la fecha de publicación</time>--}%
                %{--</span>--}%
            %{--</div>--}%

            %{--<div class="video">--}%
                %{--<span class="front">--}%
                    %{--<span class="fa fa-play-circle fa-4x"></span>--}%
                    %{--<img src="images/img-post.jpg" alt="Imagen ilustrativa del vídeo">--}%
                %{--</span>--}%
            %{--</div>--}%

            %{--<div class="more-info">--}%
                %{--<div class="row">--}%
                    %{--<div class="col-xs-8 laley">#hashtag</div>--}%
                    %{--<div class="col-xs-4 infoVotes text-right">--}%
                        %{--<span class="vote-yes">--}%
                            %{--<span>53%</span>--}%
                            %{--<span class="sr-only">Votos a favor</span>--}%
                            %{--<span class="icon-smiley fa-lg"></span>--}%
                        %{--</span>--}%
                    %{--</div>--}%
                %{--</div>--}%
            %{--</div>--}%

            %{--<p>Los usuarios de tu circunscripción a los que les interese este tema recibirán una notificación para opinar sobre tu proyecto.</p>--}%

            %{--<footer>--}%
                %{--<div class="row">--}%
                    %{--<ul class="col-xs-5 col-sm-5 col-md-6 info-kak">--}%
                        %{--<li>--}%
                            %{--<span class="fa icon2-estado fa-lg"></span>--}%
                            %{--<span class="sr-only">Ámbito estatal</span>--}%
                        %{--</li>--}%
                        %{--<li class="hidden-xs hidden-sm">--}%
                            %{--<time>85 días restantes</time>--}%
                        %{--</li>--}%
                    %{--</ul>--}%
                    %{--<div class="col-xs-7 col-sm-7 col-md-6 voting">--}%
                        %{--<ul>--}%
                            %{--<li>--}%
                                %{--<a role="button" href="#"><span class="icon-smiley fa-lg"></span> <span class="sr-only">Vota a favor</span></a>--}%
                            %{--</li>--}%
                            %{--<li>--}%
                                %{--<a role="button" href="#"><span class="icon-sad fa-lg"></span> <span class="sr-only">Vota en contra</span></a>--}%
                            %{--</li>--}%
                            %{--<li>--}%
                                %{--<a role="button" href="#"><span class="icon-neutral fa-lg"></span> <span class="sr-only">Vota abstención</span></a>--}%
                            %{--</li>--}%
                            %{--<li>--}%
                                %{--<a role="button" href="#" class="design active"><span class="fa fa-lightbulb-o fa-lg"></span> <span class="sr-only">Propón</span></a>--}%
                            %{--</li>--}%
                        %{--</ul>--}%
                    %{--</div>--}%
                %{--</div>--}%
            %{--</footer>--}%
        %{--</article>--}%
        %{--<span class="arrow2"></span>--}%
    %{--</section>--}%
    %{--<h2 class="withArrow">Ellos podrán votar el proyecto y hacerte propuestas para mejorarlo.<span class="arrow3"></span></h2>--}%

    %{--<section class="boxes info">--}%
        %{--<!-- ATENCIÓN: este article no lleva .link-wrapper ni el <a> hidden -->--}%
        %{--<article role="article" class="kakareo">--}%
            %{--<h1>Podrás ver qué propuestas tienen más apoyo de la comunidad, debatirlas y apadrinarlas.</h1>--}%
            %{--<div class="main-kakareo row">--}%
                %{--<div class="col-xs-6 col-sm-12 col-md-6 user author">--}%
                    %{--<span>--}%
                        %{--<img src="images/user.jpg" class="user-img" alt="Tu fotografía"><span>Ciudadano X</span>--}%
                    %{--</span>--}%
                %{--</div><!-- /autor -->--}%
                %{--<div class="col-xs-6 col-sm-12 col-md-6 text-right user victory">--}%
                    %{--Apadrinada por--}%
                    %{--<span>--}%
                        %{--<img class="user-img" alt="Fotografía del usuario que apadrina" src="images/user.jpg">--}%
                    %{--</span>--}%
                %{--</div><!-- /patrocinadores -->--}%
            %{--</div>--}%
            %{--<footer class="row">--}%
                %{--<ul class="col-xs-2 col-sm-2 col-md-4 info-kak">--}%
                    %{--<li>--}%
                        %{--<span class="fa icon2-region fa-lg"></span>--}%
                        %{--<span class="sr-only">Ámbito regional</span>--}%
                    %{--</li>--}%
                %{--</ul>--}%
                %{--<ul class="col-xs-10 col-sm-10 col-md-8 actions-kak">--}%
                    %{--<li class="read-later">--}%
                        %{--<a class="enabled" href="#">--}%
                            %{--<span class="fa fa-bookmark fa-lg"></span>--}%
                            %{--<span class="sr-only">Leer después</span>--}%
                        %{--</a>--}%
                    %{--</li>--}%
                    %{--<li class="like-number">--}%
                        %{--<span class="counter">32</span>--}%
                        %{--<a href="#">--}%
                            %{--<span class="fa fa-rocket fa-lg"></span>--}%
                            %{--<span class="sr-only">Impulsar</span>--}%
                        %{--</a>--}%
                    %{--</li>--}%
                    %{--<li class="kakareo-number">--}%
                        %{--<span class="counter">542</span>--}%
                        %{--<a href="#">--}%
                            %{--<span class="fa icon-megaphone fa-lg"></span>--}%
                            %{--<span class="sr-only">Kakarear</span>--}%
                        %{--</a>--}%
                    %{--</li>--}%
                    %{--<li class="more-actions">--}%
                        %{--<span class="more-actions">--}%
                            %{--... <span class="sr-only">Más acciones</span>--}%
                        %{--</span>--}%
                    %{--</li>--}%
                %{--</ul>--}%
            %{--</footer>--}%
        %{--</article>--}%
        %{--<span class="arrow4"></span>--}%
    %{--</section>--}%

    %{--<h3 class="withArrow">Cuando apadrinas una propuesta te estás comprometiendo a defenderla donde corresponda.<span class="arrow5"></span></h3>--}%

    %{--<section class="boxes info">--}%
        %{--<!-- ATENCIÓN: este article no lleva .link-wrapper ni el <a> hidden -->--}%
        %{--<article role="article" class="kakareo">--}%
            %{--<h1>Si el autor de la propuesta considera que has cumplido tu compromiso, obtendrás victoria.</h1>--}%
            %{--<div class="main-kakareo row">--}%
                %{--<div class="col-xs-6 col-sm-12 col-md-6 user author">--}%
                    %{--<span>--}%
                        %{--<img src="images/user.jpg" class="user-img" alt="Tu fotografía"><span>Ciudadano X</span>--}%
                    %{--</span>--}%
                %{--</div><!-- /autor -->--}%
                %{--<div class="col-xs-6 col-sm-12 col-md-6 text-right user victory">--}%
                    %{--Victoria de--}%
                    %{--<span>--}%
                        %{--<img class="user-img" alt="Fotografía del usuario que apadrina" src="images/user.jpg">--}%
                    %{--</span>--}%
                %{--</div><!-- /patrocinadores -->--}%
            %{--</div>--}%
            %{--<footer class="row">--}%
                %{--<ul class="col-xs-2 col-sm-2 col-md-4 info-kak">--}%
                    %{--<li>--}%
                        %{--<span class="fa icon2-region fa-lg"></span>--}%
                        %{--<span class="sr-only">Ámbito regional</span>--}%
                    %{--</li>--}%
                %{--</ul>--}%
                %{--<ul class="col-xs-10 col-sm-10 col-md-8 actions-kak">--}%
                    %{--<li class="read-later">--}%
                        %{--<a class="enabled" href="#">--}%
                            %{--<span class="fa fa-bookmark fa-lg"></span>--}%
                            %{--<span class="sr-only">Leer después</span>--}%
                        %{--</a>--}%
                    %{--</li>--}%
                    %{--<li class="like-number">--}%
                        %{--<span class="counter">32</span>--}%
                        %{--<a href="#">--}%
                            %{--<span class="fa fa-rocket fa-lg"></span>--}%
                            %{--<span class="sr-only">Impulsar</span>--}%
                        %{--</a>--}%
                    %{--</li>--}%
                    %{--<li class="kakareo-number">--}%
                        %{--<span class="counter">542</span>--}%
                        %{--<a href="#">--}%
                            %{--<span class="fa icon-megaphone fa-lg"></span>--}%
                            %{--<span class="sr-only">Kakarear</span>--}%
                        %{--</a>--}%
                    %{--</li>--}%
                    %{--<li class="more-actions">--}%
                        %{--<span class="more-actions">--}%
                            %{--... <span class="sr-only">Más acciones</span>--}%
                        %{--</span>--}%
                    %{--</li>--}%
                %{--</ul>--}%
            %{--</footer>--}%
        %{--</article>--}%
    %{--</section>--}%

%{--</aside>--}%
%{--<!-- ********************************************************************************************************* -->--}%
