<%@ page import="kuorum.core.model.UserType" %>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <title>${politician.name}</title>
    <meta name="layout" content="columnCLayout">
    <parameter name="specialContainerCssClass" value="userprofile" />
    <g:render template="userMetaTags" model="[user:politician]"/>
    <r:require modules="newStyle" />
</head>


<content tag="subHeader">
</content>

<content tag="mainContent">
    <div class="panel panel-default">
        <div class='card-header' id='obama-header' style="background: url(${image.userImgProfile(user:politician)}) 50% 50% no-repeat;">
            <userUtil:followButton user="${politician}">
                <i class="fa fa-plus"></i>
            </userUtil:followButton>
        </div>
        <div class="panel-body">
            <div class="row">
                <div class="col-xs-12 col-sm-3 profile-pic-col">
                    <div class="profile-pic">
                        <img alt="${politician.name}"
                             class="img-circle"
                             data-src="holder.js/140x140"
                             src="${image.userImgSrc(user:politician)}"
                             data-holder-rendered="true">
                        <i class="fa fa-check"></i>
                    </div>

                </div>
                <div class='col-xs-7 col-sm-5 profile-title'>
                    <h2>${politician.name}</h2>
                    <cite>President, United States</cite>
                    <p class='party'>Democratic</p>
                </div>
                <div class="col-xs-5 col-sm-4 following">
                    <strong>Followers 58K</strong><br/>
                    <i class="fa fa-check-circle-o"></i>following you
                </div>
            </div><!--/.row -->

            <div class="extra-padding">
                <h4>Causes</h4>
                <ul class='causes-tags'>
                    <li><a href="#">#anti-gun</a></li>
                    <li><a href="#">#healthcare</a></li>
                    <li><a href="#">#lovewins</a></li>
                    <li><a href="#">#universal education</a></li>
                    <li><a href="#">#tax the rich</a></li>
                </ul>
                <h4>Known For</h4>
                <ul class='known-for'>
                    <li><a href="#">Higher Education Opportunity Through Pell Grant Expansion Act</a></li>
                    <li><a href="#">E-85 Fuel Utilization and Infrastructure Development Incentives Act of 2005</a></li>
                    <li><a href="#">Hurricane Katrina Fast-Track Refunds for Working Families Act of 2005</a></li>
                    <li><a href="#">Innovation Districts for School Improvement Act</a></li>
                </ul>
                <h4>About</h4>
                <p>${politician.bio}</p>
            </div><!--/.extra-padding -->
        </div><!--/.panel-body-->
    </div><!--/.panel panel-default -->

    <div class="hidden-xs panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title">Latest Political Activity</h3>
        </div>
        <div class="panel-body">
            <table class="table panel-table">
                <thead>
                <tr>
                    <th>Date</th>
                    <th>Title</th>
                    <th>Action</th>
                    <th>Outcome</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>05 Sep 2015</td>
                    <td class='political-activity-title'><a href="#">H.R. 469 Strengthening Child Welfare Response to Trafficking Act of 2015</a></td>
                    <td>Sponsored</td>
                    <td>Approved</td>
                </tr>
                <tr>
                    <td>05 Sep 2015</td>
                    <td class="political-activity-title"><a href="#">H.Res. 64 Recognizing January 2015 as "National Mentoring Month".</a></td>
                    <td class="text-success">Voted for</td>
                    <td>Denied</td>
                </tr>
                <tr>
                    <td>05 Sep 2015</td>
                    <td class="political-activity-title"><a href="#">H.Res. 28 expressing the sense of the house of representatives that the united states postal service should take all appropriate measures to ensure the continuation of door delivery for all business and residential customers.</a></td>
                    <td class="text-danger">Voted Against</td>
                    <td>Approved</td>
                </tr>
                <tr>
                    <td>05 Sep 2015</td>
                    <td class="political-activity-title"><a href="#">Ongoing crisis in the agriculture sector</a></td>
                    <td>Debate</td>
                    <td>Pending</td>
                </tr>
                </tbody>
                <tfoot>
                <tr>
                    <td colspan="4" class="text-center"><a href="#" class="btn-primary btn-sm" role="button">See More</a></td>
                </tr>
                </tfoot>
            </table>
        </div><!--/.panel-body -->
    </div><!--/.panel.panel-default -->
</content>

