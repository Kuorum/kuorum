<div class="photo">
    <img itemprop="image" alt="${law.image.alt}" src="${law.image.url}">
    <div class="aboutLaw">
        <div class="phase">
            <span class="circle"><g:message code="${kuorum.core.model.LawStatusType.class.name}.${law.status}.iconText"/></span>
            <span class="text"><g:message code="${kuorum.core.model.LawStatusType.class.name}.${law.status}"/></span>
        </div>
        <div class="country">
            <span class="circle">${law.region.iso3166_2.split('-').last()}</span>
            <span class="text">${law.region.name}</span>
        </div>
        <sec:ifAnyGranted roles="ROLE_ADMIN">
            <div class="country">
                <span class="text">
                    <g:link mapping="adminEditLaw" params="${law.encodeAsLinkProperties()}">Editar</g:link>
                </span>
            </div>
        </sec:ifAnyGranted>
    </div>
    <g:if test="${victory}">
        <div class="info">
            <a href="#seeVictory" class="fa-stack fa-lg smooth" data-toggle="tooltip" data-placement="right" title="${message(code:'law.photo.victoryIcon')}" rel="tooltip">
                <span class="fa fa-circle fa-stack-2x"></span>
                <span class="fa icon-flag2 fa-stack-1x fa-inverse"></span>
            </a>
        </div>
    </g:if>
</div>