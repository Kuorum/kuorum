<g:if test="${relatedPosts}">
<aside class="othersPost">
    <h1><g:message code="post.show.relatedPosts.title"/></h1>
    <br/>
    <div id="postNav" class="carousel slide">
        <a href="#postNav" data-slide="prev" class="prev">
            <span class="fa fa-caret-left fa-lg"></span>
            <span class="sr-only"><g:message code="post.show.relatedPosts.beforePost"/> </span>
        </a>
        <div class="carousel-inner">
            <g:each in="${relatedPosts}" var="relatedPost">
                <article class="item user link-wrapper" data-cluck-postId="${relatedPost.id}">
                    <g:link mapping="postShow" params="${relatedPost.encodeAsLinkProperties()}" class="hidden"><g:message code="cluck.post.show"/></g:link>
                    <p>${relatedPost.title} <g:link mapping="projectShow" params="${relatedPost.project.encodeAsLinkProperties()}">${relatedPost.project.hashtag}</g:link> </p>
                    <userUtil:showUser user="${relatedPost.owner}" withPopover="false"/>


                    <!-- FIN POPOVER PARA IMÁGENES USUARIOS -->
                    <ul class="actions-kak  pull-right">
                        <li class="read-later">
                        <g:link mapping="postToggleFavorite" class="${postUtil.cssClassIfFavorite(post:relatedPost)}" params="${relatedPost.encodeAsLinkProperties()}" rel="nofollow">
                                <span class="fa fa-bookmark fa-lg"></span>
                                <span class="sr-only"><g:message code="cluck.footer.readLater"/></span>
                        </g:link>
                        </li>
                    </ul>
                </article>
            </g:each>
        </div>
        <a href="#postNav" data-slide="next" class="next">
            <span class="fa fa-caret-right fa-lg"></span>
            <span class="sr-only"><g:message code="post.show.relatedPosts.nextPost"/></span>
        </a>
    </div>
</aside>
</g:if>