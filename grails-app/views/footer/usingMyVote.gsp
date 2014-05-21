<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.footer.usingMyVote"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="info" />
</head>

<content tag="leftMenu">
    <g:render template="leftMenu" model="[activeMapping:'footerUsingMyVote']"/>
</content>

<content tag="mainContent">
    <article role="article" itemtype="http://schema.org/Article" itemscope>
        <h1><g:message code="layout.footer.usingMyVote"/></h1>
        <h2><g:message code="footer.menu.footerUsingMyVote.subtitle"/></h2>
        <div class="columns2">
            <p>
                <g:set var="linkRegister" value="${createLink(mapping:'register')}"/>
                <g:message code="footer.menu.footerUsingMyVote.description1" args="[linkRegister]" encodeAs="raw"/>
            </p>
            <p>
                <g:message code="footer.menu.footerUsingMyVote.description2"/>
            </p>
            <p>
                <g:set var="linkUserGuide" value="${createLink(mapping:'footerUserGuide')}"/>
                <g:message code="footer.menu.footerUsingMyVote.description3" args="[linkUserGuide]" encodeAs="raw"/>
            </p>
            <blockquote>
                <span class="fa fa-quote-right fa-2x"></span>
                <p><g:message code="footer.menu.footerWhatIsKuorum.description4"/></p>
            </blockquote>
        </div>
        <img src="../images/image-info-vote.jpg" alt="foto-sufragio-universal" itemprop="image">
    </article>
</content>
