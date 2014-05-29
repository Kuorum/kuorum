<%@ page import="kuorum.core.model.PostType" %>
<aside class="participate">
    <h1><g:message code="law.createPost.title"/> </h1>
    <p><g:message code="law.createPost.description"/> </p>
    <div class="row">
        <div class="col-xs-12 col-sm-4 col-md-4">
            <g:link mapping="postCreate" params="${law.encodeAsLinkProperties()+[postType:PostType.HISTORY]}">
                <span class="fa fa-comment fa-2x"></span><br>
                <g:message code="${kuorum.core.model.PostType.class.name}.${kuorum.core.model.PostType.HISTORY}"/>
            </g:link>
            <p><g:message code="law.createPost.${kuorum.core.model.PostType.HISTORY}.description"/> </P>
        </div>
        <div class="col-xs-12 col-sm-4 col-md-4">
            <g:link mapping="postCreate" params="${law.encodeAsLinkProperties()+[postType:PostType.QUESTION]}">
                <span class="fa fa-question-circle fa-2x"></span><br>
                <g:message code="${kuorum.core.model.PostType.class.name}.${kuorum.core.model.PostType.QUESTION}"/>
            </g:link>
            <p><g:message code="law.createPost.${kuorum.core.model.PostType.QUESTION}.description"/> </P>
        </div>
        <div class="col-xs-12 col-sm-4 col-md-4">
            <g:link mapping="postCreate" params="${law.encodeAsLinkProperties()+[postType:PostType.PURPOSE]}">
                <span class="fa fa-lightbulb-o fa-2x"></span><br>
                <g:message code="${kuorum.core.model.PostType.class.name}.${kuorum.core.model.PostType.PURPOSE}"/>
            </g:link>
            <p><g:message code="law.createPost.${kuorum.core.model.PostType.PURPOSE}.description"/> </P>
        </div>
    </div>
</aside>