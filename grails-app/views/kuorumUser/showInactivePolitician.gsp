<%@ page import="kuorum.core.model.UserType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="kuorum.name"/> </title>
    <meta name="layout" content="columnCLayout">
    <parameter name="specialContainerCssClass" value="userprofile" />
</head>


<content tag="mainContent">
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
        <h1><g:message code="kuorumUser.show.politicianInactive.text.h1"/> </h1>
        <a href="#" class="btn btn-blue btn-lg btn-block">Quiero que <strong>Nombre político</strong> <br>se una a kuorum</a>

        <h2><g:message code="kuorumUser.show.politicianInactive.text.p1.title"/></h2>
        <p><g:message code="kuorumUser.show.politicianInactive.text.p1.text"/></p>

        <h2><g:message code="kuorumUser.show.politicianInactive.text.p2.title"/></h2>
        <p><g:message code="kuorumUser.show.politicianInactive.text.p2.text"/></p>

        <h2><g:message code="kuorumUser.show.politicianInactive.text.p3.title"/></h2>
        <p><g:message code="kuorumUser.show.politicianInactive.text.p3.text1"/></p>
        <p><g:message code="kuorumUser.show.politicianInactive.text.p3.text2"/></p>


        <a href="#" class="btn btn-blue btn-lg btn-block">Quiero que <strong>Nombre político</strong> <br>se una a kuorum</a>
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
            <h2><g:message code="kuorumUser.show.politicianInactive.module.noted.peopleWants"/></h2>
            <userUtil:listFollowers user="${user}"/>
        </g:if>

    </section>
</content>
