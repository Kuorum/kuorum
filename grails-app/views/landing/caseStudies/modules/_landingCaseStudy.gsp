<div class="section-header">
    <h1><g:message code="${msgPrefix}.caseStudy.title"/></h1>
    <h3 class="hidden-xs"><g:message code="${msgPrefix}.caseStudy.subtitle"/></h3>
</div>
<div class="section-body">
    <div class="col-md-6 img-full-container left">
        <img src="${r.resource(dir:"images/landing/${sectionName}/${caseStudyId}/", file:"landing.png")}">
    </div>
    <div class="col-md-6 pull-right">
        <h1><g:message code="${sectionName}.${caseStudyId}.content.title"/></h1>
        <p><g:message code="${sectionName}.${caseStudyId}.card.subtitle"/></p>
        <g:link mapping="${"${sectionName}${caseStudyId}".toString()}"  class="link-case-study">
            <g:message code="${sectionName}.page.landing.seeMore"/>
        </g:link>
        <span class="link-arrow hidden-xs">></span>
    </div>
</div>