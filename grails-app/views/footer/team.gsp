<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.footer.team"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="info team" />
    <g:render template="/dashboard/landingMetaTags"
              model="[
                      kuorumTitle:g.message(code:'page.title.footer.team'),
                      kuorumDescription:g.message(code:'page.title.footer.team.description'),
                      kuorumImage:r.resource(dir:'images', file:'landingSearch-rrss.png', absolute:true)
              ]"/>
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
                <img src="${resource(dir: 'images', file: 'foto-cto.jpg')}" alt="Foto" itemprop="image">
                <h1 itemprop="name"><g:message code="footer.menu.footerTeam.member2"/></h1>
                <h2 itemprop="jobTitle"><g:message code="footer.menu.footerTeam.position2"/></h2>
                <p itemprop="description"><g:message code="footer.menu.footerTeam.description2"/></p>
                <p itemprop="description"><g:message code="footer.menu.footerTeam.description2b"/></p>
                <ul class="social">
                    <li><a href="https://twitter.com/iduetxe" target="_blank"><span class="fa fa-twitter fa-2x"></span> <span class="sr-only">Twitter</span></a></li>
                    <li><a href="https://es.linkedin.com/in/inakidominguez/es" target="_blank"><span class="fa fa-linkedin fa-2x"></span> <span class="sr-only">LinkedIn</span></a></li>
                </ul>
            </div>
        </li>
        <li itemscope itemtype="http://schema.org/Person">
            <div class="box-ppal">
                <img src="${resource(dir: 'images', file: 'foto-cfo2.png')}" alt="Foto" itemprop="image">
                <h1 itemprop="name"><g:message code="footer.menu.footerTeam.member3"/></h1>
                <h2 itemprop="jobTitle"><g:message code="footer.menu.footerTeam.position3"/></h2>
                <p itemprop="description"><g:message code="footer.menu.footerTeam.description3"/></p>
                <p itemprop="description"><g:message code="footer.menu.footerTeam.description3b"/></p>
                <ul class="social">
                    <li><a href="https://twitter.com/chemafinca" target="_blank"><span class="fa fa-twitter fa-2x"></span> <span class="sr-only">Twitter</span></a></li>
                    <li><a href="https://es.linkedin.com/in/josemariagarciadiaz/es" target="_blank"><span class="fa fa-linkedin fa-2x"></span> <span class="sr-only">LinkedIn</span></a></li>
                </ul>
            </div>
        </li>
        <li itemscope itemtype="http://schema.org/Person">
            <div class="box-ppal">
                <img src="${resource(dir: 'images', file: 'foto-abby.jpg')}" alt="Foto" itemprop="image">
                <h1 itemprop="name"><g:message code="footer.menu.footerTeam.member4"/></h1>
                <h2 itemprop="jobTitle"><g:message code="footer.menu.footerTeam.position4"/></h2>
                <p itemprop="description"><g:message code="footer.menu.footerTeam.description4"/></p>
                <p itemprop="description"><g:message code="footer.menu.footerTeam.description4b"/></p>
                <ul class="social">
                    <li><a href="https://twitter.com/abbyrodd" target="_blank"><span class="fa fa-twitter fa-2x"></span> <span class="sr-only">Twitter</span></a></li>
                    <li><a href="https://es.linkedin.com/in/abbyrodd" target="_blank"><span class="fa fa-linkedin fa-2x"></span> <span class="sr-only">LinkedIn</span></a></li>
                </ul>
            </div>
        </li>
        <li itemscope itemtype="http://schema.org/Person">
            <div class="box-ppal">
                <img src="${resource(dir: 'images', file: 'foto-guille.jpg')}" alt="Foto" itemprop="image">
                <h1 itemprop="name"><g:message code="footer.menu.footerTeam.member5"/></h1>
                <h2 itemprop="jobTitle"><g:message code="footer.menu.footerTeam.position5"/></h2>
                <p itemprop="description"><g:message code="footer.menu.footerTeam.description5"/></p>
                <ul class="social">
                    %{--<li><a href="#" target="_blank"><span class="fa fa-twitter fa-2x"></span> <span class="sr-only">Twitter</span></a></li>--}%
                    <li><a href="https://www.linkedin.com/in/escancianoguillermo/" target="_blank"><span class="fa fa-linkedin fa-2x"></span> <span class="sr-only">LinkedIn</span></a></li>
                </ul>
            </div>
        </li>

        <li itemscope itemtype="http://schema.org/Person">
            <div class="box-ppal">
                <img src="${resource(dir: 'images', file: 'foto-yan.png')}" alt="Foto" itemprop="image">
                <h1 itemprop="name"><g:message code="footer.menu.footerTeam.member11"/></h1>
                <h2 itemprop="jobTitle"><g:message code="footer.menu.footerTeam.position11"/></h2>
                <p itemprop="description"><g:message code="footer.menu.footerTeam.description11"/></p>
                <ul class="social">
                    %{--<li><a href="https://twitter.com/Jackie_Flore" target="_blank"><span class="fa fa-twitter fa-2x"></span> <span class="sr-only">Twitter</span></a></li>--}%
                    <li><a href="https://www.linkedin.com/in/yanhua-hu-75984a12a/" target="_blank"><span class="fa fa-linkedin fa-2x"></span> <span class="sr-only">LinkedIn</span></a></li>
                </ul>
            </div>
        </li>

        <li itemscope itemtype="http://schema.org/Person">
            <div class="box-ppal">
                <img src="${resource(dir: 'images', file: 'foto-majo.png')}" alt="Foto" itemprop="image">
                <h1 itemprop="name"><g:message code="footer.menu.footerTeam.member6"/></h1>
                <h2 itemprop="jobTitle"><g:message code="footer.menu.footerTeam.position6"/></h2>
                <p itemprop="description"><g:message code="footer.menu.footerTeam.description6"/></p>
                <ul class="social">
                    %{--<li><a href="https://twitter.com/Jackie_Flore" target="_blank"><span class="fa fa-twitter fa-2x"></span> <span class="sr-only">Twitter</span></a></li>--}%
                    <li><a href="https://www.linkedin.com/in/maria-jos%C3%A9-de-la-torre-17899083/" target="_blank"><span class="fa fa-linkedin fa-2x"></span> <span class="sr-only">LinkedIn</span></a></li>
                </ul>
            </div>
        </li>

        %{--<li itemscope itemtype="http://schema.org/Person">--}%
            %{--<div class="box-ppal">--}%
                %{--<img src="${resource(dir: 'images', file: 'foto-will.jpg')}" alt="Foto" itemprop="image">--}%
                %{--<h1 itemprop="name"><g:message code="footer.menu.footerTeam.member12"/></h1>--}%
                %{--<h2 itemprop="jobTitle"><g:message code="footer.menu.footerTeam.position12"/></h2>--}%
                %{--<p itemprop="description"><g:message code="footer.menu.footerTeam.description12"/></p>--}%
                %{--<p itemprop="description"><g:message code="footer.menu.footerTeam.description12b"/></p>--}%
                %{--<ul class="social">--}%
                    %{--<li><a href="https://twitter.com/Jackie_Flore" target="_blank"><span class="fa fa-twitter fa-2x"></span> <span class="sr-only">Twitter</span></a></li>--}%
                    %{--<li><a href="https://www.linkedin.com/in/william-davis7" target="_blank"><span class="fa fa-linkedin fa-2x"></span> <span class="sr-only">LinkedIn</span></a></li>--}%
                %{--</ul>--}%
            %{--</div>--}%
        %{--</li>--}%

        %{--<li itemscope itemtype="http://schema.org/Person">--}%
            %{--<div class="box-ppal">--}%
                %{--<img src="${resource(dir: 'images', file: 'foto-sara.jpg')}" alt="Foto" itemprop="image">--}%
                %{--<h1 itemprop="name"><g:message code="footer.menu.footerTeam.member8"/></h1>--}%
                %{--<h2 itemprop="jobTitle"><g:message code="footer.menu.footerTeam.position8"/></h2>--}%
                %{--<p itemprop="description"><g:message code="footer.menu.footerTeam.description8"/></p>--}%
                %{--<p itemprop="description"><g:message code="footer.menu.footerTeam.description8b"/></p>--}%
                %{--<ul class="social">--}%
                    %{--<li><a href="#" target="_blank"><span class="fa fa-twitter fa-2x"></span> <span class="sr-only">Twitter</span></a></li>--}%
                    %{--<li><a href="https://uk.linkedin.com/pub/sara-nso/18/37a/652/es" target="_blank"><span class="fa fa-linkedin fa-2x"></span> <span class="sr-only">LinkedIn</span></a></li>--}%
                %{--</ul>--}%
            %{--</div>--}%
        %{--</li>--}%
        %{--<li itemscope itemtype="http://schema.org/Person">--}%
            %{--<div class="box-ppal">--}%
                %{--<img src="${resource(dir: 'images', file: 'foto-carmen.jpg')}" alt="Foto" itemprop="image">--}%
                %{--<h1 itemprop="name"><g:message code="footer.menu.footerTeam.member9"/></h1>--}%
                %{--<h2 itemprop="jobTitle"><g:message code="footer.menu.footerTeam.position9"/></h2>--}%
                %{--<p itemprop="description"><g:message code="footer.menu.footerTeam.description9"/></p>--}%
                %{--<p itemprop="description"><g:message code="footer.menu.footerTeam.description9b"/></p>--}%
                %{--<ul class="social">--}%
                    %{--<li><a href="#" target="_blank"><span class="fa fa-twitter fa-2x"></span> <span class="sr-only">Twitter</span></a></li>--}%
                    %{--<li><a href="https://www.linkedin.com/pub/carmen-bernardo-garcía/5/b23/201/es" target="_blank"><span class="fa fa-linkedin fa-2x"></span> <span class="sr-only">LinkedIn</span></a></li>--}%
                %{--</ul>--}%
            %{--</div>--}%
        %{--</li>--}%
        %{--<li itemscope itemtype="http://schema.org/Person">--}%
            %{--<div class="box-ppal">--}%
                %{--<img src="${resource(dir: 'images', file: 'foto-carolina.jpeg')}" alt="Foto" itemprop="image">--}%
                %{--<h1 itemprop="name"><g:message code="footer.menu.footerTeam.member10"/></h1>--}%
                %{--<h2 itemprop="jobTitle"><g:message code="footer.menu.footerTeam.position10"/></h2>--}%
                %{--<p itemprop="description"><g:message code="footer.menu.footerTeam.description10"/></p>--}%
                %{--<p itemprop="description"><g:message code="footer.menu.footerTeam.description10b"/></p>--}%
                %{--<ul class="social">--}%
                    %{--<li><a href="https://twitter.com/CarolinahCoach" target="_blank"><span class="fa fa-twitter fa-2x"></span> <span class="sr-only">Twitter</span></a></li>--}%
                    %{--<li><a href="https://es.linkedin.com/pub/carolina-hernández/1a/56b/548" target="_blank"><span class="fa fa-linkedin fa-2x"></span> <span class="sr-only">LinkedIn</span></a></li>--}%
                %{--</ul>--}%
            %{--</div>--}%
        %{--</li>--}%
    </ul>
</content>
