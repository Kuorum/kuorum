<article class="kakareo post ley actualizacion" role="article" itemtype="http://schema.org/Article">
    <div class="user author" itemprop="author" itemtype="http://schema.org/Person">
        <userUtil:showUser user="${project.owner}" showRole="false"/>
        <span itemprop="datePublished">
            <time class="hidden-xs hidden-md"><g:message code="project.projectUpdate.timeElapsedPrefix"/> </time>
            <kuorumDate:humanDate date="${projectUpdate.dateCreated}"/>
            %{--<time><span class="hidden-xs hidden-md">ha actualizado </span>hace 35 <abbr title="minutos">min.</abbr></time>--}%
        </span>
    </div>

    <div class="ico-info">
        <span data-original-title="Actualización" rel="tooltip" data-placement="left" data-toggle="tooltip" class="fa icon2-update fa-2x"></span>
        <span class="sr-only"><g:message code="project.projectUpdate.label"/></span>
    </div>
    <g:set var="imageToShow" value="${projectUpdate.image?:''}"/>
    <g:set var="youtubeToShow" value="${projectUpdate.urlYoutube?:''}"/>
    <g:render template="/project/projectMultimedia" model="[hashtag:project.hashtag, projectImage:imageToShow, youtube:youtubeToShow]"/>
    <p>
        ${raw(projectUpdate.description.replaceAll('\n','</p><p>'))}
    </p>
</article>