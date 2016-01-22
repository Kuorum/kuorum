<g:applyLayout name="main">

<head>
    <title><g:layoutTitle/></title>
    <g:layoutHead/>
</head>

<body>
<g:render template="/layouts/headLanding"/>
<div class="row main landing">

    <section id="main" role="main" class="landing clearfix">
        <div class="full-video">
            <video autoplay loop poster="images/background.png" id="bgvid">
                <source src="https://s3-eu-west-1.amazonaws.com/kuorumorg/static/video/landing.webm" type="video/webm">
                <source src="https://s3-eu-west-1.amazonaws.com/kuorumorg/static/video/landing.mp4" type="video/mp4">
            </video>

            <div class="container-fluid">
                <div class="row">
                    <div class="col-sm-12">
                        <h1>A NEW AGE IN POLITICS</h1>

                        <h2>Manage all communications with electors from one single platform.</h2>
                        <a href="#saveTime" class="btn btn-white smooth">How it works</a>

                        <form action="" method="post" name="freeTrial" id="freeTrial" class="form-inline" role="form">
                            <fieldset>
                                <div class="form-group">
                                    <label for="name" class="sr-only">Name</label>
                                    <input type="text" name="name" class="form-control input-lg" id="name" required placeholder="Name" aria-required="true">
                                </div>

                                <div class="form-group">
                                    <label for="email" class="sr-only">Email</label>
                                    <input type="email" name="email" class="form-control input-lg" id="email" required placeholder="Email" aria-required="true">
                                </div>
                                <!-- para el botón, lo que prefieras, <button> o <input>-->
                                <button type="submit" class="btn">Start your free trial</button>
                                <!--                                <input type="submit" class="btn" value="Start your free trial">-->
                            </fieldset>

                            <p>You are accepting the <a href="#" target="_blank">service conditions</a></p>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </section>
</div><!-- .main -->

