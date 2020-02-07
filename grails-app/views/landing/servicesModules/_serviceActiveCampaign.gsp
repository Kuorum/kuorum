<g:if test="${landingVisibleRoles.contains(role)}">
    <li class="fontIcon col-sm-${12/landingVisibleRoles.size()}">
        <g:link mapping="${searchMapping}" title="${title}">
            <span class="fal ${icon}"></span>
        </g:link>
        <h2>${title} </h2>
        <p>${text}</p>
    </li>
</g:if>