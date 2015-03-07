<h1 class="withArrow"><g:message code="createProject.rigthColumn.label" /> </h1>
<span class="arrow1"></span>
<section class="boxes info">
    <!-- ATENCIÓN: este article no lleva .link-wrapper ni el <a> hidden -->
    <article role="article" class="kakareo post ley">

        <div class="user author">
            <span>
                <image:showUserImage/>
                <g:message code="createProject.rigthColumn.author" />
            </span>
        </div>

        <div class="video">
            <span class="front">
                <span class="fa fa-play-circle fa-4x"></span>
                <img src="${resource(dir: 'images', file: 'img-post.jpg')}" alt="${message(code:"createProject.rigthColumn.img.video")}">
            </span>
        </div>

        <div class="more-info">
            <div class="row">
                <div class="col-xs-8 laley"><g:message code="createProject.rigthColumn.hashtag" /></div>
                <div class="col-xs-4 infoVotes text-right">
                    <span class="vote-yes">
                        <span>53%</span>
                        <span class="sr-only"><g:message code="createProject.rigthColumn.votes" /></span>
                        <span class="icon-smiley fa-lg"></span>
                    </span>
                </div>
            </div>
        </div>

        <p><g:message code="createProject.rigthColumn.explain.circumscription" /></p>

        <footer>
            <div class="row">
                <ul class="col-xs-5 col-sm-5 col-md-6 info-kak">
                    <li>
                        <span class="fa icon2-estado fa-lg"></span>
                        <span class="sr-only"><g:message code="createProject.rigthColumn.circumscription.scope" /></span>
                    </li>
                    <li class="hidden-xs hidden-sm">
                        <time><g:message code="createProject.rigthColumn.circumscription.daysLeft" /></time>
                    </li>
                </ul>
                <div class="col-xs-7 col-sm-7 col-md-6 voting">
                    <ul>
                        <li>
                            <a role="button" href="#"><span class="icon-smiley fa-lg"></span> <span class="sr-only"><g:message code="createProject.rigthColumn.circumscription.voteInFavour" /></span></a>
                        </li>
                        <li>
                            <a role="button" href="#"><span class="icon-sad fa-lg"></span> <span class="sr-only"><g:message code="createProject.rigthColumn.circumscription.voteAgainst" /></span></a>
                        </li>
                        <li>
                            <a role="button" href="#"><span class="icon-neutral fa-lg"></span> <span class="sr-only"><g:message code="createProject.rigthColumn.circumscription.abstention" /></span></a>
                        </li>
                        <li>
                            <a role="button" href="#" class="design active"><span class="fa fa-lightbulb-o fa-lg"></span> <span class="sr-only"><g:message code="createProject.rigthColumn.circumscription.suggest" /></span></a>
                        </li>
                    </ul>
                </div>
            </div>
        </footer>
    </article>
    <span class="arrow2"></span>
</section>
<h2 class="withArrow"><g:message code="createProject.rigthColumn.project.visibility" /><span class="arrow3"></span></h2>

