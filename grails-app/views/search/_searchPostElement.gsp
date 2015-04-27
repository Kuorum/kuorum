<%@ page import="org.bson.types.ObjectId; kuorum.users.KuorumUser" %>
<article itemtype="http://schema.org/Article" itemscope role="article" class="kakareo">
    <postUtil:postFromSolr solrPost="${solrPost}" var="post"/>
    <div class="link-wrapper">
        <g:link mapping="postShow" class="hidden" params="${solrPost.encodeAsLinkProperties()}">Ir al post</g:link>
        <h1>
            <searchUtil:highlightedField solrElement="${solrPost}" field="name"/>
            <searchUtil:highlightedField solrElement="${solrPost}" field="hashtagProject"/>
        </h1>
        <g:render template="/post/postUsers" model="[post:post, solrPost:solrPost]"/>
        <g:render template="/cluck/cluckMultimedia" model="[post:post]"/>
        <g:render template="/cluck/footerCluck" model="[post:post, displayingColumnC:false]"/>
        <p><searchUtil:highlightedField solrElement="${solrPost}" field="text"/></p>
    </div>
</article>