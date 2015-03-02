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
        <div class="box-ppal">
        <h1><g:message code="layout.footer.whatIsKuorum"/></h1>
            <p>
                <g:message code="footer.menu.footerWhatIsKuorum.description1"/>
            </p>
            <p>
                <g:message code="footer.menu.footerWhatIsKuorum.description2"/>
            </p>
            %{--<p>--}%
                %{--<g:set var="linkPurposes" value="${createLink(mapping:'footerPurposes')}"/>--}%
                %{--<g:set var="linkQuestions" value="${createLink(mapping:'footerQuestions')}"/>--}%
                %{--<g:set var="linkHistories" value="${createLink(mapping:'footerHistories')}"/>--}%
                %{--<g:message code="footer.menu.footerWhatIsKuorum.description2" args="[linkPurposes, linkQuestions, linkHistories]" encodeAs="raw"/>--}%
            %{--</p>--}%
            %{--<blockquote>--}%
                %{--<span class="fa fa-quote-right fa-2x"></span>--}%
                %{--<p><g:message code="footer.menu.footerWhatIsKuorum.description4"/></p>--}%
            %{--</blockquote>--}%
        <img src="${resource(dir: 'images', file: 'info4.png')}" alt="foto-debate" itemprop="image">
        </div>
</content>


