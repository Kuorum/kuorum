<article class="kakareo post ley" role="article" itemtype="http://schema.org/Article">
    <div class="link-wrapper">
        <g:link mapping="projectShow" params="${project.encodeAsLinkProperties()}" class="hidden"/>
        <div class="user author" itemprop="author" itemtype="http://schema.org/Person">
            <userUtil:showUser user="${project.owner}"/>
            <span itemprop="datePublished">
                <time><span class="hidden-xs hidden-md"><g:message code="cluck.header.action.CREATE"/> </span><kuorumDate:humanDate date="${project.dateCreated}"/></time>
            </span>
        </div>
        <g:render template="/project/projectMultimedia" model="[project:project]"/>
        <g:render template="/modules/projects/projectOnListMoreInfo" model="[project:project]"/>
        <p><projectUtil:showFirstCharsFromDescription project="${project}" numChars="170"/></p>
        <g:render template="/modules/projects/projectOnListFooter" model="[project:project]"/>
    </div>
</article>