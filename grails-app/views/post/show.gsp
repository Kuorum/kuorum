<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title>${post.title}</title>
    <meta name="layout" content="columnCLayout">
    %{--<g:render template="postMetaTags" model="[post: post]"/>--}%
    <r:require modules="post"/>

</head>

<content tag="mainContent">
    <g:render template="showModules/mainContent" model="[post: post, postUser: postUser]" />
</content>

<content tag="cColumn">
    %{--<g:render template="showModules/cColumn" model="[debate: debate, debateUser: debateUser, proposalPage:proposalPage]" />--}%
    %{--<g:render template="showModules/modalLogin" model="[debate: debate, debateUser: debateUser]" />--}%
</content>

