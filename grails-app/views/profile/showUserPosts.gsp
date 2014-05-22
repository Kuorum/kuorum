<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.showUserPosts"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <h1>
        <g:message code="profile.profileMyPosts.salutation"/>,
        <br>
        <span class="user-name">${user.name}</span>
    </h1>
    <p><g:message code="profile.profileMyPosts.description"/></p>
    <g:render template="leftMenu" model="[user:user, activeMapping:'profileMyPosts', menu:menu]"/>

</content>
<content tag="mainContent">
    <h1><g:message code="profile.profileMyPosts.title"/></h1>
    <ul class="noti-filters">
        <li class="${searchUserPosts.publishedPosts==null?'active':''}"><g:link mapping="profileMyPosts"><g:message code="profile.profileMyPosts.listHead.all"/></g:link></li>
        <li class="${searchUserPosts.publishedPosts==true?'active':''}"><g:link mapping="profileMyPosts" params="[publishedPosts:true]"><g:message code="profile.profileMyPosts.listHead.published"/></g:link></li>
        <li class="${searchUserPosts.publishedPosts==false?'active':''}"><g:link mapping="profileMyPosts" params="[publishedPosts:false]"><g:message code="profile.profileMyPosts.listHead.drafts"/></g:link></li>
        <li class="dropdown pull-right">
            %{--<a data-target="#" href="" class="dropdown-toggle text-center" id="open-order" data-toggle="dropdown" role="button">Ordenar <span class="fa fa-caret-down fa-lg"></span></a>--}%
            %{--<ul id="ordenar" class="dropdown-menu dropdown-menu-right" aria-labelledby="open-order" role="menu">--}%
                %{--<li><a href="#">Opción 1</a></li>--}%
                %{--<li><a href="#">Opción 2</a></li>--}%
                %{--<li><a href="#">Opción 3</a></li>--}%
            %{--</ul>--}%
        </li>
    </ul>

    <ul class="list-post" id="list-post-id">
        <g:render template="userPostsList" model="[posts:posts]"/>
    </ul>
    <nav:loadMoreLink
            numElements="${posts.size()}"
            pagination="${searchUserPosts}"
            mapping="profileMyPosts"
            parentId="list-post-id"
            formId="list-userPosts-form"
    >
        <input type="hidden" name="publishedPosts" value="${searchUserPosts.publishedPosts}"/>
    </nav:loadMoreLink>
</content>
