<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.editUser"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <h1>
        <g:message code="profile.editUser.salutation"/>,
        <br>
        <span class="user-name">${user.name}</span>
    </h1>
    <p><g:message code="profile.editUser.description"/></p>
    <g:render template="leftMenu" model="[user:user, activeMapping:'profileEditUser', menu:menu]"/>

</content>
<content tag="mainContent">
    <h1><g:message code="profile.editUser.title"/></h1>
    <formUtil:validateForm bean="${command}" form="config1"/>
    <g:form method="POST" mapping="profileEditUser" name="config1" role="form">
        <div class="form-group">
            <formUtil:input command="${command}" field="name" required="true"/>
        </div>

        <div class="form-group groupRadio">
            <formUtil:radioEnum command="${command}" field="gender"/>
            <div  class="pull-right">
            <span class="popover-trigger fa fa-info-circle" data-toggle="popover" rel="popover" role="button"></span>
            <!-- POPOVER KUORUM -->
            <div class="popover">
                <div class="popover-kuorum">
                    <p class="text-center"><g:message code="profile.editUser.explanationPolitician.title" encodeAs="raw"/></p>
                    <p><g:message code="profile.editUser.explanationPolitician"/></p>
                </div><!-- /popover-more-kuorum -->
            </div>
            <span>¿Eres un político electo?</span>
            </div>
        </div>
        <div class="form-group">
            <span class="span-label"><g:message code="customRegister.step1.form.birthday.label"/></span>
            <div class="row">
                <div class="col-xs-4">
                    <formUtil:input
                            command="${command}"
                            field="day"
                            labelCssClass="sr-only"
                            required="true"
                            type="number"
                            maxlength="2"
                    />
                </div>
                <div class="col-xs-4">
                    <formUtil:input
                            command="${command}"
                            field="month"
                            labelCssClass="sr-only"
                            required="true"
                            type="number"
                            maxlength="2"
                    />
                </div>
                <div class="col-xs-4">
                    <formUtil:input
                            command="${command}"
                            field="year"
                            labelCssClass="sr-only"
                            required="true"
                            type="number"
                            maxlength="4"
                    />
                </div>
            </div>
        </div>
        <div class="form-group">
            <div class="row">
                <div class="col-xs-4">
                    <formUtil:input
                            command="${command}"
                            field="postalCode"
                            required="true"
                            type="number"
                            maxlength="5"
                    />
                </div>
                <div class="col-xs-4 userData">
                    <formUtil:selectEnum command="${command}" field="workingSector"/>
                </div>
                <div class="col-xs-4 userData">
                    <formUtil:selectEnum command="${command}" field="studies"/>
                </div>
                <div class="col-xs-4 organizationData">
                    <formUtil:selectEnum command="${command}" field="enterpriseSector"/>
                </div>
            </div><!-- /.row -->
        </div><!-- /.form-group -->
        <div class="form-group">
            <p class="help-block"><g:message code="profile.editUser.explanationUserData"/> </p>
        </div>
        <div class="form-group">
            <formUtil:textArea command="${command}" field="bio"/>
        </div>
        <div class="form-group">
            <formUtil:editImage command="${command}" field="photoId" fileGroup="${kuorum.core.FileGroup.USER_AVATAR}"/>
        </div>
        <div class="form-group">
            <formUtil:editImage command="${command}" field="imageProfile" fileGroup="${kuorum.core.FileGroup.USER_PROFILE}"/>
        </div>
        <div class="form-group">
            <input type="submit" value="Guardar y continuar" class="btn btn-grey btn-lg">
            <a href="#" class="cancel" tabindex="19">Cancelar</a>
        </div>
    </g:form>
</content>
