<%@ page import="org.bson.types.ObjectId; kuorum.users.KuorumUser" %>
<article itemtype="http://schema.org/Article" itemscope role="article" class="kakareo post">
    <div class="link-wrapper">
        <g:link mapping="postShow" class="hidden" params="${solrPost.encodeAsLinkProperties()}">Ir al post</g:link>
        <h1><searchUtil:highlightedField solrElement="${solrPost}" field="name"/> <searchUtil:highlightedField solrElement="${solrPost}" field="hashtagProject"/></h1>
        <p><searchUtil:highlightedField solrElement="${solrPost}" field="text"/></p>
        <div class="user author" itemprop="author" itemscope itemtype="http://schema.org/Person">
            <userUtil:showUser user="${solrPost}" showRole="true"/>
        </div>
    </div>
</article>