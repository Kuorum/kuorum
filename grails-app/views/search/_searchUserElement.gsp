<%@ page import="kuorum.users.KuorumUser; org.bson.types.ObjectId" %>

<g:set var="user" value="${KuorumUser.get(new ObjectId(solrUser.id))}"/>
<article itemtype="http://schema.org/Person" itemscope role="article" class="box-ppal clearfix">
    <div class="link-wrapper">
        <div class="card-header-photo">
            <img src="${image.userImgProfile(user:user)}" alt="${user.name}">
        </div>

        <div class="user">
            <div class='profile-pic-div'>
                <img itemprop="image" class="user-img big" alt="nombre" src="${image.userImgSrc(user:user)}">
                <g:if test="${user.verified}">
                    <i class="fa fa-check"></i>
                </g:if>
            </div>
        %{--<button type="button" class="btn btn-blue btn-lg follow allow">Seguir</button>--}%
            <userUtil:followButton user="${user}" cssSize="btn-lg">
                <i class="fa fa-plus"></i>
            </userUtil:followButton>
            <g:link mapping="userShow" params="${user.encodeAsLinkProperties()}" class="user-name" itemprop="name">
                <searchUtil:highlightedField solrElement="${solrUser}" field="name"/>
            </g:link>
            <cite><userUtil:politicianPosition user="${user}"/></cite>
            <p class="party"><userUtil:roleName user="${user}"/></p>
        </div>
        %{--<p><searchUtil:highlightedField solrElement="${solrUser}" field="text" numChars="120"/> </p>--}%
        <p><kuorumDate:showShortedText text="${user.bio}" numChars="165"/> </p>
        <div class='card-footer'>
            <userUtil:ifIsFollower user="${user}">
                <span class="fa fa-check-circle-o"></span>
                <g:message code="kuorumUser.popover.follower"/>
            </userUtil:ifIsFollower>
        </div>
    </div>
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