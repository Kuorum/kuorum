<%@ page import="kuorum.users.KuorumUser; org.bson.types.ObjectId" %>

<g:set var="user" value="${KuorumUser.get(new ObjectId(solrUser.id))}"/>
<article itemtype="http://schema.org/Person" itemscope role="article" class="box-ppal clearfix">

    <div class="photo">
        <img src="${image.userImgProfile(user:user)}" alt="${user.name}">
    </div>
    <div class="user">
        <img itemprop="image" class="user-img big" alt="nombre" src="${image.userImgSrc(user:user)}">
        %{--<button type="button" class="btn btn-blue btn-lg follow allow">Seguir</button>--}%
        <userUtil:followButton user="${user}" cssSize="btn-lg" cssExtra="cssExtra"/>
        <g:link mapping="userShow" params="${user.encodeAsLinkProperties()}" class="user-name" itemprop="name">
            <searchUtil:highlightedField solrElement="${solrUser}" field="name"/>
        </g:link>
        <span class="user-type"><userUtil:roleName user="${user}"/> </span>
    </div>
    <p><searchUtil:highlightedField solrElement="${solrUser}" field="text"/> </p>
    <g:if test="${user.verified}">
        <small><g:message code="kuorumUser.verified"/><span class="fa fa-check"></span></small>
    </g:if>
</article>


%{--<article itemtype="http://schema.org/Article" itemscope role="article" class="kakareo post">--}%
    %{--<div class="link-wrapper">--}%
        %{--<g:link mapping="userShow" class="hidden" params="${solrUser.encodeAsLinkProperties()}"><g:message code="search.list.user.goTo" args="[solrUser.name]"/> </g:link>--}%
        %{--<div class="popover-box">--}%
            %{--<div class="user" itemscope itemtype="http://schema.org/Person">--}%
                %{--<a href="#" itemprop="url">--}%
                    %{--<img src="${image.solrUserImgSrc(user:solrUser)}" alt="${solrUser.name}" class="user-img" itemprop="image"><span itemprop="name"><searchUtil:highlightedField solrElement="${solrUser}" field="name"/></span>--}%
                %{--</a>--}%
                %{--<span class="user-type">--}%
                    %{--<small>${userUtil.roleName(user:user)}</small>--}%
                %{--</span>--}%
            %{--</div><!-- /user -->--}%
            %{--<userUtil:followButton user="${user}" cssSize="btn-xs"/>--}%
            %{--<userUtil:isFollower user="${user}"/>--}%
            %{--<g:render template="/kuorumUser/userActivity" model="[user:user]"/>--}%
        %{--</div>--}%
    %{--</div>--}%
%{--</article>--}%