<content tag="cColumn">
    <ul class="share-buttons hidden-xs">
        <li>
            <div class="tooltip left" role="tooltip">
                <div class="tooltip-arrow"></div>
                <div class="tooltip-inner">Share</div>
            </div>
        </li>
        <li><a href="https://www.facebook.com/sharer/sharer.php?u=&t=" target="_blank" title="Share on Facebook"><i class="fa fa-facebook-square"></i></a></li>
        <li><a href="https://twitter.com/intent/tweet?source=&text=:%20" target="_blank" title="Tweet"><i class="fa fa-twitter-square"></i></a></li>
        <li><a href="https://plus.google.com/share?url=" target="_blank" title="Share on Google+"><i class="fa fa-google-plus-square"></i></a></li>
        <li><a href="http://pinterest.com/pin/create/button/?url=&description=" target="_blank" title="Pin it"><i class="fa fa-pinterest-square"></i></a></li>
        <li><a href="http://www.reddit.com/submit?url=&title=" target="_blank" title="Submit to Reddit"><i class="fa fa-reddit-square"></i></a></li>
        <li><a href="http://www.linkedin.com/shareArticle?mini=true&url=&title=&summary=&source=" target="_blank" title="Share on LinkedIn"><i class="fa fa-linkedin-square"></i></a></li>
    </ul>

    <section class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title">Contact</h3>
        </div>
        <div class="panel-body text-center">
            <h3>Have something to tell Barack Obama?</h3>
            <a href="#" class=" btn-primary btn-lg" role="button">Contact Now</a>
        </div>
    </section>

    <section class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title">Political Leaning Index</h3>
        </div>
        <div class="panel-body text-center">
            <!-- <span class="badge badge-default">60%</span> -->
            <div class="tooltip top" role="tooltip" style="position:relative;opacity:1;left:60%;width: 3em;margin-left: -1.5em;margin-bottom: 2px;">
                <div class="tooltip-arrow"></div>
                <div class="tooltip-inner">60%</div>
            </div>
            <div class="progress" >
                <div class="progress-bar progress-bar-warning" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: 60%">
                    <span class="sr-only">60% Complete (warning)</span>
                </div>
            </div>
            <div class="clearfix">
                <span class="pull-left">left</span><span class="pull-right">right</span>
            </div>
            <div class="text-right"><a href="#">See how we calculate PLI...</a></div>
        </div>
    </section>

    <section class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title">Professional Details</h3>
        </div>
        <div class="panel-body text-center">
            <table class="table table-condensed">
                <tbody>
                <tr>
                    <th scope="row">Position</th>
                    <td>President</td>
                </tr>
                <tr>
                    <th scope="row">Institution</th>
                    <td>The White House</td>
                </tr>
                <tr>
                    <th scope="row">Constituency</th>
                    <td>N/A</td>
                </tr>
                <tr>
                    <th scope="row">Region</th>
                    <td>United States</td>
                </tr>
                </tbody>
            </table>
            <hr/>
            <table class="table table-condensed">
                <tbody>
                <tr>
                    <th scope="row">Profession</th>
                    <td>Professor</td>
                </tr>
                <tr>
                    <th scope="row">CV</th>
                    <td><a class="pdf" href="#">cv.pdf</a></td>
                </tr>
                <tr>
                    <th scope="row">Declaration</th>
                    <td><a href="#">example.com/declaration.pdf</a></td>
                </tr>
                </tbody>
            </table>
        </div>
    </section>

    <section class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title">Quick Notes</h3>
        </div>
        <div class="panel-body text-center">
            <table class="table table-condensed">
                <thead>
                <tr>
                    <th colspan="2">Background</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <th scope="row">Name</th>
                    <td>Barack Hussein Obama II</td>
                </tr>
                <tr>
                    <th scope="row">Age</th>
                    <td>54</td>
                </tr>
                <tr>
                    <th scope="row">Date of Birth</th>
                    <td>4 Aug 1961</td>
                </tr>
                <tr>
                    <th scope="row">Place of Birth</th>
                    <td>Honolulu, Hawai, U.S</td>
                </tr>
                </tbody>
                <tfoot>
                <tr>
                    <td colspan="2"class="text-right"><a href="#">read more...</a></td>
                </tr>
                </tfoot>
            </table>
            <hr/>
            <table class="table table-condensed">
                <thead>
                <tr>
                    <th colspan="2">Education</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <th scope="row">University</th>
                    <td>Havard Law School (J.D)
                        <br>
                        Columbia University (B.A)
                        <br>
                        Occidental College School
                    </td>
                </tr>
                <tr>
                    <th scope="row">School</th>
                    <td>Punahou School</td>
                </tr>
                </tbody>
                <tfoot>
                <tr>
                    <td colspan="2"class="text-right"><a href="#">read more...</a></td>
                </tr>
                </tfoot>
            </table>
            <hr/>
            <table class="table table-condensed">
                <thead>
                <tr>
                    <th colspan="2">More Information</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <th scope="row">Website</th>
                    <td><a href="#">barackobama.com</a></td>
                </tr>
                </tbody>
            </table>
            <hr/>
            <ul class="panel-share-buttons">
                <li><a href="https://twitter.com/intent/tweet?source=&text=:%20" target="_blank" title="Tweet"><i class="fa fa-twitter-square fa-2x"></i></a></li>
                <li><a href="https://www.facebook.com/sharer/sharer.php?u=&t=" target="_blank" title="Share on Facebook"><i class="fa fa-facebook-square fa-2x"></i></a></li>
                <li><a href="http://www.linkedin.com/shareArticle?mini=true&url=&title=&summary=&source=" target="_blank" title="Share on LinkedIn"><i class="fa fa-linkedin-square fa-2x"></i></a></li>
                <li><a href="http://www.reddit.com/submit?url=&title=" target="_blank" title="Submit to Reddit"><i class="fa fa-youtube-square fa-2x"></i></a></li>
            </ul>
        </div>
    </section>
