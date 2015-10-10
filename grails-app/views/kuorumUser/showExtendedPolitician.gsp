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
                    <p class='party'>${userUtil.roleName(user:politician)}</p>
                </div>
                <div class="col-xs-5 col-sm-4 following">
                    %{--<strong>Followers 58K</strong><br/>--}%
                    <strong><g:message code="kuorumUser.show.module.followers.title" args="[politician.numFollowers]"/></strong><br/>
                    <userUtil:ifIsFollower user="${politician}">
                        <i class="fa fa-check-circle-o"></i>
                        <g:message code="kuorumUser.popover.follower"/>
                    </userUtil:ifIsFollower>
                </div>
            </div><!--/.row -->

            <div class="extra-padding">
                <h4><g:message code="politician.causes"/> </h4>
                <ul class='causes-tags'>
                    <li><a href="#">#anti-gun</a></li>
                    <li><a href="#">#healthcare</a></li>
                    <li><a href="#">#lovewins</a></li>
                    <li><a href="#">#universal education</a></li>
                    <li><a href="#">#tax the rich</a></li>
                </ul>
                <h4><g:message code="politician.knownFor"/></h4>
                <ul class='known-for'>
                    <li><a href="#">Higher Education Opportunity Through Pell Grant Expansion Act</a></li>
                    <li><a href="#">E-85 Fuel Utilization and Infrastructure Development Incentives Act of 2005</a></li>
                    <li><a href="#">Hurricane Katrina Fast-Track Refunds for Working Families Act of 2005</a></li>
                    <li><a href="#">Innovation Districts for School Improvement Act</a></li>
                </ul>
                <h4><g:message code="politician.bio"/></h4>
                <p>${politician.bio}</p>
            </div><!--/.extra-padding -->
        </div><!--/.panel-body-->
    </div><!--/.panel panel-default -->

    <g:render template="showExtendedPoliticianTemplates/politicianTimeLine" model="[politician: politician]"/>
</content>

<content tag="cColumn">
    <g:render template="showExtendedPoliticianTemplates/columnC/socialButtonsExtendedPoliticianColumnC" model="[politician:politician]"/>
    <g:render template="showExtendedPoliticianTemplates/columnC/contactPolitician" model="[politician:politician]"/>
    <g:render template="showExtendedPoliticianTemplates/columnC/leaningIndex" model="[politician:politician]"/>
    <g:render template="showExtendedPoliticianTemplates/columnC/professionalDetailExtendedPolitician" model="[politician:politician]"/>
    <g:render template="showExtendedPoliticianTemplates/columnC/quickNotesExtendedPolitician" model="[politician:politician]"/>
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