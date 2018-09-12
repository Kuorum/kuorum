<g:if test="${show}">
    <li class="fontIcon arrow">
        <span class="fal fa-angle-right fa-3x"></span>
    </li>
    <li class="fontIcon ${currentActive ?'active':''}">
        <a href="#" data-redirectLink="${link}">
            <span class="fal ${faIcon}"></span>
            <span class="label">${label}</span>
        </a>
    </li>
</g:if>