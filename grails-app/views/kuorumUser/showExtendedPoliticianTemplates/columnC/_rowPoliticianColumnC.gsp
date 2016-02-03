<g:if test='${data || link}'>
    <div class="tr">
        <div class="th" scope="row"><g:message code="${message}"/></div>
        <div class="td">
            <g:if test="${link}">
                <a class="ellipsis" href="${link}" target="_blank">${link}</a>
            </g:if>
            <g:else>
                ${data}
            </g:else>
        </div>
    </div>
</g:if>