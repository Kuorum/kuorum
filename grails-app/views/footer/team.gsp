<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.footer.userGuide"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="info" />
</head>

<content tag="leftMenu">
    <g:render template="leftMenu" model="[activeMapping:'footerTeam']"/>
</content>

<content tag="mainContent">
    <ul class="list-team">
        <li itemscope itemtype="http://schema.org/Person">
            <div class="box-ppal">
                <img src="${resource(dir: 'images', file: 'foto-ceo.jpg')}" alt="Foto" itemprop="image">
                <h1 itemprop="name"><g:message code="footer.menu.footerTeam.member1"/></h1>
                <h2 itemprop="jobTitle"><g:message code="footer.menu.footerTeam.position1"/></h2>
                <p itemprop="description"><g:message code="footer.menu.footerTeam.description1"/></p>
                <p itemprop="description"><g:message code="footer.menu.footerTeam.description1b"/></p>
                <ul class="social">
                    <li><a href="https://twitter.com/matnso" target="_blank"><span class="fa fa-twitter fa-2x"></span> <span class="sr-only">Twitter</span></a></li>
                    <li><a href="https://www.linkedin.com/in/mnsoroca" target="_blank"><span class="fa fa-linkedin fa-2x"></span> <span class="sr-only">LinkedIn</span></a></li>
                </ul>
            </div>
        </li>
        <li itemscope itemtype="http://schema.org/Person">
            <div class="box-ppal">
                <img src="${resource(dir: 'images', file: 'foto-cfo.jpg')}" alt="Foto" itemprop="image">
                <h1 itemprop="name"><g:message code="footer.menu.footerTeam.member3"/></h1>
                <h2 itemprop="jobTitle"><g:message code="footer.menu.footerTeam.position3"/></h2>
                <p itemprop="description"><g:message code="footer.menu.footerTeam.description3"/></p>
                <p itemprop="description"><g:message code="footer.menu.footerTeam.description3b"/></p>
                <ul class="social">
                    <li><a href="https://twitter.com/chemafinca" target="_blank"><span class="fa fa-twitter fa-2x"></span> <span class="sr-only">Twitter</span></a></li>
                    <li><a href="es.linkedin.com/in/josemariagarciadiaz/es" target="_blank"><span class="fa fa-linkedin fa-2x"></span> <span class="sr-only">LinkedIn</span></a></li>
                </ul>
            </div>
        </li>
        <li itemscope itemtype="http://schema.org/Person">
            <div class="box-ppal">
                <img src="${resource(dir: 'images', file: 'foto-cto3.jpg')}" alt="Foto" itemprop="image">
                <h1 itemprop="name"><g:message code="footer.menu.footerTeam.member2"/></h1>
                <h2 itemprop="jobTitle"><g:message code="footer.menu.footerTeam.position2"/></h2>
                <p itemprop="description"><g:message code="footer.menu.footerTeam.description2"/></p>
                <p itemprop="description"><g:message code="footer.menu.footerTeam.description2b"/></p>
                <ul class="social">
                    <li><a href="https://twitter.com/iduetxe" target="_blank"><span class="fa fa-twitter fa-2x"></span> <span class="sr-only">Twitter</span></a></li>
                    <li><a href="es.linkedin.com/in/inakidominguez/es" target="_blank"><span class="fa fa-linkedin fa-2x"></span> <span class="sr-only">LinkedIn</span></a></li>
                </ul>
            </div>
        </li>
    </ul>
</content>
