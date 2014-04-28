<aside class="comments">
    <g:set var="filteredComments" value="${post.comments.findAll{!(it.deleted || it.moderated)}}"/>
    <g:if test="${!filteredComments}">
        <h1><g:message code="post.show.comments.title.empty"/></h1>
    </g:if>
    <g:elseif test="${filteredComments.size()==1}">
        <h1><g:message code="post.show.comments.title.singular"/></h1>
    </g:elseif>
    <g:else>
        <h1><g:message code="post.show.comments.title.plural" args="[filteredComments.size()]"/></h1>
    </g:else>
    <p><g:message code="post.show.comments.description"/> </p>
    <ul class="listComments">
    <g:each in="${post.comments}" var="comment" status="i">
        <g:if test="${filteredComments.contains(comment)}">
            <g:render template="postComment" model="[post:post, comment:comment, pos:i]"/>
        </g:if>
    </g:each>

    </ul>

    <div class="text-center" id="ver-mas"><a href="#">Ver más</a></div>

    <form id="addComment">
        <div class="form-group">
            <label for="comment">Añade tu comentario:</label>
            <textarea id="comment" placeholder="Expresa tu opinión sobre la propuesta..." rows="5" class="form-control"></textarea>
        </div>
        <div class="form-group btns clearfix">
            <input type="submit" class="btn btn-grey btn-lg pull-right" value="Publicar comentario">
        </div>
    </form>
</aside>