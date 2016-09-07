<h1><g:message code="profile.leftMenu.title"/></h1>
<ul>
    <li class="${activeMapping=='profileEditUser'?'active':''}">
        <g:link mapping="profileEditUser"><g:message code="profile.menu.profileEditUser"/></g:link>
    </li>
    <sec:ifAnyGranted roles="ROLE_POLITICIAN">
        <li class="${activeMapping=='profilePoliticianCauses'?'active':''}">
            <g:link mapping="profilePoliticianCauses"><g:message code="profile.menu.profilePoliticianCauses"/></g:link>
        </li>
    </sec:ifAnyGranted>
    <sec:ifAnyGranted roles="ROLE_POLITICIAN">
        <li class="${activeMapping=='profilePoliticianRelevantEvents'?'active':''}">
            <g:link mapping="profilePoliticianRelevantEvents"><g:message code="profile.menu.profilePoliticianRelevantEvents"/></g:link>
        </li>
    </sec:ifAnyGranted>
    <li class="${activeMapping=='profileSocialNetworks'?'active':''}">
        <g:link mapping="profileSocialNetworks"><g:message code="profile.menu.profileSocialNetworks"/></g:link>
    </li>
    <sec:ifAnyGranted roles="ROLE_POLITICIAN">
        <li class="${activeMapping=='profilePoliticianProfessionalDetails'?'active':''}">
            <g:link mapping="profilePoliticianProfessionalDetails"><g:message code="profile.menu.profilePoliticianProfessionalDetails"/></g:link>
        </li>
        <li class="${activeMapping=='profilePoliticianQuickNotes'?'active':''}">
            <g:link mapping="profilePoliticianQuickNotes"><g:message code="profile.menu.profilePoliticianQuickNotes"/></g:link>
        </li>
        <li class="${activeMapping=='profilePoliticianExternalActivity'?'active':''}">
            <g:link mapping="profilePoliticianExternalActivity"><g:message code="profile.menu.profilePoliticianExternalActivity"/></g:link>
        </li>
        <li class="${activeMapping=='profilePoliticianExperience'?'active':''}">
            <g:link mapping="profilePoliticianExperience"><g:message code="profile.menu.profilePoliticianExperience"/></g:link>
        </li>
    </sec:ifAnyGranted>
</ul>