
<ul class="nav navbar-nav navbar-right">
    <li class="underline">
        <g:link mapping="dashboard" class="navbar-link">
            <span class="fa fa-home fa-lg"></span>
            <span class=""><g:message code="default.home.label"/> </span>
        </g:link>
    </li>
    <li class="underline">
        <a href="#" class="navbar-link">
            <span class="fa fa-paper-plane fa-lg"></span>
            <span class="">Campaigns</span>
        </a>
    </li>
    <li class="underline">
        <a href="#" class="navbar-link">
            <span class="fa fa-users fa-lg"></span>
            <span class="">Contacts</span>
        </a>
    </li>
    <li class="dropdown underline" itemscope itemtype="http://schema.org/Person">
        <a data-target="#" href="#" id="open-user-options" class="dropdown-toggle dropdown-menu-right navbar-link user-area" data-toggle="dropdown" role="button">
            <span itemprop="name" class="sr-only">${user.name}</span>
            <img src="${image.userImgSrc(user:user)}" alt="${user.name}" class="user-img" itemprop="image">
            <span class="badge pull-right" role="log" aria-labelledby="alerts" aria-live="assertive" aria-relevant="additions">${notifications.numNews?:''}</span>
            <span class="fa fa-caret-down fa-lg"></span>
        </a>
        <g:render template="/layouts/headUserMenuDropDown" model="[user:user, numFavorites:user.favorites.size(), numMessages:7]"/>
    </li>
</ul>