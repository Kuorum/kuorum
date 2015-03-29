<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.showUserPosts"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <g:render template="leftMenu" model="[user:user, activeMapping:'profileMyPosts', menu:menu]"/>
</content>

<content tag="mainContent">
    <div aria-label="button group" role="group" class="btn-group btn-group-justified filters">
        <g:link mapping="toolsMyPosts" role="button" class="btn ${searchUserPosts.publishedPosts==null?'active':''}">
            <g:message code="profile.profileMyPosts.listHead.all"/>
        </g:link>
        <g:link mapping="toolsMyPosts" params="[publishedPosts:true]" role="button" class="btn ${searchUserPosts.publishedPosts==null?'active':''}">
            <g:message code="profile.profileMyPosts.listHead.published"/>
        </g:link>
        <g:link mapping="toolsMyPosts" params="[publishedPosts:false]" role="button" class="btn ${searchUserPosts.publishedPosts==null?'active':''}">
            <g:message code="profile.profileMyPosts.listHead.drafts"/>
        </g:link>
    </div>
    %{--<ul class="noti-filters">--}%
        %{--<li class="${searchUserPosts.publishedPosts==null?'active':''}"><g:link mapping="toolsMyPosts"><g:message code="profile.profileMyPosts.listHead.all"/></g:link></li>--}%
        %{--<li class="${searchUserPosts.publishedPosts==true?'active':''}"><g:link mapping="toolsMyPosts" params="[publishedPosts:true]"><g:message code="profile.profileMyPosts.listHead.published"/></g:link></li>--}%
        %{--<li class="${searchUserPosts.publishedPosts==false?'active':''}"><g:link mapping="toolsMyPosts" params="[publishedPosts:false]"><g:message code="profile.profileMyPosts.listHead.drafts"/></g:link></li>--}%
        %{--<li class="dropdown pull-right"></li>--}%
    %{--</ul>--}%

    <div class="box-ppal">
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
    </div>
</content>
