
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="kuorum.name"/> </title>
    <meta name="layout" content="columnCLayout">
    <!-- Schema.org markup for Google+ -->
    <meta itemprop="name" content="${g.message(code:"kuorum.name")}">
    <meta itemprop="description" content="${g.message(code:"layout.head.meta.description")}">
    <meta itemprop="image" content="${resource(dir: 'images', file: 'home1.jpg')}" />
    <meta itemprop="image" content="${resource(dir: 'images', file: 'logo@2x.png')}" />
</head>


<content tag="mainContent">
    <h2 class="underline">Suggested causes for you</h2>
    <ul class="causes-list row">
        <g:each in="${suggestions.data}" var="suggestion">
            <li class="col-xs-12 col-sm-6">
                <cause:card cause="${suggestion}"/>
        </g:each>
    </ul>

    <!-- ver más -->
    <div class="load-more"><a href="#">Show more <span class="fa fa-angle-down"></span></a></div>

    <h2 class="underline">Politicians who recently joined</h2>
    <!-- LISTA DE POLÍTICOS -->
    <ul class="politician-list row">
        <g:render template="/search/searchElement" model="[docs:politicians, columnsCss:'col-sm-6']"/>
    </ul>
</content>

<content tag="cColumn">
    <g:include controller="modules" action="userProfile"/>
</content>
