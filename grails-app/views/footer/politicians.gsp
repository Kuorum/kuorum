<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.footer.politicians"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="info" />
</head>

<content tag="leftMenu">
    <g:render template="leftMenu" model="[activeMapping:'footerPoliticians']"/>
</content>

<content tag="mainContent">
    <article role="article" itemtype="http://schema.org/Article" itemscope>
        <h1><g:message code="layout.footer.politicians"/></h1>
        <h2><g:message code="footer.menu.footerPoliticians.subtitle"/></h2>
        <div class="columns2">
            <p>
                <g:message code="footer.menu.footerPoliticians.description1"/>
            </p>
            <p>
                <g:message code="footer.menu.footerPoliticians.description2"/>
            </p>
            <p>
                <g:message code="footer.menu.footerPoliticians.description3"/>
                <a href="mailto:info@kuorum.org">
                    <g:message code="footer.menu.footerPoliticians.description4"/>
                </a>
            </p>
            %{--<blockquote>--}%
            %{--<span class="fa fa-quote-right fa-2x"></span>--}%
            %{--<p><g:message code="footer.menu.footerCitizens.description3"/></p>--}%
            %{--</blockquote>--}%
        </div>
        <img src="${resource(dir: 'images', file: 'image-info.jpg')}" alt="foto-aerea-manifestaciones" itemprop="image">
    </article>
</content>
