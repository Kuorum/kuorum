<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.contacts.unsubscribe" args="[user.name]"/></title>
    <meta name="layout" content="columnCLayout">
    <parameter name="specialMainRowCssClass" value="unsubscribe-row"/>

    <meta name="robots" content="noindex">
</head>

<content tag="mainContent">
    <div class="container-fluid box-ppal unsubscribe">
        <div class="row">
            <div class="col-xs-12 profile-pic-col">
                <h4>Lista de correo de ${user.name}</h4>
                <div class="profile-pic">
                    <img alt="${message(code:'page.politicianProfile.imageAvatar.alt', args: [user.name])}"
                         class="img-circle"
                         data-src="holder.js/140x140"
                         src="${image.userImgSrc(user:user)}"
                         data-holder-rendered="true"
                         itemprop="image"/>

                    <span class="unsubscribe-text">
                        Licenciado en Pedagogía. Actualmente soy Secretario de Movilización y Dinamización del PSPV-PSOE. He tenido el honor de ser concejal en mi ciudad (Elche) entre los años 2003 y 2011. El pasado 20 de noviembre de 2011 fui elegido Diputado Nacional de la X Legislatura.
                    </span>
                </div>
            </div>
        </div>
    </div>
</content>
<content tag="preFooterSections">
    <g:render template="/kuorumUser/userShowTemplates/latestProjects" model="[politician:user, userProjects:userProjects, divCss:'unsubscribe']"/>

</content>


<content tag="cColumn">
    <section class="panel panel-default unsubscribe-panel">
        <div class="panel-heading">
            <h3 class="panel-title">Dar de baja a ${contact.name}</h3>
        </div>
        <div class="panel-body text-center">

            <form>
                <fieldset class="row">
                    %{--<div class="form-group col-md-12">--}%
                        %{--<formUtil:input command="${command}" field="name"/>--}%
                    %{--</div>--}%
                    %{--<div class="form-group col-md-12">--}%
                        %{--<formUtil:input command="${command}" field="email" type="email"/>--}%
                    %{--</div>--}%
                    <div class="form-group col-md-12">
                        <input type="submit" class="btn btn-blue inverted col-md-12" value="Dar de baja">
                    </div>
                </fieldset>
            <form>
        </div>
    </section>
</content>