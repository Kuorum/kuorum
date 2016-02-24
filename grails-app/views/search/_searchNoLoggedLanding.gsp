<section id="main" role="main" class="landing search clearfix">
    <div class="full-video">
        <!-- Linkedin is not recovering properly the image -->
        <img src="${r.resource(dir:'images', file:'background-search.jpg')}" class="hide" itemprop="image"/>
        <div class="container-fluid">
            <div class="row">
                <div class="col-sm-12">
                    <h1 class="hidden-xs"><g:message code="search.noLogged.landing.title"/></h1>
                    <h1 class="hidden-sm hidden-md hidden-lg"><g:message code="search.noLogged.landing.title.short"/></h1>
                    <h2><g:message code="search.noLogged.landing.subTitle"/></h2>

                    <g:link mapping="register" class="btn btn-white"><g:message code="login.head.register"/> </g:link>
                    <g:form mapping="searcherLanding" method="GET" name="findRepresentatives" id="findRepresentatives" class="form-inline searchRep" role="search" fragment="results">
                        <div class="form-group">
                            <formUtil:input field="word" cssClass="form-control" command="${searchParams}" labelCssClass="sr-only" showLabel="true"/>
                        </div>
                        <button type="submit" class="btn btn-blue"><g:message code="search.noLogged.landing.search"/></button>
                    </g:form>
                </div>
            </div>
        </div>
    </div>
</section>