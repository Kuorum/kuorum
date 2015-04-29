<g:set var="imageToShow" value="${project.image?:''}"/>
<g:set var="youtubeToShow" value="${project.urlYoutube?:''}"/>
<g:set var="action" value="${g.message(code: 'projectEvent.header.projectAction.PROJECT_CREATED')}"/>
<g:set var="dateCreated" value="${project.dateCreated}"/>
<g:set var="description" value="${project.description}"/>
<g:if test="${projectUpdate}">
    <g:set var="imageToShow" value="${projectUpdate.image?:''}"/>
    <g:set var="youtubeToShow" value="${projectUpdate.urlYoutube?:''}"/>
    <g:set var="action" value="${g.message(code: 'projectEvent.header.projectAction.PROJECT_UPDATE')}"/>
    <g:set var="dateCreated" value="${projectUpdate.dateCreated}"/>
    <g:set var="description" value="${projectUpdate.description}"/>
</g:if>
<article class="kakareo post ley" role="article" itemtype="http://schema.org/Article">
    <div class="link-wrapper">
        <g:link mapping="projectShow" params="${project.encodeAsLinkProperties()}" class="hidden"/>
        <div class="user author" itemprop="author" itemtype="http://schema.org/Person">
            <userUtil:showUser user="${project.owner}"/>
            <span itemprop="datePublished">
                <time><span class="hidden-xs hidden-md">${action}</span> <kuorumDate:humanDate date="${dateCreated}"/></time>
            </span>
        </div>
        <g:if test="${projectUpdate}">
            <div class="ico-info">
                <span data-original-title="Actualización" rel="tooltip" data-placement="left" data-toggle="tooltip" class="fa icon2-update fa-2x"></span>
                <span class="sr-only"><g:message code="project.projectUpdate.label"/> </span>
            </div>
        </g:if>
        <g:render template="/project/projectMultimedia" model="[hashtag:project.hashtag, projectImage:imageToShow, youtube:youtubeToShow]"/>
        <g:render template="/modules/projects/projectOnListMoreInfo" model="[project:project]"/>
        <p><kuorumDate:showShortedText text="${description}" numChars="170"/></p>
        <g:render template="/modules/projects/projectOnListFooter" model="[project:project]"/>
    </div>
</article>