<div class="row main special landing">
    <div class="container-fluid">
        <section role="complementary" class="homeSub logos">
            <h1>Alredy trusted by</h1>
            <ul class="clearfix">
                <li itemscope itemtype="http://schema.org/Organization">
                    <img itemprop="logo" src="images/organizations/logo-ibm.png" alt="IBM">
                    <span itemprop="name" class="sr-only">IBM</span>
                </li>
                <li itemscope itemtype="http://schema.org/Organization">
                    <img itemprop="logo" src="images/organizations/logo-jpmorgan.png" alt="JPMorgan">
                    <span itemprop="name" class="sr-only">JPMorgan</span>
                </li>
                <li itemscope itemtype="http://schema.org/Organization">
                    <img itemprop="logo" src="images/organizations/logo-labour.png" alt="Labour">
                    <span itemprop="name" class="sr-only">Labour</span>
                </li>
                <li itemscope itemtype="http://schema.org/Organization">
                    <img itemprop="logo" src="images/organizations/logo-eu.png" alt="EU">
                    <span itemprop="name" class="sr-only">EU</span>
                </li>
                <li itemscope itemtype="http://schema.org/Organization">
                    <img itemprop="logo" src="images/organizations/logo-everis.png" alt="Everis">
                    <span itemprop="name" class="sr-only">Everis</span>
                </li>

                <li itemscope itemtype="http://schema.org/Organization">
                    <img itemprop="logo" src="images/organizations/logo-conservatives.png" alt="Conservatives">
                    <span itemprop="name" class="sr-only">Conservatives</span>
                </li>
                <li itemscope itemtype="http://schema.org/Organization">
                    <img itemprop="logo" src="images/organizations/logo-save.png" alt="Save the Children">
                    <span itemprop="name" class="sr-only">Save the Children</span>
                </li>
                <li itemscope itemtype="http://schema.org/Organization">
                    <img itemprop="logo" src="images/organizations/logo-unicef.png" alt="Unicef">
                    <span itemprop="name" class="sr-only">Unicef</span>
                </li>
                <li itemscope itemtype="http://schema.org/Organization">
                    <img itemprop="logo" src="images/organizations/logo-psoe.png" alt="PSOE">
                    <span itemprop="name" class="sr-only">PSOE</span>
                </li>
                <li itemscope itemtype="http://schema.org/Organization">
                    <img itemprop="logo" src="images/organizations/logo-unltd.png" alt="UnLtd">
                    <span itemprop="name" class="sr-only">UnLtd</span>
                </li>
            </ul>
            <a href="#" class="open">Show more <span class="fa fa-angle-down"></span></a>
            <ul class="clearfix">
                <li itemscope itemtype="http://schema.org/Organization">
                    <img itemprop="logo" src="images/organizations/logo-lacaixa.png" alt="LaCaixa">
                    <span itemprop="name" class="sr-only">LaCaixa</span>
                </li>
                <li itemscope itemtype="http://schema.org/Organization">
                    <img itemprop="logo" src="images/organizations/logo-centralworking.png" alt="Central Working">
                    <span itemprop="name" class="sr-only">Central Working</span>
                </li>
                <li itemscope itemtype="http://schema.org/Organization">
                    <img itemprop="logo" src="images/organizations/logo-cesie.png" alt="Cesie">
                    <span itemprop="name" class="sr-only">Cesie</span>
                </li>
                <li itemscope itemtype="http://schema.org/Organization">
                    <img itemprop="logo" src="images/organizations/logo-techcity.png" alt="TechCityUK">
                    <span itemprop="name" class="sr-only">TechCityUK</span>
                </li>
                <li itemscope itemtype="http://schema.org/Organization">
                    <img itemprop="logo" src="images/organizations/logo-impacthub.png" alt="Impact Hub">
                    <span itemprop="name" class="sr-only">Impact Hub</span>
                </li>

                <li itemscope itemtype="http://schema.org/Organization">
                    <img itemprop="logo" src="images/organizations/logo-barclays.png" alt="Barclays">
                    <span itemprop="name" class="sr-only">Barclays</span>
                </li>
                <li itemscope itemtype="http://schema.org/Organization">
                    <img itemprop="logo" src="images/organizations/logo-santander.png" alt="Santander">
                    <span itemprop="name" class="sr-only">Santander</span>
                </li>
                <li itemscope itemtype="http://schema.org/Organization">
                    <img itemprop="logo" src="images/organizations/logo-unicef.png" alt="Socialiniu Inovaciju Fondas">
                    <span itemprop="name" class="sr-only">Socialiniu Inovaciju Fondas</span>
                </li>
                <li itemscope itemtype="http://schema.org/Organization">
                    <img itemprop="logo" src="images/organizations/logo-dotforge.png" alt="DOTFORGE">
                    <span itemprop="name" class="sr-only">DOTFORGE</span>
                </li>
                <li itemscope itemtype="http://schema.org/Organization">
                    <img itemprop="logo" src="images/organizations/logo-telefonica.png" alt="Telefónica">
                    <span itemprop="name" class="sr-only">Telefónica</span>
                </li>
            </ul>
        </section>
    </div>
</div><!-- .main.special.landing -->

<div class="row main special">
    <div class="container-fluid">
        <section role="complementary" class="homeSub" id="saveTime">
            <h1>Save time and get more value</h1>

            <div class="row">
                <ul class="saveTime">
                    <li class="col-sm-6 col-md-3">
                        <a href="#"><img src="images/dashboard.gif" alt=""></a>

                        <h2>Social Analytics</h2>

                        <p>Use all the power of social data analysis to understand public opinion trends.</p>
                        <a href="#">Learn more <span class="sr-only">about social analytics</span> <span class="fa fa-angle-right"></span></a>
                    </li>
                    <li class="col-sm-6 col-md-3">
                        <a href="#"><img src="images/semantic.gif" alt=""></a>

                        <h2>Semantic Analysis</h2>

                        <p>Choose the causes you care about and your inbox will be automatically sorted by issue.</p>
                        <a href="#">Learn more <span class="sr-only">about semantic analysis</span> <span class="fa fa-angle-right"></span></a>
                    </li>
                    <li class="col-sm-6 col-md-3">
                        <a href="#"><img src="images/organizer.gif" alt=""></a>

                        <h2>Mass Mailing</h2>

                        <p>Transform voters into volunteers and volunteers into donors. Segment to target your supporters.</p>
                        <a href="#">Learn more <span class="sr-only">about mass mailing</span> <span class="fa fa-angle-right"></span></a>
                    </li>
                    <li class="col-sm-6 col-md-3">
                        <a href="#"><img src="images/docman.gif" alt=""></a>

                        <h2>Team Management</h2>

                        <p>Follow your leaders' advice, report them about your progresses and assign roles to your staff.</p>
                        <a href="#">Learn more <span class="sr-only">about team management</span> <span class="fa fa-angle-right"></span></a>
                    </li>
                </ul>
            </div>
        </section>
    </div>
