<section id="main" role="main" class="landing search clearfix">
    <div class="full-video">
        <div class="container-fluid">
            <div class="row">
                <div class="col-sm-12">
                    <h1><g:message code="search.noLogged.landing.title"/></h1>
                    <h2 id="results-tag"><g:message code="search.noLogged.landing.subTitle"/></h2>

                    <g:link mapping="login" class="btn btn-white"><g:message code="login.head.register"/> </g:link>
                    <g:form mapping="searcherLanding" method="GET" name="findRepresentatives" id="findRepresentatives" class="form-inline searchRep" role="search" fragment="results">
                        <div class="form-group">
                            <formUtil:input field="word" cssClass="form-control" command="${searchParams}"/>
                        </div>
                        <button type="submit" class="btn btn-blue"><g:message code="search.noLogged.landing.search"/></button>
                    </g:form>
                </div>
            </div>
        </div>
    </div>
</section>