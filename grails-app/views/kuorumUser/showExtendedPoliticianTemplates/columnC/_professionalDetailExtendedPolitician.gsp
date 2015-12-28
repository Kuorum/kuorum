<g:if test="${['region','constituency','institution','position','profession', 'cvLink', 'declarationLink'].find{politician?.professionalDetails?."${it}"}}">
    <section class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title"><g:message code="politician.professionalDetails.title"/> </h3>
        </div>
        <div class="panel-body text-center">
            <div class="table table-condensed limit-height" data-collapsedHeight="95">
                <div class="thead"><g:message code="politician.professionalDetails.title.political"/> </div>
                <g:render template="/kuorumUser/showExtendedPoliticianTemplates/columnC/rowPoliticianColumnC" model="[
                        message:g.message(code:'politician.professionalDetails.data.position'),
                        text:politician?.professionalDetails?.position
                ]"/>
                <g:render template="/kuorumUser/showExtendedPoliticianTemplates/columnC/rowPoliticianColumnC" model="[
                        message:g.message(code:'politician.professionalDetails.data.institution'),
                        text:politician?.professionalDetails?.institution
                ]"/>
                <g:render template="/kuorumUser/showExtendedPoliticianTemplates/columnC/rowPoliticianColumnC" model="[
                        message:g.message(code:'politician.professionalDetails.data.constituency'),
                        text:politician?.professionalDetails?.constituency?.name
                ]"/>
                <g:render template="/kuorumUser/showExtendedPoliticianTemplates/columnC/rowPoliticianColumnC" model="[
                        message:g.message(code:'politician.professionalDetails.data.region'),
                        text:politician?.professionalDetails?.region?.name
                ]"/>
            </div>
            <g:if test="${['profession','university', 'school','studies', 'cvLink', 'declarationLink'].find{politician?.careerDetails?."${it}"}}">
                <div class="table table-condensed limit-height" data-collapsedHeight="110">
                    <div class="thead"><g:message code="politician.professionalDetails.title.career"/> </div>
                    <g:render template="/kuorumUser/showExtendedPoliticianTemplates/columnC/rowPoliticianColumnC" model="[
                            message:g.message(code:'politician.professionalDetails.data.profession'),
                            text:politician?.careerDetails?.profession
                    ]"/>
                    <g:render template="/kuorumUser/showExtendedPoliticianTemplates/columnC/rowPoliticianColumnC" model="[
                            message:g.message(code:'politician.quickNotes.data.education.university'),
                            text:politician?.careerDetails?.university
                    ]"/>
                    <g:render template="/kuorumUser/showExtendedPoliticianTemplates/columnC/rowPoliticianColumnC" model="[
                            message:g.message(code:'politician.quickNotes.data.education.school'),
                            text:politician?.careerDetails?.school
                    ]"/>
                    <g:render template="/kuorumUser/showExtendedPoliticianTemplates/columnC/rowPoliticianColumnC" model="[
                            message:g.message(code:'politician.quickNotes.data.education.studies'),
                            text:politician?.careerDetails?.studies?:''
                    ]"/>
                    <g:render template="/kuorumUser/showExtendedPoliticianTemplates/columnC/rowPoliticianColumnC" model="[
                            message:g.message(code:'politician.professionalDetails.data.cvLink'),
                            link:politician?.careerDetails?.cvLink?:'',
                            text:''
                    ]"/>
                    <g:render template="/kuorumUser/showExtendedPoliticianTemplates/columnC/rowPoliticianColumnC" model="[
                            message:g.message(code:'politician.professionalDetails.data.declaration'),
                            link:politician?.careerDetails?.declarationLink?:'',
                            text:''
                    ]"/>
                </div>
            </g:if>
        </div>
    </section>
</g:if>
