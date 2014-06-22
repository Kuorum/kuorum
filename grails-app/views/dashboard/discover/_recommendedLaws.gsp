<section class="col-xs-12 col-sm-4 col-md-4 laws">
    <h1><g:message code="discover.module.recommendedLaws.title"/></h1>
    <ul>
        <g:each in="${recommendedLaws}" var="law">
            <li>
                <article class="kakareo post ley" role="article" itemscope="" itemtype="http://schema.org/Article">
                    <div class="wrapper">
                        <div class="photo">
                            <img itemprop="image" alt="${law.image.alt}" src="${law.image.url}">
                        </div>
                        <div class="laley">
                            <g:link mapping="lawShow" params="${law.encodeAsLinkProperties()}" itemprop="keywords" >
                                ${law.hashtag}
                            </g:link>
                        </div>
                        <h1>${law.shortName}</h1>
                        <g:set var="intro" value="${law.introduction.encodeAsRemovingHtmlTags()}"/>
                        <p>${intro.substring(0,Math.min(56, intro.size()-1))}...</p>
                    </div>
                </article>
            </li>
        </g:each>
    </ul>
</section>