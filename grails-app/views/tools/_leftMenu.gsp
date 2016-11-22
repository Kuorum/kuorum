<h1><g:message code="tools.leftMenu.title"/></h1>
<ul>
    <li class="${activeMapping=='toolsNotifications'?'active':''}">
        <g:link mapping="toolsNotifications"><g:message code="tools.menu.profileNotifications"/></g:link>
    </li>
    <sec:ifAnyGranted roles="ROLE_POLITICIAN">
        <li>
            <g:link mapping="projectCreate"><g:message code="head.navigation.userMenu.project.new"/></g:link>
        </li>
    </sec:ifAnyGranted>
    %{--<li class="${activeMapping=='toolsFavorites'?'active':''}">--}%
        %{--<g:link mapping="toolsFavorites">--}%
            %{--<span id="post-sin-leer">--}%
                %{--<g:message code="tools.menu.profileFavorites"/>--}%
            %{--</span>--}%
            %{--<span aria-relevant="additions" aria-live="assertive" aria-labelledby="post-sin-leer" role="log" class="badge pull-right">--}%
                %{--${menu.favorites}--}%
            %{--</span>--}%
        %{--</g:link>--}%
    %{--</li>--}%
    %{--<li class="${activeMapping=='toolsMyPosts'?'active':''}">--}%
        %{--<g:link mapping="toolsMyPosts">--}%
            %{--<span id="post-para-publicar">--}%
                %{--<g:message code="tools.menu.profileMyPosts"/>--}%
            %{--</span>--}%
            %{--<span aria-relevant="additions" aria-live="assertive" aria-labelledby="post-para-publicar" role="log" class="badge pull-right">--}%
                %{--${menu.unpublishedPosts}--}%
            %{--</span>--}%
        %{--</g:link>--}%
    %{--</li>--}%
    %{--<sec:ifNotGranted roles="ROLE_POLITICIAN">--}%
        %{--<li class="${activeMapping=='toolsKuorumStore'?'active':''}">--}%
            %{--<g:link mapping="toolsKuorumStore"><g:message code="tools.menu.profileKuorumStore"/></g:link>--}%
        %{--</li>--}%
    %{--</sec:ifNotGranted>--}%
    %{--<li class="${activeMapping=='toolsKuorumStore'?'active':''}">--}%
        %{--<g:link mapping="toolsKuorumStore">--}%
            %{--<span id="mensajes">--}%
                %{--<g:message code="profile.menu.profileMessages"/>--}%
            %{--</span>--}%
            %{--<span aria-relevant="additions" aria-live="assertive" aria-labelledby="mensajes" role="log" class="badge pull-right">--}%
                %{--${menu.unreadMessages}--}%
            %{--</span>--}%
        %{--</g:link>--}%
    %{--</li>--}%
</ul>