<%@ page import="kuorum.users.KuorumUser; org.bson.types.ObjectId" %>

%{--<g:set var="user" value="${KuorumUser.get(new ObjectId(solrUser.id))}"/>--}%
<article itemtype="http://schema.org/Person" itemscope role="article" class="box-ppal clearfix">
    <div class="link-wrapper">
        <g:link mapping="userShow" params="${solrUser.encodeAsLinkProperties()}" class="hidden"></g:link>
        <div class="card-header-photo">
            <img src="${image.userImgProfile(user:solrUser)}" alt="${message(code:'kuorumUser.image.profile.alt', args:[solrUser.name])}">
        </div>

        <div class="user">
            <div class='profile-pic-div'>
                <img itemprop="image" class="user-img big" alt="${message(code:'kuorumUser.image.avatar.alt', args:[solrUser.name])}" title="${message(code:'kuorumUser.image.avatar.title', args:[solrUser.name])}" src="${image.userImgSrc(user:solrUser)}">
            </div>
        %{--<button type="button" class="btn btn-blue btn-lg follow allow">Seguir</button>--}%
            <userUtil:followButton user="${solrUser}" cssSize="btn-lg"/>
            <g:link mapping="userShow" params="${solrUser.encodeAsLinkProperties()}" class="user-name link-wrapper-clickable" itemprop="name">
                <searchUtil:highlightedField searchElement="${solrUser}" field="name"/>
            </g:link>
            <cite><userUtil:userRegionName user="${solrUser}"/></cite>
            <p class="party"><userUtil:roleName user="${solrUser}"/></p>
        </div>
        <p><searchUtil:highlightedField searchElement="${solrUser}" field="text" maxLength="165"/> </p>
        %{--<p><kuorumDate:showShortedText text="${user.bio}" numChars="140"/> </p>--}%
        <div class='card-footer'>
            <userUtil:ifIsFollower user="${solrUser}">
                <span class="fal fa-check-circle"></span>
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