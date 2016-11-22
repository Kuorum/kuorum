<li class="more-actions">
    <span class="popover-trigger more-actions" rel="popover" role="button" data-toggle="popover">
        ...
        <span class="sr-only"><g:message code="cluck.footer.moreActions"/></span>
    </span>
    <!-- POPOVER MÁS ACCIONES -->
    <div class="popover">
        <button type="button" class="close" aria-hidden="true"  data-dismiss="popover"><span class="fa fa-times-circle-o fa"></span><span class="sr-only"><g:message code="kuorumUser.popover.close"/></span></button>
        <div class="popover-more-actions">
            <ul>
                <g:render template="/post/postSocialShareList" model="[post:post,showText:true, showIcon:false]"/>
                %{--<li><a href="#">Enviar por email</a></li>--}%
                %{--<li class="kakareo-number"><a href="#" class="action cluck">Kakarear</a></li>--}%
                %{--<li class="like-number"><a href="#" class="action drive">Impulsar</a></li>--}%
                %{--<li class="read-later"><a href="#" class="enabled allow">Leer más tarde</a></li>--}%
                <li class="mark"><a href="#" class="enabled allow"><g:message code="project.social.report"/></a></li>
            </ul>
        </div><!-- /popover-more-actions -->

    </div>
    <!-- FIN POPOVER MÁS ACCIONES -->
</li>