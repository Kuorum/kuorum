<g:if test='${data || link}'>
    <div class="tr clearfix">
        <div class="th" scope="row"><g:message code="${message}"/></div>
        <div class="td" ${raw(itemprop?"itemprop='${itemprop}'":'')} ${raw(content?"content='${content}'":'')}>
            <g:if test="${link}">
                <a class="ellipsis" href="${link}" target="_blank" rel="nofollow noopener noreferrer">${link}</a>
            </g:if>
            <g:else>
                ${data}
            </g:else>
        </div>
    </div>
</g:if>