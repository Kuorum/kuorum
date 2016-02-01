<section id="main" role="main" class="landing search clearfix">
    <div class="full-video">
        <div class="container-fluid">
            <div class="row">
                <div class="col-sm-12">
                    <h1>FIND YOUR REPRESENTATIVES</h1>
                    <h2>Follow politicians and keep up to date about their activity.</h2>
                    <a href="#" class="btn btn-white">Sign up</a>
                    <g:form mapping="searcherSearch" method="GET" name="findRepresentatives" id="findRepresentatives" class="form-inline searchRep" role="search">
                        <input type="hidden" value="POLITICIAN" name="type"/>
                        <div class="form-group">
                            <formUtil:input cssClass="form-control" labelCssClass="sr-only" command="${searchParams}" field="word"/>
                        </div>
                        <button type="submit" class="btn btn-blue">Find my representatives</button>
                    </g:form>
                </div>
            </div>
        </div>
    </div>
</section>