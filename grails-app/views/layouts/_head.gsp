

<header id="header" class="row" role="banner">
    <nav class="navbar" role="navigation">
        <div class="container-fluid">
            <g:render template="/layouts/brandAndLogo"/>

            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="navbar-collapse">
                <form id="search-form" class="navbar-form navbar-left" role="search">
                    <div class="input-group">
                        <div class="input-group-btn">
                            <button class="btn search" type="submit"><span class="fa fa-search"></span></button>
                        </div>
                        <input type="text" class="form-control" placeholder="En todo kuorum.org" name="srch-term" id="srch-term">
                        <a href="#" id="open-filter-search" class="dropdown-toggle" data-toggle="dropdown"><span class="sr-only">Filtra tu búsqueda</span> <span class="fa fa-caret-down fa-lg"></span></a>
                        <ul id="filters" class="dropdown-menu dropdown-menu-right" aria-labelledby="open-filter-search">
                            <li><a href="#" id="filtro01">En todo kuorum.org</a></li>
                            <li><a href="#" id="filtro02">En ... filtro 2</a></li>
                            <li><a href="#" id="filtro03">En ... filtro 3</a></li>
                        </ul>
                    </div>
                </form>
                <ul class="nav navbar-nav navbar-left">
                    <li class="underline"><a href="#" class="navbar-link"><!-- <span class="icon-binoculars"></span> --> <span class="binoculars">Descubre</span></a></li>
                </ul>



            <sec:ifLoggedIn>
                <g:include controller="layouts" action="userHead"/>
            </sec:ifLoggedIn>
            <sec:ifNotLoggedIn>
                <g:render template="/layouts/noLoggedHead"/>
            </sec:ifNotLoggedIn>

            </div><!-- /.navbar-collapse -->
        </div><!-- /.container-fluid -->
    </nav>
</header>