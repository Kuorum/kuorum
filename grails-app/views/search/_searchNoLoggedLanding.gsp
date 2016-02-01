<section id="main" role="main" class="landing search clearfix">
    <div class="full-video">
        <div class="container-fluid">
            <div class="row">
                <div class="col-sm-12">
                    <h1><g:message code="search.noLogged.landing.title"/></h1>
                    <h2><g:message code="search.noLogged.landing.subTitle"/></h2>
                    <g:link mapping="login" class="btn btn-white"><g:message code="login.head.register"/> </g:link>
                    <g:form mapping="searcherSearch" method="GET" name="findRepresentatives" id="findRepresentatives" class="form-inline searchRep" role="search">
                        <input type="hidden" value="POLITICIAN" name="type"/>
                        <div class="form-group">
                            <formUtil:input cssClass="form-control" labelCssClass="sr-only" command="${searchParams}" field="word"/>
                        </div>
                        <button type="submit" class="btn btn-blue"><g:message code="search.noLogged.landing.search"/></button>
                    </g:form>
                </div>
            </div>
        </div>
    </div>
</section>