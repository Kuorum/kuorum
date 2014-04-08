<footer class="row">
    <ul class="col-xs-2 col-sm-2 col-md-4 info-kak">
        <li itemprop="keywords">
            <!-- en caso de ser Historia -->
            <span class="fa fa-book fa-lg"></span><!-- icono para historia -->
            <span class="sr-only">Esto es una Historia</span><!-- texto que explica el icono y no es visible -->

        <!-- en caso de ser Pregunta -->
        <!-- <span class="fa fa-question-circle fa-lg"></span>
                                            <span class="sr-only">Esto es una Pregunta</span> -->

        <!-- en caso de ser Propuesta -->
        <!-- <span class="fa fa-lightbulb-o fa-lg"></span>
                                            <span class="sr-only">Esto es una Propuesta</span> -->

        </li>
        <li class="hidden-xs hidden-sm" itemprop="datePublished">
            <kuorumDate:humanDate date="${cluck.post.dateCreated}"/>
        </li>
    </ul>
    <ul class="col-xs-10 col-sm-10 col-md-8 actions-kak">
        <li class="read-later">
            <a href="#">
                <span class="fa fa-bookmark fa-lg"></span>
                <span class="hidden-xs">Leer después</span>
            </a>
        </li>

        <li class="like-number">
            <span class="counter">3222</span>
            <meta itemprop="interactionCount" content="UserLikes:2"><!-- pasarle el valor que corresponda -->
            <a href="">
                <span class="fa fa-rocket fa-lg"></span>
                <span class="hidden-xs">Impulsar</span>
            </a>
        </li>

        <li class="kakareo-number">
            <span class="counter">5428</span>
            <a href="#">
                <span class="fa fa-bullhorn fa-lg"></span>
                <span class="hidden-xs">Kakarear</span>
            </a>
        </li>

        <li class="more-actions">
            <a href="#" class="popover-trigger" rel="popover" role="button" data-toggle="popover">
                <span class="fa fa-plus"></span> <span class="sr-only">Más acciones</span>
            </a>
        </li>
    </ul>
</footer>
