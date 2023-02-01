<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="admin.editUser.title" args="[user.name]"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <g:render template="/editorUser/editorUserMenu" model="[user:user]"/>
</content>

<content tag="titleContent">
    <h1><g:message code="admin.editUser.title" args="[user.name]"/></h1>
    <h3><g:message code="admin.menu.user.editAccount" args="[user.name]"/></h3>
</content>

<content tag="mainContent">
    <formUtil:validateForm bean="${command}" form="userRightsForm"/>
    <g:form method="POST" mapping="editorAdminUserRights" params="${user.encodeAsLinkProperties()}" name="userRightsForm" role="form" class="submitOrangeButton">
        <input type="hidden" name="userId" value="${command.userId}"/>

        <div class="box-ppal-section">
            <fieldset aria-live="polite" class="row">
                <div class="form-group col-md-6">
                    <formUtil:checkBox command="${command}" field="active" showLabel="true"/>
                </div>
            </fieldset>
            <fieldset aria-live="polite" class="row">
                <div class="form-group col-md-6">
                    <formUtil:password command="${command}" field="password" showLabel="true"/>
                </div>
            </fieldset>
        </div>

        <div class="box-ppal-section">
            <fieldset aria-live="polite" class="form-group text-center">
                <input type="submit" value="${g.message(code:'default.save')}" class="btn btn-orange btn-lg">
            </fieldset>
        </div>

        <h1>Eliminar validacion</h1>

        <div class="box-ppal-section">
            <fieldset aria-live="polite" class="row">
                <div class="form-group text-center">
                    <g:link mapping="editorAdminUserInvalidate" params="${user.encodeAsLinkProperties()}"
                            class="btn btn-orange btn-lg">Eliminar validacion</g:link>
                </div>
            </fieldset>
        </div>
    </g:form>

    <h1>Validar usuario</h1>
    <h4 class="clearfix center" style="padding: 10px 0; border-bottom: 1px solid">
        <div class="col-xs-2"><abbr title="Browser ID"><span class="fas fa-browser"></span></abbr></div>

        <div class="col-xs-1"><abbr title="Campaign ID"><span class="fas fa-paper-plane"></span></abbr></div>

        <div class="col-xs-1"><abbr title="Validation is active"><span class="fas fa-shield-check"></span></abbr></div>

        <div class="col-xs-1"><abbr title="Token Mail is granted"><span class="fas fa-envelope"></span></abbr></div>

        <div class="col-xs-1"><abbr title="Token Mail is granted"><span class="fas fa-qrcode"></span></abbr></div>

        <div class="col-xs-2"><abbr title="Code is granted"><span class="fas fa-code"></span></abbr></div>

        <div class="col-xs-2"><abbr title="Phone is granted"><span class="fas fa-phone"></span></abbr></div>

        <div class="col-xs-2"><abbr title="Census is granted"><span class="fas fa-landmark"></span></abbr></div>
    </h4>
    <g:each in="${validations}" var="validationStatus">
        <div class="clearfix center" style="padding: 10px 0; border-bottom: 1px solid">
            <div class="col-xs-2"><g:link mapping="editorAdminUserValidate"
                                          params="${user.encodeAsLinkProperties() + [browserId: validationStatus.browserId, campaignId: validationStatus.campaignId]}">${validationStatus.browserId}</g:link></div>

            <div class="col-xs-1">${validationStatus.campaignId}</div>

            <div class="col-xs-1">
                <span class="fal ${validationStatus.granted ? 'fa-check-circle' : 'fa-times-circle'}"></span>
            </div>

            <div class="col-xs-1">
                <span class="fal ${validationStatus.tokenMailStatus.granted ? 'fa-check-circle' : 'fa-times-circle'}"></span>
                <br/>
                <kuorumDate:humanDate date="${validationStatus.tokenMailStatus.timestamp}"/>
            </div>

            <div class="col-xs-1">
                <span class="fal ${validationStatus.externalIdStatus.granted ? 'fa-check-circle' : 'fa-times-circle'}"></span>
                <br/>
                <kuorumDate:humanDate date="${validationStatus.externalIdStatus.timestamp}"/>
            </div>

            <div class="col-xs-2">
                <span class="fal ${validationStatus.codeStatus.granted ? 'fa-check-circle' : 'fa-times-circle'}"></span>
                <br/>
                <kuorumDate:humanDate date="${validationStatus.codeStatus.timestamp}"/>
            </div>

            <div class="col-xs-2">
                <span class="fal ${validationStatus.phoneStatus.granted ? 'fa-check-circle' : 'fa-times-circle'}"></span>
                <br/>
                <kuorumDate:humanDate date="${validationStatus.phoneStatus.timestamp}"/>
            </div>

            <div class="col-xs-2">
                <span class="fal ${validationStatus.censusStatus.granted ? 'fa-check-circle' : 'fa-times-circle'}"></span>
                <br/>
                <kuorumDate:humanDate date="${validationStatus.censusStatus.timestamp}"/>
            </div>
        </div>

    </g:each>

</content>
