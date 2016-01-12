<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.editUser"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <g:render template="leftMenu" model="[user:user, activeMapping:'profileEditUser', menu:menu]"/>
</content>
<content tag="titleContent">
    <h1><g:message code="profile.menu.editUser"/></h1>
    <h3><g:message code="profile.menu.editUser.subtitle"/></h3>
</content>
<content tag="mainContent">
    <formUtil:validateForm bean="${command}" form="config1" />
    <g:form method="POST" mapping="profileEditUser" name="config1" role="form">
        %{--<h1><g:message code="profile.editUser.title"/></h1>--}%

        <div class="box-ppal-section">
            <h4 class="box-ppal-section-title"><g:message code="profile.editUser.profileInfo"/></h4>
            <fieldset class="row">
                <div class="form-group col-md-6 postal">
                    <g:if test="${user.personalData?.province}">
                        <span class="span-label"><g:message code="kuorum.web.commands.profile.EditUserProfileCommand.homeRegion.label"/> </span>
                        <span class="disabled">
                            ${user.personalData.province.name}
                            <span class="info-disabled">
                                <span role="button" rel="popover" data-toggle="popover" class="popover-trigger fa fa-info-circle"></span>
                                <div class="popover">
                                    <div class="popover-kuorum">
                                        <p><g:message code="kuorum.web.commands.profile.EditUserProfileCommand.postalCode.notChangeable"/> </p>
                                    </div>
                                </div>
                            </span>
                        </span>
                        <formUtil:regionInput
                                command="${command}"
                                field="homeRegion"
                                required="false"
                                showLabel="false"
                                extraCss="hide"
                        />
                    </g:if>
                    <g:else>
                        <formUtil:regionInput
                                command="${command}"
                                field="homeRegion"
                                required="true"
                                showLabel="true"
                        />
                    </g:else>
                </div>
            </fieldset>


            <fieldset class="row">
                <div class="form-group col-md-6">
                    <formUtil:date command="${command}" field="birthday" showLabel="true"/>
                </div>
                <div class="form-group col-md-6">
                    <formUtil:radioEnum command="${command}" field="gender"/>
                </div>
            </fieldset>

            <fieldset class="form-group">
                <formUtil:textArea command="${command}" field="bio" showLabel="true"/>
            </fieldset>

            <fieldset class="row userData">
                <div class="form-group col-md-6">
                    <formUtil:selectEnum command="${command}" field="workingSector"/>
                </div>
                <div class="form-group col-md-6">
                    <formUtil:selectEnum command="${command}" field="studies"/>
                </div>
            </fieldset>

            <fieldset class="row organizationData">
                <div class="form-group col-md-6">
                    <formUtil:selectEnum command="${command}" field="enterpriseSector"/>
                </div>
            </fieldset>
        </div>
        <div class="box-ppal-section">
            <h4 class="box-ppal-section-title"><g:message code="profile.editUser.profileImage"/></h4>

            <fieldset class="form-group image perfil" data-multimedia-switch="on" data-multimedia-type="IMAGE">
                <formUtil:editImage command="${command}" field="photoId" fileGroup="${kuorum.core.FileGroup.USER_AVATAR}"/>
            </fieldset>

            <fieldset class="form-group image fondoperfil" data-multimedia-switch="on" data-multimedia-type="IMAGE">
                <formUtil:editImage command="${command}" field="imageProfile" fileGroup="${kuorum.core.FileGroup.USER_PROFILE}"/>
            </fieldset>

        </div>
        <div class="box-ppal-section">
            <fieldset class="form-group text-center">
                <input type="submit" value="${g.message(code:'profile.emailNotifications.save')}" class="btn btn-orange btn-lg">
            </fieldset>

        </div>
    </g:form>
</content>
