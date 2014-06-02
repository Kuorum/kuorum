<ul>
    <li class="${activeMapping=='profileEditUser'?'active':''}">
        <g:link mapping="profileEditUser"><g:message code="profile.menu.editUser"/></g:link>
    </li>
    <li class="${activeMapping=='profileChangePass'?'active':''}">
        <g:link mapping="profileChangePass"><g:message code="profile.menu.changePass"/></g:link>
    </li>
    %{--<li class="${activeMapping=='profileChangeEmail'?'active':''}">--}%
        %{--<g:link mapping="profileChangeEmail"><g:message code="profile.menu.changeMail"/></g:link>--}%
    %{--</li>--}%
    <li class="${activeMapping=='profileSocialNetworks'?'active':''}">
        <g:link mapping="profileSocialNetworks"><g:message code="profile.menu.profileSocialNetworks"/></g:link>
    </li>
    <li class="${activeMapping=='profileNotifications'?'active':''}">
        <g:link mapping="profileNotifications"><g:message code="profile.menu.profileNotifications"/></g:link>
    </li>
    <li class="${activeMapping=='profileEmailNotifications'?'active':''}">
        <g:link mapping="profileEmailNotifications"><g:message code="profile.menu.profileEmailNotifications"/></g:link>
    </li>
    <li class="${activeMapping=='profileFavorites'?'active':''}">
        <g:link mapping="profileFavorites">
            <span id="post-sin-leer">
                <g:message code="profile.menu.profileFavorites"/>
            </span>
            <span aria-relevant="additions" aria-live="assertive" aria-labelledby="post-sin-leer" role="log" class="badge pull-right">
                ${menu.favorites}
            </span>
        </g:link>
    </li>
    <li class="${activeMapping=='profileMyPosts'?'active':''}">
        <g:link mapping="profileMyPosts">
            <span id="post-para-publicar">
                <g:message code="profile.menu.profileMyPosts"/>
            </span>
            <span aria-relevant="additions" aria-live="assertive" aria-labelledby="post-para-publicar" role="log" class="badge pull-right">
                ${menu.unpublishedPosts}
            </span>
        </g:link>
    </li>
    <li class="${activeMapping=='profileKuorumStore'?'active':''}">
        <g:link mapping="profileKuorumStore"><g:message code="profile.menu.profileKuorumStore"/></g:link>
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