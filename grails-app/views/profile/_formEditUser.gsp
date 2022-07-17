<%@ page import="kuorum.core.model.UserType" %>
<div class="box-ppal-section">
    <h4 class="box-ppal-section-title"><g:message code="profile.editUser.profileInfo"/></h4>
    <fieldset aria-live="polite" class="form-group">
        <formUtil:textArea command="${command}" field="bio" showLabel="true" texteditor="texteditor"/>
    </fieldset>

    %{--<g:if test="${user.userType == UserType.POLITICIAN || user.userType == kuorum.core.model.UserType.CANDIDATE}">--}%
        %{--<g:render template="/profile/formEditUserPolitician" model="[user:user, command:command]"/>--}%
    %{--</g:if>--}%
    %{--<g:else>--}%
        <g:render template="/profile/formEditUserCitizen" model="[user:user, command:command]"/>
    %{--</g:else>--}%
</div>

<div class="box-ppal-section">
    <fieldset aria-live="polite" class="form-group text-center">
        <input type="submit" value="${g.message(code:'default.save')}" class="btn btn-orange btn-lg">
    </fieldset>

</div>