<postUtil:ifUserCanAddDebates post="${post}">
    <div class="row">
        <div itemtype="http://schema.org/Person" itemscope="" itemprop="author" class="col-md-8 user author">
            <userUtil:showLoggedUser showRole="true"/>
        </div><!-- /autor -->
    %{--<span class="col-md-4 text-right">--}%
    %{--<time title="hace menos de 1 minuto" class="timeago" datetime="2014-04-07T13:40:50+02:00">hace 17 días</time>--}%
    %{--</span>--}%
    </div>
    <g:set var="command" value="${new kuorum.web.commands.post.CommentPostCommand()}"/>
    <formUtil:validateForm bean="${command}" form="addDebate"/>
    <g:form mapping="postAddDebate" params="${post.encodeAsLinkProperties()}" name="addDebate">
        <div class="form-group">
            <formUtil:textArea command="${command}" field="comment" cssLabel="sr-only"/>
            %{--<label for="itemDebate" class="sr-only">Debate:</label>--}%
            %{--<textarea id="itemDebate" placeholder="Debate con Fulanito de tal y Menganito de cual y expresa tu opinión" rows="5" class="form-control"></textarea>--}%
            %{--<p class="help-block">Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam.</p>--}%
        </div>
        <div class="form-group btns clearfix">
            <input type="submit" class="btn btn-lg pull-right" value="Participa en el debate">
        </div>
    </g:form>
</postUtil:ifUserCanAddDebates>