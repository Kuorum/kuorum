<article itemtype="http://schema.org/Article" itemscope role="article" class="kakareo post ley">
    <div class="link-wrapper">
        <g:link mapping="projectShow" class="hidden" params="${solrProject.encodeAsLinkProperties()}"><g:message code="search.list.project.goTo" args="[solrProject.hashtag]"/></g:link>
        <div class="photo">
            <img src="${solrProject.urlImage}" alt="${solrProject.hashtag}" itemprop="image">
        </div>
        <div class="laley"><g:link mapping="projectShow" itemprop="keywords" params="${solrProject.encodeAsLinkProperties()}"><searchUtil:highlightedField solrElement="${solrProject}" field="hashtag"/></g:link></div>
        <h1><searchUtil:highlightedField solrElement="${solrProject}" field="name"/></h1>
        <p><searchUtil:highlightedField solrElement="${solrProject}" field="text"/></p>
    </div>
</article>

