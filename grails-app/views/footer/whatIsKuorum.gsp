<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.footer.whatIsKuorum"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="info" />
</head>

<content tag="leftMenu">
    <g:render template="leftMenu" model="[activeMapping:'footerWhatIsKuorum']"/>
</content>

<content tag="mainContent">
    <article role="article" itemtype="http://schema.org/Article" itemscope>
        <h1><g:message code="layout.footer.whatIsKuorum"/></h1>
        <h2><g:message code="footer.menu.footerWhatIsKuorum.subtitle"/></h2>
        <div class="columns2">
            <p>
                <g:message code="footer.menu.footerWhatIsKuorum.description1"/>
            </p>
            <p>
                <g:set var="linkPurposes" value="${createLink(mapping:'footerPurposes')}"/>
                <g:set var="linkQuestions" value="${createLink(mapping:'footerQuestions')}"/>
                <g:set var="linkHistories" value="${createLink(mapping:'footerHistories')}"/>
                <g:message code="footer.menu.footerWhatIsKuorum.description2" args="[linkPurposes, linkQuestions, linkHistories]" encodeAs="raw"/>
            </p>
            <p>
                <g:set var="linkRegister" value="${createLink(mapping:'register')}"/>
                <g:message code="footer.menu.footerWhatIsKuorum.description3" args="[linkRegister]" encodeAs="raw"/>
            </p>
            %{--<blockquote>--}%
                %{--<span class="fa fa-quote-right fa-2x"></span>--}%
                %{--<p><g:message code="footer.menu.footerWhatIsKuorum.description4"/></p>--}%
            %{--</blockquote>--}%
        </div>
        <img src="${resource(dir: 'images', file: 'image-info-lobbist.jpg')}" alt="foto-lobbista" itemprop="image">
    </article>
</content>


