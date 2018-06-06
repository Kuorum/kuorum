
<ul class="nav navbar-nav navbar-right">
    <li class="underline" id="navigation-home">
        <g:link mapping="dashboard" class="navbar-link ${nav.activeMenuCss(mappingName:'dashboard')}">
            <span class="fa fa-home fa-lg"></span>
            <span class=""><g:message code="default.home.label"/> </span>
        </g:link>
    </li>
    <li class="underline" id="navigation-campaigns">
        <g:link mapping="politicianCampaigns" class="navbar-link ${nav.activeMenuCss(mappingName:'politicianCampaigns')}">
            <span class="fa fa-paper-plane fa-lg"></span>
            <span class=""><g:message code="head.logged.account.tools.massMailing"/> </span>
        </g:link>
    </li>
    <li class="underline" id="navigation-contacts">
        <g:link mapping="politicianContacts" class="navbar-link ${nav.activeMenuCss(mappingName:'politicianContacts')}">
            <span class="fa fa-address-card fa-lg"></span>
            <span class=""><g:message code="tools.contact.title"/> </span>
        </g:link>
    </li>
    <g:render template="/layouts/payment/paymentHeadNotifications" bean="[notificationsPage:notificationsPage]"/>
    <li class="dropdown underline" itemscope itemtype="http://schema.org/Person" id="navigation-profile">
        <a data-target="#" href="#" id="open-user-options" class="dropdown-toggle dropdown-menu-right navbar-link user-area" data-toggle="dropdown" role="button">
            <span itemprop="name" class="sr-only">${user.name}</span>
            <span itemprop="alias" class="sr-only">${user.alias}</span>
            <img src="${image.userImgSrc(user:user)}" alt="${user.name}" class="user-img" itemprop="image">
            %{--<span class="badge pull-right" role="log" aria-labelledby="alerts" aria-live="assertive" aria-relevant="additions">${notifications.numNews?:''}</span>--}%
            %{--<span class="fa fa-caret-down fa-lg"></span>--}%
        </a>
        <g:render template="/layouts/headUserMenuDropDown" model="[user:user, numMessages:7]"/>
    </li>
</ul>