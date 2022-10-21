<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.footer.termsUse"/></title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="info"/>
</head>

<content tag="leftMenu">
    <g:render template="leftMenuLegal" model="[activeMapping: 'footerTermsUse']"/>
</content>

<content tag="mainContent">
    <h1><g:message code="layout.footer.termsUse"/></h1>
    <g:if test="${legalInfo.customLegalInfo}">
        <div class="columns1">
            <p>
                ${raw(legalInfo.customLegalInfo)}
            </p>
        </div>
    </g:if>
</content>
