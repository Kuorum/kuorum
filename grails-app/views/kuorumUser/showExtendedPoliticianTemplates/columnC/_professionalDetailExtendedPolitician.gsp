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
            <g:if test="${['profession', 'cvLink', 'declarationLink'].find{politician?.professionalDetails?."${it}"}}">
                <div class="table table-condensed limit-height" data-collapsedHeight="85">
                    <div class="thead"><g:message code="politician.professionalDetails.title.career"/> </div>
                    <g:render template="/kuorumUser/showExtendedPoliticianTemplates/columnC/rowPoliticianColumnC" model="[
                            message:g.message(code:'politician.professionalDetails.data.profession'),
                            text:politician?.professionalDetails?.profession
                    ]"/>
                    <g:render template="/kuorumUser/showExtendedPoliticianTemplates/columnC/rowPoliticianColumnC" model="[
                            message:g.message(code:'politician.professionalDetails.data.cvLink'),
                            link:politician?.professionalDetails?.cvLink
                    ]"/>
                    <g:render template="/kuorumUser/showExtendedPoliticianTemplates/columnC/rowPoliticianColumnC" model="[
                            message:g.message(code:'politician.professionalDetails.data.declaration'),
                            link:politician?.professionalDetails?.declarationLink
                    ]"/>
                </div>
            </g:if>
        </div>
    </section>
</g:if>
