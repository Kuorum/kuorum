<article itemtype="http://schema.org/Article" itemscope role="article" class="kakareo post ley">
    <div class="link-wrapper">
        <g:link mapping="lawShow" class="hidden" params="${solrLaw.encodeAsLinkProperties()}"><g:message code="search.list.law.goTo" args="[solrLaw.hashtag]"/></g:link>
        <div class="photo">
            <img src="${solrLaw.urlImage}" alt="${solrLaw.hashtag}" itemprop="image">
        </div>
        <div class="laley"><g:link mapping="lawShow" itemprop="keywords" params="${solrLaw.encodeAsLinkProperties()}">${raw(solrLaw.highlighting.hashtag)}</g:link></div>
        <h1>${raw(solrLaw.highlighting.name)}</h1>
        <p>${raw(solrLaw.highlighting.text)}</p>
    </div>
</article>

