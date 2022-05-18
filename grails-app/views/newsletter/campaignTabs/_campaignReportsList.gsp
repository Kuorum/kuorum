<g:if test="${reportsList}">
    <ul class="files-list">
        <g:each in="${reportsList}" var="report">
            <li>
                <span class="file-icon ${report.icon}"></span>
                <a href="${report.url}"class="file-name" target="_blank" rel="nofollow noopener noreferrer">${report.name}</a>
                <a class="file-action" href="${report.delete}"><span class="fal fa-trash"></span></a>
            </li>
        </g:each>
    </ul>
</g:if>
<g:else>
    <div class="text-center">
        <h3 class="noReports"><g:message code="tools.massMailing.view.reports.empty"/></h3>
    </div>
</g:else>