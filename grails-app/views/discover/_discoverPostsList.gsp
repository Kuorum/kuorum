
<g:each in="${posts}" var="post">
    <li>
        <article itemtype="http://schema.org/Article" itemscope role="article" class="kakareo post">
            <div class="link-wrapper">
                <g:link mapping="postShow" class="hidden" params="${post.encodeAsLinkProperties()}">Ir al post</g:link>
                <h1>${post.title} ${post.law.hashtag}</h1>
                <p>${post.text.encodeAsRemovingHtmlTags().substring(0,Math.min(300, post.text.size()-1))}...</p>
                <div class="user author" itemprop="author" itemscope itemtype="http://schema.org/Person">
                    <userUtil:showUser user="${post.owner}" showRole="true"/>
                </div>
            </div>
        </article>
    </li>
</g:each>