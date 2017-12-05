<div class="section-header">
    <h1><g:message code="${msgPrefix}.solutions.title"/></h1>
    <h3 class="hidden-xs"><g:message code="${msgPrefix}.solutions.subtitle"/></h3>
</div>
<div class="row section-body">
    <ul class="threeColumn">
        <li class="col-sm-4 col-md-4">
            <div class="card">
                <div class="card-header">
                    <img class="img-responsive" data-src="${r.resource(dir:'images/landing', file:'administrations-small.jpg')}" alt="step 1">
                </div>
                <div class="card-body">
                    <g:link mapping="landingGovernments" class="btn btn-lg">
                        <g:message code="${msgPrefix}.solutions.administrations.button"/>
                    </g:link>
                    <p><g:message code="${msgPrefix}.solutions.administrations.text"/></p>
                </div>
            </div>
        </li>
        <li class="col-sm-4 col-md-4">
            <div class="card">
                <div class="card-header">
                    <img class="img-responsive" data-src="${r.resource(dir:'images/landing', file:'corporations-small.jpg')}" alt="step 2">
                </div>
                <div class="card-body">
                    <g:link mapping="landingEnterprise" class="btn btn-lg">
                        <g:message code="${msgPrefix}.solutions.enterprises.button"/>
                    </g:link>
                    <p><g:message code="${msgPrefix}.solutions.enterprises.text"/></p>
                </div>
            </div>
        </li>
        <li class="col-sm-4 col-md-4">
            <div class="card">
                <div class="card-header">
                    <img class="img-responsive" data-src="${r.resource(dir:'images/landing', file:'organizations-small.jpg')}" alt="step 3">
                </div>
                <div class="card-body">
                    <g:link mapping="landingOrganization" class="btn btn-lg">
                        <g:message code="${msgPrefix}.solutions.organizations.button"/>
                    </g:link>
                    <p><g:message code="${msgPrefix}.solutions.organizations.text"/></p>
                </div>
            </div>
        </li>
    </ul>
</div>