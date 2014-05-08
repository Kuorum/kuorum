<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title><g:message code="page.title.discover"/></title>
    <meta name="layout" content="normalLayout">
    <parameter name="extraCssContainer" value="discover" />
</head>


<content tag="mainContent">
    <div class="introDiscover">
        <p class="pre">Las leyes más polémicas en debate</p>
        <ul class="steps">
            <li class="active"><a href="#" class="badge">1</a></li>
            <li><a href="#" class="badge">2</a></li>
            <li><a href="#" class="badge">3</a></li>
            <li><a href="#" class="badge">4</a></li>
        </ul>
    </div>
    <section id="main" class="col-xs-12 col-sm-8 col-md-8" role="main">
        <g:set var="law" value="${relevantLaws[0]}"/>
        <g:render template="/law/lawInfo" model="[law:law, linkToLaw:true]"/>

    </section>
    <aside class="col-xs-12 col-sm-4 col-md-4" role="complementary">
        <modulesUtil:lawVotes law="${law}"/>
        <modulesUtil:lawActivePeople law="${law}"/>
        <modulesUtil:recommendedPosts law="${law}" title="${message(code:"modules.recommendedLawPosts.title")}"/>
    </aside>

    <aside class="moreActives" role="complementary">
        <h1><g:message code="discover.module.mostActiveUsers.title"/> </h1>
        <userUtil:showListUsers users="${mostActiveUsers}" visibleUsers="13" messagesPrefix="discover.module.mostActiveUsers.userList"/>
        <p><g:message code="discover.module.mostActiveUsers.footerText"/></p>
    </aside>
<aside class="row others">
    <section class="col-xs-12 col-sm-4 col-md-4 laws">
        <h1>Leyes de la semana</h1>
        <ul>
            <li>
                <article class="kakareo post ley" role="article" itemscope="" itemtype="http://schema.org/Article">
                    <div class="wrapper">
                        <div class="photo">
                            <img itemprop="image" alt="texto-alternativo-imagen" src="images/img-post.jpg">
                        </div>
                        <div class="laley"><a itemprop="keywords" href="">#laleyquesea</a></div>
                        <h1>Ut enim ad quis sit amet nostrud.</h1>
                        <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit...</p>
                    </div>
                </article>
            </li>
            <li>
                <article class="kakareo post ley" role="article" itemscope="" itemtype="http://schema.org/Article">
                    <div class="wrapper">
                        <div class="photo">
                            <img itemprop="image" alt="texto-alternativo-imagen" src="images/img-post.jpg">
                        </div>
                        <div class="laley"><a itemprop="keywords" href="">#laleyquesea</a></div>
                        <h1>Ut enim ad quis sit amet nostrud.</h1>
                        <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit...</p>
                    </div>
                </article>
            </li>
            <li>
                <article class="kakareo post ley" role="article" itemscope="" itemtype="http://schema.org/Article">
                    <div class="wrapper">
                        <div class="photo">
                            <img itemprop="image" alt="texto-alternativo-imagen" src="images/img-post.jpg">
                        </div>
                        <div class="laley"><a itemprop="keywords" href="">#laleyquesea</a></div>
                        <h1>Ut enim ad quis sit amet nostrud.</h1>
                        <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit...</p>
                    </div>
                </article>
            </li>
        </ul>
    </section>
    <modulesUtil:recommendedPosts numPost="2" title="${message(code:"modules.recommendedLawPosts.title")}" specialCssClass="col-xs-12 col-sm-4 col-md-4"/>
</aside>
</content>

