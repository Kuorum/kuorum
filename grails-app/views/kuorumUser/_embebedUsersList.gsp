<g:each in="${users}" var="user">
    <li itemtype="http://schema.org/Person" itemscope="" class="user">
        <g:link mapping="userShow" itemprop="url" params="${user.encodeAsLinkProperties()}">
            <img itemprop="image" class="user-img" alt="${user.fullName}" src="${image.userImgSrc(user:user)}"><span itemprop="name">${user.fullName}</span>
        </g:link>
        %{--<span class="user-type">--}%
            %{--<small><userUtil:roleName user="${user}"/> </small>--}%
        %{--</span>--}%
    </li><!-- /.user -->
</g:each>
%{--<li class="link"><a href="#">Ver todos</a></li>--}%