<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="kuorum.name"/> </title>
    <meta name="layout" content="editPostLayout">
    <parameter name="extraCssContainer" value="" />
</head>

<content tag="intro">
</content>

<content tag="mainContent">
    <article class="kakareo post promo" role="article" itemscope itemtype="http://schema.org/Article">
        <h1><g:message code="post.promote.step2.title"/></h1>
        <p><g:message code="post.promote.step2.description.p1"/></p>
        <p><g:message code="post.promote.step2.description.p2"/></p>
        <div class="form-promo-pay">
            <form role="form">
                %{--<div class="form-group">--}%
                    %{--<label for="titular">Titular de la tarjeta</label>--}%
                    %{--<input type="text" class="form-control input-lg" id="titular" placeholder="Nombre y Apellidos y nombre de la organización">--}%
                %{--</div>--}%
                %{--<div class="row">--}%
                    %{--<div class="form-group col-sm-6">--}%
                        %{--<label for="numero">Número de la tarjeta</label>--}%
                        %{--<input type="number" class="form-control input-lg" id="numero">--}%
                    %{--</div>--}%
                    %{--<div class="form-group col-sm-6">--}%
                        %{--<label>Fecha de caducidad</label>--}%
                        %{--<div class="row">--}%
                            %{--<div class="col-sm-6">--}%
                                %{--<label for="month" class="sr-only">Mes de caducidad</label>--}%
                                %{--<select class="form-control" id="month">--}%
                                    %{--<option>Mes</option>--}%
                                    %{--<option>01</option>--}%
                                    %{--<option>02</option>--}%
                                    %{--<option>03</option>--}%
                                    %{--<option>04</option>--}%
                                    %{--<option>05</option>--}%
                                    %{--<option>06</option>--}%
                                    %{--<option>07</option>--}%
                                    %{--<option>08</option>--}%
                                    %{--<option>09</option>--}%
                                    %{--<option>10</option>--}%
                                    %{--<option>11</option>--}%
                                    %{--<option>12</option>--}%
                                %{--</select>--}%
                            %{--</div>--}%
                            %{--<div class="col-sm-6">--}%
                                %{--<label for="year" class="sr-only">Año de caducidad</label>--}%
                                %{--<select class="form-control" id="year">--}%
                                    %{--<option>Año</option>--}%
                                    %{--<option>2000</option>--}%
                                    %{--<option>1999</option>--}%
                                    %{--<option>1998</option>--}%
                                %{--</select>--}%
                            %{--</div>--}%
                        %{--</div>--}%
                    %{--</div>--}%
                %{--</div>--}%
                %{--<div class="row">--}%
                    %{--<div class="form-group col-sm-6">--}%
                        %{--<label for="ccv">CCV o código de seguridad</label>--}%
                        %{--<input type="text" class="form-control input-lg" id="ccv">--}%
                    %{--</div>--}%
                    %{--<div class="form-group col-sm-6 link">--}%
                        %{--<small><a href="#">¿Esto qué es y dónde puedo encontrarlo?</a></small>--}%
                    %{--</div>--}%
                %{--</div>--}%
                <button type="submit" class="btn btn-blue btn-lg btn-block">Patrocinar esta propuesta <small>(Tú puedes hacer algo más)</small></button>
            </form>
            <p class="secure">
                <strong>Pago seguro</strong><br>
                <small>At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupti quos dolores et quas molestias excepturi sint occaecati cupiditate non provident, similique sunt in culpa qui officia deserunt mollitia animi, id est laborum et dolorum fuga.</small>
            </p>
        </div>
    </article><!-- /article -->
</content>

<content tag="cColumn">
    <g:render template="/modules/recommendedPosts" model="[recommendedPost:[post], title:message(code:'post.promote.columnC.postPromoted.title'),specialCssClass:'']"/>
    <section class="boxes plan">
        <h1>
            <g:message code="post.promote.step2.columnC.brief.title" args="[amount, numMails]"/>
            <span class="pull-right"><span class="fa fa-user"></span> + ${numMails}</span>
        </h1>
        <p><g:message code="post.promote.step2.columnC.brief.description" args="[amount, numMails]"/></p>
        <a href="#" class="btn btn-grey btn-lg btn-block disabled">
            <g:message code="post.promote.step2.columnC.brief.amount" args="[amount, numMails]"/>
        </a>
        <p class="text-center">
            <small>
                <g:message code="post.promote.step2.columnC.brief.footer" args="[amount, numMails]"/>
            </small>
        </p>
        %{--<a href="#" class="up-plan">--}%
            %{--Saltar a un plan superior--}%
        %{--</a>--}%
    </section>
</content>