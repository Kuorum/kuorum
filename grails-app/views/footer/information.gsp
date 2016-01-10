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
    <h1><g:message code="layout.footer.information"/></h1>
    <p><g:message code="footer.menu.footerInformation.description"/></p>
    <ul>
        <li><g:message code="footer.menu.footerInformation.journalistRelation1"/></li>
        <li><g:message code="footer.menu.footerInformation.journalistRelation2"/></li>
        <li><g:message code="footer.menu.footerInformation.journalistRelation3"/></li>
    </ul>
    <div class="video">
        <a href="#" class="front">
            <span class="fa fa-play-circle fa-4x"></span>
            <img src="https://i.vimeocdn.com/video/510864624_640x360.jpg">
            <!-- servir esta otra si el video no es alta resolucion -->
            <!-- <img src="http://img.youtube.com/vi/67cz-JGv5R4/mqdefault.jpg"> -->
        </a>
        <iframe class="youtube" itemprop="video" src="//player.vimeo.com/video/122122045?api=1&amp;player_id=vimeoplayer1&amp;autoplay=0&amp;color=ff9933&amp;title=0&amp;byline=0&amp;portrait=0" frameborder="0" webkitallowfullscreen mozallowfullscreen allowfullscreen></iframe>
    </div>
    </br>
    %{--<img src="${resource(dir: 'images', file: 'info-quekuorum.jpg')}" alt="foto-debate" itemprop="image">--}%
</content>
