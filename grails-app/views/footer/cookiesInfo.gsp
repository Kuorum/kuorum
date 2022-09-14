<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<r:require module="cookiesInfo"/>
<head>
    <title><g:message code="page.title.footer.cookiesInfo"/></title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="info"/>
</head>

<content tag="leftMenu">
    <g:render template="leftMenuLegal" model="[activeMapping: 'footerCookiesInfo']"/>
</content>

<content tag="mainContent">
    <h1><g:message code="layout.footer.cookiesInfo"/></h1>
    <h4><g:message code="footer.menu.footerCookiesInfo.subtitle01"/></h4>

    <div class="columns1">
        <p>
            <g:message code="footer.menu.footerCookiesInfo.description0101" args="[legalInfo.domainOwner]"/>
        </p>
    </div>

    <div class="columns1">
        <p>
            <g:message code="footer.menu.footerCookiesInfo.description0102"/>
        </p>
    </div>

    <div class="columns1">
        <p>
            <g:message code="footer.menu.footerCookiesInfo.description0103"/>
        </p>
    </div>
    <h4><g:message code="footer.menu.footerCookiesInfo.subtitle02"/></h4>

    <div class="columns1">
        <p>
            <g:message code="footer.menu.footerCookiesInfo.description0201"/>
        </p>
    </div>

    <div class="columns1">
        <p>
            <g:message code="footer.menu.footerCookiesInfo.description0202"/>
        </p>
    </div>
    <label class="checkbox">
        <input id="technicalCookiesAccepted" type="checkbox"/>
        <span class="check-box-icon"></span>
        <span class="label-checkbox"><g:message code="footer.menu.footerCookiesInfo.cookieTechnical.label"/></span>
    </label>


    <h4><g:message code="footer.menu.footerCookiesInfo.subtitle03"/></h4>

    <div class="columns1">
        <p>
            <g:message code="footer.menu.footerCookiesInfo.description0301"/>
        </p>
    </div>

    <div class="columns1">
        <p>
            <g:message code="footer.menu.footerCookiesInfo.description0302"/>
        </p>
    </div>

    <div class="columns1">
        <p>
            <g:message code="footer.menu.footerCookiesInfo.description0303"/>
        </p>
    </div>

    <div class="columns1">
        <p>
            <g:message code="footer.menu.footerCookiesInfo.description0304"/>
        </p>
    </div>

    <div class="columns1">
        <p>
            <g:message code="footer.menu.footerCookiesInfo.description0305"/>
        </p>
    </div>

    <label class="checkbox">
        <input id="thirdCookiesAccepted" type="checkbox"/>
        <span class="check-box-icon"></span>
        <span class="label-checkbox"><g:message code="footer.menu.footerCookiesInfo.cookieThird.label"/></span>
    </label>
</content>
