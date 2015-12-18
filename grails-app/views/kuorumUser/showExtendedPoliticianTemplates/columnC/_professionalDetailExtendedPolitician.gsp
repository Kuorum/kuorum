<g:if test="${['region','constituency','institution','position','profession', 'cvLink', 'declarationLink'].find{politician?.professionalDetails?."${it}"}}">
    <section class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title"><g:message code="politician.professionalDetails.title"/> </h3>
        </div>
        <div class="panel-body text-center">
            <table class="table table-condensed">
                <tbody>
                    <g:render template="/kuorumUser/showExtendedPoliticianTemplates/columnC/rowPoliticianColumnC" model="[
                            message:g.message(code:'politician.professionalDetails.data.position'),
                            text:politician?.professionalDetails.position
                    ]"/>
                    <g:render template="/kuorumUser/showExtendedPoliticianTemplates/columnC/rowPoliticianColumnC" model="[
                            message:g.message(code:'politician.professionalDetails.data.institution'),
                            text:politician?.professionalDetails.institution
                    ]"/>
                    <g:render template="/kuorumUser/showExtendedPoliticianTemplates/columnC/rowPoliticianColumnC" model="[
                            message:g.message(code:'politician.professionalDetails.data.constituency'),
                            text:politician?.professionalDetails.constituency?.name
                    ]"/>
                    <g:render template="/kuorumUser/showExtendedPoliticianTemplates/columnC/rowPoliticianColumnC" model="[
                            message:g.message(code:'politician.professionalDetails.data.region'),
                            text:politician?.professionalDetails.region?.name
                    ]"/>
                </tbody>
            </table>
            <g:if test="${['profession', 'cvLink', 'declarationLink'].find{politician?.professionalDetails?."${it}"}}">
                <hr/>
                <table class="table table-condensed">
                    <tbody>
                        <g:render template="/kuorumUser/showExtendedPoliticianTemplates/columnC/rowPoliticianColumnC" model="[
                                message:g.message(code:'politician.professionalDetails.data.profession'),
                                text:politician?.professionalDetails.profession
                        ]"/>
                        <g:render template="/kuorumUser/showExtendedPoliticianTemplates/columnC/rowPoliticianColumnC" model="[
                                message:g.message(code:'politician.professionalDetails.data.cvLink'),
                                link:politician?.professionalDetails?.cvLink
                        ]"/>
                        <g:render template="/kuorumUser/showExtendedPoliticianTemplates/columnC/rowPoliticianColumnC" model="[
                                message:g.message(code:'politician.professionalDetails.data.declaration'),
                                link:politician?.professionalDetails?.declarationLink
                        ]"/>
                    </tbody>
                </table>
            </g:if>
        </div>
    </section>
</g:if>
