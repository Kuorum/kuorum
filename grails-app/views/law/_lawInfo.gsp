<article class="kakareo post ley" role="article" itemscope itemtype="http://schema.org/Article">
    <g:if test="${!hideCallMobileVoteButton}">
        <div class="callMobile"><a href="#vote" class="smooth"><g:message code="law.vote.mobileButton"/> </a></div>
    </g:if>
    <g:if test="${law.image}">
        <g:render template="/law/lawPhoto" model="[law:law, victory:victories]"/>
    </g:if>
    <div itemprop="keywords" class="laley">${law.hashtag}</div>
    <h1>${law.shortName}</h1>

    <div class="row options">
        <div class="col-xs-12 col-sm-6 col-md-6 editPost">
            <!-- dejar la estructura aunque a veces esté vacío  -->
        </div>
        <div class="col-xs-12 col-sm-6 col-md-6 leerLey">
            <a href="${law.urlPdf}" target="_blank">
                <g:message code="law.showCompleteLaw"/>
            </a>
        </div>
    </div>
    <p class="cl-ntral-dark">${law.realName}</p>
    <p>
        ${raw(law.introduction.replaceAll('\n','</p><p>'))}
    </p>
    <g:if test="${readMore}">
        <div id="collapse" class="panel-collapse collapse">
            <p>
                ${raw(law.description.replaceAll('\n','</p><p>'))}
            </p>
        </div>
        <div class="readMore">
            <a data-toggle="collapse" data-parent="#accordion" href="#collapse" class="collapsed">
                <g:message code="law.text.seeMore"/>
                <span class="fa fa-chevron-circle-down fa-lg"></span>
            </a>
        </div>
    </g:if>
    <g:if test="${linkToLaw}">
        <div class="linkToLaw">
            <g:link mapping="lawShow" params="${law.encodeAsLinkProperties()}">[ver esta ley]</g:link>
        </div>
    </g:if>
</article><!-- /article -->