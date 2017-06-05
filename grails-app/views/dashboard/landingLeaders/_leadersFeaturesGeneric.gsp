<g:set var="msgPrefix" value="${msgPrefix?:'landingLeaders'}"/>
    <div class="col-md-6 img-full-container left">
        <img src="${r.resource(dir:'images/landing', file:'features-profile.png')}">
    </div>
    <div class="col-md-6 pull-right">
        <h1><g:message code="landingLeaders.featuresGeneric.title"/> </h1>
        <ul class="checklist">
            <li class="clearfix">
                <div class="col-md-1 col-xs-1"><i class="fa fa-check" aria-hidden="true"></i></div>
                <div class="col-md-11 col-xs-11"><span><g:message code="landingLeaders.featuresGeneric.bullet1.bold"/></span> - <g:message code="landingLeaders.featuresGeneric.bullet1.description"/></div>
            </li>
            <li class="clearfix">
                <div class="col-md-1 col-xs-1"><i class="fa fa-check" aria-hidden="true"></i></div>
                <div class="col-md-11 col-xs-11"><span><g:message code="landingLeaders.featuresGeneric.bullet2.bold"/></span> - <g:message code="landingLeaders.featuresGeneric.bullet2.description"/></div>
            </li>
            <li class="clearfix">
                <div class="col-md-1 col-xs-1"><i class="fa fa-check" aria-hidden="true"></i></div>
                <div class="col-md-11 col-xs-11"><span><g:message code="landingLeaders.featuresGeneric.bullet3.bold"/></span> - <g:message code="landingLeaders.featuresGeneric.bullet3.description"/></div>
            </li>
        </ul>
        <div class="col-md-8 col-md-offset-2">
            <g:link mapping="register"  class="btn btn-lg btn-sign-up">
                <g:message code="${msgPrefix}.featuresCustom.register"/>
            </g:link>
        </div>
    </div>
