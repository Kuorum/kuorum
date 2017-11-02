<%@ page import="kuorum.users.KuorumUser; org.bson.types.ObjectId" %>

<article itemtype="http://schema.org/Person" itemscope role="article" class="box-ppal clearfix">
    <div class="link-wrapper">
        <g:link mapping="postShow" params="${solrPost.encodeAsLinkProperties()}" class="hidden"></g:link>
        <g:set var="debateMultimedia" value="${false}"/>
        <g:if test="${solrPost.urlImage}">
            <div class="card-header-photo">
                <g:if test="${solrPost.urlImage}">
                    <g:set var="debateMultimedia" value="${true}"/>
                    <img src="${solrPost.urlImage}" alt="${solrPost.name}">
                </g:if>
                %{--<g:elseif test="${debate.videoUrl}">--}%
                    %{--<g:set var="debateMultimedia" value="${true}"/>--}%
                    %{--<image:showYoutube youtube="${debate.videoUrl}"/>--}%
                %{--</g:elseif>--}%
            </div>
        </g:if>
        <div class="card-body">
            <h1><searchUtil:highlightedField solrElement="${solrPost}" field="name"/></h1>
            <g:if test="${!debateMultimedia}">
                <div class="card-text"><searchUtil:highlightedField solrElement="${solrPost}" field="text" maxLength="${500}"/></div>
            </g:if>
        </div>
        <div class="card-footer">
            <ul>
                <li class="owner">
                    <userUtil:showUser
                            user="${solrPost}"
                            showName="true"
                            showActions="false"
                            showDeleteRecommendation="true"
                    />
                </li>
                <li>
                    <g:link mapping="postShow" params="${solrPost.encodeAsLinkProperties()}" fragment="openProposal" role="button">
                        <span class="fa fa fa-heart fa-lg"></span>
                        %{--<span class="label">${debate.numProposals}</span>--}%
                    </g:link>
                </li>
            </ul>
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