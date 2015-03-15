<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.footer.information"/> </title>
    <meta name="layout" content="leftMenuLayout">
    <parameter name="extraCssContainer" value="info" />
</head>

<content tag="leftMenu">
    <g:render template="leftMenuPress" model="[activeMapping:'footerInformation']"/>
</content>

<content tag="mainContent">
    <div class="box-ppal">
        <h1><g:message code="layout.footer.information"/></h1>
        <p>
            <g:message code="footer.menu.footerInformation.description1"/>
        </p>
        <p>
            <g:message code="footer.menu.footerInformation.description2"/>
        </p>
        <p>
            <g:message code="footer.menu.footerInformation.description3"/>
        </p>
        <div class="video">
            <a href="#" class="front">
                <span class="fa fa-play-circle fa-4x"></span>
                <img src="images/info-press.png">
                <!-- servir esta otra si el video no es alta resolucion -->
                <!-- <img src="http://img.youtube.com/vi/67cz-JGv5R4/mqdefault.jpg"> -->
            </a>
            <iframe class="youtube" itemprop="video" src="//player.vimeo.com/video/122122045?api=1&amp;player_id=vimeoplayer1&amp;autoplay=0&amp;color=ff9933&amp;title=0&amp;byline=0&amp;portrait=0" frameborder="0" webkitallowfullscreen mozallowfullscreen allowfullscreen></iframe>
        </div>
        </br>
        %{--<img src="${resource(dir: 'images', file: 'info-quekuorum.jpg')}" alt="foto-debate" itemprop="image">--}%
    </div>
</content>
