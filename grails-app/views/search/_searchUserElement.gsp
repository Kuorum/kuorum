<%@ page import="kuorum.users.KuorumUser; org.bson.types.ObjectId" %>

<g:set var="user" value="${KuorumUser.get(new ObjectId(solrUser.id))}"/>
<article itemtype="http://schema.org/Article" itemscope role="article" class="kakareo post">
    <div class="link-wrapper">
        <g:link mapping="userShow" class="hidden" params="${solrUser.encodeAsLinkProperties()}"><g:message code="search.list.user.goTo" args="[solrUser.name]"/> </g:link>
        <div class="popover-box">
            <div class="user" itemscope itemtype="http://schema.org/Person">
                <a href="#" itemprop="url">
                    <img src="${image.solrUserImgSrc(user:solrUser)}" alt="${solrUser.name}" class="user-img" itemprop="image"><span itemprop="name">${raw(solrUser.highlighting.name)}</span>
                </a>
                <span class="user-type">
                    <small>${userUtil.roleName(user:user)}</small>
                </span>
            </div><!-- /user -->
            <userUtil:followButton user="${user}"/>
            <userUtil:isFollower user="${user}"/>
            <g:render template="/kuorumUser/userActivity" model="[user:user]"/>
        </div>
    </div>
</article>