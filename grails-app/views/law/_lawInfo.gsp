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
            <lawUtil:ifLawIsEditable law="${law}">
                <div id="adminActions">
                    <span class="text">
                        <g:link mapping="adminEditLaw" params="${law.encodeAsLinkProperties()}">
                            <span class="fa fa-edit fa-lg"></span><g:message code="admin.menu.editLaw.link"/></g:link>
                    </span>
                </div>
            </lawUtil:ifLawIsEditable>
        </div>
        <g:if test="${law.urlPdf}">
            <div class="col-xs-12 col-sm-6 col-md-6 leerLey">
                <a href="${law.urlPdf}" target="_blank">
                    <g:message code="law.showCompleteLaw"/>
                </a>
            </div>
        </g:if>
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
            </a>
        </div>
    </g:if>
    <g:if test="${linkToLaw}">
        <p class="linkToLaw">
            <g:link mapping="lawShow" params="${law.encodeAsLinkProperties()}">[ver esta ley]</g:link>
        </p>
    </g:if>
</article><!-- /article -->