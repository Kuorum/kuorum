<%@ page import="kuorum.core.model.UserType" %>

<div class="box-ppal-section">
    <h4 class="box-ppal-section-title"><g:message code="profile.editUser.profileImage"/></h4>

    <fieldset aria-live="polite" class="form-group image perfil" data-multimedia-switch="on"
              data-multimedia-type="IMAGE">
        <label><g:message code="kuorum.web.commands.profile.EditUserProfileCommand.photoId.label"/></label>
        <formUtil:editImage command="${command}" field="photoId" fileGroup="${kuorum.core.FileGroup.USER_AVATAR}"/>
    </fieldset>

    <fieldset aria-live="polite" class="form-group image fondoperfil" data-multimedia-switch="on"
              data-multimedia-type="IMAGE">
        <label><g:message code="kuorum.web.commands.profile.EditUserProfileCommand.imageProfile.label"/></label>
        <formUtil:editImage command="${command}" field="imageProfile" fileGroup="${kuorum.core.FileGroup.USER_PROFILE}"/>
    </fieldset>

</div>

<div class="box-ppal-section">
    <fieldset aria-live="polite" class="form-group text-center">
        <input type="submit" value="${g.message(code:'default.save')}" class="btn btn-orange btn-lg">
    </fieldset>

</div>