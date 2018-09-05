<sec:ifAnyGranted roles="ROLE_POLITICIAN">
    <g:if test="${emptyFields.percentage<100}">
        <li class="dropdown underline empty-profile-menu">
            <a data-target="#" href="#" id="open-user-percent-profile" class="navbar-link user-area ${nav.activeMenuCss(mappingName: "home")}" data-toggle="dropdown" role="button">
                <span class="visible-xs"><g:message code="head.navigation.emptyProfile.title"/></span>
                <span class="fa ">
                    <g:formatNumber number="${emptyFields.percentage}" type="number" maxFractionDigits="0"/>%
                </span>
                <span class="badge-small">
                    <span class="fas fa-exclamation-circle"></span>
                </span>
            </a>
            <ul class="dropdown-menu dropdown-menu-right" aria-labelledby="open-user-percent-profile" role="menu">
                <li><g:message code="head.navigation.emptyProfile.title"/></li>

                <g:each in="${emptyFields.fields}" var="fieldToCheck">
                    <g:if test="${fieldToCheck.total}">
                        <li>
                            <g:link mapping="${fieldToCheck.urlMapping}" itemprop="url">
                                <span class="counter">${fieldToCheck.total}</span>
                                <span class="text"><g:message code="profile.menu.${fieldToCheck.urlMapping}"/></span>
                            </g:link>
                        </li>
                    </g:if>
                </g:each>
            </ul>
        </li>
    </g:if>
</sec:ifAnyGranted>