<article itemtype="http://schema.org/Article" itemscope role="article" class="kakareo post ley">
    <div class="link-wrapper">
        <g:link mapping="lawShow" class="hidden" params="${solrLaw.encodeAsLinkProperties()}"><g:message code="search.list.law.goTo" args="[solrLaw.hashtag]"/></g:link>
        <div class="photo">
            <img src="${solrLaw.urlImage}" alt="${solrLaw.hashtag}" itemprop="image">
        </div>
        <div class="laley"><g:link mapping="lawShow" itemprop="keywords" params="${solrLaw.encodeAsLinkProperties()}"><searchUtil:highlightedField solrElement="${solrLaw}" field="hashtag"/></g:link></div>
        <h1><searchUtil:highlightedField solrElement="${solrLaw}" field="name"/></h1>
        <p><searchUtil:highlightedField solrElement="${solrLaw}" field="text"/></p>
    </div>
</article>

