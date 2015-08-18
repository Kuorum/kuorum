<aside id="aside-ppal" class="col-xs-12 col-md-5" role="complementary">
    <div class="border">
        <h1 class="withArrow"><g:message code="funnel.successfulStories.leftColumn.title"/><span class="arrow1"></span></h1>
        <img src="${resource(dir: 'images', file: 'buyolo.jpg')}" alt="Federico Buyolo" class="center-block">
        <p class="first"><g:message code="funnel.successfulStories.leftColumn.description1"/><span class="arrow2"></span></p>
        <section class="boxes info">
            <!-- ATENCIÓN: este article no lleva .link-wrapper ni el <a> hidden -->
            <article role="article" class="kakareo">
                <h1><g:message code="funnel.successfulStories.leftColumn.proposal"/></h1>
                <div class="main-kakareo row">
                    <div class="col-xs-6 col-sm-12 col-md-6 user author">
                        <span>
                            <img src="${resource(dir: 'images', file: 'user.jpg')}" class="user-img" alt="Tu fotografía"><span><g:message code="funnel.successfulStories.leftColumn.author"/></span>
                        </span>
                    </div><!-- /autor -->
                    <div class="col-xs-6 col-sm-12 col-md-6 text-right user victory">
                        <g:message code="funnel.successfulStories.leftColumn.supported"/>
                        <span>
                            <img class="user-img" alt="Federico Buyolo" src="${resource(dir: 'images', file: 'buyolo.jpg')}">
                        </span>
                    </div><!-- /patrocinadores -->
                </div>
                <footer class="row">
                    <ul class="col-xs-2 col-sm-2 col-md-4 info-kak">
                        <li>
                            <span class="fa icon2-region fa-lg"></span>
                            <span class="sr-only"><g:message code="kuorum.core.model.RegionType.NATION"/></span>
                        </li>
                    </ul>
                    <ul class="col-xs-10 col-sm-10 col-md-8 actions-kak">
                        <li class="read-later">
                            <a class="enabled" href="#">
                                <span class="fa fa-bookmark fa-lg"></span>
                                <span class="sr-only"><g:message code="createProject.rigthColumn.project.cluck.readLater"/></span>
                            </a>
                        </li>
                        <li class="like-number">
                            <span class="counter">12</span>
                            <a href="#">
                                <span class="fa fa-rocket fa-lg"></span>
                                <span class="sr-only"><g:message code="createProject.rigthColumn.project.cluck.vote"/></span>
                            </a>
                        </li>
                        <li class="kakareo-number">
                            <span class="counter">54</span>
                            <a href="#">
                                <span class="fa icon-megaphone fa-lg"></span>
                                <span class="sr-only"><g:message code="createProject.rigthColumn.project.cluck"/></span>
                            </a>
                        </li>
                        <li class="more-actions">
                            <span class="more-actions">
                                ... <span class="sr-only"><g:message code="createProject.rigthColumn.project.cluck.moreActions"/></span>
                            </span>
                        </li>
                    </ul>
                </footer>
            </article>
            <span class="arrow3"></span>
        </section>
        <p class="second"><g:message code="funnel.successfulStories.leftColumn.description2"/><span class="arrow4"></span></p>
        <section class="boxes info">
            <!-- ATENCIÓN: este article no lleva .link-wrapper ni el <a> hidden -->
            <article role="article" class="kakareo post ley">
                <div class="user author">
                    <span>
                        <img src="${resource(dir: 'images', file: 'buyolo.jpg')}" alt="Federico Buyolo" class="user-img"><span><g:message code="funnel.successfulStories.leftColumn.politician"/></span>
                    </span>
                    <span>
                        <time><span class="hidden-xs"><g:message code="project.projectUpdate.timeElapsedPrefix"/></span><g:message code="funnel.successfulStories.leftColumn.time"/></time>
                    </span>
                </div>

                <div class="ico-info">
                    <span class="fa icon2-update fa-2x"></span>
                    <span class="sr-only"><g:message code="project.projectUpdate.label"/></span>
                </div>

                <div class="video">
                    <a href="#" class="front">
                        <span class="fa fa-play-circle fa-4x"></span>
                        <img src="https://img.youtube.com/vi/67cz-JGv5R4/maxresdefault.jpg">
                        <!-- servir esta otra si el video no es alta resolucion -->
                        <!-- <img src="http://img.youtube.com/vi/67cz-JGv5R4/mqdefault.jpg"> -->
                    </a>
                    <iframe class="youtube" itemprop="video" src="https://www.youtube.com/embed/67cz-JGv5R4?rel=0&amp;showinfo=0&amp;showsearch=0" frameborder="0" allowfullscreen></iframe>
                </div>

                <div class="more-info">
                    <div class="row">
                        <div class="col-xs-8 laley"><g:message code="funnel.successfulStories.leftColumn.projectHashtag"/></div>
                        <div class="col-xs-4 infoVotes text-right">
                            <span class="vote-yes">
                                <span>63%</span>
                                <span class="sr-only"><g:message code="project.subHeader.positiveVotes"/></span>
                                <span class="icon-smiley fa-lg"></span>
                            </span>
                        </div>
                    </div>
                </div>
                <p><g:message code="funnel.successfulStories.leftColumn.projectUpdate"/></p>
                <footer>
                    <div class="row">
                        <ul class="col-xs-5 col-sm-5 col-md-6 info-kak">
                            <li>
                                <span class="fa icon2-estado fa-lg"></span>
                                <span class="sr-only"><g:message code="kuorum.core.model.RegionType.NATION"/></span>
                            </li>
                        </ul>
                        <ul class="col-xs-7 col-sm-7 col-md-6 voting">
                            <li><g:message code="project.subHeader.closedProject"/></li>
                        </ul>

                    </div>
                </footer>
            </article>
            <span class="arrow5"></span>
        </section>
    </div>
</aside>


