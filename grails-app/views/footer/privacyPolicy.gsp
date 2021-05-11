<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.footer.privacyPolicy"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="info" />
</head>

<content tag="leftMenu">
    <g:render template="leftMenuLegal" model="[activeMapping:'footerPrivacyPolicy']"/>
</content>

<g:if test="${legalInfo.privacyPolicy}">
    <content tag="mainContent">
        ${legalInfo?.privacyPolicy}
    </content>
</g:if>

<g:else>
<content tag="mainContent">
    <h1><g:message code="layout.footer.privacyPolicy"/></h1>
    <h4><g:message code="footer.menu.footerPrivacyPolicy.subtitle01"/></h4>
    <div class="columns1">
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0101" args="[legalInfo.domainOwner]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0102" args="[legalInfo.domainOwner, legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0103" args="[legalInfo.domainOwner]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0104"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0105"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0106"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0107" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0108"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0109" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0110"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0111" args="[legalInfo.domainName]"/>
        </p>
    </div>

    <h4  class="border"><g:message code="footer.menu.footerPrivacyPolicy.subtitle02"/></h4>
    <div class="columns1">
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0201" args="[legalInfo.domainOwner, legalInfo.domainName, legalInfo.address, legalInfo.city, legalInfo.country, legalInfo.fileName, legalInfo.filePurpose, legalInfo.fileResponsibleName, legalInfo.fileResponsibleEmail]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0202"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0203"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0204" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0205"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0206" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0207" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0208"  args="[legalInfo.domainName]" />
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0209" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0211" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0212" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0213" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0214" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0215" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0216" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0217" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0218" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0226" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0227" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0228" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0229" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0230" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0231" args="[legalInfo.domainName]"/>
        </p>
    </div>

    <h4  class="border"><g:message code="footer.menu.footerPrivacyPolicy.subtitle03" args="[legalInfo.domainName]"/></h4>
    <div class="columns1">
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0301" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0302" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0303" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0304" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0305" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0306" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0307" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0308" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0309" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0310" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0311" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0312" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0313" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0314" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0315" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0316" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0317" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0318" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0319" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0320" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0321" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0322" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0323" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0324" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0325" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0326" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0327" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0328" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0329" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0330" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0331" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0332" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0333" args="[legalInfo.domainOwner, legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0334" args="[legalInfo.domainOwner, legalInfo.domainName]"/>
        </p>
    </div>

    <h4  class="border"><g:message code="footer.menu.footerPrivacyPolicy.subtitle04" args="[legalInfo.domainName]"/></h4>
    <div class="columns1">
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0401" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0402" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0403" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0404" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0405" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0406" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0407" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0408" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0409" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0410" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0411" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0412" args="[legalInfo.domainName]"/>
        </p>
        <li>
            <g:message code="footer.menu.footerPrivacyPolicy.description0413" args="[legalInfo.domainName]"/>
        </li>
    </br>
        <li>
            <g:message code="footer.menu.footerPrivacyPolicy.description0414" args="[legalInfo.domainName]"/>
        </li>
    </br>
        <li>
            <g:message code="footer.menu.footerPrivacyPolicy.description0415" args="[legalInfo.domainName]"/>
        </li>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description0416" args="[legalInfo.domainName]"/>
        </p>
    </div>

    <h4  class="border"><g:message code="footer.menu.footerPrivacyPolicy.subtitle05" args="[legalInfo.domainName]"/></h4>
    <div class="columns1">
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description051" args="[legalInfo.domainName]"/>
        </p>
        <p>
            <g:message code="footer.menu.footerPrivacyPolicy.description052" args="[legalInfo.domainName]"/>
        </p>
    </div>
</content>
</g:else>