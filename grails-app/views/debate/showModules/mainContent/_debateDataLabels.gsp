<g:if test="${causes}">
    <ul class="labels">
        <g:each in="${causes}" var="cause">
            <li>
                <span class="label-leader-post" itemprop="about">${cause}</span>
            </li>
        </g:each>
    </ul>
</g:if>