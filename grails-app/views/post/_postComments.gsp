<%@ page import="kuorum.web.commands.post.CommentPostCommand" %>
<aside class="comments">
    <g:set var="filteredComments" value="${post.comments.findAll{!(it.deleted || it.moderated)}}"/>
    <g:if test="${!filteredComments}">
        <sec:ifLoggedIn>
            <h1><g:message code="post.show.comments.title.empty"/></h1>
            <p><g:message code="post.show.comments.description"/> </p>
        </sec:ifLoggedIn>
    </g:if>
    <g:elseif test="${filteredComments.size()==1}">
        <h1><g:message code="post.show.comments.title.singular"/></h1>
        <p><g:message code="post.show.comments.description"/> </p>
    </g:elseif>
    <g:else>
        <h1><g:message code="post.show.comments.title.plural" args="[filteredComments.size()]"/></h1>
        <p><g:message code="post.show.comments.description"/> </p>
    </g:else>
    <ul class="listComments" id="listComments">
    <g:each in="${filteredComments}" var="comment" status="i">
        <g:if test="${filteredComments.contains(comment)}">
            <g:set var="display" value="${i>=2?'none':'block'}"/>
            <g:render template="/post/postComment" model="[post:post, comment:comment, pos:i, display:display]"/>
        </g:if>
    </g:each>

    </ul>
    <div class="text-center ${post.comments.size()>2?'':'hidden'}" id="ver-mas"><a href="#"><g:message code="post.show.comments.seeMore"/> </a></div>
    <sec:ifLoggedIn>
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
    </sec:ifLoggedIn>
</aside>