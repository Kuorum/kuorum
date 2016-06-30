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
                        data:politician?.professionalDetails?.position?:''
                ]"/>
                <g:render template="/kuorumUser/showExtendedPoliticianTemplates/columnC/rowPoliticianColumnC" model="[
                        message:g.message(code:'politician.professionalDetails.data.institution'),
                        data:politician?.professionalDetails?.institution?:'',
                        itemprop: 'worksFor'
                ]"/>
                <g:render template="/kuorumUser/showExtendedPoliticianTemplates/columnC/rowPoliticianColumnC" model="[
                        message:g.message(code:'politician.professionalDetails.data.constituency'),
                        data:politician?.professionalDetails?.constituency?.name?:''
                ]"/>
                <g:render template="/kuorumUser/showExtendedPoliticianTemplates/columnC/rowPoliticianColumnC" model="[
                        message:g.message(code:'politician.professionalDetails.data.region'),
                        data:politician?.professionalDetails?.region?.name?:''
                ]"/>
                <g:render template="/kuorumUser/showExtendedPoliticianTemplates/columnC/rowPoliticianColumnC" model="[
                        message:g.message(code:'kuorum.users.extendedPoliticianData.ProfessionalDetails.politicalParty.label'),
                        data:politician?.professionalDetails?.politicalParty?:''
                ]"/>
            </div>
            <g:if test="${['profession','university', 'school','studies', 'cvLink', 'declarationLink'].find{politician?.careerDetails?."${it}"}}">
                <div class="table table-condensed limit-height" data-collapsedHeight="110">
                    <div class="thead"><g:message code="politician.professionalDetails.title.career"/> </div>
                    <g:render template="/kuorumUser/showExtendedPoliticianTemplates/columnC/rowPoliticianColumnC" model="[
                            message:g.message(code:'politician.professionalDetails.data.profession'),
                            data:politician?.careerDetails?.profession?:''
                    ]"/>
                    <g:render template="/kuorumUser/showExtendedPoliticianTemplates/columnC/rowPoliticianColumnC" model="[
                            message:g.message(code:'politician.quickNotes.data.education.studies'),
                            data:politician?.careerDetails?.studies?:''
                    ]"/>
                    <g:render template="/kuorumUser/showExtendedPoliticianTemplates/columnC/rowPoliticianColumnC" model="[
                            message:g.message(code:'politician.quickNotes.data.education.university'),
                            data:politician?.careerDetails?.university,
                            itemprop:'alumniOf'
                    ]"/>
                    <g:render template="/kuorumUser/showExtendedPoliticianTemplates/columnC/rowPoliticianColumnC" model="[
                            message:g.message(code:'politician.quickNotes.data.education.school'),
                            data:politician?.careerDetails?.school?:''
                    ]"/>
                    <g:render template="/kuorumUser/showExtendedPoliticianTemplates/columnC/rowPoliticianColumnC" model="[
                            message:g.message(code:'politician.professionalDetails.data.cvLink'),
                            link:politician?.careerDetails?.cvLink?.url?:'',
                            data:''
                    ]"/>
                    <g:render template="/kuorumUser/showExtendedPoliticianTemplates/columnC/rowPoliticianColumnC" model="[
                            message:g.message(code:'politician.professionalDetails.data.declaration'),
                            link:politician?.careerDetails?.declarationLink?.url?:'',
                            data:''
                    ]"/>
                </div>
            </g:if>
        </div>
    </section>
</g:if>
