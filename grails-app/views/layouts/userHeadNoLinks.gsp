<div class="collapse navbar-collapse" id="navbar-collapse">
    <ul class="nav navbar-nav navbar-right">
        <li>
            <span class="navbar-link user-area">
                %{--<span class="user-name">${user.name}</span>--}%
                <img src="${image.userImgSrc(user:user)}"alt="${user.name}" class="user-img" itemprop="image">
            </span>
        </li>
        %{--<li>--}%
            %{--<span id="open-user-options" class="navbar-link">--}%
                %{--<span class="fal fa-gear fa-lg"></span>--}%
                %{--<span class="visible-xs"><g:message code="head.logged.option"/></span>--}%
            %{--</span>--}%

        %{--</li>--}%
        %{--<li>--}%
            %{--<span class="navbar-link" id="open-user-messages">--}%
                %{--<span class="fal fa-envelope fa-lg"></span>--}%
                %{--<span class="visible-xs" id="messages">Mensajes</span>--}%
            %{--</span>--}%

        %{--</li>--}%
                                                                                                                                            %{--<li>--}%
                                                                                                                                                %{--<span class="navbar-link" id="open-user-notifications">--}%
                                                                                                                                                    %{--<span class="fal fa-bell fa-lg"></span>--}%
                                                                                                                                                    %{--<span class="visible-xs" id="alerts"><g:message code="head.logged.notifications"/></span>--}%
                                                                                                                                                %{--</span>--}%

                                                                                                                                            %{--</li>--}%
    </ul>
</div>