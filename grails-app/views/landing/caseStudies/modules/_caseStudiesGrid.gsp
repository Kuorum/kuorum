<div class="section-body">
    <div class="breadcrumbs">
        <g:link mapping="landingServices" class="breadcrumb">
            <g:message code="individualCaseStudy.breadcrumb.home"/>
        </g:link>
    </div>
    <div class="row">
        <g:each in="${caseStudiesIds}" status="i" var="caseStudyId">
            <g:if test="${i%3 ==0}">
                %{--Close and open new row--}%
                </div>
                <div class="row">
            </g:if>
            <div class="col-lg-4">
                <g:render template="/landing/caseStudies/modules/caseStudyCard" model="[caseStudyId:caseStudyId, sectionName:'landingCaseStudy']"/>
            </div>
        </g:each>
    </div>
</div>