</div>

<div class="row main">
    <ul class="testimonies-list clearfix">
        <li>
            <blockquote itemtype="http://schema.org/Review" itemscope="">
                <footer>
                    <cite itemprop="publisher">
                        <img alt="Manuela Carmena" src="images/manuelaCarmena.jpg">
                    </cite>
                </footer>

                <div class="body">
                    <span class="sr-only" itemprop="itemReviewed">Kuorum.org</span>

                    <p itemprop="reviewBody">“I had a dream that something like Kuorum could exist”</p>

                    <p>Manuela Carmena - MAYOR OF MADRID</p>
                </div>
            </blockquote>
        </li>
        <li>
            <blockquote itemtype="http://schema.org/Review" itemscope="">
                <footer>
                    <cite itemprop="publisher">
                        <img alt="Manuela Carmena" src="images/davidBurrowes.jpg">
                    </cite>
                </footer>

                <div class="body">
                    <span class="sr-only" itemprop="itemReviewed">Kuorum.org</span>

                    <p itemprop="reviewBody">“Kuorum closes the gap between citizens and politicians”</p>

                    <p>David Burrowes - CONGRESMAN</p>
                </div>
            </blockquote>
        </li>
        <li>
            <blockquote itemtype="http://schema.org/Review" itemscope="">
                <footer>
                    <cite itemprop="publisher">
                        <img alt="Manuela Carmena" src="images/catSmith.jpg">
                    </cite>
                </footer>

                <div class="body">
                    <span class="sr-only" itemprop="itemReviewed">Kuorum.org</span>

                    <p itemprop="reviewBody">“Nobody believed that we could win that campaign but we did”</p>

                    <p>Cat Smith - MEMBER OF PARLIAMENT</p>
                </div>
            </blockquote>
        </li>
    </ul>
</div>

<div class="row main presskit">
    <div class="container-fluid">
        <section role="complementary">
            <div class="row">
                <div class="col-md-5 col-lg-6">
                    <h1>Discover how pioneers are shaping politics of the future with Kuorum.</h1>
                </div>

                <div class="col-md-7 col-lg-6">
                    <form action="" method="post" name="freeTrial" id="freeTrial" class="form-inline" role="form">
                        <fieldset>
                            <div class="form-group">
                                <label for="email-press" class="sr-only">Email</label>
                                <input type="email" name="email-press" class="form-control input-lg" id="email-press" required placeholder="Email" aria-required="true">
                            </div>
                            <!-- para el botón, lo que prefieras, <button> o <input>-->
                            <button type="submit" class="btn btn-blue btn-lg">Download press kit</button>
                            <!--                        <input type="submit" class="btn" value="Download press kit">-->
                        </fieldset>
                    </form>
                </div>
            </div>
        </section>
    </div>
</div>

<div class="row main special profile">
    <div class="container-fluid">
        <section class="homeSub" role="complementary">
            <h1>Create your profile in less than 1 minut</h1>
            <a href="#" class="btn btn-lg">Start your free trial</a>
        </section>
    </div>
</div>

<g:render template="/layouts/footer/footer"/>
</div><!-- .container-fluid -->
</body>
</g:applyLayout>