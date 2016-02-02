<%@ page import="kuorum.core.model.UserType" %>
<div class="box-ppal-section">
    <h4 class="box-ppal-section-title"><g:message code="profile.editUser.profileInfo"/></h4>
    <fieldset class="form-group">
        <formUtil:textArea command="${command}" field="bio" showLabel="true"/>
    </fieldset>

    <g:if test="${user.userType == UserType.POLITICIAN}">
        <g:render template="/profile/formEditUserPolitician" model="[user:user, command:command]"/>
    </g:if>
    <g:else>
        <g:render template="/profile/formEditUserCitizen" model="[user:user, command:command]"/>
    </g:else>
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