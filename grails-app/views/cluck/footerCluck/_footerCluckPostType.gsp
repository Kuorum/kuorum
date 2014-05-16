<ul class="col-xs-2 col-sm-2 col-md-4 info-kak">
    <li itemprop="keywords">
        <span class="fa ${postUtil.cssIconPostType(post:post)} fa-lg" data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'cluck.footer.'+post.postType)}" rel="tooltip"></span><!-- icono -->
        <span class="sr-only"><g:message code="cluck.footer.${post.postType}"/></span><!-- texto que explica el icono y no es visible -->
    </li>
    <g:set var="cssDatePublished" value="${displayingColumnC?'sr-only':'hidden-xs hidden-sm'}"/>
    <li class="${cssDatePublished}" itemprop="datePublished">
        <kuorumDate:humanDate date="${post.dateCreated}"/>
    </li>
</ul>