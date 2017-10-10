<div class="section-header">
    <h1><g:message code="${msgPrefix}.${msgSection}.title"/></h1>
    <h3 class="hidden-xs"><g:message code="${msgPrefix}.${msgSection}.subtitle"/></h3>
</div>
<div class="section-body">
    <div class="col-md-6 img-full-container left">
        <img src="${r.resource(dir:'images/landing', file:'features-profile.png')}">
    </div>
    <div class="col-md-6 pull-right">
        <ul class="checklist">
            <li class="clearfix">
                <div class="col-md-1 col-xs-1"><i class="fa fa-check" aria-hidden="true"></i></div>
                <div class="col-md-11 col-xs-11"><span><g:message code="${msgPrefix}.${msgSection}.bullet1.bold"/></span> - <g:message code="${msgPrefix}.${msgSection}.bullet1.description"/></div>
            </li>
            <li class="clearfix">
                <div class="col-md-1 col-xs-1"><i class="fa fa-check" aria-hidden="true"></i></div>
                <div class="col-md-11 col-xs-11"><span><g:message code="${msgPrefix}.${msgSection}.bullet2.bold"/></span> - <g:message code="${msgPrefix}.${msgSection}.bullet2.description"/></div>
            </li>
            <li class="clearfix">
                <div class="col-md-1 col-xs-1"><i class="fa fa-check" aria-hidden="true"></i></div>
                <div class="col-md-11 col-xs-11"><span><g:message code="${msgPrefix}.${msgSection}.bullet3.bold"/></span> - <g:message code="${msgPrefix}.${msgSection}.bullet3.description"/></div>
            </li>
        </ul>
        <div class="col-md-8 col-md-offset-2">
            <g:link mapping="register" class="btn btn-lg btn-sign-up">
                <g:message code="${msgPrefix}.requestDemo"/>
            </g:link>
        </div>
    </div>
</div>