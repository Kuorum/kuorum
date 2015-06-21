<%@ page import="kuorum.web.commands.post.CommentPostCommand" %>
<sec:ifLoggedIn>
    <h2 class="underline"><g:message code="post.show.comments.title"/> </h2>
</sec:ifLoggedIn>
<sec:ifNotLoggedIn>
    <g:if test="${post.comments}">
        <h2 class="underline"><g:message code="post.show.comments.title"/> </h2>
    </g:if>
    <g:else></g:else>
</sec:ifNotLoggedIn>
    <aside class="comments">
    <g:set var="filteredComments" value="${post.comments.findAll{!(it.deleted || it.moderated)}}"/>
    <ul class="listComments" id="listComments">
    <g:set var="displayedComments" value="${0}"/>
    <g:each in="${post.comments}" var="comment" status="i">
        <g:if test="${filteredComments.contains(comment)}">
            <g:set var="display" value="${displayedComments>=2?'none':'block'}"/>
            <g:render template="/post/postComment" model="[post:post, comment:comment, pos:i, display:display]"/>
            <g:set var="displayedComments" value="${displayedComments+1}"/>
        </g:if>
    </g:each>

    </ul>
</aside>
<div class="text-center ${filteredComments.size()>2?'':'hidden'}" id="ver-mas"><a href="#"><g:message code="post.show.comments.seeMore"/> </a></div>
<sec:ifLoggedIn>
    <aside class="comments">
        <ul class="listComments">
            <li>
                <time datetime="2014-04-07T13:40:50+02:00" class="timeago" title="hace menos de 1 minuto">Hace 16 días</time>
                <div class="user author" itemprop="author" itemscope itemtype="http://schema.org/Person">
                    <userUtil:showLoggedUser showRole="true"/>
                </div>
                <g:set var="commentCommand" value="${new CommentPostCommand()}"/>
                <formUtil:validateForm bean="${commentCommand}" form="addComment"/>
                <g:form mapping="postAddComment" params="${post.encodeAsLinkProperties()}" name="addComment" data-parent-id="listComments">
                    <div class="form-group">
                        <formUtil:textArea command="${commentCommand}" field="comment" rows="5"/>
                    </div>
                    <div class="form-group btns clearfix">
                        <input type="submit" class="btn btn-grey btn-lg pull-right" value="${message(code: 'post.show.comments.add.submit')}">
                    </div>
                </g:form>
            </li>
        </ul>
    </aside>
</sec:ifLoggedIn>
