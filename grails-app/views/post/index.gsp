<%@ page import="kuorum.post.Post" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'post.label', default: 'Post')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
</head>

<body>
<a href="#list-post" class="skip" tabindex="-1"><g:message code="default.link.skip.label"
                                                           default="Skip to content&hellip;"/></a>

<div class="nav" role="navigation">
    <ul>
        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
        <li><g:link class="create" action="create"><g:message code="default.new.label"
                                                              args="[entityName]"/></g:link></li>
    </ul>
</div>

<div id="list-post" class="content scaffold-list" role="main">
    <h1><g:message code="default.list.label" args="[entityName]"/></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <table>
        <thead>
        <tr>

            <g:sortableColumn property="dateCreated"
                              title="${message(code: 'post.dateCreated.label', default: 'Date Created')}"/>

            <th><g:message code="post.defender.label" default="Defender"/></th>

            <th><g:message code="post.firstCluck.label" default="First Cluck"/></th>

            <th><g:message code="post.law.label" default="Law"/></th>

            <g:sortableColumn property="numClucks"
                              title="${message(code: 'post.numClucks.label', default: 'Num Clucks')}"/>

            <g:sortableColumn property="numVotes"
                              title="${message(code: 'post.numVotes.label', default: 'Num Votes')}"/>

        </tr>
        </thead>
        <tbody>
        <g:each in="${postInstanceList}" status="i" var="postInstance">
            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                <td><kPost:postLink post="${postInstance}">${fieldValue(bean: postInstance, field: "dateCreated")}</kPost:postLink></td>

                <td>${fieldValue(bean: postInstance, field: "defender")}</td>

                <td>${fieldValue(bean: postInstance, field: "firstCluck")}</td>

                <td>${fieldValue(bean: postInstance, field: "law")}</td>

                <td>${fieldValue(bean: postInstance, field: "numClucks")}</td>

                <td>${fieldValue(bean: postInstance, field: "numVotes")}</td>

            </tr>
        </g:each>
        </tbody>
    </table>

    <div class="pagination">
        <g:paginate total="${postInstanceCount ?: 0}"/>
    </div>
</div>
</body>
</html>
