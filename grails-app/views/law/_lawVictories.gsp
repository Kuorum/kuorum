<aside class="seeVictory" id="seeVictory">
    <h1><g:message code="law.modules.victories.title"/></h1>
    <p><g:message code="law.modules.victories.subtitle"/></p>
    <strong>¡Hemos conseguido <span class="counter">${victories.size()}</span> victorias!</strong>

    <div class="controls">
        <a href="#victory" data-slide="prev">
            <span class="fa-stack fa-lg">
                <i class="fa fa-square fa-stack-2x"></i>
                <i class="fa fa-caret-left fa-stack-1x fa-inverse"></i>
            </span>
        </a>
        <span class="indexCounter"><span class='carousel-index'><span class="actual"></span> de <span class="total"></span></span> victorias</span> <!-- aquí se genere al índice por js-->
        <a href="#victory" data-slide="next">
            <span class="fa-stack fa-lg">
                <i class="fa fa-square fa-stack-2x"></i>
                <i class="fa fa-caret-right fa-stack-1x fa-inverse"></i>
            </span>
        </a>
    </div>

    <div id="victory" class="carousel slide">
        <div class="carousel-inner">

            <g:each in="${victories}" var="post">
                <div class="item">
                    <div class="sliderItem clearfix">
                        <h1>¡<g:link mapping="userShow" params="${post.owner.encodeAsLinkProperties()}">${post.owner.name}</g:link>  lo consiguió! Tú también puedes hacerlo.</h1>
                        <g:if test="${post.multimedia}">
                            <img alt="${post.multimedia.alt}" src="${post.multimedia.url}" class="actor">
                        </g:if>
                        <div class="content">
                            <p>Etiam vel lacus sed velit fringilla porta in id tortor. Donec libero eros, tristique id scelerisque eu, lobortis et est. Nlacus sed velitsas</p> <!-- 140 caracteres -->
                            <p><strong>y 3 días después</strong></p>
                            %{--<userUtil:showUser user="${post.defender}"/>--}%
                            <div class="user" itemscope itemtype="http://schema.org/Person">
                                <img src="${image.userImgSrc(user:post.defender)}" alt="${post.defender.name}" class="user-img" itemprop="image">
                                <span itemprop="name">${post.defender.name}</span> se comprometió a llevarla al congreso
                            </div>
                            <g:link mapping="postShow" params="${post.encodeAsLinkProperties()}">
                                Conoce toda la historia
                            </g:link>
                        </div>
                    </div>
                </div> <!-- slider -->
            </g:each>
        </div><!-- carousel inner -->

    </div><!-- carousel -->
</aside>