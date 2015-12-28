<h1><g:message code="profile.leftMenu.title"/></h1>
<ul>
    <li class="${activeMapping=='profileEditUser'?'active':''}">
        <g:link mapping="profileEditUser"><g:message code="profile.menu.editUser"/></g:link>
    </li>
    <li class="${activeMapping=='profileChangePass'?'active':''}">
        <g:link mapping="profileChangePass"><g:message code="profile.menu.changePass"/></g:link>
    </li>
    %{--DESCOMENTAR CUANDO FUNCIONE EL CAMBIAR EMAIL--}%
    %{--<li class="${activeMapping=='profileChangeEmail'?'active':''}">--}%
        %{--<g:link mapping="profileChangeEmail"><g:message code="profile.menu.changeMail"/></g:link>--}%
    %{--</li>--}%
    <li class="${activeMapping=='profileSocialNetworks'?'active':''}">
        <g:link mapping="profileSocialNetworks"><g:message code="profile.menu.profileSocialNetworks"/></g:link>
    </li>
    <sec:ifAnyGranted roles="ROLE_POLITICIAN">
        <li class="${activeMapping=='profilePoliticianExternalActivity'?'active':''}">
            <g:link mapping="profilePoliticianExternalActivity"><g:message code="profile.menu.profilePoliticianExternalActivity"/></g:link>
        </li>
        <li class="${activeMapping=='profilePoliticianRelevantEvents'?'active':''}">
            <g:link mapping="profilePoliticianRelevantEvents"><g:message code="profile.menu.profilePoliticianRelevantEvents"/></g:link>
        </li>
        <li class="${activeMapping=='profilePoliticianProfessionalDetails'?'active':''}">
            <g:link mapping="profilePoliticianProfessionalDetails"><g:message code="profile.menu.profilePoliticianProfessionalDetails"/></g:link>
        </li>
        <li class="${activeMapping=='profilePoliticianQuickNotes'?'active':''}">
            <g:link mapping="profilePoliticianQuickNotes"><g:message code="profile.menu.profilePoliticianQuickNotes"/></g:link>
        </li>
        <li class="${activeMapping=='profilePoliticianCauses'?'active':''}">
            <g:link mapping="profilePoliticianCauses"><g:message code="profile.menu.profilePoliticianCauses"/></g:link>
        </li>
    </sec:ifAnyGranted>
    <li class="${activeMapping=='profileEditCommissions'?'active':''}">
        <g:link mapping="profileEditCommissions"><g:message code="profile.menu.editCommissions"/></g:link>
    </li>
    <li class="${activeMapping=='profileEmailNotifications'?'active':''}">
        <g:link mapping="profileEmailNotifications"><g:message code="profile.menu.profileEmailNotifications"/></g:link>
    </li>
    %{--<li class="${activeMapping=='profileMessages'?'active':''}">--}%
        %{--<g:link mapping="profileMessages">--}%
            %{--<span id="mensajes">--}%
                %{--<g:message code="profile.menu.profileMessages"/>--}%
            %{--</span>--}%
            %{--<span aria-relevant="additions" aria-live="assertive" aria-labelledby="mensajes" role="log" class="badge pull-right">--}%
                %{--${menu.unreadMessages}--}%
            %{--</span>--}%
        %{--</g:link>--}%
    %{--</li>--}%
    <li class="${activeMapping=='profileDeleteAccount'?'active':''}">
        <g:link mapping="profileDeleteAccount"><g:message code="profile.menu.profileDeleteAccount"/></g:link>
    </li>
</ul>