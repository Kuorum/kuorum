<g:each in="${posts}" var="post">
    <li>
        <span class="text-notification">${post.title} <g:link mapping="lawShow"  params="${post.law.encodeAsLinkProperties()}">${post.law.hashtag}</g:link></span>
        <ul class="actions">
            <li><a href="#"><g:message code="profile.profileMyPosts.post.delete"/></a></li>
            <li><g:link mapping="postEdit" params="${post.encodeAsLinkProperties()}"><g:message code="profile.profileMyPosts.post.edit"/></g:link></li>
        </ul>
    </li>
</g:each>