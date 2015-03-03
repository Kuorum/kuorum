<%@ page import="kuorum.core.model.UserType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="funnel.payment.title"/></title>
    <meta name="layout" content="noScapeLayout">
</head>

<content tag="mainContent">
    <section id="main" role="main" class="contratando">
        <div class="col-md-8 col-lg-9">
            <h1><g:message code="funnel.payment.description1"/></h1>
            <h2><g:message code="funnel.payment.description2"/></h2>
            <div role="tabpanel">
                <!-- Nav tabs -->
                <ul class="nav nav-tabs" role="tablist">
                    <li role="presentation" class="active">
                        <label data-target="#tengoCuenta"><input id="no-tengo" name="cuenta" type="radio" checked><g:message code="funnel.payment.noAccount"/></label>
                    </li>
                    <li role="presentation">
                        <label data-target="#noTengoCuenta"><input id="tengo" name="cuenta" type="radio"><g:message code="funnel.payment.account"/></label>
                    </li>
                </ul>
                <!-- Tab panes -->
                <div class="tab-content">
                    <div role="tabpanel" class="tab-pane fade in active" id="noTengoCuenta">
                        <form role="form" class="form-inline" id="sign-comprar" name="sign-comprar" method="post" action="#">
                            <div class="form-group">
                                <label class="sr-only" for="nombre"><g:message code="funnel.payment.name"/></label>
                                <input type="text" aria-required="true" placeholder="Dinos tu nombre" required id="nombre" name="nombre" class="form-control input-lg">
                            </div>
                            <div class="form-group">
                                <label class="sr-only" for="email"><g:message code="funnel.payment.email"/></label>
                                <input type="email" aria-required="true" placeholder="Introduce tu email" required id="email" name="email" class="form-control input-lg">
                            </div>
                            %{--<div class="row tipo-pago">--}%
                                %{--<div class="col-sm-6">--}%
                                    %{--<a href="#" class="pago tarjeta">--}%
                                        %{--<span>Pagar con Tarjeta</span>--}%
                                    %{--</a>--}%
                                %{--</div>--}%
                                %{--<div class="col-sm-6">--}%
                                    %{--<a href="#" class="pago paypal">--}%
                                        %{--<span>Pagar con Paypal</span>--}%
                                    %{--</a>--}%
                                %{--</div>--}%
                            %{--</div>--}%
                            <div class="form-group">
                                <input type="submit" value="Domiciliar el pago" class="btn btn-lg">
                            </div>
                        </form>
                    </div>
                    <div role="tabpanel" class="tab-pane fade" id="noTengoCuenta">
                        <form role="form" class="form-inline" id="login-comprar" name="login-comprar" method="post" action="#">
                            <div class="form-group">
                                <label class="sr-only" for="email"><g:message code="funnel.payment.email"/></label>
                                <input type="email" aria-required="true" placeholder="Introduce tu email" required id="email" name="email" class="form-control input-lg">
                            </div>
                            <div class="form-group">
                                <label class="sr-only" for="pass"><g:message code="funnel.payment.password"/></label>
                                <div class="input-append input-group">
                                    <input type="password" required aria-required="true" id="pass-home" name="pass-home" class="form-control input-lg" value="" placeholder="Contraseña">
                                    <span tabindex="100" class="add-on input-group-addon">
                                        <label><input type="checkbox" name="show-pass-home" id="show-pass-home"><g:message code="login.email.form.password.show"/></label>
                                    </span>
                                </div>
                            </div>
                            <div class="form-group">
                                <input type="submit" value="Domiciliar el pago" class="btn btn-lg">
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-4 col-lg-3">
            <h3><g:message code="funnel.successfulStories.offers.${offerType.group}.name"/></h3>
            <ul>
                <li><g:message code="funnel.successfulStories.offers.${offerType.group}.1"/></li>
                <li><g:message code="funnel.successfulStories.offers.${offerType.group}.2"/></li>
                <li><g:message code="funnel.successfulStories.offers.${offerType.group}.3"/></li>
            </ul>
            <h4><funnel:formatAsElegantPrice value="${totalPrice}"/> <br/><small><g:message code="funnel.successfulStories.offers.yearly.${yearly}"/></small></h4>
        </div>
    </section>
</content>

