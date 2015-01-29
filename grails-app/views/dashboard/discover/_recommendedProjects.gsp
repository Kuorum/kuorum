<section class="col-xs-12 col-sm-4 col-md-4 projects">
    <h1><g:message code="discover.module.recommendedProjects.title"/></h1>
    <ul>
        <g:each in="${recommendedProjects}" var="project">
            <li>
                <article class="kakareo post ley" role="article" itemscope="" itemtype="http://schema.org/Article">
                    <div class="wrapper">
                        <div class="photo">
                            <img itemprop="image" alt="${project.image.alt}" src="${project.image.url}">
                        </div>
                        <div class="laley">
                            <g:link mapping="projectShow" params="${project.encodeAsLinkProperties()}" itemprop="keywords" >
                                ${project.hashtag}
                            </g:link>
                        </div>
                        <h1>${project.shortName}</h1>
                        <g:set var="intro" value="${project.introduction.encodeAsRemovingHtmlTags()}"/>
                        <p>${intro.substring(0,Math.min(46, intro.size()-1))}...</p>
                    </div>
                </article>
            </li>
        </g:each>
    </ul>
</section>