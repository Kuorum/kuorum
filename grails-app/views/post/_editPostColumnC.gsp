<h1 class="withArrow">¿Qué pasará con mi propuesta?</h1>
<span class="arrow1"></span>
<section class="boxes info">
    <!-- ATENCIÓN: este article no lleva .link-wrapper ni el <a> hidden -->
    <article role="article" class="kakareo">
        <h1><g:message code="post.edit.columnC.advises.firstFakePost.title"/></h1>
        <div class="main-kakareo row">
            <div class="col-xs-7 user author">
                <span>
                    <image:showUserImage/>
                </span>
            </div><!-- /autor -->
        </div>
        <footer class="row">
            <ul class="col-xs-2 col-sm-2 col-md-4 info-kak">
                <li>
                    <projectUtil:showProjectRegionIcon project="${project}"/>
                </li>
            </ul>
            <ul class="col-xs-10 col-sm-10 col-md-8 actions-kak">
                <li class="read-later">
                    <a class="enabled" href="#">
                        <span class="fa fa-bookmark fa-lg"></span>
                        <span class="sr-only"><g:message code="cluck.footer.readLater"/></span>
                    </a>
                </li>
                <li class="like-number">
                    <span class="counter">32</span>
                    <a href="#">
                        <span class="fa fa-rocket fa-lg"></span>
                        <span class="sr-only"><g:message code="cluck.footer.vote"/> </span>
                    </a>
                </li>
                <li class="kakareo-number">
                    <span class="counter">542</span>
                    <a href="#">
                        <span class="fa icon-megaphone fa-lg"></span>
                        <span class="sr-only"><g:message code="cluck.footer.cluckIt"/></span>
                    </a>
                </li>
                <li class="more-actions">
                    <span class="more-actions">
                        ... <span class="sr-only"><g:message code="cluck.footer.moreActions"/></span>
                    </span>
                </li>
            </ul>
        </footer>
    </article>
    <span class="arrow2 propuesta"></span>
</section>
<h2 class="withArrow"><g:message code="post.edit.columnC.advises.interFakePost"/><span class="arrow3 propuesta"></span></h2>
<section class="boxes info">
    <!-- ATENCIÓN: este article no lleva .link-wrapper ni el <a> hidden -->
    <article role="article" class="kakareo">
        <h1><g:message code="post.edit.columnC.advises.secondFakePost.title"/></h1>
        <div class="main-kakareo row">
            <div class="col-xs-6 col-sm-12 col-md-6 user author">
                <span>
                    <image:showUserImage/>
                </span>
            </div><!-- /autor -->
            <div class="col-xs-6 col-sm-12 col-md-6 text-right user victory">
                Apadrinada por
                <span>
                    <img class="user-img" alt="Fotografía del usuario que apadrina" src="${image.solrUserImgSrc()}">
                </span>
            </div><!-- /patrocinadores -->
        </div>
        <footer class="row">
            <ul class="col-xs-2 col-sm-2 col-md-4 info-kak">
                <li>
                    <projectUtil:showProjectRegionIcon project="${project}"/>
                </li>
            </ul>
            <ul class="col-xs-10 col-sm-10 col-md-8 actions-kak">
                <li class="read-later">
                    <a class="enabled" href="#">
                        <span class="fa fa-bookmark fa-lg"></span>
                        <span class="sr-only"><g:message code="cluck.footer.readLater"/></span>
                    </a>
                </li>
                <li class="like-number">
                    <span class="counter">32</span>
                    <a href="#">
                        <span class="fa fa-rocket fa-lg"></span>
                        <span class="sr-only"><g:message code="cluck.footer.vote"/></span>
                    </a>
                </li>
                <li class="kakareo-number">
                    <span class="counter">542</span>
                    <a href="#">
                        <span class="fa icon-megaphone fa-lg"></span>
                        <span class="sr-only"><g:message code="cluck.footer.cluckIt"/></span>
                    </a>
                </li>
                <li class="more-actions">
                    <span class="more-actions">
                        ... <span class="sr-only"><g:message code="cluck.footer.moreActions"/></span>
                    </span>
                </li>
            </ul>
        </footer>
    </article>
</section>
</aside>