


<h1>Lo mejor de lo mejor</h1>
<ul>
    <g:each in="${recommendedPost}" var="post">
        <li><g:link mapping="postShow" params="${post.encodeAsLinkProperties()}"> ${post.title} </g:link></li>
    </g:each>
</ul>