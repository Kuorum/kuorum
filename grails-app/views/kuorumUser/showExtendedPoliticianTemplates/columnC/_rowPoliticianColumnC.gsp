<g:if test='${text || link}'>
    <tr>
        <th scope="row"><g:message code="${message}"/></th>
        <td>
            <g:if test="${link}">
                <a class="ellipsis" href="${link}" target="_blank">${link}</a>
            </g:if>
            <g:else>
                ${text}
            </g:else>
        </td>
    </tr>
</g:if>