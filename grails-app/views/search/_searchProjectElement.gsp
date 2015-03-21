<projectUtil:projectFromSolr solrProject="${solrProject}" var="project"/>
<article class="kakareo post ley" role="article" itemtype="http://schema.org/Article">
    <div class="link-wrapper">
        <g:link mapping="projectShow" params="${solrProject.encodeAsLinkProperties()}" class="hidden"/>
        <div class="user author" itemprop="author" itemtype="http://schema.org/Person">
            <userUtil:showUser user="${solrProject}"/>
            <span itemprop="datePublished">
                <time><span class="hidden-xs hidden-md"><g:message code="cluck.header.action.CREATE"/> </span><kuorumDate:humanDate date="${project.dateCreated}"/></time>
            </span>
        </div>
        <g:render template="/project/projectMultimedia" model="[project:project]"/>
        <g:render template="/modules/projects/projectOnListMoreInfo" model="[project:project,hashtag:solrProject?.highlighting?.hashtag]"/>
        %{--<p><projectUtil:showFirstCharsFromDescription project="${project}" numChars="170"/></p>--}%
        <p><searchUtil:highlightedField solrElement="${solrProject}" field="text"/></p>
        <g:render template="/modules/projects/projectOnListFooter" model="[project:project]"/>
    </div>
</article>


%{--<div class="laley"><g:link mapping="projectShow" itemprop="keywords" params="${solrProject.encodeAsLinkProperties()}"><searchUtil:highlightedField solrElement="${solrProject}" field="hashtag"/></g:link></div>--}%
%{--<h1><searchUtil:highlightedField solrElement="${solrProject}" field="name"/></h1>--}%
%{--<p><searchUtil:highlightedField solrElement="${solrProject}" field="text"/></p>--}%