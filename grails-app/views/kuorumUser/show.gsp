<%@ page import="kuorum.core.model.UserType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title>${user.name}</title>
    <meta name="layout" content="columnCLayout">
    <parameter name="specialContainerCssClass" value="userprofile" />
</head>


<content tag="mainContent">
    <sec:ifAnyGranted roles="ROLE_ADMIN">
        <div id="adminActions">
            <span class="text">
                <g:link mapping="adminEditUser" params="${user.encodeAsLinkProperties()}">Editar</g:link>
            </span>
        </div>
    </sec:ifAnyGranted>
    <article itemtype="http://schema.org/Person" itemscope role="article" class="kakareo post ley">
        <div class="photo">
            <img src="${image.userImgProfile(user:user)}" alt="${user.name}">
        </div>
        <div class="user">
            <img itemprop="image" class="user-img big" alt="${user.name}" src="${image.userImgSrc(user:user)}">
            <span class="user-name" itemprop="name">
                ${user.name}
                <g:if test="${user.verified}">
                    <small><g:message code="kuorumUser.verified"/> <span class="fa fa-check"></span></small>
                </g:if>
            </span>
            <span class="user-type"><userUtil:roleName user="${user}"/></span>
            <userUtil:ifIsFollower user="${user}">
                <span class="mark"><span class="fa fa-check-circle-o"></span> te sigue</span>
            </userUtil:ifIsFollower>
            <ul class="infoActivity">
                <li>
                    <span class="fa fa-question-circle"></span>
                    <span class="counter">${user.activity.numQuestions}</span>
                    <span class="sr-only"><g:message code="kuorumUser.popover.questions"/></span>
                </li>
                <li>
                    <span class="fa fa-comment"></span>
                    <span class="counter">${user.activity.numHistories}</span>
                    <span class="sr-only"><g:message code="kuorumUser.popover.histories"/></span>
                </li>
                <li>
                    <span class="fa fa-lightbulb-o"></span>
                    <span class="counter">${user.activity.numPurposes}</span>
                    <span class="sr-only"><g:message code="kuorumUser.popover.purposes"/></span>
                </li>
            </ul>
        </div>
        <p>${user.bio?.replaceAll('<br>','</p><p>')}</p>
        <g:render template="userSocial" model="[user:user, provinceName:provinceName]"/>
    </article>

    <g:set var="urlLoadMore" value="${createLink(mapping: 'dashboardSeeMore')}"/>
    <g:render template="/cluck/listClucks" model="[clucks:clucks, urlLoadMore:urlLoadMore, seeMore:seeMore]"/>

</content>

<content tag="cColumn">
    <g:if test="${user.userType==UserType.POLITICIAN}">
        <g:render template="politicianKarmaProfile" model="[user:user, politicianStats:politicianStats]"/>
    </g:if>
    <g:else>
        <g:render template="userKarmaProfile" model="[user:user]"/>
    </g:else>

    <section class="boxes follow">
        <h1><span class="fa fa-user"></span> <small><span class="fa fa-forward"></span></small> <g:message code="kuorumUser.show.module.following.title"/></h1>
        <div class="kakareo follow">
            <userUtil:listFollowing user="${user}"/>
        </div>
    </section>

    <section class="boxes follow">
        <h1><span class="fa fa-user"></span> <small><span class="fa fa-backward"></span></small> <g:message code="kuorumUser.show.module.followers.title"/> </h1>
        <div class="kakareo follow">
            <userUtil:listFollowers user="${user}"/>
        </div>
    </section>

    <g:if test="${activeLaws}">
        <section class="boxes laws">
            <h1><span class="fa fa-briefcase"></span><g:message code="kuorumUser.show.module.activeLaws.title"/> </h1>
            <ul>
                <g:each in="${activeLaws}" var="activity">
                    <li>
                        <g:link mapping="lawShow" params="${activity.law.encodeAsLinkProperties()}">
                            ${activity.law.hashtag}
                        </g:link>
                        <span class="counter">${activity.numTimes}</span>
                    </li>
                </g:each>
            </ul>
            <div class="text-center" id="load-more">
                <a href="#">
                    <g:message code="kuorumUser.show.module.activeLaws.seeMore"/>
                </a>
            </div>
        </section>
    </g:if>
</content>
