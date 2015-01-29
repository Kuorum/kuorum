<div class="photo">
    <img itemprop="image" alt="${project.image.alt}" src="${project.image.url}">
    <div class="aboutProject">
        <div class="phase">
            <span class="circle"><g:message code="${kuorum.core.model.ProjectStatusType.class.name}.${project.status}.iconText"/></span>
            <span class="text"><g:message code="${kuorum.core.model.ProjectStatusType.class.name}.${project.status}"/></span>
        </div>
        <div class="country">
            <span class="circle">${project.region.iso3166_2.split('-').last()}</span>
            <span class="text">${project.region.name}</span>
        </div>
    </div>
    <g:if test="${victory}">
        <div class="info">
            <a href="#seeVictory" class="fa-stack fa-lg smooth" data-toggle="tooltip" data-placement="right" title="${message(code:'project.photo.victoryIcon')}" rel="tooltip">
                <span class="fa fa-circle fa-stack-2x"></span>
                <span class="fa icon-flag2 fa-stack-1x fa-inverse"></span>
            </a>
        </div>
    </g:if>
</div>