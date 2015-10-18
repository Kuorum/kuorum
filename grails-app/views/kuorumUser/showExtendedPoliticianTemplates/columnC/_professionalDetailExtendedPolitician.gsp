<section class="panel panel-default">
    <div class="panel-heading">
        <h3 class="panel-title"><g:message code="politician.professionalDetails.title"/> </h3>
    </div>
    <div class="panel-body text-center">
        <table class="table table-condensed">
            <tbody>
            <tr>
                <th scope="row"><g:message code="politician.professionalDetails.data.position"/></th>
                <td>${politician?.professionalDetails?.position?:"N/A"}</td>
            </tr>
            <tr>
                <th scope="row"><g:message code="politician.professionalDetails.data.institution"/></th>
                <td>${politician?.professionalDetails?.institution?:"N/A"}</td>
            </tr>
            <tr>
                <th scope="row"><g:message code="politician.professionalDetails.data.constituency"/></th>
                <td>${politician?.professionalDetails?.constituency?.name?:"N/A"}</td>
            </tr>
            <tr>
                <th scope="row"><g:message code="politician.professionalDetails.data.region"/></th>
                <td>${politician?.professionalDetails?.region?.name?:"N/A"}</td>
            </tr>
            </tbody>
        </table>
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
                    <td><a class="pdf" href="${politician?.professionalDetails?.cvLink}" target="_blank">cv.pdf</a></td>
                </tr>
            </g:if>
            <g:if test="${politician?.professionalDetails?.cvLink}">
                <tr>
                    <th scope="row"><g:message code="politician.professionalDetails.data.declaration"/></th>
                    <td><a href="${politician?.professionalDetails?.declarationLink}" target="_blank">declaration.pdf</a></td>
                </tr>
            </g:if>
            </tbody>
        </table>
    </div>
</section>
