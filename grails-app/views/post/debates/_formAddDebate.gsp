<postUtil:ifUserCanAddDebates post="${post}">
    <li>
        <time title="Ahora mismo" class="timeago" datetime="2015-02-21T13:40:50+02:00">Ahora mismo</time>
        <div itemtype="http://schema.org/Person" itemscope itemprop="author" class="user author">
            <userUtil:showLoggedUser showRole="true"/>
        </div><!-- /autor -->

        <g:set var="command" value="${new kuorum.web.commands.post.CommentPostCommand()}"/>
        <formUtil:validateForm bean="${command}" form="addDebate"/>
        <g:form mapping="postAddDebate" params="${post.encodeAsLinkProperties()}" name="addDebate">
            <div class="form-group">
                <formUtil:textArea command="${command}" field="comment" cssLabel="sr-only"/>
            </div>
            <div class="form-group btns clearfix">
                <input type="submit" class="btn btn-lg pull-right" value="Participa en el debate">
            </div>
        </g:form>
    </li>
</postUtil:ifUserCanAddDebates>