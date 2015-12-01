<g:if test="${['region','constituency','institution','position','profession', 'cvLink', 'declarationLink'].find{politician?.professionalDetails?."${it}"}}">
    <section class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title"><g:message code="politician.professionalDetails.title"/> </h3>
        </div>
        <div class="panel-body text-center">
            <table class="table table-condensed">
                <tbody>
                <g:if test="${politician?.professionalDetails?.position}">
                    <tr>
                        <th scope="row"><g:message code="politician.professionalDetails.data.position"/></th>
                        <td>${politician?.professionalDetails?.position}</td>
                    </tr>
                </g:if>
                <g:if test="${politician?.professionalDetails?.institution}">
                    <tr>
                        <th scope="row"><g:message code="politician.professionalDetails.data.institution"/></th>
                        <td>${politician?.professionalDetails?.institution}</td>
                    </tr>
                </g:if>
                <g:if test="${politician?.professionalDetails?.constituency}">
                    <tr>
                        <th scope="row"><g:message code="politician.professionalDetails.data.constituency"/></th>
                        <td>${politician?.professionalDetails?.constituency?.name}</td>
                    </tr>
                </g:if>
                <g:if test="${politician?.professionalDetails?.region}">
                    <tr>
                        <th scope="row"><g:message code="politician.professionalDetails.data.region"/></th>
                        <td>${politician?.professionalDetails?.region?.name}</td>
                    </tr>
                </g:if>
                </tbody>
            </table>
            <g:if test="${['profession', 'cvLink', 'declarationLink'].find{politician?.professionalDetails?."${it}"}}">
                <hr/>
                <table class="table table-condensed">
                    <tbody>
                    <g:if test="${politician?.professionalDetails?.profession}">
                        <tr>
                            <th scope="row"><g:message code="politician.professionalDetails.data.profession"/></th>
                            <td>${politician?.professionalDetails?.profession?:"N/A"}</td>
                        </tr>
                    </g:if>
                    <g:if test="${politician?.professionalDetails?.cvLink}">
                        <tr>
                            <th scope="row"><g:message code="politician.professionalDetails.data.cv"/></th>
                            <td><a class="ellipsis" href="${politician?.professionalDetails?.cvLink}" target="_blank">cv.pdf</a></td>
                        </tr>
                    </g:if>
                    <g:if test="${politician?.professionalDetails?.cvLink}">
                        <tr>
                            <th scope="row"><g:message code="politician.professionalDetails.data.declaration"/></th>
                            <td><a class="ellipsis"href="${politician?.professionalDetails?.declarationLink}" target="_blank">declaration.pdf</a></td>
                        </tr>
                    </g:if>
                    </tbody>
                </table>
            </g:if>
        </div>
    </section>
</g:if>
