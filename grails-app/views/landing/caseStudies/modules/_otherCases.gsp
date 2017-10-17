<div class="section-header">
    <h1><g:message code="${sectionName}.page.otherCases.title"/></h1>
    <h3><g:message code="${sectionName}.page.otherCases.subtitle"/></h3>
</div>
<div class="row section-body other-cases">
    <div class="row">
        <g:each in="${otherCases}" var="otherCaseId">
            <div class="col-lg-4">
                <g:render template="/landing/caseStudies/modules/caseStudyCard" model="[caseStudyId:otherCaseId, sectionName:sectionName]"/>
            </div>
        </g:each>
    </div>
</div>