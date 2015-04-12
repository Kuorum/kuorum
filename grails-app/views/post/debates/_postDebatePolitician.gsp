<li class="${debate.kuorumUser.userType==kuorum.core.model.UserType.POLITICIAN?'politician':''}">
    %{--<g:render template="/post/debates/postDebateEditMenu" model="[post:post, debate:debate]"/>--}%

    <kuorumDate:humanDate date="${debate.dateCreated}"/>

    <div itemtype="http://schema.org/Person" itemscope itemprop="author" class="user author">
        <userUtil:showUser user="${debate.kuorumUser}" showRole="true"/>
    </div><!-- /autor -->
    <p>${raw(debate.text.encodeAsRemovingScriptTags().replace("\n", "</p><p>"))}</p>
</li>