<section class="boxes info">
    <!-- ATENCIÓN: este article no lleva .link-wrapper ni el <a> hidden -->
    <article role="article" class="kakareo">
        <h1><g:message code="createProject.rigthColumn.project.cluck.label" /></h1>
        <div class="main-kakareo row">
            <div class="col-xs-6 col-sm-12 col-md-6 user author">
                <span>
                    <img src="${resource(dir: 'images', file: 'user.jpg')}" class="user-img" alt="${message(code: "createProject.rigthColumn.project.cluck.photo.citizen")}"><span><g:message code="createProject.rigthColumn.project.cluck.citizen" /></span>
                </span>
            </div><!-- /autor -->
            <div class="col-xs-6 col-sm-12 col-md-6 text-right user victory">
                <g:message code="createProject.rigthColumn.project.cluck.support"/>
                <span>
                    <img class="user-img" alt="${message(code: "createProject.rigthColumn.project.support.photo")}" src="${image.loggedUserImgSrc()}">
                </span>
            </div><!-- /patrocinadores -->
        </div>
        <footer class="row">
            <ul class="col-xs-2 col-sm-2 col-md-4 info-kak">
                <li>
                    <projectUtil:showProjectRegionIcon region="${region}"/>
                </li>
            </ul>
            <ul class="col-xs-10 col-sm-10 col-md-8 actions-kak">
                <li class="read-later">
                    <a class="enabled" href="#">
                        <span class="fa fa-bookmark fa-lg"></span>
                        <span class="sr-only"><g:message code="cluck.footer.readLater" /></span>
                    </a>
                </li>
                <li class="like-number">
                    <span class="counter">32</span>
                    <a href="#">
                        <span class="fa fa-rocket fa-lg"></span>
                        <span class="sr-only"><g:message code="cluck.footer.vote" /></span>
                    </a>
                </li>
                <li class="kakareo-number">
                    <span class="counter">542</span>
                    <a href="#">
                        <span class="fa icon-megaphone fa-lg"></span>
                        <span class="sr-only"><g:message code="cluck.footer.cluckIt" /></span>
                    </a>
                </li>
                <li class="more-actions">
                    <span class="more-actions">
                        ... <span class="sr-only"><g:message code="cluck.footer.moreActions" /></span>
                    </span>
                </li>
            </ul>
        </footer>
    </article>
    <span class="arrow4"></span>
</section>

<h3 class="withArrow"><g:message code="createProject.rigthColumn.project.support.defend" /><span class="arrow5"></span></h3>

<section class="boxes info">
    <!-- ATENCIÓN: este article no lleva .link-wrapper ni el <a> hidden -->
    <article role="article" class="kakareo">
        <h1><g:message code="createProject.rigthColumn.project.cluck.victory" /></h1>
        <div class="main-kakareo row">
            <div class="col-xs-6 col-sm-12 col-md-6 user author">
                <span>
                    <img src="${resource(dir: 'images', file: 'user.jpg')}" class="user-img" alt="Tu fotografía"><span><g:message code="createProject.rigthColumn.project.cluck.citizen" /></span>
                </span>
            </div><!-- /autor -->
            <div class="col-xs-6 col-sm-12 col-md-6 text-right user victory">
                Victoria de
                <span>
                    <img class="user-img" alt="${message(code: "createProject.rigthColumn.project.cluck.photo.citizen")}" src="${image.loggedUserImgSrc()}">
                </span>
            </div><!-- /patrocinadores -->
        </div>
        <footer class="row">
            <ul class="col-xs-2 col-sm-2 col-md-4 info-kak">
                <li>
                    <projectUtil:showProjectRegionIcon region="${region}"/>
                </li>
            </ul>
            <ul class="col-xs-10 col-sm-10 col-md-8 actions-kak">
                <li class="read-later">
                    <a class="enabled" href="#">
                        <span class="fa fa-bookmark fa-lg"></span>
                        <span class="sr-only"><g:message code="cluck.footer.readLater" /></span>
                    </a>
                </li>
                <li class="like-number">
                    <span class="counter">32</span>
                    <a href="#">
                        <span class="fa fa-rocket fa-lg"></span>
                        <span class="sr-only"><g:message code="cluck.footer.vote" /></span>
                    </a>
                </li>
                <li class="kakareo-number">
                    <span class="counter">542</span>
                    <a href="#">
                        <span class="fa icon-megaphone fa-lg"></span>
                        <span class="sr-only"><g:message code="cluck.footer.cluckIt" /></span>
                    </a>
                </li>
                <li class="more-actions">
                    <span class="more-actions">
                        ... <span class="sr-only"><g:message code="cluck.footer.moreActions" /></span>
                    </span>
                </li>
            </ul>
        </footer>
    </article>
</section>