<article class="kakareo post ley" role="article" itemtype="http://schema.org/Article">
    <g:if test="${!hideCallMobileVoteButton}">
        <div class="callMobile"><a href="#vote" class="smooth"><g:message code="project.vote.mobileButton"/> </a></div>
    </g:if>
    <h1>${project.shortName}</h1>

    <div class="row">
        <div class="col-sm-6 col-md-5 user author" itemprop="author" itemtype="http://schema.org/Person">
            <userUtil:showUser user="${project.owner}"/>
        </div>
        <div class="col-sm-6 col-md-7 text-right">
            <div itemprop="keywords" class="laley">${project.hashtag}</div>
        </div>
    </div>
    <div class="photo">
        <img itemprop="image" alt="${project.hashtag}" src="${project.image.url}">
    </div>
    %{--<g:if test="${project.image}">--}%
    %{--<g:render template="/project/projectPhoto" model="[project:project, victory:victories]"/>--}%
    %{--</g:if>--}%
    <div class="row options">
        <ul class="col-xs-12 col-sm-4 col-md-4 typeTime">
            <li>
                <span class="icon2-ciudad fa-lg" data-toggle="tooltip" data-placement="bottom" title="" rel="tooltip" data-original-title="Ámbito local"></span>
                <span class="sr-only">Ámbito local</span>

                <!-- <span class="fa icon2-region fa-lg" data-toggle="tooltip" data-placement="bottom" title="" rel="tooltip" data-original-title="Ámbito regional"></span>
                                    <span class="sr-only">Ámbito regional</span> -->

                <!-- <span class="fa icon2-estado fa-lg" data-toggle="tooltip" data-placement="bottom" title="" rel="tooltip" data-original-title="Ámbito estatal"></span>
                                    <span class="sr-only">Ámbito estatal</span> -->

                <!-- <span class="icon2-europe fa-lg" data-toggle="tooltip" data-placement="bottom" title="" rel="tooltip" data-original-title="Ámbito supranacional"></span>
                                    <span class="sr-only">Ámbito supranacional</span> -->
            </li>
            <li itemprop="datePublished">
                <time>cerrado <span class="hidden-sm">hace 15 días</span></time>
            </li>
        </ul>
        %{--<div class="col-xs-12 col-sm-6 col-md-6 editPost">--}%
            %{--<!-- dejar la estructura aunque a veces esté vacío  -->--}%
            %{--<projectUtil:ifProjectIsEditable project="${project}">--}%
                %{--<div id="adminActions">--}%
                    %{--<span class="text">--}%
                        %{--<g:link mapping="adminEditProject" params="${project.encodeAsLinkProperties()}">--}%
                            %{--<span class="fa fa-edit fa-lg"></span><g:message code="admin.menu.editProject.link"/></g:link>--}%
                    %{--</span>--}%
                %{--</div>--}%
            %{--</projectUtil:ifProjectIsEditable>--}%
        %{--</div>--}%
        %{--<g:if test="${project.urlPdf}">--}%
            %{--<div class="col-xs-12 col-sm-6 col-md-6 leerLey">--}%
                %{--<a href="${project.urlPdf}" target="_blank">--}%
                    %{--<g:message code="project.showCompleteProject"/>--}%
                %{--</a>--}%
            %{--</div>--}%
        %{--</g:if>--}%
    </div>
    <p class="cl-ntral-dark">${project.realName}</p>
    <p>
        ${raw(project.introduction.replaceAll('\n','</p><p>'))}
    </p>
    <g:if test="${readMore}">
        <div id="collapse" class="panel-collapse collapse">
            <p>
                ${raw(project.description.replaceAll('\n','</p><p>'))}
            </p>
        </div>
        <div class="readMore">
            <a data-toggle="collapse" data-parent="#accordion" href="#collapse" class="collapsed">
                <g:message code="project.text.seeMore"/>
            </a>
        </div>
    </g:if>
    <g:if test="${linkToProject}">
        <p class="linkToProject">
            <g:link mapping="projectShow" params="${project.encodeAsLinkProperties()}">[ver esta ley]</g:link>
        </p>
    </g:if>
</article><!-- /article -->