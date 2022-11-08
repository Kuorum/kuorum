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

    <div class="horizontal-scroll-cookies">
        <table class="table table-cookies">
            <thead>
            <tr>
                <th class="col-md-2"><g:message code="footer.menu.footerCookiesInfo.cookie.tableHead.name"/></th>
                <th class="col-md-8"><g:message code="footer.menu.footerCookiesInfo.cookie.tableHead.description"/></th>
                <th class="col-md-2"><g:message code="footer.menu.footerCookiesInfo.cookie.tableHead.expiration"/></th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>JSESSIONID</td>
                <td><g:message code="footer.menu.footerCookiesInfo.cookie.JSESSIONID.description"/></td>
                <td><g:message code="footer.menu.footerCookiesInfo.cookie.expiration.browsingSession"/></td>
            </tr>
            <tr>
                <td>KUORUM_USER_UUID</td>
                <td><g:message code="footer.menu.footerCookiesInfo.cookie.KUORUM_USER_UUID.description"/></td>
                <td><g:message code="footer.menu.footerCookiesInfo.cookie.expiration.oneMonth"/></td>
            </tr>
            <tr>
                <td>kuorumCookiesAccepted</td>
                <td><g:message code="footer.menu.footerCookiesInfo.cookie.kuorumCookiesAccepted.description"/></td>
                <td><g:message code="footer.menu.footerCookiesInfo.cookie.expiration.oneMonth"/></td>
            </tr>
            <tr>
                <td>kuorumCookiesAccepted</td>
                <td><g:message code="footer.menu.footerCookiesInfo.cookie.kuorumSecurity_rememberMe.description"/></td>
                <td><g:message code="footer.menu.footerCookiesInfo.cookie.expiration.oneMonth"/></td>
            </tr>
            <tr>
                <td>kuorumThirdCookiesAccepted</td>
                <td><g:message code="footer.menu.footerCookiesInfo.cookie.kuorumThirdCookiesAccepted.description"/></td>
                <td><g:message code="footer.menu.footerCookiesInfo.cookie.expiration.oneMonth"/></td>
            </tr>
            <tr>
                <td>org.springframework.web. servlet.i18n. CookieLocaleResolver.LOCALE</td>
                <td><g:message code="footer.menu.footerCookiesInfo.cookie.i18n_CookieLocaleResolver.description"/></td>
                <td><g:message code="footer.menu.footerCookiesInfo.cookie.expiration.browsingSession"/></td>
            </tr>
            <tr>
                <td>VISITOR_INFO1_LIVE</td>
                <td><g:message code="footer.menu.footerCookiesInfo.cookie.VISITOR_INFO1_LIVE.description"/></td>
                <td><g:message code="footer.menu.footerCookiesInfo.cookie.expiration.sixMonth"/></td>
            </tr>
            <tr>
                <td>_GRECAPTCHA</td>
                <td><g:message code="footer.menu.footerCookiesInfo.cookie.google.GRECAPTCHA"/></td>
                <td><g:message code="footer.menu.footerCookiesInfo.cookie.expiration.browsingSession"/></td>
            </tr>
            <tr>
                <td>AWSALB</td>
                <td><g:message code="footer.menu.footerCookiesInfo.cookie.google.AWSALB"/></td>
                <td><g:message code="footer.menu.footerCookiesInfo.cookie.expiration.oneweek"/></td>
            </tr>
            <tr>
                <td>AWSALBCORS</td>
                <td><g:message code="footer.menu.footerCookiesInfo.cookie.google.AWSALBCORS"/></td>
                <td><g:message code="footer.menu.footerCookiesInfo.cookie.expiration.oneweek"/></td>
            </tr>
            <tr>
                <td>YSC</td>
                <td><g:message code="footer.menu.footerCookiesInfo.cookie.google.YSC"/></td>
                <td><g:message code="footer.menu.footerCookiesInfo.cookie.expiration.browsingSession"/></td>
            </tr>
            </tbody>
        </table>
    </div>

    <p>
        <g:message code="footer.menu.footerCookiesInfo.shareServices" args="[legalInfo.domainOwner]"/>
    </p>


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

        <div class="horizontal-scroll-cookies">
            <table class="table table-cookies">
                <thead>
                <tr>
                    <th class="col-md-2"><g:message code="footer.menu.footerCookiesInfo.cookie.tableHead.name"/></th>
                    <th class="col-md-1"><g:message
                            code="footer.menu.footerCookiesInfo.cookie.tableHead.provider"/></th>
                    <th class="col-md-4"><g:message
                            code="footer.menu.footerCookiesInfo.cookie.tableHead.description"/></th>
                    <th class="col-md-1"><g:message
                            code="footer.menu.footerCookiesInfo.cookie.tableHead.expiration"/></th>
                    <th class="col-md-1"><g:message
                            code="footer.menu.footerCookiesInfo.cookie.tableHead.type"/></th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>_ga</td>
                    <td>Google</td>
                    <td><g:message code="footer.menu.footerCookiesInfo.cookie.google._ga"/></td>
                    <td><g:message code="footer.menu.footerCookiesInfo.cookie.google._ga.expiration"/></td>
                    <td><g:message code="footer.menu.footerCookiesInfo.cookie.type.analitycs"/></td>
                </tr>
                <tr>
                    <td>_gid</td>
                    <td>Google</td>
                    <td><g:message code="footer.menu.footerCookiesInfo.cookie.google._gid"/></td>
                    <td><g:message code="footer.menu.footerCookiesInfo.cookie.google._gid.expiration"/></td>
                    <td><g:message code="footer.menu.footerCookiesInfo.cookie.type.analitycs"/></td>
                </tr>
                <tr>
                    <td>_gat</td>
                    <td>Google</td>
                    <td><g:message code="footer.menu.footerCookiesInfo.cookie.google._gat"/></td>
                    <td><g:message code="footer.menu.footerCookiesInfo.cookie.google._gat.expiration"/></td>
                    <td><g:message code="footer.menu.footerCookiesInfo.cookie.type.analitycs"/></td>
                </tr>
                <tr>
                    <td>AMP_TOKEN</td>
                    <td>Google</td>
                    <td><g:message code="footer.menu.footerCookiesInfo.cookie.google.AMP_TOKEN"/></td>
                    <td><g:message code="footer.menu.footerCookiesInfo.cookie.google.AMP_TOKEN.expiration"/></td>
                    <td><g:message code="footer.menu.footerCookiesInfo.cookie.type.analitycs"/></td>
                </tr>
                </tbody>
            </table>
        </div>
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
