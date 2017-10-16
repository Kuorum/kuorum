<div class="section-header">
    <h1><g:message code="individualCaseStudy.otherCases.title"/></h1>
    <h3><g:message code="individualCaseStudy.otherCases.subtitle"/></h3>
</div>
<div class="row section-body other-cases">
    <div class="row">
        <g:each in="${otherCases}" var="otherCaseId">
            <div class="col-lg-4">
                <g:render template="/landing/caseStudies/modules/caseStudyCard" model="[caseStudyId:otherCaseId]"/>
            </div>
        </g:each>
    </div>
    </br class="hidden-xs">
    <div id="request-demo-btn" class="col-md-4 col-md-offset-4">
        <a href="#" class="btn btn-lg btn-orange btn-sign-up btn-open-modal-request-demo"><g:message code="individualCaseStudy.requestDemo"/> </a>
    </div>
</div>