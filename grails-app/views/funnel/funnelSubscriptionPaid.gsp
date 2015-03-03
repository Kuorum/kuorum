<%@ page import="kuorum.core.model.UserType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="funnel.subscriptionPaid.title"/></title>
    <meta name="layout" content="funnelLayout">
</head>

<content tag="mainContent">
    <section id="main" role="main" class="sign purchase clearfix">
        <div class="col-sm-10 col-sm-offset-1 col-md-6 col-md-offset-1 col-lg-6 col-lg-offset-1">
            <h1><g:message code="funnel.subscriptionPaid.description1"/></h1>
            <h2><g:message code="funnel.subscriptionPaid.description2"/></h2>
        </div>
        <div class="col-sm-12 col-md-4">
            <form action="#" method="post" name="sign" id="sign" class="login no-phone" role="form">
                <div class="form-group">
                    <label for="pais" class="sr-only">País</label>
                    <select name="pais" class="form-control input-lg" id="pais">
                        <option value="Elige tu país">País</option>
                        <option value="Alemania">Alemania</option>
                        <option value="Austria">Austria</option>
                        <option>...</option>
                    </select>
                </div>
                <div class="form-group pull-left">
                    <label for="phone-prefix" class="sr-only">Prefijo teléfono</label>
                    <select name="phone-prefix" class="form-control input-lg" id="phone-prefix">
                        <option value="+34">+34</option>
                        <option value="+32">+32</option>
                        <option value="+33">+33</option>
                        <option>...</option>
                    </select>
                </div>
                <div class="form-group pull-left">
                    <label for="phone" class="sr-only">Número de teléfono</label>
                    <input type="number" name="phone" class="form-control input-lg" id="phone" required placeholder="Número de teléfono" aria-required="true">
                </div>
                <div class="form-group">
                    <p class="cancel"><a href="#">Puedo esperar</a></p> <input type="submit" class="btn btn-lg" value="Enviar">
                </div>
            </form>
        </div>
    </section>
</content>