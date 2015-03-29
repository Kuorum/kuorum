<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.editUser"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <g:render template="leftMenu" model="[user:user, activeMapping:'profileEditUser', menu:menu]"/>

</content>
<content tag="mainContent">
    <formUtil:validateForm bean="${command}" form="config1" />
    <g:form method="POST" mapping="profileEditUser" name="config1" role="form" class="box-ppal">
        <h1><g:message code="profile.editUser.title"/></h1>

        <fieldset class="row">
            <div class="form-group col-md-6">
                <formUtil:input command="${command}" field="name" required="true" showLabel="true"/>
            </div>
        </fieldset>

        %{--Alias--}%
        %{--<fieldset class="row">--}%
            %{--<g:if test="${user.alias}">--}%
                %{--<div class="form-group col-md-6">--}%
                    %{--<span class="span-label"><g:message code="kuorum.web.commands.profile.EditUserProfileCommand.alias.label"/></span>--}%
                    %{--<!-- <input type="text" class="form-control input-lg" id="alias" placeholder="Establece un alias" aria-describedby="ayuda-alias" aria-required="true" required> -->--}%
                    %{--<span class="disabled">--}%
                        %{--kuorum.org/${user.alias}--}%
                        %{--<span class="info-disabled">--}%
                            %{--<span role="button" rel="popover" data-toggle="popover" class="popover-trigger fa fa-info-circle"></span>--}%
                            %{--<div class="popover">--}%
                                %{--<div class="popover-kuorum">--}%
                                    %{--<p><g:message code="kuorum.web.commands.profile.EditUserProfileCommand.alias.notChangeable"/> </p>--}%
                                %{--</div>--}%
                            %{--</div>--}%
                        %{--</span>--}%
                    %{--</span>--}%
                %{--</div>--}%
            %{--</g:if>--}%
            %{--<g:else>--}%
                %{--<div class="form-group col-md-6">--}%
                    %{--<formUtil:input command="${command}" field="alias" showLabel="true"/>--}%
                %{--</div>--}%
                %{--<div class="form-group col-md-6">--}%
                    %{--<p id="ayuda-alias" class="help-block"><g:message code="kuorum.web.commands.profile.EditUserProfileCommand.alias.warning"/></p>--}%
                %{--</div>--}%
            %{--</g:else>--}%
        %{--</fieldset>--}%

        <fieldset class="row">
            <div class="form-group col-md-6">
                <g:if test="${user.personalData?.country}">
                    <span class="span-label"><g:message code="kuorum.Region.label"/> </span>
                    <g:hiddenField name="country" value="${command.country.id}"/>
                    <span class="disabled">
                        <g:message code="kuorum.Region.${user.personalData?.country.iso3166_2}"/>
                        <span class="info-disabled">
                            <span role="button" rel="popover" data-toggle="popover" class="popover-trigger fa fa-info-circle"></span>
                            <div class="popover">
                                <div class="popover-kuorum">
                                    <p><g:message code="kuorum.Region.notChangeable"/></p>
                                </div>
                            </div>
                        </span>
                    </span>
                </g:if>
                <g:else>
                    <formUtil:selectNation command="${command}" field="country"/>
                </g:else>
            </div>
            <div class="form-group col-md-6 postal">
                <g:if test="${user.personalData?.postalCode}">
                    <span class="span-label"><g:message code="kuorum.web.commands.profile.EditUserProfileCommand.postalCode.label"/> </span>
                    <span class="disabled">
                        ${user.personalData.postalCode}
                        <span class="info-disabled">
                            <span role="button" rel="popover" data-toggle="popover" class="popover-trigger fa fa-info-circle"></span>
                            <div class="popover">
                                <div class="popover-kuorum">
                                    <p><g:message code="kuorum.web.commands.profile.EditUserProfileCommand.postalCode.notChangeable"/> </p>
                                </div>
                            </div>
                        </span>
                    </span>
                    <g:hiddenField name="postalCode" value="${command.postalCode}"/>
                </g:if>
                <g:else>
                    <formUtil:input
                            command="${command}"
                            field="postalCode"
                            required="true"
                            type="number"
                            maxlength="5"
                            showLabel="true"
                    />
                </g:else>
            </div>
        </fieldset>


        <fieldset class="row">
            <div class="form-group col-md-6">
                <formUtil:radioEnum command="${command}" field="gender"/>
            </div>
            <div class="form-group col-md-6 nacimiento">
                <formUtil:selectBirdthYear command="${command}" field="year"/>
            </div>
        </fieldset>

        <fieldset class="row">
            <div class="form-group col-md-6">
                <formUtil:selectEnum command="${command}" field="language"/>
            </div>
        </fieldset>

        <fieldset class="row">
            <div class="form-group col-md-6">
                <formUtil:selectEnum command="${command}" field="workingSector"/>
            </div>
        </fieldset>

        <fieldset class="row">
            <div class="form-group col-md-6">
                <formUtil:selectEnum command="${command}" field="studies"/>
            </div>
        </fieldset>

        <fieldset class="row">
            <div class="form-group col-md-6">
                <span class="span-label">Número de teléfono</span>
                <div class="form-group pull-left prefix">
                    <formUtil:telephoneWithPrefix command="${command}" field="phonePrefix"/>
                </div>
                <div class="form-group pull-left phone">
                    <formUtil:input command="${command}" field="telephone"/>
                </div>
            </div>
        </fieldset>

        <fieldset class="form-group">
            <formUtil:textArea command="${command}" field="bio"/>
        </fieldset>
        <fieldset class="form-group image perfil" data-multimedia-switch="on" data-multimedia-type="IMAGE">
            <formUtil:editImage command="${command}" field="photoId" fileGroup="${kuorum.core.FileGroup.USER_AVATAR}"/>
        </fieldset>

        <fieldset class="form-group image" data-multimedia-switch="on" data-multimedia-type="IMAGE">
            <formUtil:editImage command="${command}" field="imageProfile" fileGroup="${kuorum.core.FileGroup.USER_PROFILE}"/>
        </fieldset>


        <fieldset class="form-group interest">
            <formUtil:selectMultipleCommissions command="${command}" field="commissions"/>
        </fieldset>

        <fieldset class="form-group text-right">
            <a href="#" class="cancel" tabindex="19">Cancelar</a>
            <input type="submit" value="Guardar y continuar" class="btn btn-grey btn-lg">
        </fieldset>
    </g:form>
</content>
