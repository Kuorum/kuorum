<%@ page import="kuorum.core.model.UserType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title>${user.name}</title>
    <meta name="layout" content="columnCLayout">
    <parameter name="specialContainerCssClass" value="userprofile" />
    <g:render template="userMetaTags" model="[user:user]"/>
</head>


<content tag="subHeader">
    <g:render template="/kuorumUser/userSubHeader" model="[user: user]"/>
</content>

<content tag="mainContent">
    <sec:ifAnyGranted roles="ROLE_ADMIN">
        <!-- FLECHITA PARA ABRIR MENÚ -->
        <span class="popover-trigger open-menu" rel="popover" role="button" data-toggle="popover">
            <span class="fa fa-chevron-down"></span>
            <span class="sr-only">Ver opciones edición</span>
        </span>
        <!-- POPOVER OPCIONES EDICIÓN -->
        <div class="popover">
            <div class="popover-more-actions edition">
                <ul>
                    <li>
                        <a href="#">
                            <span>Editar</span>
                        </a>
                    </li>
                </ul>
            </div>
        </div>
        <!-- FIN POPOVER OPCIONES EDICIÓN -->
    </sec:ifAnyGranted>
    <div class="box-ppal profile" itemprop="author" itemscope itemtype="http://schema.org/Person">
        <div class="photo">
            <img src="${image.userImgProfile(user:user)}" alt="${user.imageProfile?user.imageProfile.originalName:user.name}">
        </div>
        <ul class="activity">
            <li><span class="counter">${user.followers.size()}</span> <br><g:message code="kuorumUser.popover.followers"/></li>
            <li><span class="counter">${user.following.size()}</span> <br><g:message code="kuorumUser.popover.following"/></li>
            <li><span class="counter">${user.activity}</span> <br><g:message code="kuorumUser.popover.post"/></li>
            <g:render template="/kuorumUser/userRecordsLi" model="[user:user]"/>
        </ul>
    </div>

    <g:render template="userSocial" model="[user:user, provinceName:provinceName]"/>
    <g:if test="${UserType.POLITICIAN == user.userType}">
        <g:render template="/kuorumUser/politicianClucks" model="[user:user, userProjects: userProjects, numUserProjects:numUserProjects, defendedPosts:defendedPosts, numDefendedPosts:numDefendedPosts, userVictoryPosts:userVictoryPost, numUserVictoryPosts:numUserVictoryPost]"/>
    </g:if>
    <g:else>
        <g:render template="/kuorumUser/userClucks" model="[user:user, clucks: clucks, numClucks:numClucks, userPosts:userPosts, numUserPost:numUserPosts, userVictoryPosts:userVictoryPosts, numUserVictoryPosts:numUserVictoryPosts]"/>
    </g:else>
</content>

<content tag="cColumn">
    <g:if test="${user.userType==UserType.POLITICIAN}">
        <g:render template="politicianKarmaProfile" model="[user:user, politicianStats:politicianStats]"/>
    </g:if>
    <g:else>
        <g:render template="userKarmaProfile" model="[user:user]"/>
    </g:else>

    <section class="boxes follow">
        <h1>
            <span class="fa fa-user"></span>
            <small><span class="fa fa-forward"></span></small>
            <g:message code="kuorumUser.show.module.following.title" args="[user.following?user.following.size():0]"/>
        </h1>
        <div class="kakareo follow">
            <userUtil:listFollowing user="${user}"/>
        </div>
    </section>

    <section class="boxes follow">
        <h1>
            <span class="fa fa-user"></span>
            <small><span class="fa fa-backward"></span></small>
            <g:message code="kuorumUser.show.module.followers.title" args="[user.followers?user.followers.size():0]"/>
        </h1>
        <div class="kakareo follow">
            <userUtil:listFollowers user="${user}"/>
        </div>
    </section>

    <g:if test="${activeProjects}">
        <section class="boxes projects">
            <h1><span class="fa fa-briefcase"></span>
                <g:message code="kuorumUser.show.module.activeProjects.title"/> </h1>
            <ul>
                <g:each in="${activeProjects}" var="activity">
                    <li>
                        <g:link mapping="projectShow" params="${activity.project.encodeAsLinkProperties()}">
                            ${activity.project.hashtag}
                        </g:link>
                        <span class="counter">${activity.numTimes}</span>
                    </li>
                </g:each>
            </ul>
            %{--<div class="text-center" id="load-more">--}%
                %{--<a href="#">--}%
                    %{--<g:message code="kuorumUser.show.module.activeProjects.seeMore"/>--}%
                %{--</a>--}%
            %{--</div>--}%
        </section>
    </g:if>
</content>
