<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.footer.developers"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="info" />
</head>

<content tag="leftMenu">
    <g:render template="leftMenu" model="[activeMapping:'footerDevelopers']"/>
</content>

<content tag="mainContent">
    <article role="article" itemtype="http://schema.org/Article" itemscope>
        <h1><g:message code="layout.footer.developers"/></h1>
        <h2><g:message code="footer.menu.footerDevelopers.subtitle"/></h2>
        <div class="columns2">
            <p>
                <g:message code="footer.menu.footerDevelopers.description1"/>
            </p>
            <p>
                <g:message code="footer.menu.footerDevelopers.description2" args="[linkRegister]" encodeAs="raw"/>
                <a href="https://github.com/Kuorum/kuorum" target="_blank">
                    <g:message code="footer.menu.footerDevelopers.description3" args="[linkRegister]" encodeAs="raw"/></a><g:message code="footer.menu.footerDevelopers.description4" args="[linkRegister]" encodeAs="raw"/>
            </p>
            %{--<blockquote>--}%
            %{--<span class="fa fa-quote-right fa-2x"></span>--}%
            %{--<p><g:message code="footer.menu.footerCitizens.description3"/></p>--}%
            %{--</blockquote>--}%
        </div>
        <img src="${resource(dir: 'images', file: 'image-info-keyboard.jpg')}" alt="foto-teclado" itemprop="image">
    </article>
</content>
