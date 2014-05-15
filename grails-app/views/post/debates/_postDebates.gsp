<g:if test="${post.debates}">
    %{--hay debates--}%
    <section class="debate">
        <h1><g:message code="post.show.debate.title"/></h1>
        <userUtil:showListUsers users="${post.debates.kuorumUser}" visibleUsers="10" messagesPrefix="post.show.debate.usersList"/>
        <ul class="chat">
            <g:each in="${post.debates}" var="debate">
                <g:render template="/post/debates/postDebate" model="[debate:debate]"/>
            </g:each>
        </ul>
        <g:render template="/post/debates/formAddDebate" model="[post:post]"/>
    </section>
</g:if>
<g:else>
    <postUtil:ifUserCanAddDebates post="${post}">
        %{--No hay debates pero el usuario puede iniciar uno--}%
        <section class="debate">
            <h1><g:message code="post.show.debate.firstPoliticianDebate.title" args="[post.owner.name]"/></h1>
            <ul class="chat">
            </ul>
            <g:render template="/post/debates/formAddDebate" model="[post:post]"/>
        </section>
    </postUtil:ifUserCanAddDebates>
</g:else>