<%@ page import="org.bson.types.ObjectId; kuorum.users.KuorumUser" %>
<article itemtype="http://schema.org/Article" itemscope role="article" class="kakareo">
    <postUtil:postFromSolr solrPost="${solrPost}" var="post"/>
    <div class="link-wrapper">
        <g:link mapping="postShow" class="hidden" params="${solrPost.encodeAsLinkProperties()}">Ir al post</g:link>
        <h1>
            <searchUtil:highlightedField solrElement="${solrPost}" field="name"/>
            <searchUtil:highlightedField solrElement="${solrPost}" field="hashtagProject"/>
        </h1>
        <div class="main-kakareo row">
            <div class="col-md-5 user author" itemprop="author" itemscope itemtype="http://schema.org/Person">
                <userUtil:showUser user="${solrPost}" showRole="true"/>
            </div>
            <div class="col-md-7 text-right sponsor">
                <userUtil:showDebateUsers post="${post}" visibleUsers="1"/>
            </div>
        </div>
        <g:render template="/cluck/cluckMultimedia" model="[post:post]"/>
        <g:render template="/cluck/footerCluck" model="[post:post, displayingColumnC:false]"/>
        <p><searchUtil:highlightedField solrElement="${solrPost}" field="text"/></p>
    </div>
</article>