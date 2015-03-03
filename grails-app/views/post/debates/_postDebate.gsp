<g:set var="debateUser" value="${debate.kuorumUser}"/>
%{--<g:if test="${debateUser.userType == kuorum.core.model.UserType.POLITICIAN}">--}%
    %{--<g:render template="/post/debates/postDebatePolitician" model="[debate:debate]"/>--}%
%{--</g:if>--}%
%{--<g:else>--}%
    %{--<g:render template="/post/debates/postDebateActivist" model="[debate:debate]"/>--}%
%{--</g:else>--}%
<g:render template="/post/debates/postDebatePolitician" model="[debate:debate]"/>
