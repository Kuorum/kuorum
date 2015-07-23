<div class="full-video">
    <video autoplay loop poster="images/home-video.png" id="bgvid">
        <source src="video/kuorum.webm" type="video/webm">
        <source src="video/kuorum.mp4" type="video/mp4">
    </video>
    <div class="container-fluid">
        <div class="row">
            <div class="col-sm-10 col-sm-offset-1 col-md-6 col-md-offset-1 col-lg-6 col-lg-offset-1">
                <h1><g:message code="landingPage.title"/></h1>
                <h2><g:message code="landingPage.subTitle.1"/> <br/> <g:message code="landingPage.subTitle.2"/></h2>
                <div class="play">
                    <a href="#" data-toggle="modal" data-target="#videoHome"><span class="fa fa-play-circle fa-3x"></span></a> <g:message code="landingPage.youtube.play"/>
                </div>
                <div id="videoHome" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="video" aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-body">
                                <button type="button" class="close" data-dismiss="modal" aria-label="${g.message(code: 'default.close')}"><span aria-hidden="true" class="fa fa-times"></span><span class="sr-only"><g:message code="default.close"/> </span></button>
                                <h3 class="sr-only modal-title" id="video"><g:message code="landingPage.youtube.label"/> </h3>
                                <iframe id="vimeoplayer" class="youtube" itemprop="video" src="//player.vimeo.com/video/134306325?api=1&amp;player_id=vimeoplayer&amp;autoplay=0&amp;color=ff9933&amp;title=0&amp;byline=0&amp;portrait=0" frameborder="0" webkitallowfullscreen mozallowfullscreen allowfullscreen></iframe>
                            </div>
                        </div>
                    </div>
                </div><!-- /#videoHome -->
            </div>
            <div class="col-sm-12 col-md-4">

                <div class="form-home-container">

                    <!-- este bloque es para Entrar -->
                    <g:include controller="login" action="homeLogin"/>
                    <!-- este bloque es para Registro -->
                    <g:render template="/layouts/registerForm" model="[registerCommand:command, formId:'sign']"/>

                    <div class="social-home-container">
                        <h3><g:message code="login.rrss.label"/> </h3>
                        <g:render template="/register/registerSocialButtons"/>
                        %{--<h3>Inicia sesión con tus redes sociales</h3>--}%
                        %{--<ul class="socialGo clearfix">--}%
                            %{--<li><a href="#" class="btn btn-lg tw"><span class="fa fa-twitter fa-lg"></span> Con Twitter</a></li>--}%
                            %{--<li><a href="#" class="btn btn-lg fb" onclick="$('#facebookLoginContainer a')[0].click(); return false;">--}%
                                %{--<span class="fa fa-facebook fa-lg"></span>--}%
                                %{--<g:message code="login.rrss.facebook"/></a></li>--}%
                        %{--</ul>--}%
                    </div>

                </div><!-- /.form-home-container -->

            </div>
        </div>
    </div>
</div>