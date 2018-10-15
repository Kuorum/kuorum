<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.profileEditAccountDetails"/> </title>
    <meta name="layout" content="leftMenuConfigLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <g:render template="leftMenuAccount" model="[user:user, activeMapping:'profileEditAccountDetails', menu:menu]"/>
    <r:require modules="forms"/>
</content>

<content tag="titleContent">
    <h1><g:message code="profile.menu.profileEditAccountDetails"/></h1>
    <h3><g:message code="profile.menu.profileEditAccountDetails.subtitle"/></h3>
</content>
<content tag="mainContent">

    <!-- ÑAPA PARA MOSTRAR EL ERROR DE VALIDACIÓN DE CONTRASEÑA FUERA DEL MODAL -->
    <g:hasErrors bean="${command}" field="password">
        <r:script>
            $(function () {
                display.error('${g.fieldError(bean: command, field: 'password')}');
            });
        </r:script>
    </g:hasErrors>

    <formUtil:validateForm bean="${command}" form="accountDetailsForm" dirtyControl="true"/>
    <g:form method="POST" mapping="profileEditAccountDetails" name="accountDetailsForm" role="form" class="submitOrangeButton" autocomplete="noFill">
        <div class="box-ppal-section">
            <g:render template="accountDetailsForm" model="[command:command]"/>
        </div>
        <div class="box-ppal-section">
            <fieldset class="form-group text-center">
                <g:if test="${requirePassword}">
                    <a href="#" class="btn btn-orange btn-lg" id="auth"><g:message code="profile.emailNotifications.save"/></a>
                </g:if>
                <g:else>
                    <button type="submit" class="btn btn-orange btn-lg" ><g:message code="profile.emailNotifications.save"/></button>
                </g:else>
                %{--<input type="submit" value="${g.message(code:'profile.emailNotifications.save')}" class="btn btn-orange btn-lg">--}%
            </fieldset>
        </div>



        <!-- MODAL AUTH PROFILE CHANGES -->
        <div class="modal fade in" id="authorizeProfileEdition" tabindex="-1" role="dialog" aria-labelledby="authorizeProfileEditionTitle" aria-hidden="true">
            <div class="modal-dialog ">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Cerrar">
                            <span aria-hidden="true" class="fal fa-times-circle fa"></span><span class="sr-only"><g:message code="modalDefend.close"/></span>
                        </button>
                        <h4>
                            <g:message code="kuorum.web.commands.profile.AccountDetailsCommand.password.modal.title"/>
                        </h4>
                    </div>
                    <div class="modal-body">

                        <fieldset class="row">
                            <div class="form-group col-md-10">
                                <input type="text" name="autocompleteNameOff" style="display:none" data-ays-ignore="true"/>
                                <formUtil:password command="${command}" field="password" required="true" showLabel="true"/>
                            </div>
                        </fieldset>

                        <p><g:message code="kuorum.web.commands.profile.AccountDetailsCommand.password.modal.body"/> </p>
                        <fieldset class="text-right">
                            <a href="#" class="btn btn-grey-light btn-lg" data-dismiss="modal" id="authorizeProfileEditionButtonClose">
                                <g:message code="kuorum.web.commands.profile.AccountDetailsCommand.password.modal.cancel"/>
                            </a>
                            <a href="#" class="btn btn-blue inverted btn-lg" id="authorizeProfileEditionButtonOk">
                                <g:message code="kuorum.web.commands.profile.AccountDetailsCommand.password.modal.confirm"/>
                            </a>
                        </fieldset>

                    </div>
                </div>
            </div>
        </div>


    </g:form>
</content>