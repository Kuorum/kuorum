<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.profile.showFavoritesPosts"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="config" />
</head>

<content tag="leftMenu">
    <g:render template="leftMenu" model="[user:user, activeMapping:'profileFavorites', menu:menu]"/>
</content>

<content tag="mainContent">
    <div class="box-ppal">
        <ul class="list-post pending">
        <g:each in="${favorites}" var="post">
        <li id="pendingPost_${post.id}">
            ${post.title}
            <g:link mapping="projectShow" class="project" params="${post.project.encodeAsLinkProperties()}">${post.project.hashtag}</g:link>
            <ul class="actions">
                <li>
                    <g:remoteLink
                            url="[mapping:'postToggleFavorite', params:post.encodeAsLinkProperties()]"
                            onSuccess="\$('#pendingPost_${post.id}').remove()"
                            rel="nofollow">
                        <g:message code="profile.profileFavorites.posts.alreadyRead"/>
                    </g:remoteLink>
                </li>
                <li><g:link mapping="postShow" params="${post.encodeAsLinkProperties()}"><g:message code="profile.profileMyPosts.post.show"/></g:link></li>
            </ul>
        </li>
        </g:each>
        </ul>
    </div>
</content>
