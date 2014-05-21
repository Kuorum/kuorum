<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.footer.kuorumStore"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="info" />
</head>

<content tag="leftMenu">
    <g:render template="leftMenu" model="[activeMapping:'footerKuorumStore']"/>
</content>

<content tag="mainContent">
    <article role="article" itemtype="http://schema.org/Article" itemscope>
        <h1><g:message code="layout.footer.kuorumStore"/></h1>
        <h2><g:message code="footer.menu.footerKuorumStore.subtitle"/></h2>
        <div class="columns2">
            <p>
                <g:message code="footer.menu.footerKuorumStore.description2"/>
            </p>
            <p>
                <g:message code="footer.menu.footerKuorumStore.description1"/>
            </p>
            <p>
                <g:link mapping="register">
                    <g:message code="footer.menu.footerKuorumStore.description3"/>
                </g:link>
            </p>
            %{--<blockquote>--}%
            %{--<span class="fa fa-quote-right fa-2x"></span>--}%
            %{--<p><g:message code="footer.menu.footerCitizens.description3"/></p>--}%
            %{--</blockquote>--}%
        </div>
        <img src="${resource(dir: 'images', file: 'image-info.jpg')}" alt="foto-aerea-manifestaciones" itemprop="image">
    </article>
</content>
