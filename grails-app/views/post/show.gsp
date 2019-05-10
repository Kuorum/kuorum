<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <g:set var="titleMessageCode" value="${post?.event==null?'post.show.title':'event.show.title'}"/>
    <title><g:message code="${titleMessageCode}" args="[post.title, _domainName]"/></title>
    <meta name="layout" content="columnCLayout">
    <g:set var="schemaData" value="${[schema:'http://schema.org/Article', name:post.title]}" scope="request"/>
    <g:render template="/post/postMetaTags" model="[post: post, titleMessageCode:titleMessageCode]"/>
    <r:require modules="post, event,forms"/>

</head>

<content tag="mainContent">
    <g:render template="/post/showModules/mainContent" model="[post: post, postUser: postUser]" />
    <g:render template="/campaigns/columnCModules/eventCallToAction" model="[eventUser: postUser,campaign:post, hideSmallDevices:true]"/>
</content>

<content tag="cColumn">
    <g:render template="/campaigns/columnCModules/eventCallToAction" model="[eventUser: postUser,campaign:post, hideSmallDevices:false]"/>
    <g:render template="/campaigns/columnCModules/eventInfo" model="[event:post.event, eventUser: postUser]"/>
    <g:render template="/post/showModules/cColumn" model="[post: post, postUser: postUser]" />
    <g:render template="/campaigns/columnCModules/campaignFiles" model="[campaignFiles:campaignFiles]"/>
</content>

