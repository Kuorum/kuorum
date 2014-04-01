
<ul class="nav navbar-nav navbar-right">
    <li class="dropdown underline" itemscope itemtype="http://schema.org/Person">
        <a data-target="#" href="#" id="open-user-options" class="dropdown-toggle dropdown-menu-right navbar-link" data-toggle="dropdown" role="button">
            <span itemprop="name"><sec:username/></span>
            <strong>, tú puedes aportar a la ley</strong>
            <img src="${image.userImgSrc(user:user)}" alt="nombre" class="user" itemprop="image">
            <span class="fa fa-caret-down fa-lg"></span>
        </a>
        <g:render template="/layouts/headUserMenuDropDown"/>
    </li>
</ul>