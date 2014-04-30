
<article itemtype="http://schema.org/Article" itemscope role="article" class="kakareo post">
    <div class="link-wrapper">
        <g:link mapping="userShow" class="hidden" params="${solrUser.encodeAsLinkProperties()}">Ir al usueario</g:link>
        <div class="popover-box">
            <div class="user" itemscope itemtype="http://schema.org/Person">
                <a href="#" itemprop="url">
                    <img src="${image.solrUserImgSrc(user:solrUser)}" alt="${solrUser.name}" class="user-img" itemprop="image"><span itemprop="name">${raw(solrUser.highlighting.name)}</span>
                </a>
                <span class="user-type">
                    <small>${userUtil.roleNameSolrUser(user:solrUser)}</small>
                </span>
            </div><!-- /user -->
            <button type="button" class="btn btn-blue btn-xs allow enabled" id="follow">Seguir</button>
            <div class="pull-right"><span class="fa fa-check-circle-o"></span> <small>te sigue</small></div>
            <ul class="infoActivity clearfix">
                <li><span class="fa fa-question-circle"></span> <span class="counter">31</span> <span class="sr-only">preguntas</span></li>
                <li><span class="fa fa-book"></span> 25 <span class="sr-only">historias</span></li>
                <li><span class="fa fa-lightbulb-o"></span> 58 <span class="sr-only">propuestas</span></li>
                <li><span class="fa fa-user"></span> <small><span class="fa fa-forward"></span></small> 458 <span class="sr-only">siguiendo</span></li>
                <li><span class="fa fa-user"></span> <small><span class="fa fa-backward"></span></small> 328 <span class="sr-only">seguidores</span></li>
                <li class="pull-right"><span class="sr-only">Verificada</span> <span class="fa fa-check"></span></li>
            </ul>
        </div>
    </div>
</article>