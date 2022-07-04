<%@ page import="kuorum.core.model.UserType" %>


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
