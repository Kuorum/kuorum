<ul class="col-xs-2 col-sm-2 col-md-4 info-kak">
    <li itemprop="keywords">
        <span class="fa ${postUtil.cssIconPostType(post:post)} fa-lg" data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'cluck.footer.'+post.postType)}" rel="tooltip"></span><!-- icono -->
        <span class="sr-only"><g:message code="cluck.footer.${post.postType}"/></span><!-- texto que explica el icono y no es visible -->
    </li>
    <li class="hidden-xs hidden-sm" itemprop="datePublished">
        <kuorumDate:humanDate date="${post.dateCreated}"/>
    </li>
</ul>