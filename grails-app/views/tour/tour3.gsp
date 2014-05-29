<g:applyLayout name="main">
%{--Es igual al layout de columnC pero cambia la cabecera--}%
    <head>
        <title><g:message code="tour.tour3.title"/></title>
    </head>

    <body itemscope itemtype="http://schema.org/WebPage">
    <g:render template="headTourFake" model="[user:user]"/>
    <div class="row main">
        <div class="container-fluid">
            <div class="row" >
                <section id="main" class="col-xs-12 col-sm-12 col-md-8" role="main">
                    <g:set var="important" value=""/>
                    <postUtil:ifIsImportant post="${post}">
                        <g:set var="important" value="important"/>
                    </postUtil:ifIsImportant>

                    <div class="author ${important}">
                        <postUtil:politiciansHeadPost post="${post}"/>
                        <article class="kakareo post ${multimedia}" role="article" itemscope itemtype="http://schema.org/Article" data-cluck-postId="${post.id}">
                            <div class="wrapper">
                                <g:render template="/cluck/cluckMain" model="[post:post]"/>
                            </div>
                            <g:render template="fakeFooterCluck" model="[post:post,displayingColumnC:false]"/>

                            <g:render template="/post/postBody" model="[post:post]"/>
                            <g:render template="/post/debates/postDebates" model="[post:post]"/>
                            <div class="wrapper">
                                <g:render template="/cluck/cluckUsers" model="[post:post]"/>
                            </div>
                            <g:render template="fakeFooterCluck" model="[post:post,displayingColumnC:false]"/>
                        </article><!-- /article -->
                    </div>
                    <g:render template="/post/relatedPosts" model="[relatedPosts:relatedPost]"/>
                    <g:render template="/post/postComments" model="[post:post]"/>

                </section>

                <aside class="col-xs-12 col-sm-12 col-md-4" role="complementary">
                    <section class="boxes noted likes ${important}">
                        <h1><g:message code="post.show.boxes.like.title"/></h1>
                        <p class="text-left"><g:message code="post.show.boxes.like.description"/> </p>
                        <g:render template="/post/likesContainer" model="[post:post]"/>
                        <div class="sponsor">
                            <userUtil:showListUsers users="${usersVotes}" visibleUsers="5" messagesPrefix="post.show.boxes.like.userList"/>
                        </div>
                        <form>
                            <a href="#" onclick="return false;"
                                    class="btn  ${important ?: 'btn-blue'} btn-lg btn-block"
                                    data-postId="${post.id}">
                                <g:message code="post.show.boxes.like.vote.button" encodeAs="raw"/>
                            </a>
                            <div class="form-group">
                                <label class="checkbox-inline">
                                    <input type="checkbox" name="anonymous"
                                           value="private"/>
                                    <g:message code="post.show.boxes.like.vote.anonymousCheckBoxLabel"/>
                                </label>
                            </div>
                        </form>

                        <g:render template="/post/postSocialShare" model="[post:post]"/>

                    </section>
                </aside>
            </div>
        </div><!-- /.conatiner-fluid -->
    </div><!-- /#main -->

    <g:render template="/layouts/footer/footer"/>

    <r:require module="tour"/>

    </body>

</g:applyLayout>