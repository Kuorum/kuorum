<g:each in="${posts}" var="post">
    <li>
        <span class="text-notification">${post.title} <g:link mapping="lawShow"  params="${post.law.encodeAsLinkProperties()}">${post.law.hashtag}</g:link></span>
        <ul class="actions">
            <postUtil:ifPostIsDeletable post="${post}">
                <li><g:link mapping="postDelete" params="${post.encodeAsLinkProperties()}"><g:message code="profile.profileMyPosts.post.delete"/></g:link></li>
            </postUtil:ifPostIsDeletable>
            <postUtil:ifPostIsEditable post="${post}">
                <li><g:link mapping="postEdit" params="${post.encodeAsLinkProperties()}"><g:message code="profile.profileMyPosts.post.edit"/></g:link></li>
            </postUtil:ifPostIsEditable>
        </ul>
    </li>
</g:each>