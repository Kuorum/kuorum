<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="kuorum.name"/> </title>
    <meta name="layout" content="editPostLayout">
    <parameter name="extraCssContainer" value="edit-post" />
</head>

<content tag="intro">
</content>

<content tag="mainContent">
    GRAICAS
</content>

<content tag="cColumn">
    <g:render template="/modules/recommendedPosts" model="[recommendedPost:[post], title:message(code:'post.promote.columnC.postPromoted.title'),specialCssClass:'']"/>
</content>