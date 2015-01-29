
<g:each in="${projects}" var="project">
    <li>
        <article itemtype="http://schema.org/Article" itemscope role="article" class="kakareo post ley">
            <div class="link-wrapper">
                <g:link mapping="projectShow" class="hidden" params="${project.encodeAsLinkProperties()}"><g:message code="search.list.project.goTo" args="[project.hashtag]"/></g:link>
                <div class="photo">
                    <img src="${project.image.url}" alt="${project.hashtag}" itemprop="image">
                </div>
                <div class="laley"><g:link mapping="projectShow" itemprop="keywords" params="${project.encodeAsLinkProperties()}">${project.hashtag}</g:link></div>
                <h1>${project.shortName}</h1>
                <g:set var="intro" value="${project.introduction.encodeAsRemovingHtmlTags()} ${project.description.encodeAsRemovingHtmlTags()}"/>
                <p>${intro.substring(0,Math.min(300, intro.size()-1))}...</p>
            </div>
        </article>
    </li>
</g:each>