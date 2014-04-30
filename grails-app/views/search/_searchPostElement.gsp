<%@ page import="org.bson.types.ObjectId; kuorum.users.KuorumUser" %>
<article itemtype="http://schema.org/Article" itemscope role="article" class="kakareo post">
    <div class="link-wrapper">
        <g:link mapping="postShow" class="hidden" params="${solrPost.encodeAsLinkProperties()}">Ir al post</g:link>
        <h1>${raw(solrPost.highlighting.name)} ${raw(solrPost.highlighting.hashtagLaw)}</h1>
        <p>${raw(solrPost.highlighting.text)}</p>
        <userUtil:showUser user="${solrPost}" showRole="true"/>
    </div>
</article>