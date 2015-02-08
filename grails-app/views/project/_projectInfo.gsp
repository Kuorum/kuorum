<article class="kakareo post ley" role="article" itemtype="http://schema.org/Article">
    <g:render template="/project/projectOwnerMenu" model="[project:project]"/>
    %{--<g:if test="${!hideCallMobileVoteButton}">--}%
        %{--<div class="callMobile"><a href="#vote" class="smooth"><g:message code="project.vote.mobileButton"/> </a></div>--}%
    %{--</g:if>--}%
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
                <projectUtil:showProjectRegionIcon project="${project}"/>
            </li>
            <li itemprop="datePublished">
                <time>cerrado <span class="hidden-sm">hace 15 días</span></time>
            </li>
        </ul>
        <g:render template="projectStats" model="[project:project, regionStats:regionStats, projectStats:projectStats]"/>
    </div>
    <p>
        ${raw(project.description.replaceAll('\n','</p><p>'))}
    </p>
</article><!-- /article -->