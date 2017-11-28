<g:if test="${debate.causes}">
    <ul class="labels">
        <g:each in="${debate.causes}" var="cause">
            <li>
                <span class="label-leader-post" itemprop="about">${cause}</span>
            </li>
        </g:each>
    </ul>
</g:if>