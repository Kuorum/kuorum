<h1><g:message code="page.title.profile.editUser"/></h1>
<ul>
    <li class="${activeMapping=='profileEditUser'?'active':''}">
        <g:link mapping="profileEditUser"><g:message code="profile.menu.profileEditUser"/></g:link>
    </li>
    <li class="${activeMapping=='profilePictures'?'active':''}">
        <g:link mapping="profilePictures"><g:message code="profile.menu.profilePictures"/></g:link>
    </li>
    <li class="${activeMapping=='profileCauses'?'active':''}">
        <g:link mapping="profileCauses"><g:message code="profile.menu.profileCauses"/></g:link>
    </li>
    <li class="${activeMapping=='profileNews'?'active':''}">
        <g:link mapping="profileNews"><g:message code="profile.menu.profileNews"/></g:link>
    </li>
    <li class="${activeMapping=='profileSocialNetworks'?'active':''}">
        <g:link mapping="profileSocialNetworks"><g:message code="profile.menu.profileSocialNetworks"/></g:link>
    </li>
    %{--<li class="${activeMapping=='profileProfessionalDetails'?'active':''}">--}%
        %{--<g:link mapping="profileProfessionalDetails"><g:message code="profile.menu.profileProfessionalDetails"/></g:link>--}%
    %{--</li>--}%
    %{--<li class="${activeMapping=='profileQuickNotes'?'active':''}">--}%
        %{--<g:link mapping="profileQuickNotes"><g:message code="profile.menu.profileQuickNotes"/></g:link>--}%
    %{--</li>--}%
</ul>