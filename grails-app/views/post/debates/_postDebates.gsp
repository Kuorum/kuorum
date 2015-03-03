<h2 class="underline">Debate</h2>
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
        <postUtil:ifUserCanAddDebates post="${post}">
            <ul class="chat">
                <g:render template="/post/debates/formAddDebate" model="[post:post]"/>
            </ul>
        </postUtil:ifUserCanAddDebates>
        <postUtil:elsIfUserCanAddDebates>
            <g:set var="gogoLink" value="${g.createLink(mapping: 'footerUserGuide')}"/>
            <p class="no-debate"><g:message code="post.debate.empty" args="[gogoLink]" encodeAs="raw"/> </p>
        </postUtil:elsIfUserCanAddDebates>
    </g:else>
</aside>