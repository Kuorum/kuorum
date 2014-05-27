<div class="row options">
    <div class="col-xs-12 col-sm-6 col-md-6 editPost">
        <postUtil:ifPostIsEditable post="${post}">
            <g:link mapping="postEdit" params="${post.encodeAsLinkProperties()}">
                <span class="fa fa-edit fa-lg"></span><g:message code="post.show.editLink.${post.postType}"/>
            </g:link>
        </postUtil:ifPostIsEditable>
    </div>
    <div class="col-xs-12 col-sm-6 col-md-6 leerLey">
        <g:if test="${post.pdfPage}">
            <a target="_blank" href="${post.law.urlPdf}#page=${post.pdfPage}"><g:message code="post.show.pdfLink"/></a>
        </g:if>
    </div>
</div>
<p>${raw(post.text.replaceAll('<br>','</p><p>'))}</p>