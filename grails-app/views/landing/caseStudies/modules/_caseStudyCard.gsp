<g:set var="cardTitle"><g:message code="landingCasesStudy.${caseStudyId}.card.title"/></g:set>
<g:set var="cardSubTitle"><g:message code="landingCasesStudy.${caseStudyId}.card.subtitle"/></g:set>
<div class="card">
    <div class="card-header">
        <img class="img-responsive" src="${r.resource(dir:"images/landing/caseStudies/${caseStudyId}", file:"card.jpg")}" alt="${cardTitle}">
    </div>
    <div class="card-body">
        <h3><g:link mapping="${"landingCaseStudy${caseStudyId}".toString()}">${cardTitle}</g:link></h3>
        <p>${cardSubTitle}</p>
    </div>
</div>