<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.footer.userGuide"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="info" />
</head>

<content tag="leftMenu">
    <g:render template="leftMenu" model="[activeMapping:'footerUserGuide']"/>
</content>

<content tag="mainContent">
    <article role="article" itemtype="http://schema.org/Article" itemscope>
        <h1><g:message code="layout.footer.userGuide"/></h1>
        <h2><g:message code="footer.menu.footerUserGuide.subtitle1"/></h2>
        <div class="columns2">
            <p>
                <g:message code="footer.menu.footerUserGuide.description11"/>
            </p>
            <blockquote>
                <span class="fa fa-quote-right fa-2x"></span>
                <p><g:message code="footer.menu.footerUserGuide.description12"/></p>
            </blockquote>
            <p>
                <g:message code="footer.menu.footerUserGuide.description13"/>
            </p>
        </div>

        <h2 class="border"><g:message code="footer.menu.footerUserGuide.subtitle2"/></h2>
        <div class="columns2">
            <p>
                <g:set var="linkHistories" value="${createLink(mapping:'footerHistories')}"/>
                <g:set var="linkQuestions" value="${createLink(mapping:'footerQuestions')}"/>
                <g:set var="linkPurposes" value="${createLink(mapping:'footerPurposes')}"/>
                <g:message code="footer.menu.footerUserGuide.description21" args="[linkHistories, linkQuestions, linkPurposes]" encodeAs="raw"/>
            </p>
        </div>

        <h2 class="border"><g:message code="footer.menu.footerUserGuide.subtitle3"/></h2>
        <div class="columns1">
            <p>
                <g:message code="footer.menu.footerUserGuide.description31"/>
            </p>
            <blockquote>
                <span class="fa fa-quote-right fa-2x"></span>
                <p><g:message code="footer.menu.footerUserGuide.description32"/></p>
            </blockquote>
        </div>

        <h2 class="border"><g:message code="footer.menu.footerUserGuide.subtitle4"/></h2>
        <div class="columns1">
            <p>
                <g:message code="footer.menu.footerUserGuide.description41"/>
            </p>
        </div>

        <img src="../images/image-info.jpg" alt="foto-aerea-manifestaciones" itemprop="image">

        <h2 class="border"><g:message code="footer.menu.footerUserGuide.subtitle5"/></h2>
        <div class="columns2">
            <p>
                <g:message code="footer.menu.footerUserGuide.description51"/>
            </p>
            <p>
                <g:set var="linkTermsUse" value="${createLink(mapping:'footerTermsUse')}"/>
                <g:message code="footer.menu.footerUserGuide.description52" args="[linkTermsUse]" encodeAs="raw"/>
            </p>
        </div>

        <h2 class="border"><g:message code="footer.menu.footerUserGuide.subtitle6"/></h2>
        <div class="columns2">
            <p>
                <g:message code="footer.menu.footerUserGuide.description61"/>
            </p>
s
            <blockquote>
                <span class="fa fa-quote-right fa-2x"></span>
                <p><g:message code="footer.menu.footerUserGuide.description62"/></p>
            </blockquote>
            <p>
                <g:message code="footer.menu.footerUserGuide.description63"/>
            </p>
        </div>

        <h2 class="border"><g:message code="footer.menu.footerUserGuide.subtitle7"/></h2>
        <div class="columns2">
            <p>
                <g:message code="footer.menu.footerUserGuide.description71"/>
            </p>
            <p>
                <g:set var="linkKuorumStore" value="${createLink(mapping:'footerKuorumStore')}"/>
                <g:message code="footer.menu.footerUserGuide.description72" args="[linkKuorumStore]" encodeAs="raw"/>
            </p>
        </div>

        <img src="../images/image-info.jpg" alt="foto-aerea-manifestaciones" itemprop="image">
    </article>
</content>
