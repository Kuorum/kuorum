<%@ page import="kuorum.core.model.UserType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="kuorum.name"/> </title>
    <meta name="layout" content="columnCLayout">
    <parameter name="specialContainerCssClass" value="userprofile" />
</head>


<content tag="mainContent">
    <sec:ifAnyGranted roles="ROLE_EDITOR">
        <div id="adminActions">
            <span class="text">
                <g:link mapping="editorKuorumAccountEdit" params="${user.encodeAsLinkProperties()}">
                    <span class="fa fa-edit fa-lg"></span>Editar perfil</g:link>
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
            </span>
            <span class="user-type"><userUtil:roleName user="${user}"/></span>
        </div>
        <p>${user.bio?.replaceAll('<br>','</p><p>')}</p>
    </article>


    <article itemtype="http://schema.org/Article" itemscope role="article" class="noprofile">
        %{--<h1><g:message code="kuorumUser.show.politicianInactive.text.h1"/> </h1>--}%
        %{--<userUtil:followButton user="${user}" prefixMessages="kuorumUser.follow.inactivePolitician" cssSize="btn-lg"/>--}%

        <h2><g:message code="kuorumUser.show.politicianInactive.text.p1.title"/></h2>
        <p><g:message code="kuorumUser.show.politicianInactive.text.p1.text"/></p>

        <h2><g:message code="kuorumUser.show.politicianInactive.text.p2.title"/></h2>
        <p><g:message code="kuorumUser.show.politicianInactive.text.p2.text"/></p>

        <h2><g:message code="kuorumUser.show.politicianInactive.text.p3.title"/></h2>
        <p><g:message code="kuorumUser.show.politicianInactive.text.p3.text1"/></p>

        <span class="hidden-sm hidden-xs">
            <userUtil:followButton user="${user}" prefixMessages="kuorumUser.follow.inactivePolitician" cssSize="btn-lg"/>
        </span>
    </article>
</content>

<content tag="cColumn">
    <g:render template="politicianKarmaProfileInactive" model="[user:user]"/>

    <section class="boxes noted">
        <span><g:message code="kuorumUser.show.politicianInactive.module.noted.advise"/> </span>
        <h1><g:message code="kuorumUser.show.politicianInactive.module.noted.title" args="[user.name]"/> </h1>
        <p><g:message code="kuorumUser.show.politicianInactive.module.noted.p1"/> </p>
        <p><g:message code="kuorumUser.show.politicianInactive.module.noted.p2"/> </p>
        <g:if test="${!user.followers.isEmpty()}">
            <h2><g:message code="kuorumUser.show.politicianInactive.module.noted.peopleWants" args="[user.numFollowers]"/></h2>
            <div class="follow">
                <userUtil:listFollowers user="${user}"/>
            </div>
        </g:if>

    </section>
</content>
