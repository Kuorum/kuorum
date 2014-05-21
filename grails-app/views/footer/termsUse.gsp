<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.footer.termsUse"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="info" />
</head>

<content tag="leftMenu">
    <g:render template="leftMenu" model="[activeMapping:'footerTermsUse']"/>
</content>

<content tag="mainContent">
    <article role="article" itemtype="http://schema.org/Article" itemscope>
        <h1><g:message code="layout.footer.termsUse"/></h1>
        <h2><g:message code="footer.menu.footerTermsUse.subtitle1"/></h2>
        <div class="columns1">
            <p>
                <g:message code="footer.menu.footerTermsUse.description11"/>
            </p>
        </div>

        <h2  class="border"><g:message code="footer.menu.footerTermsUse.subtitle2"/></h2>
        <div class="columns1">
            <p>
                <g:message code="footer.menu.footerTermsUse.description21"/>
            </p>
        </div>
        <img src="../images/image-info.jpg" alt="foto-aerea-manifestaciones" itemprop="image">
    </article>
</content>
