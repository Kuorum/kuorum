<h2 class="underline"><g:message code="post.debate.title"/></h2>
<aside class="debate">
    <g:if test="${post.debates}">
        <ul class="chat">
            <g:each in="${post.debates}" var="debate">
                <g:render template="/post/debates/postDebate" model="[debate:debate]"/>
            </g:each>
            <g:render template="/post/debates/formAddDebate" model="[post:post]"/>
        </ul>
    </g:if>
    <g:else>
        <sec:ifNotLoggedIn>
            <p class="no-debate"><g:message code="post.debate.empty"/></p>
        </sec:ifNotLoggedIn>
        <postUtil:ifUserCanAddDebates post="${post}">
            <ul class="chat">
                <g:render template="/post/debates/formAddDebate" model="[post:post]"/>
            </ul>
        </postUtil:ifUserCanAddDebates>
        <postUtil:elsIfUserCanAddDebates>
            <p class="no-debate"><g:message code="post.debate.empty"/></p>
        </postUtil:elsIfUserCanAddDebates>
    </g:else>
</aside>