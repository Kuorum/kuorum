<div class="section-header">
    <h1><g:message code="individualCaseStudy.otherCases.title"/></h1>
    <h3><g:message code="individualCaseStudy.otherCases.subtitle"/></h3>
</div>
<div class="row section-body other-cases">
    <div class="row">
        <div class="col-lg-4">
            <g:render template="/landing/caseStudies/caseStudyCard" model="[caseStudyId:'001']"/>
        </div>
        <div class="col-lg-4">
            <g:render template="/landing/caseStudies/caseStudyCard" model="[caseStudyId:'001']"/>
        </div>
        <div class="col-lg-4">
            <g:render template="/landing/caseStudies/caseStudyCard" model="[caseStudyId:'001']"/>
        </div>
    </div>
    </br class="hidden-xs">
    <div id="request-demo-btn" class="col-md-4 col-md-offset-4">
        <a href="#" class="btn btn-lg btn-orange btn-sign-up btn-open-modal-request-demo"><g:message code="${msgPrefix}.requestDemo"/> </a>
    </div>
</div>