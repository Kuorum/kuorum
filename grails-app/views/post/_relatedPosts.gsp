<g:if test="${relatedPosts}">
    <section id="otras-propuestas" class="row main">
        <div class="container-fluid">
            <h2 class="underline"><g:message code="post.show.relatedPosts.title"/></h2>
            <div class="row">
                <ul class="kakareo-list" aria-relevant="additions" aria-live="assertive" role="log">
                    <g:each in="${relatedPosts}" var="post">
                        <li itemscope itemtype="http://schema.org/Article" class="col-md-4">
                            <g:render template="/cluck/cluck" model="[post:post, displayingColumnC:true, displayingHorizontalModule:true]"/>
                        </li>
                    </g:each>
                </ul>
            </div>
        </div>
    </section>
</g:if>