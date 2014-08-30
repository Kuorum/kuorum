
<g:each in="${laws}" var="law">
    <li>
        <article itemtype="http://schema.org/Article" itemscope role="article" class="kakareo post ley">
            <div class="link-wrapper">
                <g:link mapping="lawShow" class="hidden" params="${law.encodeAsLinkProperties()}"><g:message code="search.list.law.goTo" args="[law.hashtag]"/></g:link>
                <div class="photo">
                    <img src="${law.image.url}" alt="${law.hashtag}" itemprop="image">
                </div>
                <div class="laley"><g:link mapping="lawShow" itemprop="keywords" params="${law.encodeAsLinkProperties()}">${law.hashtag}</g:link></div>
                <h1>${law.shortName}</h1>
                <g:set var="intro" value="${law.introduction.encodeAsRemovingHtmlTags()} ${law.description.encodeAsRemovingHtmlTags()}"/>
                <p>${intro.substring(0,Math.min(300, intro.size()-1))}...</p>
            </div>
        </article>
    </li>
</g:each>