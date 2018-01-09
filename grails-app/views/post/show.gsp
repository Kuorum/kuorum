<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="post.show.title" args="[post.title]"/></title>
    <meta name="layout" content="columnCLayout">
    <g:set var="schemaData" value="${[schema:'http://schema.org/Article', name:post.title]}" scope="request"/>
    <g:render template="postMetaTags" model="[post: post]"/>
    <r:require modules="post, event"/>

</head>

<content tag="mainContent">
    <g:render template="showModules/mainContent" model="[post: post, postUser: postUser]" />
    <g:render template="/campaigns/columnCModules/eventCallToAction" model="[eventUser: postUser,event:post.event]"/>
</content>

<content tag="cColumn">
    <g:render template="/campaigns/columnCModules/eventCallToAction" model="[eventUser: postUser,event:post.event]"/>
    <g:render template="/campaigns/columnCModules/eventInfo" model="[event:post.event]"/>
    <g:render template="showModules/cColumn" model="[post: post, postUser: postUser]" />
</content>

