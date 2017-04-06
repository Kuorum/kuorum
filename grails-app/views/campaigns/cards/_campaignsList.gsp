<g:each in="${debates}" var="debate">
    <g:render template="/campaigns/cards/debateList" model="[debate:debate, referred:'dashboard']" />
</g:each>
<g:each in="${posts}" var="post">
    <g:render template="/campaigns/cards/postList" model="[post:post,referred:'dashboard']"/>
</g:each>