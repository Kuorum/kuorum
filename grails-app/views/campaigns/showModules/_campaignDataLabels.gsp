<g:if test="${causes}">
    <ul class="labels">
        <g:each in="${causes}" var="cause">
            <li>
                <g:link mapping="searcherSearchByCAUSE" params="[word:cause]" absolute="true" class="label-leader-post" itemprop="about">
                    ${cause}
                </g:link>
            </li>
        </g:each>
    </ul>
</g:if>