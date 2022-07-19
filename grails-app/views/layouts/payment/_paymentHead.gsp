<ul class="nav navbar-nav navbar-right">
    <li class="underline" id="navigation-home">
        <g:link mapping="dashboard" class="navbar-link ${nav.activeMenuCss(controller: 'dashboard')}">
            <span class="fas fa-home fa-lg"></span>
            <span class=""><g:message code="default.home.label"/></span>
        </g:link>
    </li>
    <g:set var="CAMPAIGN_ROLES_LIST"
           value="${org.kuorum.rest.model.kuorumUser.UserRoleRSDTO.values().findAll { role -> role.toString().startsWith("ROLE_CAMPAIGN") }}"/>
    <sec:ifAnyGranted roles="${CAMPAIGN_ROLES_LIST.join(",")}">
        <li class="underline" id="navigation-campaigns">
            <g:link mapping="politicianCampaigns"
                    class="navbar-link ${nav.activeMenuCss(controller: 'newsletter', action: 'index')}">
                <span class="fas fa-paper-plane fa-lg"></span>
                <span class=""><g:message code="head.logged.account.tools.massMailing"/></span>
            </g:link>
        </li>
    </sec:ifAnyGranted>
    <sec:ifAnyGranted roles="ROLE_CAMPAIGN_NEWSLETTER">
        <li class="underline" id="navigation-contacts">
            <g:link mapping="politicianContacts"
                    class="navbar-link ${nav.activeMenuCss(controller: 'contacts', action: 'index')}">
                <span class="fas fa-address-card fa-lg"></span>
                <span class=""><g:message code="tools.contact.title"/></span>
            </g:link>
        </li>
    </sec:ifAnyGranted>
    <g:render template="/layouts/payment/paymentHeadNotifications" bean="[notificationsPage: notificationsPage]"/>
    <li class="dropdown underline" itemscope itemtype="http://schema.org/Person" id="navigation-profile">
        <a data-target="#" href="#" id="open-user-options"
           class="dropdown-toggle dropdown-menu-right navbar-link user-area" data-toggle="dropdown" role="button">
            <span itemprop="name" class="sr-only">${user.name}</span>
            <span itemprop="alias" class="sr-only">${user.alias}</span>
            <img src="${image.userImgSrc()}" alt="${user.name}" class="user-img" itemprop="image">
            %{--<span class="badge pull-right" role="log" aria-labelledby="alerts" aria-live="assertive" aria-relevant="additions">${notifications.numNews?:''}</span>--}%
            %{--<span class="fas fa-caret-down fa-lg"></span>--}%
        </a>
        <g:render template="/layouts/headUserMenuDropDown" model="[user: user, numMessages: 7]"/>
    </li>



</ul>