</content>


<content tag="extraRowData">
    <div class="panel panel-default" id="political-experience">
        <div class="panel-heading">
            <h3 class="panel-title">Political Experience</h3>
        </div>
        <div class="panel-body text-center">
            <ul class="timeline">
                <li>
                    <div class="timeline-badge warning small"></div>
                    <div class="timeline-panel">
                        <div class="timeline-heading">
                            <h4 class="timeline-title">member of the illinois senate from the 13th district</h4>
                            <!--  <p class='updates-meta'><i class="glyphicon glyphicon-time"></i> 11 hours ago via Twitter</small></p> -->
                        </div>
                        <div class="timeline-body">
                            <p>Mussum ipsum cacilds, vidis litro abertis. Consetis adipiscings elitis. Pra lá , depois divoltis porris, paradis. Paisis, filhis, espiritis santis. Mé faiz elementum girarzis, nisi eros vermeio, in elementis mé pra quem é amistosis quis leo. Manduma pindureta quium dia nois paga. Sapien in monti palavris qui num significa nadis i pareci latim. Interessantiss quisso pudia ce receita de bolis, mais bolis eu num gostis.</p>
                        </div>
                    </div>
                    <div class="date text-warning">1997</div>
                </li>
                <li class="timeline-inverted">
                    <div class="timeline-badge warning small"></div>
                    <div class="timeline-panel">
                        <div class="timeline-heading">
                            <h4 class="timeline-title">presidential campaign</h4>
                        </div>
                        <div class="timeline-body">
                            <p>Mussum ipsum cacilds, vidis litro abertis. Consetis adipiscings elitis. Pra lá , depois divoltis porris, paradis. Paisis, filhis, espiritis santis. Mé faiz elementum girarzis, nisi eros vermeio, in elementis mé pra quem é amistosis quis leo. Manduma pindureta quium dia nois paga. Sapien in monti palavris qui num significa nadis i pareci latim. Interessantiss quisso pudia ce receita de bolis, mais bolis eu num gostis.</p>
                            <p>Suco de cevadiss, é um leite divinis, qui tem lupuliz, matis, aguis e fermentis. Interagi no mé, cursus quis, vehicula ac nisi. Aenean vel dui dui. Nullam leo erat, aliquet quis tempus a, posuere ut mi. Ut scelerisque neque et turpis posuere pulvinar pellentesque nibh ullamcorper. Pharetra in mattis molestie, volutpat elementum justo. Aenean ut ante turpis. Pellentesque laoreet mé vel lectus scelerisque interdum cursus velit auctor. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam ac mauris lectus, non scelerisque augue. Aenean justo massa.</p>
                        </div>
                    </div>
                    <div class="date text-warning">2008</div>
                </li>
                <li>
                    <div class="timeline-badge warning"></div>
                    <div class="timeline-panel">
                        <div class="timeline-heading">
                            <h4 class="timeline-title">united states senator from illinois</h4>
                        </div>
                        <div class="timeline-body">
                            <p>Mussum ipsum cacilds, vidis litro abertis. Consetis adipiscings elitis. Pra lá , depois divoltis porris, paradis. Paisis, filhis, espiritis santis. Mé faiz elementum girarzis, nisi eros vermeio, in elementis mé pra quem é amistosis quis leo. Manduma pindureta quium dia nois paga. Sapien in monti palavris qui num significa nadis i pareci latim. Interessantiss quisso pudia ce receita de bolis, mais bolis eu num gostis.</p>
                        </div>
                    </div>
                    <div class="date text-warning">2009</div>
                </li>
                <li class="timeline-inverted">
                    <div class="timeline-badge warning small"></div>
                    <div class="timeline-panel">
                        <div class="timeline-heading">
                            <h4 class="timeline-title">united states senator</h4>
                        </div>
                        <div class="timeline-body">
                            <p>Mussum ipsum cacilds, vidis litro abertis. Consetis adipiscings elitis. Pra lá , depois divoltis porris, paradis. Paisis, filhis, espiritis santis. Mé faiz elementum girarzis, nisi eros vermeio, in elementis mé pra quem é amistosis quis leo. Manduma pindureta quium dia nois paga. Sapien in monti palavris qui num significa nadis i pareci latim. Interessantiss quisso pudia ce receita de bolis, mais bolis eu num gostis.</p>
                        </div>
                    </div>
                    <div class="date text-warning">2012</div>
                </li>
                <li>
                    <div class="timeline-badge warning small"></div>
                    <div class="timeline-panel">
                        <div class="timeline-heading">
                            <h4 class="timeline-title">2012 presidential campaign</h4>
                        </div>
                        <div class="timeline-body">
                            <p>Mussum ipsum cacilds, vidis litro abertis. Consetis adipiscings elitis. Pra lá , depois divoltis porris, paradis. Paisis, filhis, espiritis santis. Mé faiz elementum girarzis, nisi eros vermeio, in elementis mé pra quem é amistosis quis leo. Manduma pindureta quium dia nois paga. Sapien in monti palavris qui num significa nadis i pareci latim. Interessantiss quisso pudia ce receita de bolis, mais bolis eu num gostis.</p>
                            <!-- <hr>
                        <div class="btn-group">
                          <button type="button" class="btn btn-primary btn-sm dropdown-toggle" data-toggle="dropdown">
                            <i class="glyphicon glyphicon-cog"></i> <span class="caret"></span>
                          </button>
                          <ul class="dropdown-menu" role="menu">
                            <li><a href="#">Action</a></li>
                            <li><a href="#">Another action</a></li>
                            <li><a href="#">Something else here</a></li>
                            <li class="divider"></li>
                            <li><a href="#">Separated link</a></li>
                          </ul>
                        </div> -->
                        </div>
                    </div>
                    <div class="date text-warning">2013</div>
                </li>
                <li>
                    <div class="timeline-badge warning small"></div>
                    <div class="timeline-panel">
                        <div class="timeline-heading">
                            <h4 class="timeline-title">obama care</h4>
                        </div>
                        <div class="timeline-body">
                            <p>Mussum ipsum cacilds, vidis litro abertis. Consetis adipiscings elitis. Pra lá , depois divoltis porris, paradis. Paisis, filhis, espiritis santis. Mé faiz elementum girarzis, nisi eros vermeio, in elementis mé pra quem é amistosis quis leo. Manduma pindureta quium dia nois paga. Sapien in monti palavris qui num significa nadis i pareci latim. Interessantiss quisso pudia ce receita de bolis, mais bolis eu num gostis.</p>
                        </div>
                    </div>
                    <div class="date text-warning">2014</div>
                </li>
                <li class="timeline-inverted">
                    <div class="timeline-badge warning"></div>
                    <div class="timeline-panel">
                        <div class="timeline-heading">
                            <h4 class="timeline-title">44th president of the united states</h4>
                        </div>
                        <div class="timeline-body">
                            <p>Mussum ipsum cacilds, vidis litro abertis. Consetis adipiscings elitis. Pra lá , depois divoltis porris, paradis. Paisis, filhis, espiritis santis. Mé faiz elementum girarzis, nisi eros vermeio, in elementis mé pra quem é amistosis quis leo. Manduma pindureta quium dia nois paga. Sapien in monti palavris qui num significa nadis i pareci latim. Interessantiss quisso pudia ce receita de bolis, mais bolis eu num gostis.</p>
                        </div>
                    </div>
                    <div class="date text-warning">2015</div>
                </li>
            </ul><!--/.timeline-->
        </div><!--/.panel-body.text-center -->
    </div>
</content>

<content tag="preFooterSections">
    <g:render template="showExtendedPoliticianTemplates/latestProjects" model="[politician:politician, userProjects:userProjects]"/>
    <g:render template="showExtendedPoliticianTemplates/recommendedPoliticians" model="[politician:politician, recommendPoliticians:recommendPoliticians]"/>

</content>